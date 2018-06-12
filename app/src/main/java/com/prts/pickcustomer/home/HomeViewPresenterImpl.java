package com.prts.pickcustomer.home;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.prts.pickcustomer.booking.DriverCurrentLocation;
import com.prts.pickcustomer.driver.ConfirmedDriver;
import com.prts.pickcustomer.driver.DriverLocationUpdates;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.VolleyHelper;
import com.prts.pickcustomer.login.Device;
import com.prts.pickcustomer.maps.Response;
import com.prts.pickcustomer.restapi.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_ADDRESS_FROM_LATLNG;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_BOOKING_INFO;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_CONFIRMED_DRIVER_DETAILS;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_TRUCKS_FROM_NEAR_LOCATION;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GOOGLE_MAP_BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.HAS_CUSTOMER_DUE_PAYMENT;
import static com.prts.pickcustomer.restapi.RestAPIConstants.IS_CUSTOMER_IN_TRIP;
import static com.prts.pickcustomer.restapi.RestAPIConstants.IS_DRIVER_REACHED_PICK_UP_LOCATION;
import static com.prts.pickcustomer.restapi.RestAPIConstants.SAVE_DEVICE_ID_IN_SERVER;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;
import static com.prts.pickcustomer.restapi.RestClient.getRestService;


public class HomeViewPresenterImpl implements HomeViewPresenter {

    private HomeView mHomeView;
    private Context mContext;

    HomeViewPresenterImpl(Context context, HomeView homeActivity1) {
        mHomeView = homeActivity1;
        mContext = context;
    }

    @Override
    public void getAddressFromLatLng(String fromAddress) {

        try {
            if(!NetworkHelper.hasNetworkConnection(mContext)){
                ToastHelper.noInternet(mContext);
                return;
            }

            Map<String, String> map = new HashMap<>();
            map.put("latlng", fromAddress);
            map.put("sensor", "true");
            map.put("language", String.valueOf(Locale.ENGLISH));
            map.put("language", String.valueOf(Locale.getDefault().getCountry()));

            LogUtils.appendLog(mContext,"-----getAddressFromLatLng-----");
            LogUtils.appendLog(mContext,"API:-"+GOOGLE_MAP_BASE_URL+GET_ADDRESS_FROM_LATLNG);
            LogUtils.appendLog(mContext,"QueryParams:-"+GOOGLE_MAP_BASE_URL+GET_ADDRESS_FROM_LATLNG);

            RestClient.getRestService(GOOGLE_MAP_BASE_URL).getAddressFromLatLng(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response response) {
                            String address = "";
                            try {
                                if (response.getResults().size() > 0) {
                                    address = response.getResults().get(0).getFormattedAddress();
                                }

                               mHomeView.setCurrentAddress(address);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            try {
                                LogUtils.appendLog(mContext,"getAddressFromLatLng:onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            LogUtils.appendLog(mContext, "Home" + " getAddressFromLatLng error :- Exception" + String.valueOf(e));

        }
    }

    @Override
    public void sendTokenAndMobileToServer( String deviceId, final String number) {

        if(deviceId == null)
            deviceId= FirebaseInstanceId.getInstance().getToken();

        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setMobileNo(number);

        Log.e("TAG","device "+new Gson().toJson(device));

        LogUtils.appendLog(mContext,"-----sendTokenAndMobileToServer-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+SAVE_DEVICE_ID_IN_SERVER);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
        LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(device));


        getRestService(BASE_URL).sendDeviceIdToServer(getHeaders(mContext), device)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean customer) {
                        Log.e("TAG", "DeviceId Saving.." + customer);
                        LogUtils.appendLog(mContext,"sendDeviceIdToServer Reponse:-"+customer+"\n\n\n\n\n");

                    }

                    @Override
                    public void onError(Throwable e) {


                        try {
                            String message = e.getMessage();
                            ActivityHelper.handleException(mContext, message);
                            LogUtils.appendLog(mContext,"sendDeviceIdToServer Reponse:-"+new Gson().toJson(e)+"\n\n\n\n\n");

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTrucksNearByLocation(final NearestData nearestData) {

        Log.e("TAG", "NearsetData " + new Gson().toJson(nearestData));
        LogUtils.appendLog(mContext,"-----getTrucksNearByLocation-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+GET_TRUCKS_FROM_NEAR_LOCATION);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
        LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(nearestData));



        RestClient.getRestService(BASE_URL).getTrucksFromNearLocation(getHeaders(mContext), nearestData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TruckInNearLocation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<TruckInNearLocation> truckInNearLocations) {
                        try {
                            LogUtils.appendLog(mContext,"getTrucksNearByLocation Reponse:-"+new Gson().toJson(truckInNearLocations)+"\n\n\n\n\n");

                            if (truckInNearLocations != null && truckInNearLocations.size() > 0) {
                                mHomeView.setNearByTrucksOnMap(truckInNearLocations);
                            } else {
                                mHomeView.noTruckFound();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        String message = e.getMessage();
                        LogUtils.appendLog(mContext,"getTrucksNearByLocation onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");

                        //LogUtils.appendLog(mContext, "Home" + " getTrucksNearByLocation error :- Exception" + message);

                        try {
                            if (message!=null) {
                                if (message.contains(TIME_OUT)) {
                                    getTrucksNearByLocation(nearestData);
                                } else {
                                    ActivityHelper.handleException(mContext, message);
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTravelTimeBwSourceAndDest(final String fromLatLng, final String toLatLng, final boolean isDriver) {

        try {
         RequestQueue queue = VolleyHelper.getVolleyInstance(mContext);//.newRequestQueue(mContext);
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + fromLatLng + "&destinations=" + toLatLng + "&key=AIzaSyDZzsUIdzyATngclxURzHTX9ijd_GlpuV0&mode=driving";

            Log.e("TAG","url "+url);

            LogUtils.appendLog(mContext,"-----getTravelTimeBwSourceAndDest-----");
            LogUtils.appendLog(mContext,"API:-"+url);


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Distance", "response " + response);
                    LogUtils.appendLog(mContext,"getTravelTimeBwSourceAndDest Resonpce:-"+response+"\n\n\n\n\n");
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");

                        if (jsonArray != null && jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                JSONArray jsonArray1 = jsonObj.getJSONArray("elements");

                                if (jsonArray1 != null && jsonArray1.length() > 0) {
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject eleObj = jsonArray1.getJSONObject(j);
                                        JSONObject durationObj = eleObj.getJSONObject("duration");
                                        String duration = durationObj.getString("text");
                                        JSONObject distanceObj = eleObj.getJSONObject("distance");
                                        String distance = durationObj.getString("text");
                                        String[]  dis=distance.split(" ");
                                        double d=Integer.parseInt(dis[0])*0.3048;
                                     //1ft=0.3048

                                        if (!isDriver) {
                                            mHomeView.setETAToSelectedForTruck(duration,String.valueOf(d));
                                        } else {
                                            mHomeView.setETAToSelectedForDriver(duration,String.valueOf(d));
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtils.appendLog(mContext,"getTravelTimeBwSourceAndDest Resonpce:-"+new Gson().toJson(error)+"\n\n\n\n\n");
                   // LogUtils.appendLog(mContext, "Home" + " getTravelTimeBwSourceAndDest error :- Exception" + String.valueOf(error));
                    ActivityHelper.handleException(mContext, String.valueOf(error));
                }
            });
            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkIsCustomerInTrip() {

        LogUtils.appendLog(mContext,"-----checkIsCustomerInTrip-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+IS_CUSTOMER_IN_TRIP);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
       // LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(nearestData));


        RestClient.getRestService(BASE_URL).isCustomerInTrip(getHeaders(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TripStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TripStatus tripStatus) {
                        try {
                            LogUtils.appendLog(mContext,"checkIsCustomerInTrip Resonpce:-"+new Gson().toJson(tripStatus)+"\n\n\n\n\n");

                            if (tripStatus != null) {
                                if (tripStatus.isIsintrip()) {

                                    Log.e("TAG", "tripStatus.getBookingno()" + tripStatus.getBookingno());
                                    getDriverDetails(tripStatus.getBookingno(), true, false);

                                } else {
                                    checkIsCustomerReachedPickUpLocation();
                                }
                            } else {
                                mHomeView.initializeTheTrucksAndBookingLatouyts();
                            }
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "Home" + " checkIsCustomerInTrip onNext :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            String message = e.getMessage();
                            LogUtils.appendLog(mContext,"checkIsCustomerInTrip onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");

                            if (message.contains(TIME_OUT)) {
                                checkIsCustomerInTrip();

                            } else {
                                ActivityHelper.handleException(mContext, message);
                                mHomeView.initializeTheTrucksAndBookingLatouyts();


                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void checkIsCustomerReachedPickUpLocation() {

        LogUtils.appendLog(mContext,"-----checkIsCustomerReachedPickUpLocation-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+IS_DRIVER_REACHED_PICK_UP_LOCATION);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
        // LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(nearestData));


        RestClient.getRestService(BASE_URL).isDriverReachedPickUpLocation(getHeaders(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IsReachPickUp>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(IsReachPickUp isReachPickUp) {
                        LogUtils.appendLog(mContext,"checkIsCustomerReachedPickUpLocation Resonpce:-"+new Gson().toJson(isReachPickUp)+"\n\n\n\n\n");

                        if (isReachPickUp != null) {
                            if (isReachPickUp.getBookingNo() != null)
                            {
                                if (isReachPickUp.getBookingNo().length() <= 0) {
                                    checkPaymentDue();
                                } else {
                                  //  mHomeView.checkReachedPickUpLocation(isReachPickUp.getMessage());
                                    getDriverDetails(isReachPickUp.getBookingNo(), false, false);
                                }
                            } else {
                                mHomeView.initializeTheTrucksAndBookingLatouyts();
                            }

                        } else {
                            checkPaymentDue();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            String message = e.getMessage();
                            LogUtils.appendLog(mContext,"checkIsCustomerInTrip onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");
                            Log.e("TAG", "Exceptiuon " + message);

                            if (message!=null) {
                                ActivityHelper.handleException(mContext, message);
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void getBookingInfo(final String bookingInfo, final boolean isTripStarted) {

        LogUtils.appendLog(mContext,"-----getBookingInfo-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+GET_BOOKING_INFO+"/"+bookingInfo);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
        // LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(nearestData));


        RestClient.getRestService(BASE_URL).getBookingInfoOfCustomer(getHeaders(mContext), bookingInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookingInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookingInfo bookingInfo1) {
                        LogUtils.appendLog(mContext,"getBookingInfo onResponse:-"+new Gson().toJson(bookingInfo1)+"\n\n\n\n\n");

                        if (bookingInfo1 != null && bookingInfo1.getBookingNo() != null && bookingInfo1.getBookingNo().length() > 0) {
                            mHomeView.setBookingData(bookingInfo1);
                            getDriverDetails(bookingInfo, isTripStarted);
                        }else{
                            mHomeView.initializeTheTrucksAndBookingLatouyts();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            LogUtils.appendLog(mContext,"getBookingInfo onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");

                            String message = e.getMessage();
                            ActivityHelper.handleException(mContext, message);



                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getDriverDetails(final String bookingNumber, final boolean isTripStarted, boolean isFirstTime) {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            ToastHelper.noInternet(mContext);
            return;
        }

        if(!isFirstTime)
        getBookingInfo(bookingNumber, isTripStarted);
        else
        getDriverDetails(bookingNumber, isTripStarted);

    }


    private void getDriverDetails(final String bookingNumber, final boolean isTripStarted) {

        LogUtils.appendLog(mContext,"-----getDriverDetails-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+GET_CONFIRMED_DRIVER_DETAILS+"/"+bookingNumber);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
        // LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(nearestData));

        RestClient.getRestService(BASE_URL).getConfirmedDriverDetails(getHeaders(mContext), bookingNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ConfirmedDriver>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ConfirmedDriver confirmedDriver) {

                        try {
                            LogUtils.appendLog(mContext,"getDriverDetails onResponse:-"+new Gson().toJson(confirmedDriver)+"\n\n\n\n\n");

                            Log.e("TAG", "ConfirmedDriver " + new Gson().toJson(confirmedDriver));

                            if (confirmedDriver != null && confirmedDriver.isConfirm()) {
                                confirmedDriver.setBookingNumber(bookingNumber);
                                confirmedDriver.setInTrip(isTripStarted);
                                confirmedDriver.setReachedPickupLocation(isTripStarted);
                                CredentialManager.setDriverId(mContext,confirmedDriver.getDriverId());
                                mHomeView.showDriverDetailsOnMap(confirmedDriver);
                            } else {
                                mHomeView.initializeTheTrucksAndBookingLatouyts();
                            }
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "Home" + " getDriverDetails onNext :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            LogUtils.appendLog(mContext,"getDriverDetails onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");
                            ActivityHelper.handleException(mContext, String.valueOf(e));

                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, "Home" + " getDriverDetails onError :- Exception" + e1.getMessage());

                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void checkPaymentDue() {

        LogUtils.appendLog(mContext,"-----checkPaymentDue-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+HAS_CUSTOMER_DUE_PAYMENT);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
        // LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(nearestData));

        RestClient.getRestService(BASE_URL).checkPaymentDueOfCustomer(getHeaders(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PaymentStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PaymentStatus paymentStatus) {

                        try {
                            LogUtils.appendLog(mContext,"checkPaymentDue NearByPlacesResponse:-"+new Gson().toJson(paymentStatus)+"\n\n\n\n\n");
                            Log.e("TAG", "paymentStatus " + new Gson().toJson(paymentStatus));
                            if (paymentStatus != null && !paymentStatus.getStatus() && paymentStatus.getResult().length() > 0) {
                                mHomeView.navigateTOPaymentActivty(paymentStatus.getResult());
                            } else {
                                mHomeView.initializeTheTrucksAndBookingLatouyts();
                            }

                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "Home" + " checkPaymentDue onNext :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            LogUtils.appendLog(mContext,"checkPaymentDue onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");
                            String message = e.getMessage();
                            ActivityHelper.handleException(mContext, message);
                            LogUtils.appendLog(mContext, "Home" + " checkPaymentDue onError :- Exception" + message);
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


}
