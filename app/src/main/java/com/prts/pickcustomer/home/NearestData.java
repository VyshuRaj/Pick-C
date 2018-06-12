package com.prts.pickcustomer.home;

import com.google.gson.annotations.SerializedName;

import static com.prts.pickcustomer.helpers.Constants.LATITUDE;
import static com.prts.pickcustomer.helpers.Constants.LONGITUDE;
import static com.prts.pickcustomer.helpers.Constants.VEHICLE_GROUP_JSON_KEY;
import static com.prts.pickcustomer.helpers.Constants.VEHICLE_TYPE_JSON_KEY;

/**
 * Created by LOGICON on 28-12-2017.
 */

public class NearestData {

    @SerializedName(LATITUDE)
    private double latitude;

    @SerializedName(LONGITUDE)
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOpenCloseId() {
        return openCloseId;
    }

    public void setOpenCloseId(String openCloseId) {
        this.openCloseId = openCloseId;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    @SerializedName(VEHICLE_TYPE_JSON_KEY)
    String openCloseId;

    @SerializedName(VEHICLE_GROUP_JSON_KEY)
    String truckId;

}
