package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-14
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：不填充堆栈的信息的异常
 * 	     权限验证异常
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class CredentialException  extends Throwable{
	
	private static final long serialVersionUID = 1L;
	
	public CredentialException() {}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
