package com.prts.pickcustomer.driver;

import com.prts.pickcustomer.booking.Cancel;

/**
 * Created by LOGICON on 01-01-2018.
 */

public interface DriverDetailsPresnter {
    void getDriverAvgRating(String driverId);
    void cancelBooking(Cancel cancel);
    void getDriverCurrentLatLngOnReachedPickUp(String driverId, boolean isReachedDeliveryLoc);

    void monitorDriver(String driverId);
}
