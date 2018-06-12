package com.prts.pickcustomer.payment.online;

import com.google.gson.annotations.SerializedName;

public class RSAKey {

	@SerializedName("RSAKey")
	private String rSAKey;

	@SerializedName("Status")
	boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setRSAKey(String rSAKey){
		this.rSAKey = rSAKey;
	}

	public String getRSAKey(){
		return rSAKey;
	}


}