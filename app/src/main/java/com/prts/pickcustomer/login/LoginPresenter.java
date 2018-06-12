package com.prts.pickcustomer.login;

/**
 * Created by LOGICON on 13-12-2017.
 */

public interface LoginPresenter {
    void login(String mobile, String pwd,boolean isCallingFromRetry);

    boolean validateMobileNumber(String string);

    boolean validatePassword(String string);
}
