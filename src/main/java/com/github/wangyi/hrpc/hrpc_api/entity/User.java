package com.github.wangyi.hrpc.hrpc_api.entity;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 5450789427608452950L;

	 private long id;
	    private String name;
	    private String phone;


	    public User(long id, String name, String phone) {
	        this.id = id;
	        this.name = name;
	        this.phone = phone;
	    }

	    public long getId() {
	        return id;
	    }

	    public void setId(long id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    @Override
	    public String toString() {
	        return "User{" +
	                "id=" + id +
	                ", name='" + name + '\'' +
	                ", phone='" + phone + '\'' +
	                '}';
	    }
}
