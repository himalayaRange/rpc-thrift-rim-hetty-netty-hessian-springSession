package com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.base;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.server.ServerProcessor;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.IPResolve;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.MessageConvert.Param;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.PathUtils;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.ServiceDefinition;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.LocalNetworkIPResolve;

/**
 * 
 * ===============服务端====================================
 * 日 期：@2016-12-12
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：发布服务
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ServicePublisher<P,R> implements ServerProcessor<P, R> ,InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(ServicePublisher.class);
	
	//private final Map<String,Resource> resources = new HashMap<String,Resource>();
	
	private final ConcurrentMap<String, Resource> resources = new ConcurrentHashMap<String,Resource>(); //多线程下安全的Map
	
	private final Set<Class<?>> verified = new HashSet<Class<?>>();
	
	public MessageConvert<P, R> messageConvert;
	
	private ServiceDefinition[] definitions;
	
	private IPResolve ipResolve;
	
	public ServicePublisher() { }

	public ServicePublisher(MessageConvert<P, R> messageConvert, ServiceDefinition[] definitions) {
		this.messageConvert = messageConvert;
		this.definitions = definitions;
	}

	public void setMessageConvert(MessageConvert<P, R> messageConvert) {
		this.messageConvert = messageConvert;
	}

	public void setIpResolve(IPResolve ipResolve) {
		this.ipResolve = ipResolve;
	}

	public void setDefinitions(ServiceDefinition[] definitions) {
		this.definitions = definitions;
	}
	
	public void init()throws Exception{
		if(messageConvert==null){
			throw new IllegalArgumentException("messageConvert is null");
		}
		if(definitions!=null){
			for(ServiceDefinition def : definitions){
				this.publish(def);
			}
		}
	}
	
	/**
	 * 遍历发布的接口
	 * @param def
	 * @throws Exception
	 */
	private void publish(ServiceDefinition def) throws Exception{
		if(LOG.isInfoEnabled()){
			LOG.info(this.getClass().getSimpleName()+":procesing service definition:"+def);
		}
		if(ipResolve==null){
			ipResolve = new LocalNetworkIPResolve();
		}
		String serverIP =ipResolve.getServerIp(); 
		Class<?> interfaceClass = def.getInterfaceClass(); //接口
		Object implInstance = def.getImplInstance(); //实现类
		validate(interfaceClass, implInstance); //校验
		Method[] interfaceMethods = interfaceClass.getMethods();
		//1.判断接口的类上面是否标注@SPI，如果标注化了@SPI，则该接口的所有方法都将被发布
		//2.发布的服务@SPI注解在类上面
		for(Method method : interfaceMethods){
			String path = PathUtils.buildServicePath(def.getServiceName(), interfaceClass, method,serverIP,false,def.getVersion());
			if(path!=null&&LOG.isWarnEnabled()){
				LOG.warn("publish path:"+serverIP+path);
			}
			if(path != null){
				validateTypes(method);
				Resource resource = new Resource(method, implInstance);
				//将发布的接口放入ConcurrentMap<String,Resource>中
				Resource exist = resources.put(path, resource);
				if(exist != null){
					throw new IllegalArgumentException(this.getClass().getSimpleName() + ": Publish duplicate " + path);
				}
				if(LOG.isInfoEnabled()){
					LOG.info("Publish service [" + path + "]");
				}
			}
		}
			verified.clear();
	}

	private void validateTypes(Method method) throws Exception {
		validateType(method.getReturnType());
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes != null) {
			for (Class<?> parameterType : parameterTypes) {
				validateType(parameterType);
			}
		}
	}

	private void validateType(Class<?> type) throws Exception {
		if(verified.add(type)){
			if(type.isPrimitive() || type.isEnum()){//可以是基本类型，枚举
				return;
			}
			if(type.isInterface() || Modifier.isAbstract(type.getModifiers())){//可以是接口或抽象类
				return;
			}
			if (type.isArray()) {//可以是数组，但是要检查单个元素
				validateType(type.getComponentType()); 
				return;
			}
			if(type.isAnonymousClass()){//不可以是匿名内部类
				throw new IllegalArgumentException(type + " not be anonymous class ");
			}
			if(Modifier.isPrivate(type.getModifiers()) ){//不可以是private修饰的类
				throw new IllegalArgumentException(type + " not be private class ");
			}
			if(!Serializable.class.isAssignableFrom(type)){//必须实现序列化
				throw new IllegalArgumentException(type + " must be " + Serializable.class.getName());
			}
			BeanInfo info = Introspector.getBeanInfo(type);
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals("class")) continue;
				validateType(pd.getPropertyType());
			}
		}
	}

	private void validate(Class<?> interfaceClass, Object implInstance) throws Exception {
		if(interfaceClass == null || implInstance == null){
			throw new IllegalArgumentException("PublishDetail error, interfaceClass:" + interfaceClass + ", implInstance" + implInstance);
		}
		if(!interfaceClass.isInterface()){
			throw new IllegalArgumentException(interfaceClass + " must be interface");
		}
		if(!interfaceClass.isAssignableFrom(implInstance.getClass())){
			throw new IllegalArgumentException(implInstance.getClass() + " must be " + interfaceClass);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}

	@Override
	public R processor(P parameter) throws Throwable {
		try{
		Param param = messageConvert.readParameters(parameter);
		Resource resource = resources.get(param.path);
		if(resource == null){
			throw new NoSuchMethodException("service [" + param.path + "] not support");
		}
		Object result = resource.method.invoke(resource.implInstance, param.parameters);
		return messageConvert.buildReturn(result);
		}catch(InvocationTargetException e){
			//仅拦截应用层异常，其他异常视为框架BUG,将被客户端包装成RemoteException抛出
			return messageConvert.buildException(e.getTargetException());
		}catch (Throwable e) {
			throw e;
		}
	}

	private static class Resource {
		Method method;
		Object implInstance;
		public Resource(Method method, Object implInstance) throws Exception {
			this.method = method;
			this.implInstance = implInstance;
		}
		@Override
		public String toString() {
			return "[method=" + method + ", implInstance=" + implInstance + "]";
		}
	}
	
	
}
