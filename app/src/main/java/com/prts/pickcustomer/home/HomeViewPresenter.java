package com.prts.pickcustomer.home;

/**
 * Created by LOGICON on 19-12-2017.
 */

public interface HomeViewPresenter {

    void sendTokenAndMobileToServer(String token,String number);

    void getAddressFromLatLng(String fromAddress);

    void getTrucksNearByLocation(NearestData nearestData);

    void getTravelTimeBwSourceAndDest(String fromLatLng,String toLatLng,boolean isTruck);

    void checkIsCustomerInTrip();

    void getDriverDetails(String bookingNo, boolean fa, boolean isFirstTime);

}
