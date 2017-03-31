package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.server;

import java.util.HashMap;
import java.util.Map;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.TConstant;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-14
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： Server端工具类
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public final  class ServerHelper {

	/**
	 * 创建服务端响应
	 * @return
	 */
	public static Response newResponse(){
		Response response= new Response();
		response.setHeaders(new HashMap<String,String>());
		response.setStatus(TConstant.RESPONSE_STATUS_SUCCESS);
		response.unsetBody();
		return response;
	}
		
	/**
	 * 往客户端结果中添加Error信息，客户端进行验证
	 * @param response
	 * @param message
	 * @return
	 */
	public static Response putError(Response response,String message){
		response.setStatus(TConstant.RESPONSE_STATUS_ERROR);
		Map<String, String> headers = response.getHeaders();
		if(headers==null){
			headers=new HashMap<String,String>();
			response.setHeaders(headers);
		}
		response.getHeaders().put(TConstant.RESPONSE_MESSAGE, message);
		return response;
	}
	
	/**
	 * 验证客户端的凭证
	 * @param request
	 * @param allowedFromTokens 访问凭证无效
	 * @throws CredentialException 
	 */
	public static void valudateCredential(Request request,Map<String,String> allowedFromTokens) throws CredentialException{
		if(request==null){
			throw new CredentialException();
		}
		Map<String, String> requestHeaders = request.getHeaders();
		if(requestHeaders == null){
			throw new CredentialException();
		}
		String from = requestHeaders.get(TConstant.REQUEST_FROM_KEY);
		if(from==null){
			throw new CredentialException();
		}	
		String token=requestHeaders.get(TConstant.REQUEST_TOKEN_KEY);
		if(token==null){
			throw new CredentialException();
		}
		String allowedToken = allowedFromTokens.get(from);
		if(allowedToken ==null||!allowedToken.equals(token)){
			throw new CredentialException();
		}
	}
}
