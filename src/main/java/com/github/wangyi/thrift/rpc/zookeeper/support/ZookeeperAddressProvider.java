package com.github.wangyi.thrift.rpc.zookeeper.support;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.github.wangyi.thrift.rpc.constant.SystemConfig;
import com.github.wangyi.thrift.rpc.exception.ThriftException;
import com.github.wangyi.thrift.rpc.thrift.ThriftServiceClientProxyFactory;
import com.github.wangyi.thrift.rpc.zookeeper.AddressProvider;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-23
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：使用zookepper作为config中心，通过Apache-curator简化zookepper开发
 * 		 AddressProvider:Thrift服务提供者
 * 		 InitializingBean：为bean对象提供初始化方法，方法体afterPropertiesSet
 * 		 ApplicationContextAware：spring容器初始化时可以方便的获取所有的bean，方法体setApplicationContext
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ZookeeperAddressProvider implements AddressProvider,InitializingBean,ApplicationContextAware{

	private static final Logger LOG = LoggerFactory.getLogger(ZookeeperAddressProvider.class);
	
	//服务的版本号
	private String version = SystemConfig.THRIFT_VERSION;
	
	//默认权重
	private static final Integer DEFAULT_WEIGHT = SystemConfig.THRIFT_WEIGHT;
	
	//加锁对象
	private Object lock = new Object();
	
	// 用来保存当前provider所接触过的地址记录,当zookeeper集群故障时,可以使用trace中地址,作为"备份"
	private Set<String> trace = new HashSet<String>();
	
	// IP套接字地址（IP地址+端口号）容器,valatile强制从共享内存中获取最新的值并进行同步，保证缓存数据和共享内存数据一致
	private volatile List<InetSocketAddress> container = new ArrayList<InetSocketAddress>();
	
	// IP套接字地址（IP地址+端口号）队列
	private Queue<InetSocketAddress> inner = new LinkedList<InetSocketAddress>();
		
	// 注册服务
	private String service;
	
	// 监控ZKNode的子节点
	private PathChildrenCache cachedPath;
	
	//zk客户端连接
	private CuratorFramework zkClient;
	
	//Thfift客户端代理工厂
	private ThriftServiceClientProxyFactory clientProxyFactory; 
	
	public ZookeeperAddressProvider() {
	}

	public ZookeeperAddressProvider(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}
	
	
	public void setService(String service) {
		this.service = service;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}
	
	
	public Object getLock() {
		return lock;
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}

	public Set<String> getTrace() {
		return trace;
	}

	public void setTrace(Set<String> trace) {
		this.trace = trace;
	}

	public List<InetSocketAddress> getContainer() {
		return container;
	}

	public void setContainer(List<InetSocketAddress> container) {
		this.container = container;
	}

	public Queue<InetSocketAddress> getInner() {
		return inner;
	}

	public void setInner(Queue<InetSocketAddress> inner) {
		this.inner = inner;
	}

	public PathChildrenCache getCachedPath() {
		return cachedPath;
	}

	public void setCachedPath(PathChildrenCache cachedPath) {
		this.cachedPath = cachedPath;
	}

	public ThriftServiceClientProxyFactory getClientProxyFactory() {
		return clientProxyFactory;
	}

	public void setClientProxyFactory(
			ThriftServiceClientProxyFactory clientProxyFactory) {
		this.clientProxyFactory = clientProxyFactory;
	}

	public String getVersion() {
		return version;
	}

	public CuratorFramework getZkClient() {
		return zkClient;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.clientProxyFactory=(ThriftServiceClientProxyFactory) applicationContext.getBean(ThriftServiceClientProxyFactory.class);
	}

	/**
	 * 成功注入bean实例后，进行一些资源的初始化操作
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(zkClient.getState()==CuratorFrameworkState.STOPPED){
			throw new ThriftException("zookepper has stopped,please restart zookepper ....");
		}
		// 如果zk没有启动，则启动
		if(zkClient.getState()==CuratorFrameworkState.LATENT){
			zkClient.start();
		}
		// 构建注册服务缓存
		buildPathChildrenCache(zkClient, getServicePath(), true);
		cachedPath.start(StartMode.POST_INITIALIZED_EVENT);
	}
	

	/**
	 * 获取zk服务的路径，注册服务的路径是："/" + service + "/" + version+"/address"形成一个tree的结构
	 * @return
	 */
	public String getServicePath(){
		return "/" + service + "/" + version;
	}

	/**
	 * 构建cachePath
	 * @param client
	 * @param path
	 * @param cacheData
	 */
	private void buildPathChildrenCache(final CuratorFramework client,String path, boolean cacheData) {
		cachedPath=new PathChildrenCache(client, path, cacheData);
		cachedPath.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)throws Exception {
				PathChildrenCacheEvent.Type eventType = event.getType();
				switch (eventType) {
				case CONNECTION_RECONNECTED:
					LOG.info("***************--->Connection is reconection.");
					break;
				case CONNECTION_SUSPENDED:
					LOG.info("**************--->Connection is suspended.");
					break;
				case CONNECTION_LOST:
					LOG.warn("****************--->Connection error,waiting...");
					return;
				default:
				}
				//任何节点的数据都随时变动，都会rebuild,此处做个简单的做法
				cachedPath.rebuild();
				rebuild();
			}
			
			protected void rebuild() throws Exception {
				List<ChildData> children = cachedPath.getCurrentData();
				if (children == null || children.isEmpty()) {
					// 有可能所有的thrift server都与zookeeper断开了链接
					// 但是,有可能,thrift client与thrift server之间的网络是良好的
					// 因此此处是否需要清空container,是需要多方面考虑的.
					container.clear();
					LOG.error("*****************--->thrift server-cluster error....");
					return;
				}
				List<InetSocketAddress> current = new ArrayList<InetSocketAddress>();
				String path = null;
				for (ChildData data : children) {
					path = data.getPath();
					LOG.debug("**************--->get path:{}", path);
					path = path.substring(getServicePath().length()+1);
					LOG.debug("****************--->get serviceAddress:{}", path);
					String address = new String(path.getBytes(), "utf-8");
					current.addAll(transfer(address));
					trace.add(address);
				}
				//集合打乱
				Collections.shuffle(current);
				synchronized (lock) {
					container.clear();
					container.addAll(current);
					inner.clear();
					inner.addAll(current);
					clientProxyFactory.buildPools();
				}
			}
			
		});
	}
	
	
	/**
	 * 根据权重将address添加到集合
	 * @param address
	 * @return
	 */
	private List<InetSocketAddress> transfer(String address) {
		String[] hostname = address.split(":");
		Integer weight = DEFAULT_WEIGHT;
		if (hostname.length == 3) {
			weight = Integer.valueOf(hostname[2]);
		}
		String ip = hostname[0];
		Integer port = Integer.valueOf(hostname[1]);
		List<InetSocketAddress> result = new ArrayList<InetSocketAddress>();
		// 根据优先级，将ip：port添加多次到地址集中，然后随机取地址实现负载
		for (int i = 0; i < weight; i++) {
			result.add(new InetSocketAddress(ip, port));
		}
		return result;
	}
	
	@Override
	public String getService() {
		return service;
	}

	/**
	 * Collections.unmodifiableList不可修改的Collections
	 */
	@Override
	public List<InetSocketAddress> findServerAddressList() {
		return Collections.unmodifiableList(container);
	}

	/**
	 * 获取一个合适的Address,可以随机获取等，内部可以使用合适的算法
	 */
	@Override
	public  synchronized InetSocketAddress selector() {
		if(inner.isEmpty()){
			if(!container.isEmpty()){
				inner.addAll(container);
			}else if(!trace.isEmpty()){
				synchronized (lock) {
					for(String hostname:trace){
						container.addAll(transfer(hostname));
					}
					Collections.shuffle(container);
					inner.addAll(container);
				}
			}
		}
		return inner.poll();
	}

	/**
	 * 关闭资源
	 */
	@Override
	public void close() {
		try {
			cachedPath.close();
			zkClient.close();
		} catch (Exception e) {
			LOG.debug("zookperProvider close exception,e"+e);
		}
	}

}
