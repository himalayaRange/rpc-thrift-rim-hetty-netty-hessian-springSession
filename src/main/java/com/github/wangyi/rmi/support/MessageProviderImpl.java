package com.github.wangyi.rmi.support;

import com.github.wangyi.rmi.service.MessageProvider;

public class MessageProviderImpl implements MessageProvider {

	@Override
	public String queryForMessage(String name) {
		return "Hello, " + name;
	}

}