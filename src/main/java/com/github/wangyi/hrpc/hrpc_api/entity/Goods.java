package com.github.wangyi.hrpc.hrpc_api.entity;

import java.io.Serializable;

public class Goods implements Serializable{

	private static final long serialVersionUID = -4075623885650265576L;

	private String title;
	
	private float price;
	
	public Goods(String title, float price) {
        this.title = title;
        this.price = price;
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
        return "Goods{" +
                "title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
