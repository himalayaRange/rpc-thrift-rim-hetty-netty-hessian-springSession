package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-15
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift 客户端和服务端通用配置
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftGenericConfig {

	// 客户端默认配置IP,port
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final int DEFAULT_PORT= 9090;
	
	//服务端的默认配置
	public static final int WORKERTHREADS = 5;     // 工作线程数
	public static final int CLIENTTIMEOUT = 30000; // 客户端过期时间
	public static final boolean SECURITY = false;  // 是否加密
	public static final int STOPTIMEOUTVAL = 2000; // 服务停止超时时间
	public static final int MINWORKERRHREAD = 1 ;  // 最小线程数
	public static final int SELECTORTHREADS= 2 ;   // 选择线程数
	
}
