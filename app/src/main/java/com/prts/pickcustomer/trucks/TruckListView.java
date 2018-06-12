package com.prts.pickcustomer.trucks;



import java.util.List;

/**
 * Created by LOGICON on 19-12-2017.
 */

public interface TruckListView {

    void setDefaultTrucks(List<VehicleGroupType> truck);
    void setDefaultOpenClosed(List<VehicleType> openClosedList);

    void noInternet();

    void trucksNotavailable(String s);

    void setRateCardData(RateCard rateCard);

    void setTripEstimateData(TripEstimateRes response);

    // void setRateCards(List<RateCard> rateCards);
}
