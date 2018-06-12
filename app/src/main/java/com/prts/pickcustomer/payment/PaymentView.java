package com.prts.pickcustomer.payment;

/**
 * Created by satya on 30-Dec-17.
 */

public interface PaymentView {
    void noInternet();

    void setAmountOfCurrentBooking(AmountResponse amountResponse);

    void showDialog();

    void dismissDialog();

    void paymentAmtNotAvailable(String s);
}
