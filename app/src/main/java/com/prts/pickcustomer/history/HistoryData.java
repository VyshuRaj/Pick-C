package com.prts.pickcustomer.history;

import com.google.gson.annotations.SerializedName;

public class HistoryData {

    @SerializedName("BookingDate")
    private String bookingDate;

    @SerializedName("IsPaid")
    private boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @SerializedName("IsConfirm")
    private boolean isConfirm;

    @SerializedName("VehicleNo")
    private String vehicleNo;

    @SerializedName("ConfirmDate")
    private String confirmDate;

    @SerializedName("LocationTo")
    private String locationTo;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("CustomerID")
    private String customerID;

    @SerializedName("PayLoad")
    private String payLoad;

    @SerializedName("ReceiverMobileNo")
    private String receiverMobileNo;

    @SerializedName("DriverRating")
    private int driverRating;

    @SerializedName("ToLongitude")
    private double toLongitude;

    @SerializedName("DriverID")
    private String driverID;

    @SerializedName("LocationFrom")
    private String locationFrom;

    @SerializedName("Remarks")
    private String remarks;

    @SerializedName("PaymentType")
    private int paymentType;

    @SerializedName("CustomerName")
    private String customerName;

    @SerializedName("LoadingUnLoadingDescription")
    private String loadingUnLoadingDescription;

    @SerializedName("CompleteTime")
    private String completeTime;

    @SerializedName("Status")
    private int status;

    @SerializedName("RequiredDate")
    private String requiredDate;

    @SerializedName("CancelTime")
    private String cancelTime;

    @SerializedName("IsCancel")
    private boolean isCancel;

    @SerializedName("VehicleType")
    private int vehicleType;

    @SerializedName("ToLatitude")
    private double toLatitude;

    @SerializedName("IsComplete")
    private boolean isComplete;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("VehicleGroup")
    private int vehicleGroup;

    @SerializedName("CargoDescription")
    private String cargoDescription;

    @SerializedName("CancelRemarks")
    private String cancelRemarks;

    @SerializedName("BookingNo")
    private String bookingNo;

    @SerializedName("TripAmount")
    private double tripAmount;

    @SerializedName("LoadingUnLoading")
    private int loadingUnLoading;

    @SerializedName("CargoType")
    private String cargoType;


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    private boolean isCompleted;

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }


    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setIsConfirm(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }

    public boolean isIsConfirm() {
        return isConfirm;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setReceiverMobileNo(String receiverMobileNo) {
        this.receiverMobileNo = receiverMobileNo;
    }

    public String getReceiverMobileNo() {
        return receiverMobileNo;
    }

    public void setDriverRating(int driverRating) {
        this.driverRating = driverRating;
    }

    public int getDriverRating() {
        return driverRating;
    }

    public void setToLongitude(double toLongitude) {
        this.toLongitude = toLongitude;
    }

    public double getToLongitude() {
        return toLongitude;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setLoadingUnLoadingDescription(String loadingUnLoadingDescription) {
        this.loadingUnLoadingDescription = loadingUnLoadingDescription;
    }

    public String getLoadingUnLoadingDescription() {
        return loadingUnLoadingDescription;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public boolean isIsCancel() {
        return isCancel;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setToLatitude(double toLatitude) {
        this.toLatitude = toLatitude;
    }

    public double getToLatitude() {
        return toLatitude;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public boolean isIsComplete() {
        return isComplete;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setVehicleGroup(int vehicleGroup) {
        this.vehicleGroup = vehicleGroup;
    }

    public int getVehicleGroup() {
        return vehicleGroup;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCancelRemarks(String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setTripAmount(double tripAmount) {
        this.tripAmount = tripAmount;
    }

    public double getTripAmount() {
        return tripAmount;
    }

    public void setLoadingUnLoading(int loadingUnLoading) {
        this.loadingUnLoading = loadingUnLoading;
    }

    public int getLoadingUnLoading() {
        return loadingUnLoading;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public String getCargoType() {
        return cargoType;
    }


    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    private String driverId;

    public String getDriverId() {
        return driverId;
    }


    public void setAvgDriverRating(String avgDriverRating) {
        this.avgDriverRating = avgDriverRating;
    }

    String avgDriverRating;

    public String getAvgDriverRating() {
        return avgDriverRating;
    }
}