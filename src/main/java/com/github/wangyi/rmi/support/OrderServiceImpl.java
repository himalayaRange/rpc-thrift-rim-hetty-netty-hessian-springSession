package com.github.wangyi.rmi.support;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import com.github.wangyi.rmi.service.OrderService;
/**
 * 
 * ========================================================
 * 日 期：@2017-2-4
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：通过JNDI发布服务需要继承UnicastRemoteObject类
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class OrderServiceImpl extends UnicastRemoteObject implements OrderService{

	private static final long serialVersionUID = -8763746201809581159L;

	public OrderServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public String selectOrder(String orderNo) throws RemoteException {
		return String.format("您的订单号为： %s",orderNo);
	}

}
