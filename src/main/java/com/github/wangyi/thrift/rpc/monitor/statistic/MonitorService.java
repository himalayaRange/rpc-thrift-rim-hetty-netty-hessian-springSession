package com.github.wangyi.thrift.rpc.monitor.statistic;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-24
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：数据收集服务接口
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
@SuppressWarnings("rawtypes") 
public abstract class MonitorService implements Monitor{

	//多线程下HashMap不安全，而HashTable性能低下，concurrentMap使用分段技术解决此问题
	private final ConcurrentMap<String, AtomicInteger> concurrents = new ConcurrentHashMap<String, AtomicInteger>();
	
	/**
	 * 监控服务启动后调用此方法
	 */
	public abstract void start();
	
	/**
	 * 监控统计数据采集
	 * 记录每次调用的服务名，服务版本，接口，方法，方法参数，并发数，耗时，调用是否成功
	 * @param serviceName
	 * @param serviceVersion
	 * @param clazz
	 * @param method
	 * @param args
	 * @param concurrent
	 * @param takeTime
	 * @param isError
	 */
	public abstract void collect(String serviceName , String serviceVersion , Class clazz, Method method, Object[] args, int concurrent, long takeTime, boolean isError);
	
	
	/**
	 * 获取并发计数器
	 * @param clazz
	 * @param methond
	 * @return
	 */
	public AtomicInteger getConcurrent(Class<?> clazz , Method method){
		String key = clazz.getName() + "." + method.getName();
		AtomicInteger concurrent = concurrents.get(key);
		if(concurrent==null){
			concurrents.putIfAbsent(key, new AtomicInteger());
			concurrent=concurrents.get(key);
		}
		return concurrent;
	}
	
}
