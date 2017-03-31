package com.github.wangyi.hrpc.hrpc_core.client;
/**
 * 
 * ClassName: AsyncRPCCallback  
 * Function:  异步RPC请求回调
   <br>
 * @author wangyi
   <br>
 * @date: 2017-2-24 下午3:58:04 
   <br> 
 * @version  
   <br>
 * @since JDK 1.7
 */
public interface AsyncRPCCallback {

	/**
	 * 成功请求回调
	 * @param result
	 *
	 */
	void success(Object result);
	
	/**
	 * 异常请求回调
	 * @param e
	 *
	 */
	void fail(Exception e);
	
}
