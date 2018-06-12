package com.prts.pickcustomer.payment.paytm;

import com.google.gson.annotations.SerializedName;

public class Paytm {
    @SerializedName("CustomerID")
    private String customerId;
    @SerializedName("OrderNo")
    private String orderNo;
    @SerializedName("Amount")
    private String amount;
    @SerializedName("MobileNo")
    private String mobileNo;
    @SerializedName("CustomerEmailID")
    private String customerEmailId;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }


}
