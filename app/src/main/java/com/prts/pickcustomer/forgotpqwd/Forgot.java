package com.prts.pickcustomer.forgotpqwd;

import com.google.gson.annotations.SerializedName;

public class Forgot {
	@SerializedName("MobileNo")
	private String mobileNo;

	@SerializedName("NewPassword")
	private String newPassword;

	private String oldPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	@SerializedName("OTP")
	private String oTP;

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setNewPassword(String newPassword){
		this.newPassword = newPassword;
	}

	public String getNewPassword(){
		return newPassword;
	}

	public void setOTP(String oTP){
		this.oTP = oTP;
	}

	public String getOTP(){
		return oTP;
	}

	@Override
 	public String toString(){
		return 
			"Forgot{" +
			"mobileNo = '" + mobileNo + '\'' + 
			",newPassword = '" + newPassword + '\'' + 
			",oTP = '" + oTP + '\'' + 
			"}";
		}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}