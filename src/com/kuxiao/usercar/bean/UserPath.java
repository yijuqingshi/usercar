package com.kuxiao.usercar.bean;

public class UserPath {

	private String startAddress;

	private String endAddress;

	private String startName;

	private String endName;
	
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

	private double starLatitude;

	private double starLongitude;

	private double endLatitude;

	private double endLongitude;

	private int id;

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public double getStarLatitude() {
		return starLatitude;
	}

	public void setStarLatitude(double starLatitude) {
		this.starLatitude = starLatitude;
	}

	public double getStarLongitude() {
		return starLongitude;
	}

	public void setStarLongitude(double starLongitude) {
		this.starLongitude = starLongitude;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UserPath [startAddress=" + startAddress + ", endAddress="
				+ endAddress + ", starLatitude=" + starLatitude
				+ ", starLongitude=" + starLongitude + ", endLatitude="
				+ endLatitude + ", endLongitude=" + endLongitude + ", id=" + id
				+ "]";
	}

	public UserPath(String startAddress, String endAddress,
			double starLatitude, double starLongitude, double endLatitude,
			double endLongitude, int id) {
		super();
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.starLatitude = starLatitude;
		this.starLongitude = starLongitude;
		this.endLatitude = endLatitude;
		this.endLongitude = endLongitude;
		this.id = id;
	}

	public UserPath() {

	}
}
