package com.prts.pickcustomer.splash;

/**
 * Created by LOGICON on 12-12-2017.
 */

public interface SplashView {
    void startAnimation();

    void showLoginSignUpBtns();

    void hideLoginSignUpBtns();

    void navigateToHomePage();

    void loginAgain();

    void noInternet();
}
