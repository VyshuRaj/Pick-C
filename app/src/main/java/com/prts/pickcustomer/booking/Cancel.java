package com.prts.pickcustomer.booking;


import com.google.gson.annotations.SerializedName;

public class Cancel {

	@SerializedName("BookingNo")
	private String bookingNo;

	@SerializedName("Remarks")
	private String remarks;

	public void setBookingNo(String bookingNo){
		this.bookingNo = bookingNo;
	}

	public String getBookingNo(){
		return bookingNo;
	}

	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

	public String getRemarks(){
		return remarks;
	}

	@Override
 	public String toString(){
		return 
			"Cancel{" +
			" bookingNo  = '" + bookingNo + '\'' + 
			", remarks  = '" + remarks + '\'' + 
			"}";
		}
}