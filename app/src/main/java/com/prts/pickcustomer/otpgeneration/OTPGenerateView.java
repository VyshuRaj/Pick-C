package com.prts.pickcustomer.otpgeneration;

/**
 * Created by satya on 18-Dec-17.
 */

public interface OTPGenerateView {

    void enterMobileNumber(String s);
    void isNewNumber();

    void showProgressDialog();

    void dismissDialog();

    void notAbleToVerifyNumber();

    void otpNotGenerated(String s);
}
