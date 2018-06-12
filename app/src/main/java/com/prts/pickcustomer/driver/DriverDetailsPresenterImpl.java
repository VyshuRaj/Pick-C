package com.prts.pickcustomer.driver;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.prts.pickcustomer.booking.Cancel;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.restapi.RestClient;
import com.prts.pickcustomer.userrating.DriverRating;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.CANCEL_BOOKING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.DRIVER_CUR_LATLNG_PICKUP;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_DRIVER_RATING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.HAS_CUSTOMER_DUE_PAYMENT;
import static com.prts.pickcustomer.restapi.RestAPIConstants.MONITOR_DRIVER;

/**
 * Created by LOGICON on 01-01-2018.
 */

public class DriverDetailsPresenterImpl implements DriverDetailsPresnter {
    private Context mContext;
    private DriverDetailsView mDriverDetailsView;

    DriverDetailsPresenterImpl(Context context, DriverDetailsView driverDetailsFragment) {
        mContext = context;
        mDriverDetailsView = driverDetailsFragment;
    }

    @Override
    public void getDriverAvgRating(final String driverId) {

        if(!NetworkHelper.hasNetworkConnection(mContext)){
            ToastHelper.noInternet(mContext);
            return;
        }

        LogUtils.appendLog(mContext,"-----getDriverAvgRating-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+GET_DRIVER_RATING+"/"+driverId);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
        // LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(nearestData));


        CredentialManager.setShowingLiveUpdateMarker(mContext, true);

        RestClient.getRestService(BASE_URL).getDriverAvgRarting(getHeaders(mContext), driverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DriverRating>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DriverRating driverRating) {
                        try {
                            LogUtils.appendLog(mContext,"getDriverAvgRarting onError:-"+new Gson().toJson(driverRating)+"\n\n\n\n\n");

                            mDriverDetailsView.setDriverRating(driverRating.getRating());
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "DriverDetailsFragment" + " getDriverAvgRating onNext :- Exception" + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                           // LogUtils.appendLog(mContext, "DriverDetailsFragment" + " getDriverAvgRating onError :- Exception" + e.getMessage());

                            LogUtils.appendLog(mContext,"getDriverAvgRarting onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");

                            String message = e.getMessage();
                            ActivityHelper.handleException(mContext, message);

                          /*  if (message.contains(TIME_OUT)) {
                                getDriverAvgRating(driverId);
                            } else {
                                ActivityHelper.handleException(mContext, message);
                            }*/

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
    public void cancelBooking(final Cancel cancel) {
        LogUtils.appendLog(mContext,"-----CancelBooking-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+CANCEL_BOOKING);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
         LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(cancel));


        RestClient.getRestService(BASE_URL).cancelBooking(getHeaders(mContext), cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isCancelled) {
                        LogUtils.appendLog(mContext,"cancelBooking NearByPlacesResponse:-"+new Gson().toJson(isCancelled)+"\n\n\n\n\n");

                        try {
                            if (isCancelled)
                                mDriverDetailsView.bookingCanceled("Booking cancelled successfully", true);
                            else
                                mDriverDetailsView.bookingCanceled("Booking cancel request has been failed", false);
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "DriverDetailsFragment" + " cancelBooking onNext :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            String message = e.getMessage();
                            ActivityHelper.handleException(mContext, message);
                            LogUtils.appendLog(mContext,"cancelBooking onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");
                          /*  if (message.contains(TIME_OUT)) {
                                cancelBooking(cancel);
                            } else {
                                ActivityHelper.handleException(mContext, message);
                            }*/
                        } catch (Exception e1) {
                           // LogUtils.appendLog(mContext, "DriverDetailsFragment" + " cancelBooking onError :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getDriverCurrentLatLngOnReachedPickUp(final String driverId, final boolean isReachedDeliveryLoc) {

        if(!NetworkHelper.hasNetworkConnection(mContext))
        {
            ToastHelper.noInternet(mContext);
            return;
        }

        LogUtils.appendLog(mContext,"-----getDriverCurrentLatLngOnReachedPickUp-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+DRIVER_CUR_LATLNG_PICKUP+"/"+driverId);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
       // LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(cancel));


        RestClient.getRestService(BASE_URL).getDriverCurrentLat(getHeaders(mContext), driverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DriverCurLatLng>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DriverCurLatLng driverCurLatLng) {
                        LogUtils.appendLog(mContext,"getDriverCurrentLatLngOnReachedPickUp NearByPlacesResponse:-"+new Gson().toJson(driverCurLatLng)+"\n\n\n\n\n");

                        try {
                            if (driverCurLatLng != null && driverCurLatLng.getCurrentLong() > 0 && driverCurLatLng.getCurrentLat() > 0)
                                mDriverDetailsView.createDriverRoute(driverCurLatLng, isReachedDeliveryLoc);
                        } catch (Exception e) {
                           // LogUtils.appendLog(mContext, "DriverDetailsFragment" + " getDriverCurrentLatLngOnReachedPickUp onNext :- Exception" + e.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            LogUtils.appendLog(mContext,"getDriverCurrentLatLngOnReachedPickUp onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");
                            String message = e.getMessage();
                            ActivityHelper.handleException(mContext, message);

                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, "DriverDetailsFragment" + " getDriverCurrentLatLngOnReachedPickUp onError :- Exception" + e.getMessage());

                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void monitorDriver(final String driverId) {

        if(!NetworkHelper.hasNetworkConnection(mContext)){
            ToastHelper.noInternet(mContext);
            return;
        }


        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Call<DriverLocationUpdates> driverDutyStatus = RestClient.getRestService(BASE_URL).updateDriverLocation(getHeaders(mContext), driverId);

                    try {
                        retrofit2.Response<DriverLocationUpdates> response = driverDutyStatus.execute();

                        LogUtils.appendLog(mContext,"-----monitorDriver-----");
                        LogUtils.appendLog(mContext,"API:-"+BASE_URL+MONITOR_DRIVER+"/"+driverId);
                        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));

                        if (response.body() != null) {
                            DriverLocationUpdates resu = response.body();



                            mDriverDetailsView.monitorDriver(resu);

                            Log.e("DriDetPresenterImpl","monitorDriver "+new Gson().toJson(resu));

                            LogUtils.writeToLog(mContext, "NearByPlacesResponse:-" + new Gson().toJson(resu));
                            LogUtils.appendLog(mContext, "NearByPlacesResponse:-" + new Gson().toJson(resu));
                        }else{
                            LogUtils.appendLog(mContext,"-----monitorDriver-----");
                            LogUtils.appendLog(mContext,"API:-"+BASE_URL+MONITOR_DRIVER+"/"+driverId);
                            LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
                            LogUtils.appendLog(mContext, "NearByPlacesResponse:- null" );
                        }

                    } catch (Exception e) {
                        Log.e("DriDetPresenterImpl"," monitorDriver:-   Exception" + e.getMessage());
                        LogUtils.appendLog(mContext,  " monitorDriver:-   Exception" + e.getMessage());
                        ActivityHelper.handleException(mContext,String.valueOf(e.getMessage()));
                    }
                }
                catch (Exception ex) {
                    Log.e("TAG","Exception "+ex.getMessage());
                }
            }
        }).start();


    }
}
