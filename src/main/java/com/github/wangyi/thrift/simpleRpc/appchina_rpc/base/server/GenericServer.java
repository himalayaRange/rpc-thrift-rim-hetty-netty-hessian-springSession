package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.server;

import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：通用Server模板,用来继承
 * 		 InitializingBean:Spring容器初始化Bean后进行的一些初始化
 * 		 DisposableBean：Spring容器销毁Bean时候调用的destory方法
 *  	 Ordered：接口实现排序问题
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public abstract class GenericServer<P,R> implements Server,InitializingBean,DisposableBean,Ordered  {

	protected static final Logger LOG = LoggerFactory.getLogger(GenericServer.class);
	
	protected ServerProcessor<P, R> processor;

	public void setProcessor(ServerProcessor<P, R> processor) {
		this.processor = processor;
	}
	
	public GenericServer(){
		
	}
	
	public GenericServer(ServerProcessor<P, R> processor){
		this.processor=processor;
	}
	
	/**
	 * 将请求交给{@link ServerProcessor}处理
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	public R doServerProcessor(P param)throws Throwable{
		return processor.processor(param);
	}
	
	/**
	 * 启动服务
	 */
	@Override
	public final void start()throws ServerException {
		if(processor==null){
			throw new  ServerException("server processor is null");
		}
		try {
			if(LOG.isInfoEnabled()){
				LOG.info("remote server Starting ...."+toString());
			}
			startServer();
			if(LOG.isInfoEnabled()){
				LOG.info("remote server has started"+toString());
			}
		} catch (Exception e) {
			throw new ServerException(e);
		}
	}

	/**
	 * 停止服务
	 */
	@Override
	public final void stop() {
		if(LOG.isInfoEnabled()){
			LOG.info("remote server stoping.... " + toString());
		}
		stopServer();
		if(LOG.isInfoEnabled()){
			LOG.info("remote server has stoped " + toString());
		}
	}
	
	/**
	 * 抽象接口用来继承
	 * 具体的启动服务的实现
	 * @throws TTransportException
	 */
	protected abstract void startServer() throws TTransportException;
	
	/**
	 * 抽象接口用来继承
	 * 具体的停止服务实现
	 */
	protected abstract void stopServer();

	
	/**
	 * Spring是一个使用很多策略设计的框架，通过orderd来处理接口相同的优先级问题
	 * 设置为最低级别（数值最大）
	 */
	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 销毁对象停止服务
	 */
	@Override
	public void destroy() throws Exception {
		this.stop();
	}

	/**
	 * 创建Bean对象后启动服务
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.start();
	}
	
	
}
