package com.github.wangyi.thrift.rpc.monitor;


import net.sf.cglib.proxy.Proxy;

import com.github.wangyi.thrift.rpc.monitor.statistic.MonitorService;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-28
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：服务代理类
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ServiceProxy {

	/**
	 * 生成代理类<类加载器，类实现接口，动态代理类>
	 * @param service
	 * @param serviceName
	 * @param serviceVersion
	 * @param monitorService
	 * @param isMonitor
	 * @return
	 */
	public Object wrapper(Object service,String serviceName,String serviceVersion,MonitorService monitorService,boolean isMonitor){
		return Proxy.newProxyInstance(
				service.getClass().getClassLoader(), 
				service.getClass().getInterfaces(),
				new ServiceProxyHandler(service, serviceName, serviceVersion, monitorService, isMonitor));
		
	}
}
