package com.prts.pickcustomer.booking;

/**
 * Created by LOGICON on 01-01-2018.
 */

public interface BookingPresenter {
    void downloadCargoTypes();
    void confirmBooking(BookingInfor bookingInfor);
    void cancelBooking(Cancel cancel);

}
