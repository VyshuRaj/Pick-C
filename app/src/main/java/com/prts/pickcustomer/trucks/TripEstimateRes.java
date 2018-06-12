package com.prts.pickcustomer.trucks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LOGICON on 18-01-2018.
 */

public class TripEstimateRes {

    @SerializedName("tripEstimateForCustomer")
    private List<TripEstimateForCustomerItem> tripEstimateForCustomer;

    public void setTripEstimateForCustomer(List<TripEstimateForCustomerItem> tripEstimateForCustomer){
        this.tripEstimateForCustomer = tripEstimateForCustomer;
    }

    public List<TripEstimateForCustomerItem> getTripEstimateForCustomer(){
        return tripEstimateForCustomer;
    }
}
