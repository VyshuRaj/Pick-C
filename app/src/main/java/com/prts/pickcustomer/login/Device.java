package com.prts.pickcustomer.login;

import com.google.gson.annotations.SerializedName;

public class Device {

	@SerializedName("MobileNo")
	private String mobileNo;

	@SerializedName("DeviceId")
	private String deviceId;

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return deviceId;
	}
}