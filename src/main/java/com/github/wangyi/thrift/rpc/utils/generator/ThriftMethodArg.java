package com.github.wangyi.thrift.rpc.utils.generator;

import java.lang.reflect.Type;

/**
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
public class ThriftMethodArg {
	
	private Generic genericType;
	
	private String name;

	/**
	 * 
	 */
	public ThriftMethodArg() {
		
	}
	
	public ThriftMethodArg(Type type, String paramName) {
		this.name = paramName;
	}
	
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
	 * @return the generic
	 */
	public Generic getGenericType() {
		return genericType;
	}

	/**
	 * @param generic the generic to set
	 */
	public void setGenericType(Generic generic) {
		this.genericType = generic;
	}
	
	
	
}
