package com.prts.pickcustomer.signup;

/**
 * Created by satya on 17-Dec-17.
 */

public interface SignUpView {
    void enterName(String s);

    void enterMobileNumber(String s);

    void enterValidEmail(String s);

    void enterPassword(String s);

    void numberAlreadyExisted();

    void navigateToTCPage();

    void reEnterPwd(String s);

    void noInternet();

    void registrationFailed();

    void showProgressDialog(String s);

    void dismissProgressDialog();

    void mobileVerificationFailed();

    void serverBusy(String s);

    void navigateToHomePage();
}
