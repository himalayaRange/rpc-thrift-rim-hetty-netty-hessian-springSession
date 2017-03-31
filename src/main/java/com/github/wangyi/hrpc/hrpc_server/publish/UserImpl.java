package com.github.wangyi.hrpc.hrpc_server.publish;

import com.github.wangyi.hrpc.hrpc_api.entity.User;
import com.github.wangyi.hrpc.hrpc_api.service.UserService;
import com.github.wangyi.hrpc.hrpc_core.annotation.HRPCService;

@HRPCService(UserService.class)
public class UserImpl implements UserService{

	@Override
    public User getUser(String phone) {
        User user =new User(111,"wangyi",phone);
        return user;
    }

    @Override
    public User updateUser(User user) {
        user.setName("wangyi@sina.com.cn");
        return user;
    }

}
