package com.github.wangyi.hrpc.hrpc_api.entity;

import java.io.Serializable;

public class Order implements Serializable{
	
	private static final long serialVersionUID = -7521527091459009044L;
	private String uuid;
    private long userId;
    private String title;
    private float price;

    public Order(String uuid, long userId, String title, float price) {
        this.uuid = uuid;
        this.userId = userId;
        this.title = title;
        this.price = price;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uuid='" + uuid + '\'' +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
