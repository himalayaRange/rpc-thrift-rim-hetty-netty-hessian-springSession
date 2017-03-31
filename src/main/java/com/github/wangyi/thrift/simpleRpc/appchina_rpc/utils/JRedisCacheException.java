package com.github.wangyi.thrift.simpleRpc.appchina_rpc.utils;

public class JRedisCacheException extends Exception{
	private static final long serialVersionUID = 8887861137385128012L;

	public JRedisCacheException() {
		super();
	}


	public JRedisCacheException(String message) {
		super(message);
	}

	public JRedisCacheException(Throwable e) {
		super(e);
	}
}
