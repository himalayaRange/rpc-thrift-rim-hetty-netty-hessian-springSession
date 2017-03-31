package com.github.wangyi.hetty.object;

import java.io.Serializable;
/**
 * 
 * ClassName: RequestWrapper  
 * Function: 请求数据包装类
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-10 下午5:40:14 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class RequestWrapper implements Serializable{

	private static final long serialVersionUID = -8394480769991220264L;

	private String user = null;
	
	private String password = null;
	
	private String serviceName; //服务名称
	
	private String methodName;  //执行的方法名称
	
	private Object[] args = null; //参数
	
	private Class<?>[] argsTypes =null; //参数类型
	
	private String clientIP = null;
	
	public RequestWrapper(){
		
	}
	
	public RequestWrapper(String user, String password,String clientIP,String serviceName) {
		this.user = user;
		this.password = password;
		this.clientIP = clientIP;
		this.serviceName = serviceName;
	}
	
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Class<?>[] getArgsTypes() {
		return argsTypes;
	}

	public void setArgsTypes(Class<?>[] argsTypes) {
		this.argsTypes = argsTypes;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
}
