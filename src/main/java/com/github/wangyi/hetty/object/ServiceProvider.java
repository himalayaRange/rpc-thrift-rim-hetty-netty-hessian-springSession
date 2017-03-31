package com.github.wangyi.hetty.object;

import java.io.Serializable;

import com.esotericsoftware.reflectasm.MethodAccess;
/**
 * ]
 * ClassName: ServiceProvider  
 * Function: 服务提供者
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-13 上午10:06:15 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class ServiceProvider implements Serializable {

	private static final long serialVersionUID = 1789959585138445942L;

	private String version;
	
	private Class<?> processorClass;
	
	private MethodAccess methodAccess;
	

	public ServiceProvider() {

	}

	public ServiceProvider(String version, Class<?> processorClass) {
		this.version = version;
		this.processorClass = processorClass;
		this.methodAccess = MethodAccess.get(processorClass);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setProcessorClass(Class<?> processorClass) {
		this.processorClass = processorClass;
	}
	
	public Class<?> getProcessorClass(){
		return processorClass;
	}
	
	public MethodAccess getMethodAccess(){
		return methodAccess;
	}
}
