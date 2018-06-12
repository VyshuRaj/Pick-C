package com.prts.pickcustomer.restapi;

/**
 * Created by LOGICON on 13-12-2017.
 */

public interface RestAPIConstants {

  /*   String BASE_URL = "http://192.168.0.120/PickCApi/api/";
    String WEB_API_ADDRESS = "http://192.168.0.120/PickCApi/";*/

    String BASE_URL = "http://api.pickcargo.in/api/";
    String WEB_API_ADDRESS = "http://api.pickcargo.in/";
    String LOGIN = "master/customer/login";
    String IS_DRIVER_REACHED_PICK_UP_LOCATION = "master/customer/isReachPickupWaiting";//api/master/customer/isReachPickupWaiting
    String USER_DETAILS = "master/customer/{mobile}";
    String SAVE_DEVICE_ID_IN_SERVER = "master/customer/deviceId";
    String SAVE_CUSTOMER_DETAILS = "master/customer/save";
    String CHANGE_PASSWORD = "master/customer/changePassword/{mobile}";
    String IS_NEW_NUMBER = "master/customer/check/{mobile}";
    String VERIFY_OTP = "master/customer/verifyOtp/{mobile}/{otp}";
    String GENERATE_OTP = "master/customer/forgotPassword/{mobile}";
    String FORGOT_PWD = "master/customer/forgotPassword";
    String LOGOUT = "master/customer/logout";
    String GET_VEHICLE_TYPES = "master/customer/vehicleGroupList";
    String GET_OPEN_CLOSED = "master/customer/vehicleTypeList";
    String GET_TRUCKS_FROM_NEAR_LOCATION = "master/customer/user";
    String BOOKING_HISTORY = "master/customer/bookingHistoryListbyCustomerMobileNo/{mobile}";
    String VALIDATE_YOUR_PWD = "master/customer/checkCustomerPassword/{mobile}/{password}";
    String UPDATE_USER_DATA = "master/customer/{mobile}";
    String IS_CUSTOMER_IN_TRIP = "master/customer/isInTrip";
    String HAS_CUSTOMER_DUE_PAYMENT = "master/customer/customerPaymentsIsPaidCheck";
    String GET_BOOKING_INFO = "master/customer/booking/{bookingno}";
    String GET_AMT_CURRENT_BOOKING = "master/customer/billDetails/{bno}";
    String PAY_BY_CASH = "master/customer/pay/{bookingNo}/{mDriverId}/{payType}";
    String USER_RATING_DRIVER = "master/customer/driverRating";
    String GET_USER_INVOICE_DETAILS = "master/customer/tripInvoice/{bookingNumber}";
    String GET_DRIVER_RATING = "master/customer/avgDriverRating/{mDriverId}";
    String SEND_QUERY = "master/customer/sendMessageToPickC";
    String SEND_INVOICE_MAIL = "master/customer/sendInvoiceMail/{bno}/{email}/true";

    String ONLINE_PAYMENT = "master/customer/getRSAKey";
    String GENERATE_CHECKSUM = "master/customer/GeneratePayTMCheckSum";
    String CHECK_TRANSCATION_STATUS="master/customer/CheckPayTMTransactionStatus";
   // {"BookingNo":"BK180600059","Remarks":"Driver is late,Changed my mind"}
    //api/master/customer/getRSAKey
    // String GET_DRIVER_DETAILS = "master/customer/isConfirm/{bno}";
    String GET_CONFIRMED_DRIVER_DETAILS = "master/customer/isConfirm/{bno}";
    String GET_CARGO_TYPES = "master/customer/cargoTypeList";
    String GET_TRIP_ESTIMATE = "master/customer/tripEstimate";
    String SELECTED_RATE_CARD = "master/rateCard/{closedOpenId}/{truckId}";
    String CONFIRM_BOOKING = "master/customer/bookingSave";
    String MONITOR_DRIVER = "master/customer/driverMonitorInCustomer/{drvierId}";
    String CANCEL_BOOKING = "master/customer/cancelBooking";//master/customer/CancelBooking
    String DRIVER_CUR_LATLNG_PICKUP = "master/customer/drivergeOposition/{dId}";
    String TIME_OUT = "TimeoutError";
    int INTERVAL = 10000;

    //Google maps constans
    String GOOGLE_MAP_BASE_URL = "https://maps.googleapis.com/maps/api/";
    String GET_ADDRESS_FROM_LATLNG = "geocode/json";
    String GET_TRAVEL_TIME_BET_SOURCE_DESTINATION = "distancematrix/json";

    // https://maps.googleapis.com/maps/api/distancematrix/json?
}
