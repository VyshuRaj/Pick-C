package com.prts.pickcustomer.payment;

import com.google.gson.annotations.SerializedName;

public class AmountResponse {

	@SerializedName("DriverId")
	private String driverID;

	@SerializedName("BookingNo")
	private String bookingNo;

	@SerializedName("TotalAmount")
	private String totalAmount;

	public void setDriverID(String driverID){
		this.driverID = driverID;
	}

	public String getDriverID(){
		return driverID;
	}

	public void setBookingNo(String bookingNo){
		this.bookingNo = bookingNo;
	}

	public String getBookingNo(){
		return bookingNo;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}


}