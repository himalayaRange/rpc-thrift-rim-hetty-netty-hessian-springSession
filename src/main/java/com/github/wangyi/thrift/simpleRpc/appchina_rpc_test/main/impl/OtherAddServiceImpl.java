package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.impl;

import com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.api.AddService;

public class OtherAddServiceImpl implements AddService{

	@Override
	public Integer add(Integer param) {
		return ++param;
	}

	@Override
	public void exception() throws AddServiceException {
		
	}

}
