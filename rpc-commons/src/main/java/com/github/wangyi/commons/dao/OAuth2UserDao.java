package com.github.wangyi.commons.dao;

import com.github.wangyi.commons.db.mybatis.MyBatisRepository;
import com.github.wangyi.commons.model.Oauth2User;

@MyBatisRepository
public interface OAuth2UserDao {

	public Integer insertSelective(Oauth2User entity)throws Exception;

}
