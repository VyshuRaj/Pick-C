package com.prts.pickcustomer.userrating;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satya on 30-Dec-17.
 */

public class Rating {
    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @SerializedName("BookingNo")
    String bookNo;
    @SerializedName("DriverID")
    String driverId;
    @SerializedName("Rating")
    String rating;
    @SerializedName("Remarks")
    String remarks;
}
