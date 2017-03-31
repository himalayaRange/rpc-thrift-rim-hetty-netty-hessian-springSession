package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.server;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：调用{@link Server#start()}可能抛出的异常，无法启动是抛出异常
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 8887861137385128012L;

	public ServerException() {
		super();
	}

	public ServerException(String message, Throwable e, boolean enableSuppression, boolean writableStackTrace) {
		super(message, e, enableSuppression, writableStackTrace);
	}
	
	public ServerException(String message, Throwable e) {
		super(message, e);
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(Throwable e) {
		super(e);
	}
	
}
