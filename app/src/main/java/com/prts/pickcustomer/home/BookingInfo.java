package com.prts.pickcustomer.home;

import com.google.gson.annotations.SerializedName;

public class BookingInfo {

	@SerializedName("BookingDate")
	private String bookingDate;

	@SerializedName("IsConfirm")
	private boolean isConfirm;

	@SerializedName("VehicleNo")
	private String vehicleNo;

	@SerializedName("ConfirmDate")
	private String confirmDate;

	@SerializedName("LocationTo")
	private String locationTo;

	@SerializedName("DriverName")
	private String driverName;

	@SerializedName("Latitude")
	private double latitude;

	@SerializedName("OTP")
	private String oTP;

	@SerializedName("CustomerID")
	private String customerID;

	@SerializedName("PayLoad")
	private String payLoad;

	@SerializedName("InvoiceAmount")
	private int invoiceAmount;

	@SerializedName("ReceiverMobileNo")
	private String receiverMobileNo;

	@SerializedName("ToLongitude")
	private double toLongitude;

	@SerializedName("DriverID")
	private String driverID;

	@SerializedName("LocationFrom")
	private String locationFrom;

	@SerializedName("VehicleTypeDescription")
	private String vehicleTypeDescription;

	@SerializedName("Remarks")
	private String remarks;

	@SerializedName("CustomerName")
	private String customerName;

	@SerializedName("LoadingUnLoadingDescription")
	private String loadingUnLoadingDescription;

	@SerializedName("CompleteTime")
	private String completeTime;

	@SerializedName("Status")
	private int status;

	@SerializedName("RequiredDate")
	private String requiredDate;

	@SerializedName("CancelTime")
	private String cancelTime;

	@SerializedName("IsCancel")
	private boolean isCancel;

	@SerializedName("VehicleType")
	private int vehicleType;

	@SerializedName("InvoiceNo")
	private String invoiceNo;

	@SerializedName("ToLatitude")
	private double toLatitude;

	@SerializedName("IsComplete")
	private boolean isComplete;

	@SerializedName("Longitude")
	private double longitude;

	@SerializedName("VehicleGroup")
	private int vehicleGroup;

	@SerializedName("CargoDescription")
	private String cargoDescription;

	@SerializedName("CancelRemarks")
	private String cancelRemarks;

	@SerializedName("BookingNo")
	private String bookingNo;

	@SerializedName("LoadingUnLoading")
	private int loadingUnLoading;

	@SerializedName("CargoType")
	private String cargoType;

	public void setBookingDate(String bookingDate){
		this.bookingDate = bookingDate;
	}

	public String getBookingDate(){
		return bookingDate;
	}

	public void setIsConfirm(boolean isConfirm){
		this.isConfirm = isConfirm;
	}

	public boolean isIsConfirm(){
		return isConfirm;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleNo(){
		return vehicleNo;
	}

	public void setConfirmDate(String confirmDate){
		this.confirmDate = confirmDate;
	}

	public String getConfirmDate(){
		return confirmDate;
	}

	public void setLocationTo(String locationTo){
		this.locationTo = locationTo;
	}

	public String getLocationTo(){
		return locationTo;
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

	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}

	public String getCustomerID(){
		return customerID;
	}

	public void setPayLoad(String payLoad){
		this.payLoad = payLoad;
	}

	public String getPayLoad(){
		return payLoad;
	}

	public void setInvoiceAmount(int invoiceAmount){
		this.invoiceAmount = invoiceAmount;
	}

	public int getInvoiceAmount(){
		return invoiceAmount;
	}

	public void setReceiverMobileNo(String receiverMobileNo){
		this.receiverMobileNo = receiverMobileNo;
	}

	public String getReceiverMobileNo(){
		return receiverMobileNo;
	}

	public void setToLongitude(double toLongitude){
		this.toLongitude = toLongitude;
	}

	public double getToLongitude(){
		return toLongitude;
	}

	public void setDriverID(String driverID){
		this.driverID = driverID;
	}

	public String getDriverID(){
		return driverID;
	}

	public void setLocationFrom(String locationFrom){
		this.locationFrom = locationFrom;
	}

	public String getLocationFrom(){
		return locationFrom;
	}

	public void setVehicleTypeDescription(String vehicleTypeDescription){
		this.vehicleTypeDescription = vehicleTypeDescription;
	}

	public String getVehicleTypeDescription(){
		return vehicleTypeDescription;
	}

	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

	public String getRemarks(){
		return remarks;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return customerName;
	}

	public void setLoadingUnLoadingDescription(String loadingUnLoadingDescription){
		this.loadingUnLoadingDescription = loadingUnLoadingDescription;
	}

	public String getLoadingUnLoadingDescription(){
		return loadingUnLoadingDescription;
	}

	public void setCompleteTime(String completeTime){
		this.completeTime = completeTime;
	}

	public String getCompleteTime(){
		return completeTime;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setRequiredDate(String requiredDate){
		this.requiredDate = requiredDate;
	}

	public String getRequiredDate(){
		return requiredDate;
	}

	public void setCancelTime(String cancelTime){
		this.cancelTime = cancelTime;
	}

	public String getCancelTime(){
		return cancelTime;
	}

	public void setIsCancel(boolean isCancel){
		this.isCancel = isCancel;
	}

	public boolean isIsCancel(){
		return isCancel;
	}

	public void setVehicleType(int vehicleType){
		this.vehicleType = vehicleType;
	}

	public int getVehicleType(){
		return vehicleType;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceNo(){
		return invoiceNo;
	}

	public void setToLatitude(double toLatitude){
		this.toLatitude = toLatitude;
	}

	public double getToLatitude(){
		return toLatitude;
	}

	public void setIsComplete(boolean isComplete){
		this.isComplete = isComplete;
	}

	public boolean isIsComplete(){
		return isComplete;
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

	public void setCargoDescription(String cargoDescription){
		this.cargoDescription = cargoDescription;
	}

	public String getCargoDescription(){
		return cargoDescription;
	}

	public void setCancelRemarks(String cancelRemarks){
		this.cancelRemarks = cancelRemarks;
	}

	public String getCancelRemarks(){
		return cancelRemarks;
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

	public void setCargoType(String cargoType){
		this.cargoType = cargoType;
	}

	public String getCargoType(){
		return cargoType;
	}

	@Override
 	public String toString(){
		return 
			"BookingInfo{" +
			"bookingDate = '" + bookingDate + '\'' + 
			",isConfirm = '" + isConfirm + '\'' + 
			",vehicleNo = '" + vehicleNo + '\'' + 
			",confirmDate = '" + confirmDate + '\'' + 
			",locationTo = '" + locationTo + '\'' + 
			",driverName = '" + driverName + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",oTP = '" + oTP + '\'' + 
			",customerID = '" + customerID + '\'' + 
			",payLoad = '" + payLoad + '\'' + 
			",invoiceAmount = '" + invoiceAmount + '\'' + 
			",receiverMobileNo = '" + receiverMobileNo + '\'' + 
			",toLongitude = '" + toLongitude + '\'' + 
			",driverID = '" + driverID + '\'' + 
			",locationFrom = '" + locationFrom + '\'' + 
			",vehicleTypeDescription = '" + vehicleTypeDescription + '\'' + 
			",remarks = '" + remarks + '\'' + 
			",customerName = '" + customerName + '\'' + 
			",loadingUnLoadingDescription = '" + loadingUnLoadingDescription + '\'' + 
			",completeTime = '" + completeTime + '\'' + 
			",status = '" + status + '\'' + 
			",requiredDate = '" + requiredDate + '\'' + 
			",cancelTime = '" + cancelTime + '\'' + 
			",isCancel = '" + isCancel + '\'' + 
			",vehicleType = '" + vehicleType + '\'' + 
			",invoiceNo = '" + invoiceNo + '\'' + 
			",toLatitude = '" + toLatitude + '\'' + 
			",isComplete = '" + isComplete + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",vehicleGroup = '" + vehicleGroup + '\'' + 
			",cargoDescription = '" + cargoDescription + '\'' + 
			",cancelRemarks = '" + cancelRemarks + '\'' + 
			",bookingNo = '" + bookingNo + '\'' + 
			",loadingUnLoading = '" + loadingUnLoading + '\'' + 
			",cargoType = '" + cargoType + '\'' + 
			"}";
		}
}