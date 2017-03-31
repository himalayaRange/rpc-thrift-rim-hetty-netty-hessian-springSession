package com.github.wangyi.thrift.rpc.utils.generator;

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
public class ThriftEnumField {
	private String name;
	private Integer value;
	
	
	/**
	 * @param name
	 * @param value
	 */
	public ThriftEnumField(String name, Integer value) {
		super();
		this.name = name;
		this.value = value;
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
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}
	
	
}
