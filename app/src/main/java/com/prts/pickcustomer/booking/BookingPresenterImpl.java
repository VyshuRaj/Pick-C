package com.prts.pickcustomer.booking;

import android.content.Context;

import com.google.gson.Gson;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.CANCEL_BOOKING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.CONFIRM_BOOKING;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_CARGO_TYPES;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by LOGICON on 01-01-2018.
 */

public class BookingPresenterImpl implements BookingPresenter {
    String TAG = "BookingPresenterImpl";
    private Context mContext;
    private BookingView mBookingView;

    BookingPresenterImpl(Context context, BookingView bookingFragment) {
        mBookingView = bookingFragment;
        mContext = context;
    }


    @Override
    public void downloadCargoTypes() {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mBookingView.noInternet();
            return;
        }

        LogUtils.appendLog(mContext, "-----downloadCargoTypes-----");
        LogUtils.appendLog(mContext, "API:-" + BASE_URL + GET_CARGO_TYPES);

        RestClient.getRestService(BASE_URL).getCargoTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CargoType>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CargoType> cargoTypes) {
                        try {
                            LogUtils.appendLog(mContext, "downloadCargoTypes NearByPlacesResponse:-" + new Gson().toJson(cargoTypes) + "\n\n\n\n\n");

                            if (cargoTypes != null && cargoTypes.size() > 0) {
                                mBookingView.setCargoTypes(cargoTypes);
                            } else {
                                mBookingView.notAbleToCargoTypes();
                            }
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, " downloadCargoTypes onNext :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.appendLog(mContext, "downloadCargoTypes onError:-" + new Gson().toJson(e) + "\n\n\n\n\n");

                        try {
                            //LogUtils.appendLog(mContext, "BF" + " downloadCargoTypes onError :- Exception" + e.getMessage());

                            String message = e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                downloadCargoTypes();
                            } else {
                                ActivityHelper.handleException(mContext, message);
                            }
                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, " downloadCargoTypes onError :- Exception" + e1.getMessage());

                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void confirmBooking(final BookingInfor bookingInfor) {

        if (mBookingView == null)
            return;

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mBookingView.noInternet();
            return;
        }

        LogUtils.appendLog(mContext, "-----confirmBooking-----");
        LogUtils.appendLog(mContext, "API:-" + BASE_URL + CONFIRM_BOOKING);
        LogUtils.appendLog(mContext, "Headers:-" + getHeaders(mContext));
        LogUtils.appendLog(mContext, "Body:-" + new Gson().toJson(bookingInfor));

        mBookingView.showProgressDialog();

        RestClient.getRestService(BASE_URL).confirmBooking(getHeaders(mContext), bookingInfor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookingStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookingStatus bookingStatus) {
                        mBookingView.dismissDialog();
                        LogUtils.appendLog(mContext, "confirmBooking NearByPlacesResponse:-" + new Gson().toJson(bookingStatus) + "\n\n\n\n\n");


                        if (bookingStatus != null) {

                            if (bookingStatus.getBookingNo().length() > 0) {
                                mBookingView.bookingCreated(bookingStatus.getBookingNo());
                            } else {
                                mBookingView.bookingNotCreated(bookingStatus.getMessage());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {


                            String msg = e.getMessage();
                            LogUtils.appendLog(mContext, "confirmBooking onError:-" + new Gson().toJson(e) + "\n\n\n\n\n");
                            ActivityHelper.handleException(mContext, String.valueOf(e));

                            if (msg != null && msg.length()>0) {

                                if (msg.contains("500") || msg.contains("503")) {
                                    confirmBooking(bookingInfor);
                                } else {
                                    mBookingView.dismissDialog();
                                }
                            } else {
                                mBookingView.dismissDialog();
                            }

                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, "BF" + " confirmBooking onError :- Exception" + e1.getMessage());

                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @Override
    public void cancelBooking(final Cancel cancel) {


        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mBookingView.noInternet();
            return;
        }
        LogUtils.appendLog(mContext, "-----cancelBooking-----");
        LogUtils.appendLog(mContext, "API:-" + BASE_URL + CANCEL_BOOKING);
        LogUtils.appendLog(mContext, "Headers:-" + getHeaders(mContext));
        LogUtils.appendLog(mContext, "Body:-" + new Gson().toJson(cancel));


        RestClient.getRestService(BASE_URL).cancelBooking(getHeaders(mContext), cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        LogUtils.appendLog(mContext, "cancelBooking NearByPlacesResponse:-" + s + "\n\n\n\n\n");
                        mBookingView.bookingCanceled("Booking cancelled successfully");


                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            LogUtils.appendLog(mContext, "cancelBooking onError:-" + new Gson().toJson(e) + "\n\n\n\n\n");
                            ActivityHelper.handleException(mContext, String.valueOf(e));

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
