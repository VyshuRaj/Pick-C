package com.prts.pickcustomer.booking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LOGICON on 02-01-2018.
 */

public class DriverCurrentLocation {
    @SerializedName("DriverId")
    private String driverId;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(String currentLng) {
        this.currentLng = currentLng;
    }

    @SerializedName("CurrentLat")
    private String currentLat;
    @SerializedName("CurrentLong")
    private String currentLng;

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    @SerializedName("SnappedPoints")
    private float accuracy;



}
