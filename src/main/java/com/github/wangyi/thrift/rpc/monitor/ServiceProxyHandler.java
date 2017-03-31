package com.github.wangyi.thrift.rpc.monitor;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.InvocationHandler;
import com.github.wangyi.thrift.rpc.monitor.statistic.MonitorService;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-24
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：服务功能动态代理类
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ServiceProxyHandler implements InvocationHandler{

	private Object service;
	
	private String serviceName;
	
	private String serviceVersion;
	
	private MonitorService monitorService;
	
	private Boolean isMonitor;
	
    protected ServiceProxyHandler(Object service, String serviceName, String serviceVersion, MonitorService monitorService, boolean isMonitor){
    	this.service=service;
    	this.serviceName = serviceName;
    	this.serviceVersion = serviceVersion;
    	this.isMonitor = isMonitor;
    	this.monitorService = monitorService;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long startTime = System.currentTimeMillis();
		
		if(isMonitor){
			this.monitorService.getConcurrent(this.service.getClass(), method).incrementAndGet();//并发计数返回新值
		}
		
		Object result=null;
		try {
			result=method.invoke(this.service, args); 
			if(isMonitor){
				long endTime = System.currentTimeMillis();
				long takeTime = endTime-startTime;
				int concurrent = this.monitorService.getConcurrent(this.service.getClass(), method).get(); // 从AtomicInteger获取并发数
				this.monitorService.collect(serviceName, serviceVersion, this.service.getClass(), method, args, concurrent, takeTime, false);
			}
		} catch (Exception e) {
			if (isMonitor) {
				long endTime=System.currentTimeMillis(); // 记录结束时间戮
				long takeTime = endTime - startTime;
				int concurrent = this.monitorService.getConcurrent(this.service.getClass(), method).get(); // 当前并发数
				this.monitorService.collect(serviceName, serviceVersion, this.service.getClass(), 
						method, args, concurrent, takeTime, true);
			}
			throw e;
		}finally{
			if(isMonitor){
				this.monitorService.getConcurrent(this.service.getClass(), method).decrementAndGet();//并发计数
			}
		}
		return result;
	}

}
