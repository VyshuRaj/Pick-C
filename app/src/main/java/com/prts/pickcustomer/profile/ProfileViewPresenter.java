package com.prts.pickcustomer.profile;

/**
 * Created by satya on 21-Dec-17.
 */

public interface ProfileViewPresenter {
    void updateUserData(String name, String mobileNo, String emailID);
    void getUserDataFromServer(String mobileNO);
    boolean validateName(String name);
    boolean validateMobileNumber(String number);
    boolean validateEmail(String number);
    void validatePassword(String password, String mobileNo, String name, String emailID);
}
