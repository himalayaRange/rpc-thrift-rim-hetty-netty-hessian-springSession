package com.github.wangyi.rmi.exception;

public class SecurityException extends Exception{

	private static final long serialVersionUID = -5663220790902938542L;

	public SecurityException(){
		
	}
	
	public SecurityException(String emsg){
		super(emsg);
	}
}
