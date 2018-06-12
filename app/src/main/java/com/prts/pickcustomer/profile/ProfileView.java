package com.prts.pickcustomer.profile;

import com.prts.pickcustomer.helpers.DialogBox;
import com.prts.pickcustomer.login.Customer;

/**
 * Created by satya on 20-Dec-17.
 */

public interface ProfileView {
    void showInternet();

    void setDataToViews(Customer customer);

    void enterName(String s);

    void checkMultipleSpacesInName(String s);

    void enterValidMail(String s);

    DialogBox getDialogBox();

    void errorMessage(String s);

    void askPassworddToUpdateUserData();

    void unableToProcessRequest(String s);

    void updateSuccessfully(String s);

    void navigateToHomePage();

    void enterMobileNumber(String string);

    void enterValidEmail(String string);
}
