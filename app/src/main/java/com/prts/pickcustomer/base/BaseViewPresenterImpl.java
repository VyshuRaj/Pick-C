package com.prts.pickcustomer.base;

import android.content.Context;

import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import retrofit2.Callback;
import retrofit2.Response;

import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by satya on 20-Dec-17.
 */

public class BaseViewPresenterImpl implements BaseViewPresenter {
    private Context mContext;
    private BaseView mBaseView;

    BaseViewPresenterImpl(Context baseActivity, BaseView baseActivity1) {
        mContext = baseActivity;
        mBaseView = baseActivity1;
    }

    @Override
    public void doLogoutCall() {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mBaseView.noInternet();
            return;
        }
        retrofit2.Call<Boolean> call = RestClient.getRestService(BASE_URL).logout(CredentialManager.getHeaders(mContext));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(retrofit2.Call<Boolean> call, Response<Boolean> response) {

                boolean logoutStatus = false;
                try {
                    logoutStatus = response.body();
                } catch (Exception e) {
                    ActivityHelper.handleException(mContext, e.getMessage());
                }

                if (logoutStatus) {
                    CredentialManager.clearAuthToken(mContext);
                    CredentialManager.clearMobileNumber(mContext);
                    mBaseView.navigateToLoginPage();
                } else {
                    mBaseView.notAbleToLoggedOut();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Boolean> call, Throwable t) {

                try {
                    String message = t.getMessage();
                    if (message.contains(TIME_OUT)) {
                        doLogoutCall();
                    } else {
                        ActivityHelper.handleException(mContext, message);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });


    }

}
