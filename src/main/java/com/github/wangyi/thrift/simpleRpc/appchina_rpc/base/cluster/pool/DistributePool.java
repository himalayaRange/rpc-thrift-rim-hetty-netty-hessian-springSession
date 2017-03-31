package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.cluster.pool;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.Client;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.ClientIOException;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils.ExceptionUtils;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils.PoolUtils;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-9
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：随机负载均衡连接池
 * 1、使用commons-pool实现，当集群数量较少，使用此类非常合适，优点是高性能，为每个工厂维护一个连接池，使用负载均衡从不同的连接池取得连接。<br>
 * <br>
 * 2、缺点是浪费连接，即使轮询的方式很均匀，然而连接被借走的时间并不均匀，所以需要为每个池子多设置一些连接，maxIde决定了长连接的数量，maxActive可以设置大一些<br>
 * <br>
 * 3、内置心跳机制，保证连接池的及时弹出和恢复，只检测池内一个实例，建议心跳间隔短一些，心跳并不会影响性能，两次心跳的间隔是容错的空档。<br>
 * <br>
 * 4、内置了重试机制，如果连接池耗尽，在maxWait时限内无法取得连接，可以重试下一个池，当然这会导致整体wait提高，因此可以设置maxWait短一些，甚至取消maxWait<br>
 * <br>
 * 5、心跳机制无法保证每次取出的连接均可用，如果有这种需求，可以启用testOnBorrow，但是会影响性能，增加请求的延迟。<br>
 * <br>
 * 6、由于集群实例数量可能较多，所以禁用了空闲对象扫描线程，以便节省性能，可以通过连接维持时间或者连接使用次数来实现连接的释放，内部使用lifo=false的策略<br>
 * <br>
 * 7、适用场景：集群数量小于 0.1 * maxIdle<br>
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class DistributePool<P,R> extends ConfigurableDistributePool<P,R> implements Pool<P,R>,InitializingBean,DisposableBean{

	protected static Logger log = LoggerFactory.getLogger(DistributePool.class);
		
	protected GenericObjectPool<Client<P, R>>[] pools;
	
	protected ScheduledExecutorService executorService;

	/**
	 * 初始化连接池
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() throws IllegalAccessException {
		if(log.isWarnEnabled()){
			log.warn("DistributePool Initing ....");
		}
		if (factoryProvider == null) {
			throw new IllegalArgumentException("factoryProvider is null");
		}
		//获取所有工厂
		List<ClientFactory<P, R>> factories = factoryProvider.getFactories();
		if(factories == null || factories.isEmpty()){
			throw new IllegalArgumentException("factories is null or empty");
		}
		
		//为每个工厂维护一个连接池
		pools = new GenericObjectPool[factories.size()];
		//禁用了空闲对象扫描线程，以便节省性能，可以通过连接维持时间或者连接使用次数来实现连接的释放。使用lifo=false的策略
		GenericObjectPool.Config config = this.getPoolConfig();
		for (int index = 0; index < pools.length; index++) {
			//为每个连接池初始化连接池配置
			pools[index] = new GenericObjectPool<Client<P, R>>(factories.get(index), config);
			if (log.isInfoEnabled()) {
				log.info("Create pool:" + this.toString());
			}
		}
		
		//初始化负载均衡器
		loadBalance.allocat(pools.length);
		
		//启用心跳线程，保证连接池的及时弹出和恢复
		if (heartbeat > 0 && pools.length > 1) {
			//为每个连接池创建一个带调度的心跳线程池，线程池大小==连接池数量
			executorService = Executors.newScheduledThreadPool(pools.length > maxHeartbeatThread ? maxHeartbeatThread : pools.length);
			for (int index = 0; index < pools.length; index++) {
				//前一次心跳检测结束后和下次执行的时间间隔
				executorService.scheduleWithFixedDelay(new HeartbeatCommand(index), heartbeat, heartbeat, TimeUnit.MILLISECONDS); 
			}
		}
		
		if (log.isInfoEnabled()) {
			log.info("Create DistributePool pool.");
		}
	}
	
	/**
	 * 关闭所有的连接池对象pools和任务调度
	 */
	@Override
	public void close() {
		for (GenericObjectPool<Client<P, R>> pool : pools) {
			if (log.isInfoEnabled()) {
				log.info("Close pool:" + this.toString());
			}
			PoolUtils.close(pool);
		}
		if (executorService != null) {
			executorService.shutdownNow();
		}
		if (log.isInfoEnabled()) {
			log.info("Close DistributePool pool.");
		}
	}

	/**
	 * 从连接池中获取目标对象
	 */
	@Override
	public BorrowedClient<P, R> borrowClient() throws NoSuchElementException, ClientIOException {
		return borrowClientAndRetry(retry);
	}

	/**
	 * 有重试机制，如果连接池耗尽，在maxWait时限内无法取得连接，重试下一个池
	 * @param retry
	 * @return
	 * @throws ClientIOException
	 * @throws NoSuchElementException
	 **/
	protected BorrowedClient<P, R> borrowClientAndRetry(int retry) throws ClientIOException, NoSuchElementException {
		//使用负载均衡从不同的连接池取得连接。
		int index = loadBalance.nextIndex();
		GenericObjectPool<Client<P, R>> pool = pools[index];
		Client<P, R> client = null;
		try {
			//从当前的连接池中获取目标对象
			client = borrowClientAndExclude(pool);
			return new BorrowedClient<P, R>(client, index);
		} catch (ClientIOException | NoSuchElementException e) {
			//ClientIOException  when {@link ClientFactory#makeObject()} throws an {@link ClientIOException}.
			//NoSuchElementException 连接池耗尽，无法提供连接，或者maxWait等待超时。
			PoolUtils.invalidateObject(pool, client);
			if(retry > 0){
				return borrowClientAndRetry(--retry);
			} else {
				throw e;
			}
		} catch (Throwable e) {
			//根本不会有这种异常
			PoolUtils.invalidateObject(pool, client);
			throw ExceptionUtils.getRuntimeException(e);
		}
	}
	
	/**
	 * 从pool获取连接，如果连接使用次数过多，或者连接保持时间过久，直接销毁并且再次获取。无限循环，直到获取成功或者池子耗尽抛出异常。
	 * @param pool
	 * @return
	 * @throws Exception
	 **/
	protected Client<P, R> borrowClientAndExclude(GenericObjectPool<Client<P, R>> pool) throws Exception {
		Client<P, R> client = null;
		try {
			client = pool.borrowObject();
			if(shouldInvalidate(client)){
				//需要销毁的对象进行销毁并重新从连接池中获取
				PoolUtils.invalidateObject(pool, client);
				return borrowClientAndExclude(pool);
			} else {
				//不需要销毁的对象直接返回
				return client;
			}
		} catch (Throwable e) {
			PoolUtils.invalidateObject(pool, client);
			throw e;
		}
	}

	/**
	 * 当前的池对象是是否需要销毁
	 * @param client
	 * @return
	 */
	public boolean shouldInvalidate(Client<P, R> client){
		if(maxSendCount > 0 && client.sendCount() >= maxSendCount){
			//连接使用次数过多，进入销毁
			return true;
		}else if(maxKeepMillis > 0 && (System.currentTimeMillis() - client.openTimestamp()) >= maxKeepMillis){
			//连接使用时间过久，进入销毁
			return true;
		}else if(client.closed()){
			//防止将销毁的对象放回来
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 返回的连接根据其【使用次数】，以及【使用时间】，判断是否将其销毁。
	 * **/
	@Override
	public void returnClient(BorrowedClient<P, R> client) {
		if (client != null) {
			if(shouldInvalidate(client.client)){
				PoolUtils.invalidateObject(pools[client.index], client.client);
			}else{
				PoolUtils.returnObject(pools[client.index], client.client);
			}
		}
	}

	/**
	 * 校验连接池
	 */
	@Override
	public void invalidateClient(BorrowedClient<P, R> client) {
		if (client != null) {
			PoolUtils.invalidateObject(pools[client.index], client.client);
		}
	}

	/**
	 * 定时触发，将有效池放回，将无效池弹出。 <br>
	 * 
	 * 认为同一个池内的连接状态是相同的，一个不可用，全部不可用，一个可用，全部可用 <br>
	 * <br>
	 * 这个方法可以高频率运行，保证了无效的池子及时弹出。 保证有效的池子及时恢复。
	 */
	protected class HeartbeatCommand implements Runnable {
		protected int index; //某个连接池的索引

		protected HeartbeatCommand(int index) {
			this.index = index;
		}
		
		@Override
		public void run() {
			Client<P, R> client = null;
			GenericObjectPool<Client<P, R>> pool = pools[index];
			try {
				client = pool.borrowObject();
				if (client.ping()) {
					// 测试通过认为有效返回连接池负载均衡中有效
					PoolUtils.returnObject(pool, client);
					loadBalance.avaliable(index);
				} else { 
					// 测试不通过，认为无效销毁对象不返回连接池负载均衡中无效
					PoolUtils.invalidateObject(pool, client);
					loadBalance.invalid(index);
					pools[index].clear();
					if (log.isWarnEnabled()) {
						log.warn("Index invalid, ping=false, index=" + index);
					}
				}
			} catch (ClientIOException e) {
				// when ClientFactory.makeObject() throws an ClientIOException.
				PoolUtils.invalidateObject(pool, client);
				loadBalance.invalid(index);
				pools[index].clear();
				if (log.isWarnEnabled()) {
					log.warn("Index invalid, can not open client, index=" + index+ ", " + e.getMessage());
				}
			} catch (Throwable e) {
				// NoSuchElementException - 连接池耗尽，无法提供连接，或者maxWait等待超时。
				PoolUtils.returnObject(pool, client);
				if (log.isWarnEnabled()) {
					log.warn("Heartbeat error, index=" + index + ", " + e.getMessage());
				}
			}
		}
	}

	@Override
	public void destroy() throws Exception {
		this.close();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}
}
