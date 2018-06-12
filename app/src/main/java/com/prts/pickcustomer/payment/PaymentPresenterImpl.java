package com.prts.pickcustomer.payment;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by satya on 30-Dec-17.
 */

public class PaymentPresenterImpl implements PaymentPresenter {
    Context mContext;
    PaymentView mPaymentView;

    PaymentPresenterImpl(Context context, PaymentView paymentView) {
        mContext = context;
        mPaymentView = paymentView;
    }

    @Override
    public void getAmountOfCurrentBooking(final String bookingNumber) {

        if(!NetworkHelper.hasNetworkConnection(mContext)){
            mPaymentView.noInternet();
            return;
        }

        mPaymentView.showDialog();

        RestClient.getRestService(BASE_URL).getAmtOfCurBooking(getHeaders(mContext), bookingNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AmountResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<AmountResponse> amountResponse) {
                        mPaymentView.dismissDialog();
                        Log.e("TAG","amountResponse "+ new Gson().toJson(amountResponse));

                        if(amountResponse!=null && amountResponse.size()>0)
                        mPaymentView.setAmountOfCurrentBooking(amountResponse.get(0));
                        else
                            mPaymentView.paymentAmtNotAvailable(mContext.getString(R.string.payment_details_not_available));
                        

                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mPaymentView.dismissDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                getAmountOfCurrentBooking(bookingNumber);
                            }else{
                                ActivityHelper.handleException(mContext,message);
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
}
