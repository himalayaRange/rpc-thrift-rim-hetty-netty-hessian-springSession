package com.github.wangyi.hrpc.hrpc_core.protocol;
/**
 * 
 * ClassName: RPCRequest  
 * Function: HRPC request 包 
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-21 上午11:36:18 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public class RPCRequest {
	//请求ID
	private String requestId;
	//类名称（可以定义成类的全路径名称）
	private String className;
	//方法名称
	private String methodName;
	//请求参数
	private Object[] parameters;
	//请求参数类型
	private Class<?>[] parameterTypes;
	
	public String getRequestId() {
		return requestId;
	}
	
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public Object[] getParameters() {
		return parameters;
	}
	
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	
}
