package com.github.wangyi.thrift.rpc.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ========================================================
 * 日 期：@2016-11-30
 * 作 者：wangyi
 * 版 本：1.0.0
 * 类说明：订单系统
 * TODO
 * ========================================================
 * 修订日期 :   
 * 修订人 :
 * 描述:
 */
public class Order implements Serializable{

	private static final long serialVersionUID = -4030317140124062803L;
	
	private String orderNo;
	
	private String orderName;
	
	private Long orderPrice;
	
	private Date createTime;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Long getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Long orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
