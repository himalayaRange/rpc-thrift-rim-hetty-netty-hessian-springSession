package com.github.wangyi.thrift.rpc.service.support;

import org.apache.thrift.TException;
import com.github.wangyi.thrift.rpc.service.Order;


public class HelloWorldServiceImpl implements com.github.wangyi.thrift.rpc.service.HelloWorldService.Iface{

	@Override
	public String sayHello(String username) throws TException {
		return "hello : " + username;
	}
	
	
	@Override
	public Order sendOrder(Order order) throws TException {
		return order;
	}

}
