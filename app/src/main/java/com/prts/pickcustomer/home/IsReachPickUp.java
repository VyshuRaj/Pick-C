package com.prts.pickcustomer.home;

import com.google.gson.annotations.SerializedName;

public class IsReachPickUp{

	@SerializedName("Message")
	private String message;

	@SerializedName("BookingNo")
	private String bookingNo;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setBookingNo(String bookingNo){
		this.bookingNo = bookingNo;
	}

	public String getBookingNo(){
		return bookingNo;
	}

}