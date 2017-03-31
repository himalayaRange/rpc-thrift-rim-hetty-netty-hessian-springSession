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
 * 描述: 服务端处理器的标准开放接口{@link GenericServer}接收到的任何请求都由此类处理，处理具体的业务逻辑
 */
public interface ServerProcessor<P,R> {

	/**
	 * 获取标准处理器
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	public abstract R processor(P param)throws Throwable;

}
