package com.github.wangyi.rmi.constant;
/**
 * 
 * ========================================================
 * 日 期：@2017-2-6
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：zookepper常量配置
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class RConstant {

	/**
	 * zookepper地址
	 */
	public static final String ZK_CONNECTION_STRING="localhost:2181";
	
	/**
	 * zookepper连接超时时间
	 */
	public static final int ZK_SESSION_TIMEOUT=5000;
	
	/**
	 * RMI服务注册根目录
	 */
	public static final String ZK_REGISTRY_PATH="/registry";
	
	/**
	 * RMI服务提供者地址
	 */
	public static final String ZK_PROVIDER_PATH=ZK_REGISTRY_PATH+"/provider";
	
}
