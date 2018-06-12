package com.prts.pickcustomer.forgotpqwd;

/**
 * Created by LOGICON on 18-12-2017.
 */

public interface FPView {

    void enterOTP(String s);

    void enterPwd(String s);

    void enterRePwd(String s);


    void showDialogBox();

    void dismissDialog();

    void pwdIsNotUpdated();

    void navigateToLoginPage();
}
