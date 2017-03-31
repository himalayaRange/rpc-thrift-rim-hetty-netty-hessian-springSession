package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：客户端IOException
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ClientIOException extends Exception{

	private static final long serialVersionUID = 7822504679761710463L;

	public ClientIOException() {
		super();
	}

	public ClientIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientIOException(String message) {
		super(message);
	}

	public ClientIOException(Throwable cause) {
		super(cause);
	}

}
