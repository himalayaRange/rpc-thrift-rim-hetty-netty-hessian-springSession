package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.impl;

import java.util.Date;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.SPI;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api.OrderService;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Order;

public class OrderServiceImpl implements OrderService {

	@Override
	@SPI("INSERT-ORDER")
	public Order insert(Order order) {
		return order;
	}

	@Override
	@SPI("DELETE-ORDER")
	public String delete(String stid) {
		return "return:"+stid;
	}

	@Override
	@SPI("UPDATE-ORDER")
	public Order update(Order order) {
		order.setStid("update:"+order.getStid());
		return order;
	}

	@Override
	@SPI("QUERY-ORDER")
	public Order query(String stid) {
		Order order = new Order();
		order.setStid("查询的商品STID："+stid);
		order.setName("三星盖乐世S6");
		order.setPrice(5000.00);
		order.setCreateTime(new Date());
		return order;
	}

	@Override
	@SPI("SYSINIT")
	public Order sysInit(Order order, String stid) {
		order.setStid(stid);
		return order;
	}

}
