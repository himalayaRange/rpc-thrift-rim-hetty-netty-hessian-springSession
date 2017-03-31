package com.github.wangyi.thrift.rpc.zookeeper;

import org.apache.curator.framework.CuratorFramework;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-23
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift发布的服务的地址和端口到服务注册中心（zookepper服务器）
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface AddressRegister {

	/**
	 * 发布服务接口
	 * @param service 服务接口名称，一个产品中不能重复
	 * @param version 服务的版本，默认1.0.0
	 * @param address 服务的地址和端口号
	 */
	void register(String service,String version,String address);
	
	/**
	 * 取消服务
	 * @param name 服务名称
	 * @param version 服务版本
	 * @param address 服务地址
	 */
	void unregister(String name,String version,String address);

	/**
	 * 关闭服务
	 */
	void close();

}
