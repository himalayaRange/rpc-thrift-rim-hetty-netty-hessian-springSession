package com.github.wangyi.rmi.test;

import java.rmi.RemoteException;
import com.github.wangyi.rmi.customer.ServiceConsumer;
import com.github.wangyi.rmi.service.HelloService;
import com.github.wangyi.rmi.service.OrderService;

/**
 * 
 * ========================================================
 * 日 期：@2017-2-6
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：RMI+Zookepper客户端
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RZClient {
	public static void main(String[] args) throws RemoteException {
		 ServiceConsumer consumer = new ServiceConsumer();
		 while(true){
			 // HelloService helloService  = consumer.lookup();
			 // String ret = helloService.sayHello("wangyi");
			 OrderService orderService=consumer.lookup();
			 String ret = orderService.selectOrder("245415647685");
			 System.out.println(ret);
	         try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 }
	}
}
