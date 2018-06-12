package com.prts.pickcustomer.userrating;

import android.content.Context;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.invoice.UserTripInvoice;
import com.prts.pickcustomer.restapi.RestClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by LOGICON on 30-Dec-17.
 */

public class UserRatingPresenterImpl implements UserRatingPresenter {
    Context mContext;
    UserRatingView mUserRatingView;

    public UserRatingPresenterImpl(Context userRatingBarActivity, UserRatingView userRatingBarActivity1) {
        mContext = userRatingBarActivity;
        mUserRatingView = userRatingBarActivity1;
    }

    @Override
    public void sendUserRating(final Rating rating) {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mUserRatingView.noInterNet();
            return;
        }

        mUserRatingView.showProgressDialog("Save driver rating");
        RestClient.getRestService(BASE_URL).sendUserRating(getHeaders(mContext), rating)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        try {
                            mUserRatingView.savedDriverRating(mContext.getString(R.string.saved_driver_rating));
                            mUserRatingView.dissmissDialog();
                            mUserRatingView.navigateToHomePage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            mUserRatingView.dissmissDialog();

                            ActivityHelper.handleException(mContext, String.valueOf(e.getMessage()));

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
