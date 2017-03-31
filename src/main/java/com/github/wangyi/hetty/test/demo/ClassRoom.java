package com.github.wangyi.hetty.test.demo;

import java.io.Serializable;
import java.util.Date;

public class ClassRoom implements Serializable {

	private static final long serialVersionUID = -4334535256287209603L;

	private String roomNum;
	
	private String roomName;
	
	private int totalPerson;

	private double score;
	
	private Date create_date;

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getTotalPerson() {
		return totalPerson;
	}

	public void setTotalPerson(int totalPerson) {
		this.totalPerson = totalPerson;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	
}
