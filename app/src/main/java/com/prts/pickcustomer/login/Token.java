package com.prts.pickcustomer.login;

import com.google.gson.annotations.SerializedName;

public class Token {

	@SerializedName("Status")
	private boolean status;

	@SerializedName("Token")
	private String token;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean getStatus(){
		return status;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

}