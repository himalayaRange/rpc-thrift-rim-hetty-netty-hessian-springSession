package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.client;

import java.util.HashMap;
import java.util.Map;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.RemoteException;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.TConstant;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-14
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Client工具类
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public final  class ClientHelper {
	

	/**
	 * 创建请求
	 * @param path
	 * @return
	 */
	public static Request newRequest(String path){
		Request request  = new Request();
		request.setHeaders(new HashMap<String,String>());
		request.setPath(path);
		request.unsetBody();
		return request;
	}
	
	/**
	 * 验证远程服务端是否错误
	 * @param response 服务端返回的数据
	 * @throws RemoteException 服务端异常
	 */
	public static void validateError(Response response)throws RemoteException{
		int status=response.getStatus();
		if(status==TConstant.RESPONSE_STATUS_ERROR){
			Map<String,String> headers = response.getHeaders();
			if(headers==null){
				//System error
				throw new RemoteException();
			}else{
				// User error
				throw new RemoteException(headers.get(TConstant.RESPONSE_MESSAGE));
			}
		}
	}
	
	/**
	 * 客户端写入凭证，服务端进行验证
	 * @param request
	 * @param from
	 * @param token
	 */
	public static void putCredential(Request request,String from,String token){
		if(request==null){
			return;
		}
		if(from==null){
			return;
		}
		if(token==null){
			return;
		}
		Map<String, String> headers = request.getHeaders();
		if(headers==null){
			headers=new HashMap<String,String>();
			request.setHeaders(headers);
		}
		headers.put(TConstant.REQUEST_FROM_KEY, from);
		headers.put(TConstant.REQUEST_TOKEN_KEY, token);
	}
}
