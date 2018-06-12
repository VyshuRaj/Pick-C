package com.prts.pickcustomer.signup;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satya on 17-Dec-17.
 */

public class SignUp {
    @SerializedName("Name")
    String userName;
    @SerializedName("MobileNo")
    String mobileNumber;
    @SerializedName("EmailID")
    String email;
    @SerializedName("Password")
    String password;
    String reEnterPwd;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReEnterPwd() {
        return reEnterPwd;
    }

    public void setReEnterPwd(String reEnterPwd) {
        this.reEnterPwd = reEnterPwd;
    }

}
