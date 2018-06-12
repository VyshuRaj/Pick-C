package com.prts.pickcustomer.base;

/**
 * Created by satya on 20-Dec-17.
 */

public interface BaseView {
    void navigateToLoginPage();

    void noInternet();

    void tryAagin();

    void notAbleToLoggedOut();

    void showProgressDialog(String s);

    void dismissDialog();
}
