package com.prts.pickcustomer.login;

/**
 * Created by LOGICON on 13-12-2017.
 */

public interface LoginView {
    void enterMobileNumber(String mobile);

    void enterPassword();

    void showProgressDialog(String title);

    void dismissProgressDialog();

    void enterValidCredentials();

    void noInternet();

    void navigateToHomePage();

    void serverBusy(String s);
}
