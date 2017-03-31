package com.github.wangyi.thrift.rpc.zookeeper.support;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import com.alibaba.druid.util.StringUtils;
import com.github.wangyi.thrift.rpc.constant.SystemConfig;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-24
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：zookeeper工厂类，创建zk客户端连接
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ZookepperFactory implements FactoryBean<CuratorFramework>{

	// zk地址集合
	private String zkHosts;
	
	// session超时
	private int sessionTimeout=SystemConfig.SESSION_TTMEOUT;
	
	// 连接超时
	private int connectionTimeout=SystemConfig.CONNECTION_TTMEOUT;
	
	//默认重连接次数
	private int maxRetryPoliy=SystemConfig.MAX_TETRYPOLIY;
	
	//是否共享一个zk（单例）
	private boolean singleton= true;
	
	//namespace，全局路径前缀，区分不同的应用
	private String namespace;
	
	//path前缀
	private static final String ROOT="rpc";
	
	//zk客户端连接
	private CuratorFramework zkClient;
	
	public void setZkHosts(String zkHosts) {
		this.zkHosts = zkHosts;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setMaxRetryPoliy(int maxRetryPoliy) {
		this.maxRetryPoliy = maxRetryPoliy;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}

	public CuratorFramework create(){
		if(StringUtils.isEmpty(namespace)){
			namespace=ROOT;
		}else{
			namespace=ROOT+"/"+namespace;
		}
		return create(zkHosts, sessionTimeout, connectionTimeout, namespace);
	}
	
	/**
	 *  Curator Framework提供了简化使用zookeeper更高级的API接口。它包涵很多优秀的特性，主要包括以下三点：
		自动连接管理：自动处理zookeeper的连接和重试存在一些潜在的问题；可以watch NodeDataChanged event和获取updateServerList;Watches可以自动被Cruator recipes删除；
		更干净的API：简化raw zookeeper方法，事件等；提供现代流式API接口
		Recipe实现：leader选举，分布式锁，path缓存，和watcher,分布式队列等。
		Zookeeper在实际生产环境中应用比较广泛，比如SOA的服务监控系统，Hadoop，spark的分布式调度系统。Curator框架提供的优秀特性可以使得我们更加便捷的开发zookeeper应用
	 * @param connectString
	 * @param sessionTimeout
	 * @param connectionTimeout
	 * @param namespace
	 * @return
	 */
	private CuratorFramework create(String connectString, int sessionTimeout,int connectionTimeout, String namespace) {
		Builder builder = CuratorFrameworkFactory.builder();
		//ExponentialBackoffRetry retryPoliy = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE); //重连机制
		return builder
				.connectString(connectString)
				.sessionTimeoutMs(sessionTimeout)
				.connectionTimeoutMs(connectionTimeout)
				.canBeReadOnly(true)
				.namespace(namespace)
				.retryPolicy(new ExponentialBackoffRetry(1000, maxRetryPoliy))
				.defaultData(null)
				.build();
	}

	
	public void close(){
		if(zkClient!=null){
			zkClient.close();
		}
	}
	
	@Override
	public CuratorFramework getObject() throws Exception {
		if(singleton){
			if(zkClient==null){
				zkClient=create();
				zkClient.start();
			}
			return zkClient;
		}
		return create();
	}

	@Override
	public Class<?> getObjectType() {
		
		return CuratorFramework.class;
	}

	@Override
	public boolean isSingleton() {
		
		return singleton;
	}

}
