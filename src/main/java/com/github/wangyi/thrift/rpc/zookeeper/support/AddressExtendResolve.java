package com.github.wangyi.thrift.rpc.zookeeper.support;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.wangyi.thrift.rpc.exception.ThriftException;
import com.github.wangyi.thrift.rpc.zookeeper.AddressExtend;

public class AddressExtendResolve implements AddressExtend{

private static final Logger LOG= LoggerFactory.getLogger(AddressExtendResolve.class);
	
	private CuratorFramework zkClient;
	
	
	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}
	
	public AddressExtendResolve(){
		
	}
	
	public AddressExtendResolve(CuratorFramework zkClient){
		this.zkClient=zkClient;
	}

	/**
	 * 获取节点
	 */
	@Override
	public String getNodeData(String path) {
		try {
			return new String(zkClient.getData().forPath(path));
		} catch (Exception e) {
			LOG.debug("getNodeData exception，case："+e.getMessage());
			throw new ThriftException("getNodeData exception，case：",e);
		}
	}

	/**
	 * 更新节点
	 */
	@Override
	public void updateNodeData(String path,String data) {
		try {
			zkClient.setData().forPath(path,data.getBytes());
		} catch (Exception e) {
			LOG.debug("updateNodeData exception，case："+e.getMessage());
			throw new ThriftException("updateNodeData exception，case：",e);
		}
	}
	
	
	/**
	 * 删除节点
	 */
	@Override
	public void deleNoeData(String path) {
		try {
			zkClient.delete().forPath(path);
		} catch (Exception e) {
			LOG.debug("deleNoeData exception，case："+e.getMessage());
			throw new ThriftException("deleNoeData exception，case：",e);
		}
	}
	
}
