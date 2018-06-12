package com.prts.pickcustomer.userrating;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.trucks.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.prts.pickcustomer.helpers.Constants.FEEDBACK;

public class UserRatingActivity extends AppCompatActivity implements OnItemClick, UserRatingView {

    @BindView(R.id.ratingBar2)
    RatingBar ratingBar;
    private static final String TAG = "UserRatingActivity";
    @BindView(R.id.amountTextView)
    TextView amountTV;
    @BindView(R.id.truckTypeTv)
    TextView truckTypeTv;
    @BindView(R.id.from_location_address_TV)
    TextView fromlocationTv;
    @BindView(R.id.dest_location_address_TV)
    TextView toLocationTv;
    @BindView(R.id.avgratingTv)
    TextView avgratingTv;
    @BindView(R.id.driverNam)
    TextView mDriverNam;
    public static final String DRIVERID = "driver";
    private   String mDriverId;
    int intRating;
    List<Feedback> mFeedback;
    private StringBuilder mSelectedRemark = new StringBuilder("");
    private UserRatingPresenter mUserRatingPresenter;
    private String mBookingNumber;
    private int mRatings;

    private Dialog mFeedbackDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating_bar);
        ButterKnife.bind(this);
        mFeedback = new ArrayList<>();
        mUserRatingPresenter = new UserRatingPresenterImpl(UserRatingActivity.this, this);


        for (String aFEEDBACK : FEEDBACK) {
            Feedback feedback = new Feedback();
            feedback.setText(aFEEDBACK);
            mFeedback.add(feedback);
        }

        mBookingNumber = CredentialManager.getBookingNO(getApplicationContext());
        try {
            Intent frintent = getIntent();
            mDriverId = frintent.getStringExtra(DRIVERID);
            String amount=frintent.getStringExtra("amount");
            String truckType=frintent.getStringExtra("truckType");
            String fromLoc=frintent.getStringExtra("fromLoc");
            String toLoc=frintent.getStringExtra("toLoc");
            String driverName=frintent.getStringExtra("driverName");

            mDriverNam.setText(String.format("Driver Name : %s", driverName));
            amountTV.setText(amount);
            fromlocationTv.setText(fromLoc);
            toLocationTv.setText(toLoc);
            truckTypeTv.setText(truckType);

            try {
                String rating = "Driver Rating: ";
                rating = rating + CredentialManager.getDriverRating(UserRatingActivity.this);
                avgratingTv.setText(rating);
            } catch (Exception e) {
                Log.e(TAG, "GettingRat" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void submit(View view) {
        mRatings = (int) ratingBar.getRating();

        if (mRatings == 0) {
            ToastHelper.showToastLenShort(UserRatingActivity.this, "Rate the driver");
        } else if (mRatings <= 3 && mSelectedRemark.toString().isEmpty()) {
            createCancelRemarksDialogAndShow();
        } else {
            intRating = (int) Math.round(ratingBar.getRating());
            sendRatingtoServer();
        }
    }

    private void sendRatingtoServer() {
        Rating rating = new Rating();
        rating.setBookNo(mBookingNumber);
        String driverId = mDriverId == null ? "" : mDriverId;
        rating.setDriverId(driverId);
        String rat = "" + mRatings;
        rating.setRating(rat);
        rating.setRemarks(mSelectedRemark.toString());

        if (driverId.length() > 0) {
            mUserRatingPresenter.sendUserRating(rating);
        }
    }

    private void createCancelRemarksDialogAndShow() {

        mFeedbackDialog = new Dialog(UserRatingActivity.this);
        mFeedbackDialog.setContentView(R.layout.driver_less_rating);
        mFeedbackDialog.setCancelable(false);

        if (mFeedbackDialog.getWindow()!=null) {
            mFeedbackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mFeedbackDialog.show();
        PickCCustomTextVIew submitBtn = mFeedbackDialog.findViewById(R.id.cancelBookingTV);
        PickCCustomTextVIew closeTv=mFeedbackDialog.findViewById(R.id.cancle_tv);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeedbackDialog.dismiss();
            }
        });
        final ListView cancelRemarksListView = mFeedbackDialog.findViewById(R.id.listViewCancelDialog);
        CancelRemarksAdapter mFeedbackAdapter = new CancelRemarksAdapter(mFeedback, this, UserRatingActivity.this);
        cancelRemarksListView.setAdapter(mFeedbackAdapter);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedRemark.toString().isEmpty()) {
                    Toast.makeText(UserRatingActivity.this, "Please select a reason to cancel", Toast.LENGTH_SHORT).show();
                    return;
                }

                mSelectedRemark.setLength(mSelectedRemark.length()-1);
                mFeedbackDialog.dismiss();
            }
        });

    }

    @Override
    public void onItemClick(int pos, View view) {
        mSelectedRemark.append(mFeedback.get(pos).getText()).append(",");

    }

    @Override
    public void noInterNet() {
        ToastHelper.noInternet(UserRatingActivity.this);
    }

    @Override
    public void showProgressDialog(String s) {
        ProgressDialogHelper.showProgressDialog(UserRatingActivity.this, s);
    }

    @Override
    public void dissmissDialog() {
        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void tryAgain(String s) {
        ToastHelper.showToastLenShort(UserRatingActivity.this, "Server busy try again!");//, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHomePage() {
        Intent intent = new Intent(UserRatingActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(getString(R.string.is_from_rating), true);
        startActivity(intent);
        finish();
    }


    @Override
    public void savedDriverRating(String s) {
        ToastHelper.showToastLenShort(UserRatingActivity.this, s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserRatingPresenter = null;
    }

    @Override
    public void onBackPressed() {

        if(mFeedbackDialog!=null && mFeedbackDialog.isShowing()){
            mFeedbackDialog.dismiss();
        }else{
            ToastHelper.showToastLenShort(UserRatingActivity.this, "Please give the rating");//, Toast.LENGTH_SHORT).show();

        }
    }
}
