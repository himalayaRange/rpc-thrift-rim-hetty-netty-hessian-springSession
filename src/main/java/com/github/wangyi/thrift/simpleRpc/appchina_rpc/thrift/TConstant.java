package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-12
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：RPC　REQUEST TConstant
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class TConstant {

	// 服务器正常返回
	public static final int RESPONSE_STATUS_SUCCESS = 0;
	
	// 服务器返回异常
	public static final int RESPONSE_STATUS_ERROR = 1;
	
	// 服务端返回的附加信息的key
	public static final String RESPONSE_MESSAGE="RESPONSE-MESSAGE";
	
	// 请求的凭证key
	public static final String REQUEST_FROM_KEY = "REQUEST_FROM";
	public static final String REQUEST_TOKEN_KEY="REQUEST_TOKEN";
	
	
	
	
}
