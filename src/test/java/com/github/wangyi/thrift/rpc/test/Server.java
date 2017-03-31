package com.github.wangyi.thrift.rpc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-28
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift Server
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class Server {
	@SuppressWarnings("resource")
	public static void main(String[] args) {//spring-context-thrift-server.xml
		new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
