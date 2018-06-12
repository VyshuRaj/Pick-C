package com.prts.pickcustomer.restapi;

import com.prts.pickcustomer.booking.BookingInfor;
import com.prts.pickcustomer.booking.BookingStatus;
import com.prts.pickcustomer.booking.Cancel;
import com.prts.pickcustomer.booking.CargoType;
import com.prts.pickcustomer.changepassword.ChangePwd;
import com.prts.pickcustomer.driver.ConfirmedDriver;
import com.prts.pickcustomer.driver.DriverCurLatLng;
import com.prts.pickcustomer.driver.DriverLocationUpdates;
import com.prts.pickcustomer.forgotpqwd.Forgot;
import com.prts.pickcustomer.history.HistoryData;
import com.prts.pickcustomer.home.BookingInfo;
import com.prts.pickcustomer.home.IsReachPickUp;
import com.prts.pickcustomer.home.NearestData;
import com.prts.pickcustomer.home.PaymentStatus;
import com.prts.pickcustomer.home.TripStatus;
import com.prts.pickcustomer.home.TruckInNearLocation;
import com.prts.pickcustomer.invoice.UserTripInvoice;
import com.prts.pickcustomer.login.Credentials;
import com.prts.pickcustomer.login.Customer;
import com.prts.pickcustomer.login.Device;
import com.prts.pickcustomer.login.Token;
import com.prts.pickcustomer.maps.Response;
import com.prts.pickcustomer.payment.AmountResponse;
import com.prts.pickcustomer.payment.online.Online;
import com.prts.pickcustomer.payment.online.RSAKey;
import com.prts.pickcustomer.payment.paytm.Paytm;
import com.prts.pickcustomer.payment.paytm.TransactionStatus;
import com.prts.pickcustomer.profile.ProfileViewPresenterImpl;
import com.prts.pickcustomer.queries.Query;
import com.prts.pickcustomer.signup.SignUp;
import com.prts.pickcustomer.trucks.RateCard;
import com.prts.pickcustomer.trucks.TripEstimate;
import com.prts.pickcustomer.trucks.TripEstimateRes;
import com.prts.pickcustomer.trucks.VehicleGroupType;
import com.prts.pickcustomer.trucks.VehicleType;
import com.prts.pickcustomer.userrating.DriverRating;
import com.prts.pickcustomer.userrating.Rating;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import static com.prts.pickcustomer.restapi.RestAPIConstants.BOOKING_HISTORY;
import static com.prts.pickcustomer.restapi.RestAPIConstants.CANCEL_BOOKING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.CHANGE_PASSWORD;
import static com.prts.pickcustomer.restapi.RestAPIConstants.CHECK_TRANSCATION_STATUS;
import static com.prts.pickcustomer.restapi.RestAPIConstants.CONFIRM_BOOKING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.DRIVER_CUR_LATLNG_PICKUP;
import static com.prts.pickcustomer.restapi.RestAPIConstants.FORGOT_PWD;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GENERATE_CHECKSUM;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GENERATE_OTP;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_ADDRESS_FROM_LATLNG;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_AMT_CURRENT_BOOKING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_BOOKING_INFO;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_CARGO_TYPES;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_CONFIRMED_DRIVER_DETAILS;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_DRIVER_RATING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_OPEN_CLOSED;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_TRIP_ESTIMATE;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_TRUCKS_FROM_NEAR_LOCATION;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_USER_INVOICE_DETAILS;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_VEHICLE_TYPES;
import static com.prts.pickcustomer.restapi.RestAPIConstants.HAS_CUSTOMER_DUE_PAYMENT;
import static com.prts.pickcustomer.restapi.RestAPIConstants.IS_CUSTOMER_IN_TRIP;
import static com.prts.pickcustomer.restapi.RestAPIConstants.IS_DRIVER_REACHED_PICK_UP_LOCATION;
import static com.prts.pickcustomer.restapi.RestAPIConstants.IS_NEW_NUMBER;
import static com.prts.pickcustomer.restapi.RestAPIConstants.LOGIN;
import static com.prts.pickcustomer.restapi.RestAPIConstants.LOGOUT;
import static com.prts.pickcustomer.restapi.RestAPIConstants.MONITOR_DRIVER;
import static com.prts.pickcustomer.restapi.RestAPIConstants.ONLINE_PAYMENT;
import static com.prts.pickcustomer.restapi.RestAPIConstants.PAY_BY_CASH;
import static com.prts.pickcustomer.restapi.RestAPIConstants.SAVE_CUSTOMER_DETAILS;
import static com.prts.pickcustomer.restapi.RestAPIConstants.SAVE_DEVICE_ID_IN_SERVER;
import static com.prts.pickcustomer.restapi.RestAPIConstants.SELECTED_RATE_CARD;
import static com.prts.pickcustomer.restapi.RestAPIConstants.SEND_INVOICE_MAIL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.SEND_QUERY;
import static com.prts.pickcustomer.restapi.RestAPIConstants.UPDATE_USER_DATA;
import static com.prts.pickcustomer.restapi.RestAPIConstants.USER_DETAILS;
import static com.prts.pickcustomer.restapi.RestAPIConstants.USER_RATING_DRIVER;
import static com.prts.pickcustomer.restapi.RestAPIConstants.VALIDATE_YOUR_PWD;
import static com.prts.pickcustomer.restapi.RestAPIConstants.VERIFY_OTP;


/**
 * Created by LOGICON on 13-12-2017.
 */


public interface RestService {

    @POST(SAVE_CUSTOMER_DETAILS)
    Observable<Boolean> saveCustomerDetails(@HeaderMap Map<String, String> headers, @Body SignUp signUp);

    @GET(IS_NEW_NUMBER)
    Observable<Boolean> isExistedNumber(@Path("mobile") String mobile);

    @GET(IS_DRIVER_REACHED_PICK_UP_LOCATION)
    Observable<IsReachPickUp> isDriverReachedPickUpLocation(@HeaderMap Map<String, String> headers);

    @POST(LOGIN)
    Observable<Token> getToken(@HeaderMap Map<String, String> headers, @Body Credentials credentials);

    @GET(USER_DETAILS)
    Observable<Customer> getCustomerDetails(@HeaderMap Map<String, String> headers, @Path("mobile") String mobile);

    @POST(SAVE_DEVICE_ID_IN_SERVER)
    Observable<Boolean> sendDeviceIdToServer(@HeaderMap Map<String, String> headers, @Body Device device);

    @GET(VERIFY_OTP)
    Observable<Boolean> verifyOTP(@Path("mobile") String mobile, @Path("otp") String otp);

    @GET(GENERATE_OTP)
    Observable<Boolean> generateOTP(@Path("mobile") String mobile);

    @POST(FORGOT_PWD)
    Observable<Boolean> updatePassord(@HeaderMap Map<String, String> headers, @Body Forgot forgot);

    @GET(LOGOUT)
    Call<Boolean> logout(@HeaderMap Map<String, String> headers);

    @GET(GET_VEHICLE_TYPES)
    Observable<List<VehicleGroupType>> getVehicleGroups();

    @GET(GET_OPEN_CLOSED)
    Observable<List<VehicleType>> getOpenClosedList();

    @GET(VALIDATE_YOUR_PWD)
    Observable<Boolean> validateYourPwd(@HeaderMap Map<String, String> headers, @Path("mobile") String mobile, @Path("password") String pwd);

    @POST(UPDATE_USER_DATA)
    Call<Boolean> updateUserDataInServer(@HeaderMap Map<String, String> headers, @Path("mobile") String mobile, @Body ProfileViewPresenterImpl.data customer);

    @GET(BOOKING_HISTORY)
    Observable<List<HistoryData>> getBookingHistory(@HeaderMap Map<String, String> headers, @Path("mobile") String mobile);

    @GET(IS_CUSTOMER_IN_TRIP)
    Observable<TripStatus> isCustomerInTrip(@HeaderMap Map<String, String> map);

    @GET(HAS_CUSTOMER_DUE_PAYMENT)
    Observable<PaymentStatus> checkPaymentDueOfCustomer(@HeaderMap Map<String, String> map);

    @GET(GET_BOOKING_INFO)
    Observable<BookingInfo> getBookingInfoOfCustomer(@HeaderMap Map<String, String> map, @Path("bookingno") String bookNo);

    @POST(GET_TRUCKS_FROM_NEAR_LOCATION)
    Observable<List<TruckInNearLocation>> getTrucksFromNearLocation(@HeaderMap Map<String, String> map, @Body NearestData nabv);

    @POST(CHANGE_PASSWORD)
    Observable<Boolean> changePassword(@HeaderMap Map<String, String> map, @Body ChangePwd changePwd, @Path("mobile") String mobile);

    //Payment
    @GET(GET_AMT_CURRENT_BOOKING)
    Observable<List<AmountResponse>> getAmtOfCurBooking(@HeaderMap Map<String, String> map, @Path("bno") String bno);

    @GET(PAY_BY_CASH)
    Observable<Boolean> payAmountByCash(@HeaderMap Map<String, String> map, @Path("bookingNo") String bno, @Path("mDriverId") String driverId,@Path("payType") String payTypeId);

    @POST(ONLINE_PAYMENT)
    Observable<RSAKey> getRSAKey(@HeaderMap Map<String, String> userAgent, @Body Online online);

    @POST(GENERATE_CHECKSUM)
    Observable<String> getCheckSum(@HeaderMap Map<String, String> map, @Body Paytm online);

    @POST(CHECK_TRANSCATION_STATUS)
    Observable<String> checkTransactionStatus(@Body Paytm online);

    @POST(USER_RATING_DRIVER)
    Observable<Boolean> sendUserRating(@HeaderMap Map<String, String> map, @Body Rating rating);

    @GET(GET_USER_INVOICE_DETAILS)
    Observable<UserTripInvoice> getUserInvoiceDetails(@HeaderMap Map<String, String> map, @Path("bookingNumber") String bookingNumber);

    @GET(GET_DRIVER_RATING)
    Observable<DriverRating> getDriverAvgRarting(@HeaderMap Map<String, String> map, @Path("mDriverId") String driverId);

    @POST(SEND_QUERY)
    Observable<Boolean> sendQueryTOServer(@HeaderMap Map<String, String> map, @Body Query query);

    @GET(SEND_INVOICE_MAIL)
    Observable<Boolean> sendInvoiceMail(@Path("bno") String bookninum, @Path("email") String email);

    @GET(GET_CARGO_TYPES)
    Observable<List<CargoType>> getCargoTypes();

    @POST(GET_TRIP_ESTIMATE)
    Observable<TripEstimateRes> getTripEstaimate(@HeaderMap Map<String, String> map, @Body TripEstimate tripEstimate);

    @GET(SELECTED_RATE_CARD)
    Observable<List<RateCard>> getSelectedTruckRateCard(@HeaderMap Map<String, String> map, @Path("closedOpenId") String openClosedId, @Path("truckId") String truckId);

    @POST(CONFIRM_BOOKING)
    Observable<BookingStatus> confirmBooking(@HeaderMap Map<String, String> map, @Body BookingInfor bookingInfor);

    @GET(GET_CONFIRMED_DRIVER_DETAILS)
     Observable<ConfirmedDriver> getConfirmedDriverDetails(@HeaderMap Map<String, String> map, @Path("bno") String bnumber);

  /*  @GET(MONITOR_DRIVER)
    Observable<DriverLocationUpdates> updateDriverLocation(@HeaderMap Map<String, String> map, @Path("drvierId") String drvierId);
*/
    @GET(MONITOR_DRIVER)
    Call<DriverLocationUpdates> updateDriverLocation(@HeaderMap Map<String, String> map, @Path("drvierId") String drvierId);

    @GET(DRIVER_CUR_LATLNG_PICKUP)
    Observable<DriverCurLatLng> getDriverCurrentLat(@HeaderMap Map<String, String> map, @Path("dId") String drvierId);

    @POST(CANCEL_BOOKING)
    Observable<Boolean> cancelBooking(@HeaderMap Map<String, String> map, @Body Cancel cancel);

    //Google Map
    // http://maps.googleapis.com/maps/api/geocode/json?latlng=17.4436,78.4458 &sensor=true&language=en&country=india
    @GET(GET_ADDRESS_FROM_LATLNG)
    Observable<Response> getAddressFromLatLng(@QueryMap Map<String, String> map);

    //Paytm transction status api

    @FormUrlEncoded
    @GET("getTxnStatus=")
    Observable<TransactionStatus> getTransactionStatus(@FieldMap(encoded = true) Map<String,String> map);
}

