package com.prts.pickcustomer.home;

import com.google.gson.annotations.SerializedName;

public class DriverMonitorList{

	@SerializedName("DriverId")
	private String driverId;

	@SerializedName("CurrentLong")
	private double currentLong;

	@SerializedName("CurrentLat")
	private double currentLat;

	public void setDriverId(String driverId){
		this.driverId = driverId;
	}

	public String getDriverId(){
		return driverId;
	}

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

	@Override
 	public String toString(){
		return 
			"DriverMonitorList{" + 
			"driverId = '" + driverId + '\'' + 
			",currentLong = '" + currentLong + '\'' + 
			",currentLat = '" + currentLat + '\'' + 
			"}";
		}
}