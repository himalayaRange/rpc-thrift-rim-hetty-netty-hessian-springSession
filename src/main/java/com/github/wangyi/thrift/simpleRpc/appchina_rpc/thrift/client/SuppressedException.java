package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.client;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-14
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：不填充堆栈信息的异常
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class SuppressedException extends Throwable {

	private static final long serialVersionUID = 1L;

	public SuppressedException(String message){
		super(message);
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
