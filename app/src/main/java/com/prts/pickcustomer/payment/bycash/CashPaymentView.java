package com.prts.pickcustomer.payment.bycash;

/**
 * Created by satya on 30-Dec-17.
 */

public interface CashPaymentView {


    void noInterNet();

    void showProgressDialog();

    void tryAgainMessage();

    void dismissDialog();

    void navigateToInvoicePage(String paymentReceived);
}
