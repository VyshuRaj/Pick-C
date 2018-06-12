package com.prts.pickcustomer.queries;

import android.content.Context;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getContentType;
import static com.prts.pickcustomer.helpers.ValidationHelper.isValidEmail;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by LOGICON on 31-12-2017.
 */

public class SendingQueryPreseneterImpl implements SendQueryPresenter {
    Context mContext;
    SendQueryView mSendQueryView;


    public SendingQueryPreseneterImpl(Context sendingQueriesToPickCActivity, SendQueryView sendingQueriesToPickCActivity1) {
        mContext = sendingQueriesToPickCActivity;
        mSendQueryView = sendingQueriesToPickCActivity1;
    }


    @Override
    public void validateTheFields(Query quey) {

        if (quey.getName().isEmpty()) {
            mSendQueryView.enterName(mContext.getString(R.string.enter_name));
            return;
        }

        if (!isValidEmailCheck(quey.getEmail()))
            return;
        if (!isValidMobileNumber(quey.getMobile()))
            return;


        if (quey.getSubject().isEmpty()) {
            mSendQueryView.enterSuject(mContext.getString(R.string.enter_subject));
            return;
        }

        if (quey.getQuery().isEmpty()) {
            mSendQueryView.enterQuery(mContext.getString(R.string.enter_query));
            return;
        }

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mSendQueryView.noInternet();
            return;
        }
        mSendQueryView.proceedFurther();

        mSendQueryView.showProgressDialog();
        sendQueryToServer(quey);
    }

    private void sendQueryToServer(final Query quey) {


        RestClient.getRestService(BASE_URL).sendQueryTOServer(getContentType(), quey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        mSendQueryView.dismissDialog();

                        if (s) {
                            mSendQueryView.tryAgain(mContext.getString(R.string.message_sent_succefully));
                            mSendQueryView.getToPreviousPage();
                        } else {
                            mSendQueryView.tryAgain(mContext.getString(R.string.message_sent_failed));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            mSendQueryView.dismissDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                sendQueryToServer(quey);
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

    @Override
    public boolean isValidMobileNumber(String mobile) {
        if (mobile.isEmpty()) {
            mSendQueryView.enterMobileNumber("Please enter mobile no");
            return false;
        }
        if (mobile.length() < 10) {
            mSendQueryView.enterMobileNumber(mContext.getString(R.string.enter_valid_Phone));

            return false;
        }

        return true;
    }

    @Override
    public boolean isValidEmailCheck(String email) {
        if (email.isEmpty()) {
            mSendQueryView.enterEmail(mContext.getString(R.string.enter_email));
            return false;
        }

        if (!isValidEmail(email)) {
            mSendQueryView.enterEmail(mContext.getString(R.string.valid_email));
            return false;
        }

        return true;
    }
}
