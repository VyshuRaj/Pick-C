package com.prts.pickcustomer.booking;


import com.google.gson.annotations.SerializedName;


public class BookingStatus {

	@SerializedName("BookingNo")
	private String bookingNo;

	@SerializedName("Status")
	private String message;

	public void setBookingNo(String bookingNo){
		this.bookingNo = bookingNo;
	}

	public String getBookingNo(){
		return bookingNo;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}


}