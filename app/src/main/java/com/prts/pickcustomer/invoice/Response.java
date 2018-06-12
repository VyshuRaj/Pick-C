package com.prts.pickcustomer.invoice;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("GSTTax")
	private Object gSTTax;

	@SerializedName("VehicleNo")
	private Object vehicleNo;

	@SerializedName("EndTime")
	private Object endTime;

	@SerializedName("FromLongitude")
	private Object fromLongitude;

	@SerializedName("TravelTimeFare")
	private Object travelTimeFare;

	@SerializedName("BaseFare")
	private Object baseFare;

	@SerializedName("DriverName")
	private Object driverName;

	@SerializedName("LocationTo")
	private Object locationTo;

	@SerializedName("TravelTime")
	private int travelTime;

	@SerializedName("StartDate")
	private Object startDate;

	@SerializedName("TotalBillAmount")
	private Object totalBillAmount;

	@SerializedName("ToLongitude")
	private Object toLongitude;

	@SerializedName("VehicleMaker")
	private Object vehicleMaker;

	@SerializedName("DriverID")
	private Object driverID;

	@SerializedName("LocationFrom")
	private Object locationFrom;

	@SerializedName("TotalDistanceKm")
	private Object totalDistanceKm;

	@SerializedName("PaymentType")
	private Object paymentType;
	@SerializedName("InvoiceDate")
	private Object invoiceDate;
	@SerializedName("CustomerName")
	private Object customerName;
	@SerializedName("ToLatitute")
	private Object toLatitute;
	@SerializedName("LoadingUnloading")
	private Object loadingUnloading;
	@SerializedName("DistanceKM")
	private Object distanceKM;
	@SerializedName("VehicleType")
	private Object vehicleType;
	@SerializedName("StartTime")
	private Object startTime;
	@SerializedName("InvoiceNo")
	private Object invoiceNo;

	@SerializedName("FromLatitute")
	private Object fromLatitute;

	@SerializedName("TotalAmount")
	private Object totalAmount;

	@SerializedName("LoadingUnLoadingCharges")
	private Object loadingUnLoadingCharges;

	@SerializedName("EndDate")
	private Object endDate;

	@SerializedName("TripID")
	private Object tripID;

	@SerializedName("VehicleGroup")
	private Object vehicleGroup;

	@SerializedName("TotalFare")
	private Object totalFare;

	@SerializedName("CustomerMobileNo")
	private Object customerMobileNo;

	@SerializedName("BookingNo")
	private Object bookingNo;

	@SerializedName("WaitingCharges")
	private Object waitingCharges;

	@SerializedName("OtherCharges")
	private Object otherCharges;

	@SerializedName("DistanceFare")
	private Object distanceFare;

	@SerializedName("BaseKM")
	private Object baseKM;

	public void setGSTTax(Object gSTTax){
		this.gSTTax = gSTTax;
	}

	public Object getGSTTax(){
		return gSTTax;
	}

	public void setVehicleNo(Object vehicleNo){
		this.vehicleNo = vehicleNo;
	}

	public Object getVehicleNo(){
		return vehicleNo;
	}

	public void setEndTime(Object endTime){
		this.endTime = endTime;
	}

	public Object getEndTime(){
		return endTime;
	}

	public void setFromLongitude(Object fromLongitude){
		this.fromLongitude = fromLongitude;
	}

	public Object getFromLongitude(){
		return fromLongitude;
	}

	public void setTravelTimeFare(Object travelTimeFare){
		this.travelTimeFare = travelTimeFare;
	}

	public Object getTravelTimeFare(){
		return travelTimeFare;
	}

	public void setBaseFare(Object baseFare){
		this.baseFare = baseFare;
	}

	public Object getBaseFare(){
		return baseFare;
	}

	public void setDriverName(Object driverName){
		this.driverName = driverName;
	}

	public Object getDriverName(){
		return driverName;
	}

	public void setLocationTo(Object locationTo){
		this.locationTo = locationTo;
	}

	public Object getLocationTo(){
		return locationTo;
	}

	public void setTravelTime(int travelTime){
		this.travelTime = travelTime;
	}

	public int getTravelTime(){
		return travelTime;
	}

	public void setStartDate(Object startDate){
		this.startDate = startDate;
	}

	public Object getStartDate(){
		return startDate;
	}

	public void setTotalBillAmount(Object totalBillAmount){
		this.totalBillAmount = totalBillAmount;
	}

	public Object getTotalBillAmount(){
		return totalBillAmount;
	}

	public void setToLongitude(Object toLongitude){
		this.toLongitude = toLongitude;
	}

	public Object getToLongitude(){
		return toLongitude;
	}

	public void setVehicleMaker(Object vehicleMaker){
		this.vehicleMaker = vehicleMaker;
	}

	public Object getVehicleMaker(){
		return vehicleMaker;
	}

	public void setDriverID(Object driverID){
		this.driverID = driverID;
	}

	public Object getDriverID(){
		return driverID;
	}

	public void setLocationFrom(Object locationFrom){
		this.locationFrom = locationFrom;
	}

	public Object getLocationFrom(){
		return locationFrom;
	}

	public void setTotalDistanceKm(Object totalDistanceKm){
		this.totalDistanceKm = totalDistanceKm;
	}

	public Object getTotalDistanceKm(){
		return totalDistanceKm;
	}

	public void setPaymentType(Object paymentType){
		this.paymentType = paymentType;
	}

	public Object getPaymentType(){
		return paymentType;
	}

	public void setInvoiceDate(Object invoiceDate){
		this.invoiceDate = invoiceDate;
	}

	public Object getInvoiceDate(){
		return invoiceDate;
	}

	public void setCustomerName(Object customerName){
		this.customerName = customerName;
	}

	public Object getCustomerName(){
		return customerName;
	}

	public void setToLatitute(Object toLatitute){
		this.toLatitute = toLatitute;
	}

	public Object getToLatitute(){
		return toLatitute;
	}

	public void setLoadingUnloading(Object loadingUnloading){
		this.loadingUnloading = loadingUnloading;
	}

	public Object getLoadingUnloading(){
		return loadingUnloading;
	}

	public void setDistanceKM(Object distanceKM){
		this.distanceKM = distanceKM;
	}

	public Object getDistanceKM(){
		return distanceKM;
	}

	public void setVehicleType(Object vehicleType){
		this.vehicleType = vehicleType;
	}

	public Object getVehicleType(){
		return vehicleType;
	}

	public void setStartTime(Object startTime){
		this.startTime = startTime;
	}

	public Object getStartTime(){
		return startTime;
	}

	public void setInvoiceNo(Object invoiceNo){
		this.invoiceNo = invoiceNo;
	}

	public Object getInvoiceNo(){
		return invoiceNo;
	}

	public void setFromLatitute(Object fromLatitute){
		this.fromLatitute = fromLatitute;
	}

	public Object getFromLatitute(){
		return fromLatitute;
	}

	public void setTotalAmount(Object totalAmount){
		this.totalAmount = totalAmount;
	}

	public Object getTotalAmount(){
		return totalAmount;
	}

	public void setLoadingUnLoadingCharges(Object loadingUnLoadingCharges){
		this.loadingUnLoadingCharges = loadingUnLoadingCharges;
	}

	public Object getLoadingUnLoadingCharges(){
		return loadingUnLoadingCharges;
	}

	public void setEndDate(Object endDate){
		this.endDate = endDate;
	}

	public Object getEndDate(){
		return endDate;
	}

	public void setTripID(Object tripID){
		this.tripID = tripID;
	}

	public Object getTripID(){
		return tripID;
	}

	public void setVehicleGroup(Object vehicleGroup){
		this.vehicleGroup = vehicleGroup;
	}

	public Object getVehicleGroup(){
		return vehicleGroup;
	}

	public void setTotalFare(Object totalFare){
		this.totalFare = totalFare;
	}

	public Object getTotalFare(){
		return totalFare;
	}

	public void setCustomerMobileNo(Object customerMobileNo){
		this.customerMobileNo = customerMobileNo;
	}

	public Object getCustomerMobileNo(){
		return customerMobileNo;
	}

	public void setBookingNo(Object bookingNo){
		this.bookingNo = bookingNo;
	}

	public Object getBookingNo(){
		return bookingNo;
	}

	public void setWaitingCharges(Object waitingCharges){
		this.waitingCharges = waitingCharges;
	}

	public Object getWaitingCharges(){
		return waitingCharges;
	}

	public void setOtherCharges(Object otherCharges){
		this.otherCharges = otherCharges;
	}

	public Object getOtherCharges(){
		return otherCharges;
	}

	public void setDistanceFare(Object distanceFare){
		this.distanceFare = distanceFare;
	}

	public Object getDistanceFare(){
		return distanceFare;
	}

	public void setBaseKM(Object baseKM){
		this.baseKM = baseKM;
	}

	public Object getBaseKM(){
		return baseKM;
	}


}