package com.github.wangyi.hrpc.hrpc_api.service;

import com.github.wangyi.hrpc.hrpc_api.entity.JD;

public interface JDService {

	public JD insert(JD order);
	
	public String delete(String stid);
	
	public JD update(JD order);
	
	public JD query(String stid);
	
	public JD sysInit(JD order,String stid);
}
