package com.github.wangyi.thrift.rpc.utils.generator;

import java.util.List;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-30
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class ThriftService {
	
	private String name;
	
	private List<ThriftMethod> methods;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the methods
	 */
	public List<ThriftMethod> getMethods() {
		return methods;
	}

	/**
	 * @param methods the methods to set
	 */
	public void setMethods(List<ThriftMethod> methods) {
		this.methods = methods;
	}
	
	
}
