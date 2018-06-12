package com.prts.pickcustomer.driver;

import com.google.gson.annotations.SerializedName;

public class DriverLocationUpdates{

	@SerializedName("driverMonitorList")
	private DriverMonitorList driverMonitorList;
	@SerializedName("accuracy")
	private Accuracy accuracy;

	public double getAcc() {
		return acc;
	}

	public void setAcc(double acc) {
		this.acc = acc;
	}

	public double getBearing() {
		return bearing;
	}

	public void setBearing(double bearing) {
		this.bearing = bearing;
	}

	@SerializedName("_Accuracy")
	private double acc;
	@SerializedName("_Bearing")
	private double bearing;

	public void setDriverMonitorList(DriverMonitorList driverMonitorList){
		this.driverMonitorList = driverMonitorList;
	}

	public DriverMonitorList getDriverMonitorList(){
		return driverMonitorList;
	}

	public void setAccuracy(Accuracy accuracy){
		this.accuracy = accuracy;
	}

	public Accuracy getAccuracy(){
		return accuracy;
	}
}