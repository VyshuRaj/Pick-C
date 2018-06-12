package com.prts.pickcustomer.login;

import com.google.gson.annotations.SerializedName;

public class Customer{
	@SerializedName("MobileNo")
	private String mobileNo;

	@SerializedName("Status")
	private String status;

	@SerializedName("EmailID")
	private String emailID;

	@SerializedName("Name")
	private String name;

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setEmailID(String emailID){
		this.emailID = emailID;
	}

	public String getEmailID(){
		return emailID;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}