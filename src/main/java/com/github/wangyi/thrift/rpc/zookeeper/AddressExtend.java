package com.github.wangyi.thrift.rpc.zookeeper;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-1
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：zookeeper服务拓展操作
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface AddressExtend {
	
	/**
	 * 获取节点
	 */
	public String getNodeData(String path);

	/**
	 * 更新节点
	 */
	public void updateNodeData(String path,String data);
	
	/**
	 * 删除节点
	 */
	public void deleNoeData(String path);
}
