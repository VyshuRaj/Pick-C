package com.prts.pickcustomer.home;


import com.google.gson.annotations.SerializedName;

public class PaymentStatus {

	@SerializedName("Status")
	private boolean status;

	@SerializedName("BookingNo")//BookingNo
	private String result;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean getStatus(){
		return status;
	}

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}


}