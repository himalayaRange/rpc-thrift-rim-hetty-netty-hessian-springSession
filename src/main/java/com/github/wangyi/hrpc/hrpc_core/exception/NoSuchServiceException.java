package com.github.wangyi.hrpc.hrpc_core.exception;

public class NoSuchServiceException extends RuntimeException{

	private static final long serialVersionUID = 8246321796898362568L;

	public NoSuchServiceException(String msg){
		super(msg);
	}
}
