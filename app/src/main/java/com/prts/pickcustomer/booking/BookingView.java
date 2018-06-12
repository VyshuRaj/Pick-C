package com.prts.pickcustomer.booking;

import java.util.List;

/**
 * Created by LOGICON on 01-01-2018.
 */

public interface BookingView {
    void notAbleToCargoTypes();

    void setCargoTypes(List<CargoType> cargoTypes);

    void noInternet();

    void showProgressDialog();

    void dismissDialog();

    void bookingNotCreated(String message);

    void bookingCreated(String bookingNo);

    void bookingCanceled(String s);

    void tryAgain(String s);

}
