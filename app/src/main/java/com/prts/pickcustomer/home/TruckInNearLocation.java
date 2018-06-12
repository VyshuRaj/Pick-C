package com.prts.pickcustomer.home;

import com.google.gson.annotations.SerializedName;

public class TruckInNearLocation {

	@SerializedName("DriverID")
	private String driverID;

	@SerializedName("VehicleNo")
	private String vehicleNo;

	@SerializedName("PhoneNo")
	private String phoneNo;

	@SerializedName("VehicleType")
	private int vehicleType;

	@SerializedName("Latitude")
	private double latitude;

	@SerializedName("Longitude")
	private double longitude;

	@SerializedName("VehicleGroup")
	private int vehicleGroup;

	public void setDriverID(String driverID){
		this.driverID = driverID;
	}

	public String getDriverID(){
		return driverID;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleNo(){
		return vehicleNo;
	}

	public void setPhoneNo(String phoneNo){
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo(){
		return phoneNo;
	}

	public void setVehicleType(int vehicleType){
		this.vehicleType = vehicleType;
	}

	public int getVehicleType(){
		return vehicleType;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	public void setVehicleGroup(int vehicleGroup){
		this.vehicleGroup = vehicleGroup;
	}

	public int getVehicleGroup(){
		return vehicleGroup;
	}

	@Override
	public String toString(){
		return
				"Response{" +
						"driverID = '" + driverID + '\'' +
						",vehicleNo = '" + vehicleNo + '\'' +
						",phoneNo = '" + phoneNo + '\'' +
						",vehicleType = '" + vehicleType + '\'' +
						",latitude = '" + latitude + '\'' +
						",longitude = '" + longitude + '\'' +
						",vehicleGroup = '" + vehicleGroup + '\'' +
						"}";
	}
	/*
	@SerializedName("Latitude")
	private double latitude;

	@SerializedName("Longitude")
	private double longitude;

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}*/


}