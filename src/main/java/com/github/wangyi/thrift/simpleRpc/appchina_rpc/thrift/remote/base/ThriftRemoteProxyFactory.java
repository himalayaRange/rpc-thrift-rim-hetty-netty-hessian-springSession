package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.base;

import com.github.wangyi.thrift.simpleRpc.appchina_rpc.base.client.ThreadSafetyClient;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.base.RemoteProxyFactory;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.ThriftMessageConvert;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-15
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift实现的远程代理工厂
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftRemoteProxyFactory<T> extends RemoteProxyFactory<T, Request, Response> {

	public ThriftRemoteProxyFactory(){
		this.messageConvert=new ThriftMessageConvert();
	}
	
	public ThriftRemoteProxyFactory(Class<T> proxyInterface,String version, ThreadSafetyClient<Request, Response> client) {
		super(proxyInterface,version, client, new ThriftMessageConvert());
	}
	
	public ThriftRemoteProxyFactory(Class<T> proxyInterface,String serviceName,String version,ThreadSafetyClient<Request, Response> client){
		super(proxyInterface, serviceName,version, client, new ThriftMessageConvert());
	}
	
	
}
