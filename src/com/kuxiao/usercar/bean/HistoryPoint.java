package com.kuxiao.usercar.bean;

public class HistoryPoint {

	private double latitude;
	private double longitude;
	private String name;
	private String address;
	private int id;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "HistoryPoint [latitude=" + latitude + ", longitude="
				+ longitude + ", name=" + name + ", address=" + address
				+ ", id=" + id + "]";
	}

	public HistoryPoint(double latitude, double longitude, String name,
			String address, int id) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.address = address;
		this.id = id;
	}

	public HistoryPoint() {

	}

}
