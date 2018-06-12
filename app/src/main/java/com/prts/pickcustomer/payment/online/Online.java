package com.prts.pickcustomer.payment.online;

import com.google.gson.annotations.SerializedName;

public class Online {

	@SerializedName("Order_id")
	private String orderId;

	@SerializedName("Access_code")
	private String accessCode;

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setAccessCode(String accessCode){
		this.accessCode = accessCode;
	}

	public String getAccessCode(){
		return accessCode;
	}
}