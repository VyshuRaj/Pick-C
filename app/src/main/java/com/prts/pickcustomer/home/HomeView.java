package com.prts.pickcustomer.home;

import com.prts.pickcustomer.driver.ConfirmedDriver;

import java.util.List;

/**
 * Created by LOGICON on 19-12-2017.
 */

public interface HomeView {


    void initializeTheTrucksAndBookingLatouyts();

    void setNearByTrucksOnMap(List<TruckInNearLocation> truckInNearLocations);

    void setETAToSelectedForTruck(String duration, String distance);

    void setETAToSelectedForDriver(String duration, String distance);

    void tryAgain();

    void navigateTOPaymentActivty(String result);

    void showDriverDetailsOnMap(ConfirmedDriver confirmedDriver);

    void noTruckFound();

    void setBookingData(BookingInfo bookingInfo1);

    void setCurrentAddress(String address);
}
