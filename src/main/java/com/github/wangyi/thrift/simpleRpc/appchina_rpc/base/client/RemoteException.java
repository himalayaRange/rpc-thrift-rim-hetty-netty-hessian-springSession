package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述: 服务端出现异常，不建议直接抛出，而是将异常信息进行包装返回进行处理<br>
 */

public class RemoteException extends Exception{

	private static final long serialVersionUID = -2685025519294704329L;

	
	public RemoteException(){
		super();
	}
	
	public RemoteException(String message){
		super(message);
	}
	
}
