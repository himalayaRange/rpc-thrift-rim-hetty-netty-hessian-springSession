package com.github.wangyi.thrift.rpc.zookeeper.support;

import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.util.StringUtils;
import com.github.wangyi.thrift.rpc.constant.SystemConfig;
import com.github.wangyi.thrift.rpc.exception.ThriftException;
import com.github.wangyi.thrift.rpc.zookeeper.AddressRegister;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-23
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：注册服务列表到Zookeeper
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ZookepperAddressRegister implements AddressRegister{

	private static final Logger LOG= LoggerFactory.getLogger(ZookepperAddressRegister.class);
	
	//zk客户端
	private CuratorFramework zkClient;
	
	//zk创建znode节点模式
	private int nodeModel=SystemConfig.DEF_NODEMODEL;
	
	
	public void setNodeModel(int nodeModel) {
		this.nodeModel = nodeModel;
	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}
	
	public ZookepperAddressRegister(){
		
	}
	
	public ZookepperAddressRegister(CuratorFramework zkClient){
		this.zkClient=zkClient;
	}
	
	
	@Override
	public void register(String service, String version, String address) {
		// 如果Zookepper停止抛出异常
		if(zkClient.getState()==CuratorFrameworkState.STOPPED){
			throw new ThriftException("zookepper has stopped,please restart zookepper ....");
		}
		// 如果Zookepper是休眠状态则启动
		if(zkClient.getState()==CuratorFrameworkState.LATENT){
			zkClient.start();
		}
		if(StringUtils.isEmpty(version)){
			//DEFAULT VERSION=1.0.0
			version=SystemConfig.THRIFT_VERSION;
		}
		try {
			//zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/"+service+"/"+version+"/"+address);
			createNode(zkClient, "/"+service+"/"+version+"/"+address, getCreateModel(nodeModel), null);
			LOG.debug("register servic	e address is :{}","/"+service+"/"+version+"/"+address);
		} catch (Exception e) {
			LOG.error("register service address to zookeeper exception:{}",e);
			throw new ThriftException("register service address to zookeeper exception:{}", e);
		}
	}
	
	/**
	 * 获取创建节点模式
	 * @param nodeModel
	 * @return
	 */
	public static CreateMode getCreateModel(int nodeModel){
		if(nodeModel==1){
			return CreateMode.PERSISTENT;
		}else if(nodeModel==2){
			return CreateMode.PERSISTENT_SEQUENTIAL;
		}else if(nodeModel==3){
			return CreateMode.EPHEMERAL;
		}else if(nodeModel==4){
			return CreateMode.EPHEMERAL_SEQUENTIAL;
		}else{
			throw new  ThriftException("can not get CreateModel,the value must in 1~4 ....");
		}
	}

	/**
	 * Zookepper的节点操作：
	 * 	CreateMode.PERSISTENT: 创建节点后，不删除就永久存在
		CreateMode.PERSISTENT_SEQUENTIAL：节点path末尾会追加一个10位数的单调递增的序列
		CreateMode.EPHEMERAL：创建后，回话结束节点会自动删除
		CreateMode.EPHEMERAL_SEQUENTIAL：节点path末尾会追加一个10位数的单调递增的序列
	 * @param zkClient 客户端
	 * @param path 路径
	 * @param createModel 创建节点的模式、
	 * @param data 数据
	 */
	public static void createNode(CuratorFramework zkClient,String path,CreateMode createModel,String data){
		try {
			if(data!=null){
				zkClient.create().creatingParentsIfNeeded().withMode(createModel).forPath(path,data.getBytes());
			}else{
				zkClient.create().creatingParentsIfNeeded().withMode(createModel).forPath(path);
			}
		} catch (Exception e) {
			LOG.error("创建节点失败,case :"+e.getMessage());
		}
	}
	
	@Override
	public void unregister(String name, String version, String address) {
		String servicePath = "/"+name + "/" + version +"/"+ address;
		try {
			if(zkClient.getState()==CuratorFrameworkState.STOPPED){
				throw new ThriftException("zookepper has stopped,please restart zookepper ....");
			}
			if(zkClient.getState() == CuratorFrameworkState.LATENT) {
				zkClient.start();
			}
			if(zkClient.blockUntilConnected(3000, TimeUnit.MILLISECONDS)) {
				zkClient.delete().forPath(servicePath);
				LOG.debug("delete path [{}] successful.", servicePath);
			}
		} catch (InterruptedException e) {
			LOG.error("CuratorFramework Client interrupted. Exception message [{}].", e.getMessage());
			throw new ThriftException("unregister service address to zookeeper block interrupted",e);
		} catch (Exception e) {
			LOG.error("delete path[{}] failed. Message:{}.", servicePath, e.getMessage());
			throw new ThriftException("unregister service address to zookeeper exception:{}", e);
		}
	}

	@Override
	public void close() {
		zkClient.close();
	}


}
