package com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.utils;

/**
 * 
 * ========================================================
 * 日 期：@2016-12-8
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： 运行异常Utils
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ExceptionUtils {

	public static RuntimeException getRuntimeException(Throwable e){
		if(e instanceof RuntimeException){
			return (RuntimeException)e;
		}else{
			return new RuntimeException(e);
		}
	}
}
