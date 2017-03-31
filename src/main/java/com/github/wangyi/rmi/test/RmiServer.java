package com.github.wangyi.rmi.test;

import org.springframework.context.ApplicationContext;
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
public class RmiServer {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext  ctx = new ClassPathXmlApplicationContext("classpath:rmi/spring-rmi-provider.xml");
		System.out.println("已成功发布RMI服务类");
	}
}
