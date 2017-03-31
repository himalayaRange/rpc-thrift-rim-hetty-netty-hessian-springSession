package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.server;
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
 * 描述:服务端标准接口
 */
public interface Server {

	/**
	 * 服务启动
	 * @throws ServerException 
	 */
	public void start()throws ServerException;
	
	/**
	 * 停止服务
	 */
	public void stop();

}
