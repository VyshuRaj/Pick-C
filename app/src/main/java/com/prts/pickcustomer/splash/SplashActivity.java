package com.prts.pickcustomer.splash;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.google.firebase.iid.FirebaseInstanceId;
import com.paytm.pgsdk.Log;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.login.LoginActivity;
import com.prts.pickcustomer.signup.SignUpActivity;


public class SplashActivity extends AppCompatActivity implements SplashView {
    ImageView mImageView;
    private LinearLayout mLinearLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FirebaseInstanceId.getInstance().getToken();

        Log.e("SplashActivity","FirebaseInstanceId.getInstance().getToken();"+FirebaseInstanceId.getInstance().getToken());

        try {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(SplashActivity.this, R.color.colorPrimaryDark));
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);
            InitialSetUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (!NetworkHelper.hasNetworkConnection(SplashActivity.this)) {
                 NetworkHelper.turnOnNetworkSettings(SplashActivity.this);
                 ToastHelper.showToastLenShort(SplashActivity.this, "Please Connect to the internet and Try Again");
                return;
            }

            SplashPresenterImpl splashPresenter = new SplashPresenterImpl(SplashActivity.this, this);
            splashPresenter.stayForFewSeconds();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void InitialSetUp() {

        try {
            mImageView = findViewById(R.id.imageview);
            mLinearLayout = findViewById(R.id.bottomLinear);
            CredentialManager.setPresentState(SplashActivity.this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void startAnimation() {
        try {
            Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            mImageView.startAnimation(animFadein);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoginSignUpBtns() {
        mLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginSignUpBtns() {
        mLinearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void navigateToHomePage() {
        try {
            CredentialManager.setPresentState(SplashActivity.this, false);

            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            intent.putExtra(getString(R.string.is_from_rating), false);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginAgain() {
        ToastHelper.showToastLenShort(SplashActivity.this, "Something went wrong.Please login again!!");
    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(SplashActivity.this);
    }

    public void signUpUser(View view) {
        try {
            CredentialManager.setPresentState(SplashActivity.this, false);
            startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginUser(View view) {
        try {
            CredentialManager.setPresentState(SplashActivity.this, false);
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
