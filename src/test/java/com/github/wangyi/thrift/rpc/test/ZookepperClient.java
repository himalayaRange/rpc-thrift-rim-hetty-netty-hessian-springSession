package com.github.wangyi.thrift.rpc.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.wangyi.commons.util.guava.ObjectsUtil;
import com.github.wangyi.thrift.rpc.zookeeper.support.AddressExtendResolve;
import com.github.wangyi.thrift.rpc.zookeeper.support.ZookeeperAddressProvider;


/**
 * 
 * ========================================================
 * 日 期：@2016-12-1
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：对zookpepper服务进行操作
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ZookepperClient {

	@SuppressWarnings("resource")  
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-test-applicationContext.xml");
		ZookeeperAddressProvider provider= (ZookeeperAddressProvider)context.getBean("addressProvider");
		AddressExtendResolve ext= (AddressExtendResolve)context.getBean("addressExtend");
		String server = provider.getService();
		String address = provider.getServicePath();
		System.err.println("获取服务:"+server);
		System.out.println("服务地址："+address);
		System.out.println(ObjectsUtil.stringhelper(provider));
		
		String serverPath= ext.getNodeData("/com.github.wangyi.thrift.rpc.service.HelloWorldService/1.0.0");
		System.out.println("******:"+serverPath);
	}
}
