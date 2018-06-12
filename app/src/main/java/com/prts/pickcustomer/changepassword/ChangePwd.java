package com.prts.pickcustomer.changepassword;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satya on 30-Dec-17.
 */

public class ChangePwd {
    @SerializedName("MobileNo")
    String mobileNo;
    @SerializedName("Password")
    String password;
    @SerializedName("NewPassword")
    String newPassword;
    @SerializedName("OTP")
    String otp;


    @SerializedName("OTPVerifiedDate")
    String otpVerifyDate;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOtpVerifyDate() {
        return otpVerifyDate;
    }

    public void setOtpVerifyDate(String otpVerifyDate) {
        this.otpVerifyDate = otpVerifyDate;
    }


    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
