package com.github.wangyi.rmi.interceptor;

import java.rmi.server.RemoteServer;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 
 * ========================================================
 * 日 期：@2017-1-22
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：方法拦截器，用于RMI安全策略中
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class SecurityInterceptor implements MethodInterceptor{

	private Set<String> allowed;
	
	/**
	 * 
	 * 采用IP策略或者Token策略
	 */
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		//获取RMI远程请求的IP
		String clientHost = RemoteServer.getClientHost();
		if(allowed!=null && allowed.contains(clientHost)){
			return methodInvocation.proceed();
		}else{
			throw new SecurityException("您没有权限访问！");
		}
	}

	public void setAllowed(Set<String> allowed){
		this.allowed=allowed;
	}
	
}
