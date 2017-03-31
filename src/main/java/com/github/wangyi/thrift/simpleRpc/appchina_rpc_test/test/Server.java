package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * 发布服务
 * ========================================================
 * 日 期：@2016-12-16
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：发布Server
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class Server {

	public static ConfigurableApplicationContext ctx;
	
	public static void main(String[] args) {
		ctx=new ClassPathXmlApplicationContext("/spring-thrift-rpc-provider.xml");
		ctx.registerShutdownHook();
		ctx.start();
	}
}
