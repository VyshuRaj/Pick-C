package com.prts.pickcustomer.booking;

import com.google.gson.annotations.SerializedName;

public class BookingInfor {

	@SerializedName("BookingDate")
	private String bookingDate;

	@SerializedName("RequiredDate")
	private String requiredDate;

	@SerializedName("VehicleType")
	private int vehicleType;

	@SerializedName("Latitude")
	private double latitude;

	@SerializedName("LocationTo")
	private String locationTo;

	@SerializedName("CustomerID")
	private String customerID;

	@SerializedName("ToLatitude")
	private double toLatitude;

	@SerializedName("VehicleGroup")
	private int vehicleGroup;

	@SerializedName("ToLongitude")
	private double toLongitude;

	@SerializedName("CargoDescription")
	private String cargoDescription;

	@SerializedName("LocationFrom")
	private String locationFrom;

	@SerializedName("Remarks")
	private String remarks;

	@SerializedName("BookingNo")
	private String bookingNo;

	@SerializedName("LoadingUnLoading")
	private int loadingUnLoading;

	@SerializedName("Longitude")
	private double longitude;

	public String getReceiverMobileNo() {
		return receiverMobileNo;
	}

	public void setReceiverMobileNo(String receiverMobileNo) {
		this.receiverMobileNo = receiverMobileNo;
	}

	@SerializedName("ReceiverMobileNo")
	private String receiverMobileNo;


	public void setBookingDate(String bookingDate){
		this.bookingDate = bookingDate;
	}

	public String getBookingDate(){
		return bookingDate;
	}

	public void setRequiredDate(String requiredDate){
		this.requiredDate = requiredDate;
	}

	public String getRequiredDate(){
		return requiredDate;
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

	public void setLocationTo(String locationTo){
		this.locationTo = locationTo;
	}

	public String getLocationTo(){
		return locationTo;
	}

	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}

	public String getCustomerID(){
		return customerID;
	}

	public void setToLatitude(double toLatitude){
		this.toLatitude = toLatitude;
	}

	public double getToLatitude(){
		return toLatitude;
	}

	public void setVehicleGroup(int vehicleGroup){
		this.vehicleGroup = vehicleGroup;
	}

	public int getVehicleGroup(){
		return vehicleGroup;
	}

	public void setToLongitude(double toLongitude){
		this.toLongitude = toLongitude;
	}

	public double getToLongitude(){
		return toLongitude;
	}

	public void setCargoDescription(String cargoDescription){
		this.cargoDescription = cargoDescription;
	}

	public String getCargoDescription(){
		return cargoDescription;
	}

	public void setLocationFrom(String locationFrom){
		this.locationFrom = locationFrom;
	}

	public String getLocationFrom(){
		return locationFrom;
	}

	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

	public String getRemarks(){
		return remarks;
	}

	public void setBookingNo(String bookingNo){
		this.bookingNo = bookingNo;
	}

	public String getBookingNo(){
		return bookingNo;
	}

	public void setLoadingUnLoading(int loadingUnLoading){
		this.loadingUnLoading = loadingUnLoading;
	}

	public int getLoadingUnLoading(){
		return loadingUnLoading;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

}