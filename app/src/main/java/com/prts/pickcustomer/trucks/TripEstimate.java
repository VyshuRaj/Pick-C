package com.prts.pickcustomer.trucks;

import com.google.gson.annotations.SerializedName;

public class TripEstimate {
	@SerializedName("LdUdCharges")
	private int ldUdCharges;

	@SerializedName("VehicleType")
	private int vehicleType;

	@SerializedName("frmLatLong")
	private String frmLatLong;

	@SerializedName("toLatLong")
	private String toLatLong;

	@SerializedName("VehicleGroup")
	private int vehicleGroup;

	public void setLdUdCharges(int ldUdCharges){
		this.ldUdCharges = ldUdCharges;
	}

	public int getLdUdCharges(){
		return ldUdCharges;
	}

	public void setVehicleType(int vehicleType){
		this.vehicleType = vehicleType;
	}

	public int getVehicleType(){
		return vehicleType;
	}

	public void setFrmLatLong(String frmLatLong){
		this.frmLatLong = frmLatLong;
	}

	public String getFrmLatLong(){
		return frmLatLong;
	}

	public void setToLatLong(String toLatLong){
		this.toLatLong = toLatLong;
	}

	public String getToLatLong(){
		return toLatLong;
	}

	public void setVehicleGroup(int vehicleGroup){
		this.vehicleGroup = vehicleGroup;
	}

	public int getVehicleGroup(){
		return vehicleGroup;
	}


}