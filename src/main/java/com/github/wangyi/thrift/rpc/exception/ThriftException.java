package com.github.wangyi.thrift.rpc.exception;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-23
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：ThriftException
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述: 支持异步调用
 */
public class ThriftException extends RuntimeException{

	private static final long serialVersionUID = 4053700249326949051L;

	public ThriftException(){
		super();
	}
	
	public ThriftException(String msg){
		super(msg);
	}
	
	public ThriftException(Throwable e){
		super(e);
	}
	
	public ThriftException(String msg,Throwable e){
		super(msg,e);
	}
	
	
}
