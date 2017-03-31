package com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote;

import java.util.Arrays;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-12
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：消息转化器，封装客户端和服务端的转换细节
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public interface MessageConvert<P,R> {


	/**
	 * 包装请求参数，以便发给服务端
	 * */
	public P buildParameters(Param param) throws MessageConvertException;
	
	/**
	 * 读取客户端传过来的参数
	 **/
	public Param readParameters(P parameter)throws MessageConvertException;
	
	/**
	 * 返回结果包装，以便发给客户端
	 **/
	public R buildReturn(Object result)throws MessageConvertException;
	
	/**
	 * 读取服务器的返回结果
	 * */
	public Object readReturn(R result) throws MessageConvertException;
	
	/**
	 * 将异常信息包装，以便发给客户端 
	 **/
	public R buildException(Throwable exception)throws MessageConvertException;
	
	/**
	 * 读取服务端异常信息
	 **/
	public Throwable readException(R result)throws MessageConvertException;
	
	

	/**
	 * 
	 * ========================================================
	 * 日 期：@2016-12-12
	 * 作 者：wangyi
	 * 版 本：1.0.0
	 * 类说明：消息转换异常
	 * TODO
	 * ========================================================
	 * 修订日期 :   
	 * 修订人 :
	 * 描述:
	 */
	public class MessageConvertException extends Exception {
		
		private static final long serialVersionUID = 1L;
		
		public MessageConvertException() { }
		
		public MessageConvertException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public MessageConvertException(String message) {
			super(message);
		}
		
		public MessageConvertException(Throwable cause) {
			super(cause);
		}
	}
	
	/**
	 * 
	 * ========================================================
	 * 日 期：@2016-12-12
	 * 作 者：wangyi
	 * 版 本：1.0.0
	 * 类说明：参数封装
	 * TODO
	 * ========================================================
	 * 修订日期 :   
	 * 修订人 :
	 * 描述:
	 */
	public static class Param {

		public Object[] parameters;
		
		public String path;

		public Param(String path, Object[] parameters) {
			if(parameters != null && parameters.getClass() != Object[].class){
				throw new RuntimeException("parameters type must == Object[].class");
			}
			this.parameters = parameters;
			this.path = path;
		}

		@Override
		public String toString() {
			return "[path=" + path + ", parameters=" + Arrays.toString(parameters) + "]";
		}

	}
	
	
}

