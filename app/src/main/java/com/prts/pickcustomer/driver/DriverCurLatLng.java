package com.prts.pickcustomer.driver;

import com.google.gson.annotations.SerializedName;

public class DriverCurLatLng {

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	@SerializedName("DriverId")
	private String driverId;

	@SerializedName("CurrentLong")
	private double currentLong;

	@SerializedName("CurrentLat")
	private double currentLat;

	public void setCurrentLong(double currentLong){
		this.currentLong = currentLong;
	}

	public double getCurrentLong(){
		return currentLong;
	}

	public void setCurrentLat(double currentLat){
		this.currentLat = currentLat;
	}

	public double getCurrentLat(){
		return currentLat;
	}


}