package com.github.wangyi.rmi.test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api.OrderService;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity.Order;

/**
 * 
 * ========================================================
 * 日 期：@2017-1-23
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：原生java调用RMI服务
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class JavaRMI {
	private static final String RMI_URL="rmi://192.168.1.176:10080/orderRmiService";
	
	public static void main(String[] args) {
		
		try {
			OrderService order=(OrderService)Naming.lookup(RMI_URL);
			
			Order req=new Order();
			req.setStid("88888");
			req.setName("三星盖乐世S7 edge");
			req.setPrice(6888.00);
			req.setCreateTime(new Date());
			
			Order insert = order.insert(req);
			System.err.println(insert.toString());
			
			String delete = order.delete("88888");
			System.err.println(delete);
			
			Order update = order.update(req);
			System.err.println(update.toString());
			
			Order query = order.query("88888");
			System.err.println(query);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}
}
