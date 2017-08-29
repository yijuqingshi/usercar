package com.kuxiao.usercar.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {

	private static final long serialVersionUID = 3484941368580743019L;
	private Date time;
	private String userId;
	private String startName;
	private String endName;
	private String text;
	private String drivieId;
	private boolean isFinished;
	private String phoneNumber;
	private int price;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStartName() {
		return startName;
	}

	public void setStartName(String startName) {
		this.startName = startName;
	}

	public String getEndName() {
		return endName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDrivieId() {
		return drivieId;
	}

	public void setDrivieId(String drivieId) {
		this.drivieId = drivieId;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Order(Date time, String userId, String startName, String endName,
			String text, String drivieId, boolean isFinished,
			String phoneNumber, int price) {
		super();
		this.time = time;
		this.userId = userId;
		this.startName = startName;
		this.endName = endName;
		this.text = text;
		this.drivieId = drivieId;
		this.isFinished = isFinished;
		this.phoneNumber = phoneNumber;
		this.price = price;
	}

	public Order() {

	}

	@Override
	public String toString() {
		return "Order [time=" + time + ", userId=" + userId + ", startName="
				+ startName + ", endName=" + endName + ", text=" + text
				+ ", drivieId=" + drivieId + ", isFinished=" + isFinished
				+ ", phoneNumber=" + phoneNumber + ", price=" + price + "]";
	}

}
