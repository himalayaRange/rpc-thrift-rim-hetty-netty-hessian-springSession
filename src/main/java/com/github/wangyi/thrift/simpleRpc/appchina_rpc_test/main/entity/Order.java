package com.github.wangyi.thrift.simpleRpc.appchina_rpc_test.main.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
/**
 * 
 * ========================================================
 * 日 期：@2016-12-22
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明： 订单实体类
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class Order implements Serializable{

	private static final long serialVersionUID = 3482746129109212805L;

	private String stid;
	
	private String name;
	
	private double price;
	
	private byte[] picture;
	
	private Date createTime;

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Order [stid=" + stid + ", name=" + name + ", price=" + price
				+ ", picture=" + Arrays.toString(picture) + ", createTime="
				+ createTime + "]";
	}
	
	
}
