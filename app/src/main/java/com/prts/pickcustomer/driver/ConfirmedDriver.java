package com.prts.pickcustomer.driver;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConfirmedDriver implements Serializable{

	public int getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(int vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	@SerializedName("VehicleCategory")
	int vehicleCategory;
	@SerializedName("MobileNo")
	private String mobileNo;
	@SerializedName("IsConfirm")
	private boolean isConfirm;
	@SerializedName("DriverId")
	private String driverId;
	@SerializedName("VehicleNo")
	private String vehicleNo;
	@SerializedName("DriverImage")
	private String driverImage;
	@SerializedName("VehicleType")
	private int vehicleType;
	@SerializedName("DriverName")
	private String driverName;
	@SerializedName("Latitude")
	private double latitude;
	@SerializedName("OTP")
	private String oTP;
	@SerializedName("Longitude")
	private double longitude;
	private String bookingNumber="";

	public boolean isReachedPickupLocation() {
		return isReachedPickupLocation;
	}

	public void setReachedPickupLocation(boolean reachedPickupLocation) {
		isReachedPickupLocation = reachedPickupLocation;
	}

	private boolean isInTrip=false;
	private boolean isReachedPickupLocation=false;

	public boolean isConfirm() {
		return isConfirm;
	}

	public void setConfirm(boolean confirm) {
		isConfirm = confirm;
	}

	public boolean isInTrip() {
		return isInTrip;
	}

	public void setInTrip(boolean inTrip) {
		isInTrip = inTrip;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setDriverId(String driverId){
		this.driverId = driverId;
	}

	public String getDriverId(){
		return driverId;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleNo(){
		return vehicleNo;
	}

	public void setDriverImage(String driverImage){
		this.driverImage = driverImage;
	}

	public String getDriverImage(){
		return driverImage;
	}

	public void setVehicleType(int vehicleType){
		this.vehicleType = vehicleType;
	}

	public int getVehicleType(){
		return vehicleType;
	}

	public void setDriverName(String driverName){
		this.driverName = driverName;
	}

	public String getDriverName(){
		return driverName;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setOTP(String oTP){
		this.oTP = oTP;
	}

	public String getOTP(){
		return oTP;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

}