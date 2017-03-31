package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述: 线程安全的客户端接口
 */

public interface ThreadSafetyClient<P,R> {

	/**
	 * 发送请求，并返回服务端结果
	 * @param param
	 * @return
	 * @throws ClientIOException  网络异常
	 * @throws RemoteException	  服务端异常
	 */
	public R send(P param)throws ClientIOException,RemoteException;
	
}
