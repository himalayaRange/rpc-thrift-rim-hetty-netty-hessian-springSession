package com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.ThreadSafetyClient;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils.ExceptionUtils;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.IPResolve;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.PathUtils;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.LocalNetworkIPResolve;

/**
 * 
 * ====================客户端===============================
 * 日 期：@2016-12-12
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：用来创建客户端代理，当代理被调用转发到{@link #callRemote(String,Object[])}
 * 		  每个接口只被创建一个实例
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RemoteProxyFactory<T,P,R>  implements FactoryBean<T>{

	private static final Logger LOG = LoggerFactory.getLogger(RemoteProxyFactory.class);
	
	public Class<T> proxyInterface; //代理接口
	
	public String serviceName; //服务名称
	
	public String version="1.0.0";//服务的名称，默认1.0.0
	
	public ThreadSafetyClient<P, R> client; //发送请求客户端
	
	public MessageConvert<P, R> messageConvert; //封装参数客户端
	
	public T proxy; //服务代理
	
	public IPResolve ipResolve;
	
	public RemoteProxyFactory() { }

	public RemoteProxyFactory(Class<T> proxyInterface, String version,ThreadSafetyClient<P, R> client, MessageConvert<P, R> messageConvert) {
		this(proxyInterface, null,version, client, messageConvert);
	}

	public RemoteProxyFactory(Class<T> proxyInterface, String serviceName,String version, ThreadSafetyClient<P, R> client, MessageConvert<P, R> messageConvert) {
		this.proxyInterface = proxyInterface;
		this.serviceName = serviceName;
		this.version=version;
		this.client = client;
		this.messageConvert = messageConvert;
	}

	public void setProxyInterface(Class<T> proxyInterface) {
		this.proxyInterface = proxyInterface;
	}

	public void setClient(ThreadSafetyClient<P, R> client) {
		this.client = client;
	}

	public void setMessageConvert(MessageConvert<P, R> messageConvert) {
		this.messageConvert = messageConvert;
	}
	
	
	public void setIpResolve(IPResolve ipResolve) {
		this.ipResolve = ipResolve;
	}
	

	public void setVersion(String version) {
		this.version = version;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 从工厂中获取实例对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized T getObject() throws Exception {
		if(proxy==null){
			if(client==null){
				throw new IllegalArgumentException("client is null !");
			}
			if(messageConvert==null){
				throw new IllegalArgumentException("messageConvert is null !");
			}
			if(proxyInterface==null){
				throw new IllegalArgumentException("proxyInterface is null !");
			}
			if(!proxyInterface.isInterface()){
				throw new IllegalArgumentException("proxy class not a interface !");
			}
			proxy= (T)Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class<?>[]{proxyInterface}, new ProxyHandler());
			if(LOG.isInfoEnabled()){
				System.out.println("create proxy:" + proxyInterface);
				LOG.info("create proxy:" + proxyInterface);
			}
		}
		return proxy;
	}

	protected class ProxyHandler implements InvocationHandler{

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if(ipResolve==null){
				ipResolve = new LocalNetworkIPResolve();
			}
			String serverIP =ipResolve.getServerIp();
			String path=PathUtils.buildServicePath(serviceName, proxyInterface, method,serverIP,true,version);
			/*if(LOG.isWarnEnabled()){
				LOG.warn("remote rpc IP:"+serverIP+", PATH:"+path);
			}*/
			if(path!=null){
				return callRemote(path,args);
			}else{
				throw new RuntimeException("The method does not support remote invoke:"+method);
			}
		}
		
	}
	
	/**
	 * 工厂类类型
	 */
	@Override
	public Class<?> getObjectType() {
		return proxyInterface;
	}

	/**
	 * 远程RPC调用
	 * @param path 接口路径
	 * @param args 参数
	 * @return
	 * @throws Throwable
	 */
	public Object callRemote(String path, Object[] args) throws Throwable {
		try {
			Param param=new Param(path,args);
			P parameter  = messageConvert.buildParameters(param);
			R result = client.send(parameter);
			Throwable ex = messageConvert.readException(result);
			if(ex!=null){
				throw new RemoteInvocationException(ex);
			}
			return messageConvert.readReturn(result);
		} catch (RemoteInvocationException  e) {
			//抛出原始异常
			Throwable targetException = e.getTargetException();
			targetException.addSuppressed(new Exception());
			throw targetException;
		}catch (Throwable e) {
			//当接口被调用，而实例是个代理，代理之中抛出了接口未定义的异常时，JVM会将这种未定义的异常信息包装为超级丑的 UndeclaredThrowableException。
			//这些都是框架异常，不影响上层异常。
			throw ExceptionUtils.getRuntimeException(e);
		}
	}

	/**
	 * 是否是单利模式
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	
	public static class RemoteInvocationException extends InvocationTargetException{

		private static final long serialVersionUID = 1L;
		
		public RemoteInvocationException(Throwable cause){
			super(cause);
		}

		@Override
		public  Throwable fillInStackTrace() {
			return this;
		}
		
	}

}
