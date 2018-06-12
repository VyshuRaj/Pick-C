package com.prts.pickcustomer.splash;

import android.content.Context;
import android.os.Handler;

import com.prts.pickcustomer.helpers.CredentialManager;

/**
 * Created by LOGICON on 12-12-2017.
 */

class SplashPresenterImpl implements SplashPresenter {
    private static final long SPLASH_DISPLAY_LENGTH = 2000;
    private SplashView mSplashView;
    private Context mContext;

    SplashPresenterImpl(Context context, SplashView splashView) {
        mSplashView = splashView;
        mContext = context;
        mSplashView.hideLoginSignUpBtns();
    }

    @Override
    public void stayForFewSeconds() {
        try {
            mSplashView.startAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CredentialManager.getMobileNO(mContext) != null && CredentialManager.getMobileNO(mContext).length()>0) {
                        mSplashView.navigateToHomePage();
                    } else {
                        mSplashView.showLoginSignUpBtns();
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
