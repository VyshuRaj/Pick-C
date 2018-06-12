package com.prts.pickcustomer.driver;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LOGICON on 01-01-2018.
 */

public class Driver implements Serializable {


    @SerializedName("VehicleNo")
    private String vehicleNo;

    @SerializedName("IsConfirm")
    private boolean isConfirm;

    @SerializedName("DriverId")
    private String driverId;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("VehicleType")
    private int vehicleType;

    @SerializedName("DriverImage")
    private String driverImage;

    @SerializedName("DriverName")
    private String driverName;

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    @SerializedName("OTP")

    private int oTP;

    @SerializedName("Longitude")
    private double longitude;
    @SerializedName("MobileNo")
    private String  mobileNumber;

   // private String  bookingNumber;

   /* public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }*/

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setVehicleNo(String vehicleNo){
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNo(){
        return vehicleNo;
    }


    public void setDriverId(String driverId){
        this.driverId = driverId;
    }

    public String getDriverId(){
        return driverId;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setVehicleType(int vehicleType){
        this.vehicleType = vehicleType;
    }

    public int getVehicleType(){
        return vehicleType;
    }

    public void setDriverImage(String driverImage){
        this.driverImage = driverImage;
    }

    public String getDriverImage(){
        return driverImage;
    }

    public void setDriverName(String driverName){
        this.driverName = driverName;
    }

    public String getDriverName(){
        return driverName;
    }

    public void setOTP(int oTP){
        this.oTP = oTP;
    }

    public int getOTP(){
        return oTP;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return longitude;
    }


  //  public String getBookingNumber() {
    //    return bookingNumber;
   // }
}
