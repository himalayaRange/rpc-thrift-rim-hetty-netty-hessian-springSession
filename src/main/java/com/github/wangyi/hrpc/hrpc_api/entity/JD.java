package com.github.wangyi.hrpc.hrpc_api.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class JD implements Serializable{

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
