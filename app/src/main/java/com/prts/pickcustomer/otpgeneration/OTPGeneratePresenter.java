package com.prts.pickcustomer.otpgeneration;

/**
 * Created by satya on 18-Dec-17.
 */

public interface OTPGeneratePresenter {
    void getOTP(String number);

    boolean validateMobileNumber(String string);
}
