package com.prts.pickcustomer.invoice;

/**
 * Created by LOGICON on 31-12-2017.
 */

public interface InvoiceView {
    void noInterNet();

    void showProgressDialog(String string);

    void dismissDialog();


    void messageSent(String s);

    void setInoviceDetails(UserTripInvoice userTripInvoice);

    void invoiceDetailsNotAvailable(String s);
}
