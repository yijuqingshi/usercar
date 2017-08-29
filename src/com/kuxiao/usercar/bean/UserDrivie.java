package com.kuxiao.usercar.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class UserDrivie extends BmobObject {

	private static final long serialVersionUID = 1L;

	private String carId;
	private String carName;
	private String name;
	private boolean canCall;
	private String phoneNumber;
	private String city;
	private String password;
	private int orderId;
	private BmobGeoPoint location;

	public BmobGeoPoint getLaction() {
		return location;
	}

	public void setLaction(BmobGeoPoint laction) {
		this.location = laction;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCanCall() {
		return canCall;
	}

	public void setCanCall(boolean canCall) {
		this.canCall = canCall;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserDrivie(double latitude, double longitude, String carId,
			String carName, String name, boolean canCall, String phoneNumber,
			String city, String password) {
		super();

		this.carId = carId;
		this.carName = carName;
		this.name = name;
		this.canCall = canCall;
		this.phoneNumber = phoneNumber;
		this.city = city;
		this.password = password;
	}

	public UserDrivie() {

	}

	@Override
	public String toString() {
		return "UserDrivie [carId=" + carId + ", carName=" + carName
				+ ", name=" + name + ", canCall=" + canCall + ", phoneNumber="
				+ phoneNumber + ", city=" + city + ", password=" + password
				+ ", orderId=" + orderId + ", laction的纬度： ="
				+ location.getLatitude() + "laction的经度： ="
				+ location.getLongitude() + "]";
	}

}
