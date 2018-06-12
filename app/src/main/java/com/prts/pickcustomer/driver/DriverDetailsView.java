package com.prts.pickcustomer.driver;

import com.prts.pickcustomer.booking.DriverCurrentLocation;
import com.prts.pickcustomer.home.DriverMonitorList;

/**
 * Created by LOGICON on 01-01-2018.
 */

public interface DriverDetailsView {
    void setDriverRating(int rating);
    void tryAgain();
    void bookingCanceled(String s, Boolean isCancelled);
    void createDriverRoute(DriverCurLatLng driverCurLatLng, boolean isReachedDeliveryLoc);

    void monitorDriver(DriverLocationUpdates driverCurrentLocation);
}
