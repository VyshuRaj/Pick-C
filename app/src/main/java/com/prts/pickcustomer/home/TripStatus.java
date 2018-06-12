package com.prts.pickcustomer.home;

import com.google.gson.annotations.SerializedName;

public class TripStatus {


	@SerializedName("BookingNo")
	private String bookingno;

	@SerializedName("IsInTrip")
	private boolean isintrip;

	public void setBookingno(String bookingno){
		this.bookingno = bookingno;
	}

	public String getBookingno(){
		return bookingno;
	}

	public void setIsintrip(boolean isintrip){
		this.isintrip = isintrip;
	}

	public boolean isIsintrip(){
		return isintrip;
	}


}