package com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-12
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：服务的接口和实现类
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ServiceDefinition {

	/**
	 * 服务接口类
	 */
	private Class<?> interfaceClass;
	
	/**
	 * 接口实现者
	 */
	private Object implInstance;
	
	/**
	 * 服务名称
	 */
	private String serviceName;
	
	/**
	 * 接口版本号，默认1.0.0
	 */
	private String version = "1.0.0"; 
	
	public ServiceDefinition() {
		super();
	}

	public ServiceDefinition(Class<?> interfaceClass, Object implInstance,String version) {
		this.interfaceClass = interfaceClass;
		this.implInstance = implInstance;
		this.version=version;
	}

	public ServiceDefinition(Class<?> interfaceClass, Object implInstance, String serviceName,String version) {
		this.interfaceClass = interfaceClass;
		this.implInstance = implInstance;
		this.serviceName = serviceName;
		this.version=version;
	}
	
	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public Object getImplInstance() {
		return implInstance;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public void setImplInstance(Object implInstance) {
		this.implInstance = implInstance;
	}

	public String getServiceName() {
		return serviceName;
	}
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "[interfaceClass=" + interfaceClass + ", implInstance=" + implInstance + ", serviceName=" + serviceName + "+version="+version+"]";
	}

}
