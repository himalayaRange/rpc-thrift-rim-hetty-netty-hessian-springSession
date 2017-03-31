package com.github.wangyi.rmi.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * ========================================================
 * 日 期：@2017-1-22
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： 公司信息,必须实现Serializable接口
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class Company implements Serializable{

	private static final long serialVersionUID = -2084704858248349156L;

	private String uid;
	
	private String name;
	
	private String address;
	
	private Integer count; //人数

	private Date create_date;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	
}
