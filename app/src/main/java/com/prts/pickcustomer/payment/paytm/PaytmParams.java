package com.prts.pickcustomer.payment.paytm;

public interface PaytmParams {
    //Credentials
    String MERCHANT_ID = "PRTECH81813676074829";
    String MERCHANT_KEY = "7PeD5JQt7od2qf7V";
    String INDUSTRY_TYPE_ID = "Retail";
    String CHANNLE_ID = "WAP";
    String WEBSITE = "APPSTAGING";
    String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";

    //Keys
    String CALLBACK_URL_KEY = "CALLBACK_URL";
    String CHANNEL_ID_KEY = "CHANNEL_ID";
    String CHECKSUMHASH_KEY = "CHECKSUMHASH";
    String CUST_ID_KEY = "CUST_ID";
    String INDUSTRY_TYPE_ID_KEY = "INDUSTRY_TYPE_ID";
    String MID_KEY = "MID";
    String ORDER_ID_KEY = "ORDER_ID";
    String TXN_AMOUNT_KEY = "TXN_AMOUNT";
    String WEBSITE_KEY = "WEBSITE";


}
