package com.github.wangyi.rmi.test;

import java.rmi.RemoteException;
import com.github.wangyi.rmi.provider.ServerProvider;
import com.github.wangyi.rmi.service.HelloService;
import com.github.wangyi.rmi.service.OrderService;
import com.github.wangyi.rmi.support.HelloServiceImpl;
import com.github.wangyi.rmi.support.OrderServiceImpl;
/**
 * 
 * ========================================================
 * 日 期：@2017-2-6
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：RMI结合Zookepper发布服务
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RZServer {

	public static void main(String[] args) {
		
		String host = "localhost";
	    
		int port = 1099;

	    try {
	    ServerProvider provider=new ServerProvider();
	    
	    HelloService helloService=new HelloServiceImpl();
	    
	    OrderService orderService=new OrderServiceImpl();
	    
	    provider.publish(orderService,host, port);
	    
		Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException |RemoteException  e) {
			e.printStackTrace();
		}
	}
}
