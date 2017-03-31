package com.github.wangyi.thrift.rpc.test;

public class Car {

	private double price;
	
	private String color;

	
	public Car(double price, String color) {
		super();
		this.price = price;
		this.color = color;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Car [price=" + price + ", color=" + color + "]";
	}
	
	
}
