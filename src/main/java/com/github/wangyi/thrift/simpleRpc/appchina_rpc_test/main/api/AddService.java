package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api;

import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.SPI;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-16
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： 发布服务
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface AddService {

	@SPI("ADD")
	public Integer add(Integer param);
	
	@SPI("EXCEPTION")
	public void exception()throws AddServiceException;
	
	public static class AddServiceException extends Exception{
		
		private static final long serialVersionUID = 1L;
		
		public AddServiceException(){
			
		}
		
		public AddServiceException(String message){
			super(message);
		}
	}
}
