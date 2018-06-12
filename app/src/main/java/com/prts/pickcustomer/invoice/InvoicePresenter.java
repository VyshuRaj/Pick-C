package com.prts.pickcustomer.invoice;

/**
 * Created by LOGICON on 31-12-2017.
 */

public interface InvoicePresenter {


    void sendInoviceEmail(String emailIdentered, String bookingNo);

    void getTheInvoceDetails(String bookingNumber);
}
