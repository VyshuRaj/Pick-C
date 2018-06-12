package com.prts.pickcustomer.signup;

/**
 * Created by satya on 17-Dec-17.
 */

public interface SignUpPresenter {
    void register(SignUp signUp);
    void checkMobileInServer(String mobile);
    boolean validateName(String name);
    boolean validateMobileNumber(String number);
    boolean validateEmail(String number);
    boolean validatePassword(String pawd);
    boolean checkMatchingOfPwds(String pwd,String newPwd);

   // void verfyOTP(String string, String enteredOTP);
}
