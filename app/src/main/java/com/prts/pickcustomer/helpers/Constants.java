package com.prts.pickcustomer.helpers;

/**
 * Created by LOGICON on 18-12-2017.
 */

public interface Constants {

    int VEHICLE_TYPE_OPEN = 1300;
    int VEHICLE_TYPE_CLOSED = 1301;

    //String REL_SHA1= "CC:82:DF:07:2B:58:EE:62:83:43:F8:33:42:99:F9:8E:EC:76:8F:78"; //RElease Sha1
    //String DEBUG_SHA1= "A2:F8:EB:2C:21:6C:76:6D:18:C5:3B:CC:DD:65:B7:E2:03:07:30:BF";//Debug SHA1


    String RATE_CARD_URL = "http://pickcargo.in/RateCard/mobile";
    String TERMS_AND_CONDITIONS = "http://pickcargo.in/dashboard/mobiletermsandconditions";
    String PRIVACY_POLICY = "http://pickcargo.in/Dashboard/PrivacyPolicyMobile";
    String HELP_URL = "http://pickcargo.in/Dashboard/helpmobile";
    String STATUS_CONFIRMED = "CONFIRMED";
    String STATUS_CANCELLED = "CANCELLED";
    String STATUS_COMPLETED = "COMPLETED";
    String STATUS_PENDING = "PENDING";
    String STATUS_NOT_SPECIFIED = "NO_STATUS";
    String LATITUDE = "Latitude";
    String LONGITUDE = "Longitude";
    String VEHICLE_TYPE_JSON_KEY = "VehicleType";
    String VEHICLE_GROUP_JSON_KEY = "VehicleGroup";
    String PARAMETER_SEP = "&";
    String PARAMETER_EQUALS = "=";
    String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";
    String[] FEEDBACK = {"Driver is late", "Driver attitude was not good", "Driver was drunk", "Driver behaviour was not good"};
    String rupee = "₹";
    String dateSeperator = "/";
    String timeSeperator = ":";
    String FINISH_ACTIVITY = "finish";
    String ENABLE_BOOKING = "enable_booking";
    String DISABLE_ON_BACK_PRESS = "disable_back_press";
    String TOKEN_EXPIRED = "INVALID MOBILENO OR AUTH TOKEN";

    String BOOKING_CONFIRMED = "Booking Confirmed";
    String BOOKING_FAILED = "Booking Failed";
    String BOOKING_CANCELLED_BY_DRIVER = "Booking Cancelled by driver";
    String TRIP_STARTED = "Trip Started";
    String TRIP_END = "Trip End";
    String DRIVER_REACHED_PICK_UP_LOCATION = "Driver has reached pick up location";
    String DRIVER_REACHED_DROP_LOCATION = "Driver reached delivery location";
    String INVOICE_GENERATED = "Invoice Generated";
    String GENERATE_INVOICE = "DriverpaymentReceived";
    String ABOUTTOREACHPICKUPLOCATION = "Driver is about to reach pickup location";
    String BODY_KEY = "body";
    String BOOKING_NO_KEY = "bookingNo";
}
