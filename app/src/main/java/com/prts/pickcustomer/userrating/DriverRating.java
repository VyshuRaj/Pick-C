package com.prts.pickcustomer.userrating;

import com.google.gson.annotations.SerializedName;

public class DriverRating {

	@SerializedName("DriverID")
	private String driverID;

	@SerializedName("Remarks")
	private String remarks;

	@SerializedName("BookingNo")
	private String bookingNo;

	@SerializedName("Rating")
	private int rating;

	public void setDriverID(String driverID){
		this.driverID = driverID;
	}

	public String getDriverID(){
		return driverID;
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

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	@Override
 	public String toString(){
		return 
			"DriverRating{" +
			"driverID = '" + driverID + '\'' + 
			",remarks = '" + remarks + '\'' + 
			",bookingNo = '" + bookingNo + '\'' + 
			",rating = '" + rating + '\'' + 
			"}";
		}
}