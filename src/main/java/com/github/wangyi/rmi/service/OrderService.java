package com.github.wangyi.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * 
 * ========================================================
 * 日 期：@2017-2-4
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：通过JNDI来定义接口需要继承Rmote接口
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface OrderService extends Remote{

	String selectOrder(String orderNo)throws RemoteException;
	
}
