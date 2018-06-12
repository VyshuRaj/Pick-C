package com.prts.pickcustomer.payment.bycash;

import android.content.Context;

import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;


public class CashPaymentPresenterImpl implements CashPaymentPresnter {
    private CashPaymentView mCashPaymentView;
    private Context mContext;

    public CashPaymentPresenterImpl(CashPaymentActivity cashPaymentActivity, CashPaymentView cashPaymentActivity1) {
        mCashPaymentView = cashPaymentActivity1;
        mContext = cashPaymentActivity;
    }

    @Override
    public void payBookingAmount(final String bookNumber, final String driverId) {

        if (!NetworkHelper.hasNetworkConnection(mContext))
            mCashPaymentView.noInterNet();

        mCashPaymentView.showProgressDialog();
        RestClient.getRestService(BASE_URL).payAmountByCash(getHeaders(mContext), bookNumber, driverId,"1100")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        mCashPaymentView.dismissDialog();

                        if (s){
                            mCashPaymentView.navigateToInvoicePage("Customer payment received");
                        }else{
                            mCashPaymentView.tryAgainMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {



                        try {
                            mCashPaymentView.dismissDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                payBookingAmount(bookNumber,driverId);
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
