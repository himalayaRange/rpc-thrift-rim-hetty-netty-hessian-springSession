package com.github.wangyi.thrift.rpc.zookeeper;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-23
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift-sever服务提供者，以便构建客户端连接池
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface AddressProvider {

	/**
	 * 获取服务名称
	 * @return
	 */
	public String getService();
	
	
	/**
	 * 获取所有的服务地址
	 * @return
	 */
	public List<InetSocketAddress> findServerAddressList();
	
	/**
	 * 选取一个合理的address,可以随机获取等,内部可以使用合适的算法.
	 * @return
	 */
	public InetSocketAddress  selector();
	
	/**
	 * 关闭连接
	 */
	public void close();
	
}
