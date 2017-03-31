package com.github.wangyi.hrpc.hrpc_api.service;

import com.github.wangyi.hrpc.hrpc_api.entity.User;

public interface UserService {

	public User getUser(String phone);
	
	public User updateUser(User user);
	
}
