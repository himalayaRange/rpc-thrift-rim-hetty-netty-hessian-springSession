package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api;

import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.SPI;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Order;


//如果在类上面标注了@SPI,则整个接口将以默认的路径进行服务发布，方法上面的所有标注将以类标注为主
//@SPI(blackList="192.168.1.176",available=false) 黑白名单和是的有效对整个类接口有效
//@SPI(version="1.0.2")注解版本只在类上有效，方法上注解无效
public interface OrderService {

	@SPI("INSERT-ORDER")
	public Order insert(Order order);

	/**
	 * value 自定义服务名称
	 * blackList:黑名单列表
	 * available:接口是都有效，默认有效
	 * @param stid
	 * @return
	 */
	@SPI(value="DELETE-ORDER",blackList="192.168.1.176",available=true)
	public String delete(String stid);
	
	@SPI("UPDATE-ORDER")
	public Order update(Order order);
	
	@SPI("QUERY-ORDER")
	public Order query(String stid);
	
	@SPI("SYSINIT")
	public Order sysInit(Order order,String stid);
}
