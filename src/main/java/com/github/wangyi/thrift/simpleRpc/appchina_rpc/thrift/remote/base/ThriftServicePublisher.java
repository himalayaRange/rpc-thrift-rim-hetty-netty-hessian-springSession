package com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.base;

import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.ServiceDefinition;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.remote.base.ServicePublisher;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.remote.ThriftMessageConvert;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Request;
import com.github.wangyi.thrift.simpleRpc.appchina_rpc.thrift.support.Response;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-15
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：Thrift实现的服务发布处理器
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftServicePublisher extends ServicePublisher<Request, Response>{
	
	public ThriftServicePublisher(){
		this.messageConvert=new ThriftMessageConvert();
	}
	
	public ThriftServicePublisher(ServiceDefinition...definitions){
		super(new ThriftMessageConvert(),definitions);
	}
}
