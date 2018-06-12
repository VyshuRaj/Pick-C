package com.prts.pickcustomer.userrating;

import com.prts.pickcustomer.invoice.UserTripInvoice;

/**
 * Created by satya on 30-Dec-17.
 */

public interface UserRatingView {
    void noInterNet();

    void showProgressDialog(String s);

    void dissmissDialog();

    void tryAgain(String s);

    void navigateToHomePage();

    void savedDriverRating(String s);
}
