package com.prts.pickcustomer.driver;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.booking.Cancel;
import com.prts.pickcustomer.booking.UserCancelBookingAdapter;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.help.WebViewActivity;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.PermissionChecker;
import com.prts.pickcustomer.helpers.TextToSpeechHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.userrating.Feedback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.prts.pickcustomer.helpers.Constants.FINISH_ACTIVITY;
import static com.prts.pickcustomer.helpers.Constants.HELP_URL;
import static com.prts.pickcustomer.helpers.PermissionChecker.READ_WRITE_PERMISSIONS;
import static com.prts.pickcustomer.helpers.SnackbarHelper.showSnackBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverDetailsFragment extends Fragment implements DriverDetailsView, TextToSpeech.OnInitListener/*, PushNotificationListener*/ {

    private static final String CONFIRM_BOOKING_INFO_BUNDLE_KEY = "confirm_bundle_key";

    private static final String IS_TRIP_STARTED_BOOKING_INFO_BUNDLE_KEY = "is_trip_started";
    private static final String TAG = "DriverDetailsFragment";
    @BindView(R.id.driver_imageView)
    ImageView driverIV;
    @BindView(R.id.ratingBar2)
    RatingBar rtBar;
    @BindView(R.id.driverNameTV)
    PickCCustomTextVIew driverNameTV;
    @BindView(R.id.vehicleDescTV)
    PickCCustomTextVIew vehicleDescTV;
    @BindView(R.id.bookingNoTV)
    PickCCustomTextVIew bookingVehicleNosTV;
    @BindView(R.id.ratingText)
    TextView mRatingTV;
    @BindView(R.id.eta_time_TV)
    PickCCustomTextVIew eTA_Time_TV;
    @BindView(R.id.otpTV)
    PickCCustomTextVIew otpTV;
    @BindView(R.id.callDriverLL)
    LinearLayout callDriverLL;
    @BindView(R.id.supportLL)
    LinearLayout supportLL;
    @BindView(R.id.cancelBookingLL)
    LinearLayout mCancelLayout;
    @BindView(R.id.topLL_driver)
    LinearLayout mTopLayout;
    @BindView(R.id.pickTVAR)
    PickCCustomTextVIew pickTVAR;
    boolean driverReached;
    Activity activity;
    ConfirmedDriver mDriver;
    DriverDetailsPresnter mDriverDetailsPresnter;
    private String mDuration = "0";
    final String[] CANCEL_REMARKS = {"Driver is late", "Changed my mind", "Booked another truck", "Driver denied duty"};
    private String mCancelRemarks = "";
    private DriverListener mListener;
    private TextToSpeech.OnInitListener onInitListener;
    private boolean mIsTripStarted;
    //private MapAsync mMapAsync;
    private boolean mCanCancel;
    private static String mDriverId = "";
    Timer mTimer = null;

    private static final int TIMER_DELAY_FIVE_SECS = 5000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @OnClick(R.id.callDriverLL)
    public void makeACallToDriver(View view) {
        try {
            try {
                String[] callPerm = {Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PRIVILEGED};

                if (!PermissionChecker.checkPermission(getContext(), callPerm) && activity != null)
                    PermissionChecker.reqPermissions(activity, callPerm);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            intent.setData(Uri.parse("tel:" + mDriver.getMobileNo()));
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.cancelBookingLL)
    public void cancelRequest(View view) {
        try {
            createCancelRemarksDialogAndShow(mDriver.getBookingNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createCancelRemarksDialogAndShow(final String bookingNo) {

        try {
            final List<Feedback> mFeedback = new ArrayList<>();

            if (CANCEL_REMARKS != null) {

                for (String cancelRemark : CANCEL_REMARKS) {
                    Feedback feedback = new Feedback();
                    feedback.setText(cancelRemark);
                    mFeedback.add(feedback);
                }
            }

            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.cancel_booking_layout);
            dialog.setCancelable(false);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.show();

            PickCCustomTextVIew cancelBtn = (PickCCustomTextVIew) dialog.findViewById(R.id.cancelBookingTV);
            PickCCustomTextVIew doNotCancelBtn = (PickCCustomTextVIew) dialog.findViewById(R.id.donotCancelTV);
            final ListView cancelRemarksListView = (ListView) dialog.findViewById(R.id.listViewCancelDialog);
            final UserCancelBookingAdapter adapter = new UserCancelBookingAdapter(getContext(), mFeedback);
            cancelRemarksListView.setAdapter(adapter);
            cancelRemarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean isSelected = mFeedback.get(position).isSelected();
                    mFeedback.get(position).setSelected(!isSelected);
                    cancelRemarksListView.invalidate();
                    adapter.notifyDataSetChanged();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCancelRemarks = "";

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mFeedback.size(); i++) {
                        if (mFeedback.get(i).isSelected()) {
                            sb.append(mFeedback.get(i).getText()).append(",");
                        }
                    }
                    if (sb.length() > 0) {
                        sb.setLength(sb.length() - 1);
                        mCancelRemarks = sb.toString();
                    }

                    if (mCancelRemarks.isEmpty()) {
                        TextToSpeechHelper.speakOut(getActivity(), "Please choose the reason to cancel");
                        Toast.makeText(activity, "Please choose the reason to cancel", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cancelBooking(bookingNo, mCancelRemarks);
                    dialog.dismiss();
                }
            });

            doNotCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void cancelBooking(String bookingNo, String mCancelRemarks) {
        try {
            Cancel cancel = new Cancel();
            cancel.setBookingNo(bookingNo);
            cancel.setRemarks(mCancelRemarks);

            Log.e(TAG, "cancel " + new Gson().toJson(cancel));

            if (mDriverDetailsPresnter != null) {
                mDriverDetailsPresnter.cancelBooking(cancel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.supportLL)
    public void customerSupport(View view) {

        try {
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra(WebViewActivity.URL, HELP_URL);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOpenOrClosedType(int id) {

        if (id == 1300)
            return "Open";
        else
            return "Closed";

    }

    private String getCategory(int id) {

        String truck = "truck";

        switch (id) {

            case 1000:
                return "Mini " + truck;
            case 1001:
                return "Small " + truck;
            case 1002:
                return "Medium " + truck;
            case 1003:
                return "Large " + truck;
            default:
                return "";

        }
    }

    private void setDriverData() {
        try {
            {
                if (mDriver != null) {

                    if (mDriver.getDriverName() != null && mDriver.getDriverName().length() > 0) {
                        driverNameTV.setText(String.format("Name: %s", mDriver.getDriverName()));
                    }


                    CredentialManager.setDriverName(getActivity(),mDriver.getDriverName());
                    if (mDriver.getDriverImage() != null && mDriver.getDriverImage().length() > 0) {
                        Picasso.with(getContext()).load(mDriver.getDriverImage()).into(driverIV);
                    }


                    mDriverId = mDriver.getDriverId();
                    String type = getOpenOrClosedType(mDriver.getVehicleType());
                    String category = getCategory(mDriver.getVehicleCategory());
                    vehicleDescTV.setText(String.format("%s-%s", type, category));
                    otpTV.setText(String.format("OTP: %s", mDriver.getOTP()));
                    bookingVehicleNosTV.setText(String.format("Veh No. %s/%s", mDriver.getVehicleNo(), mDriver.getBookingNumber())/*+ "/" + mDriver.getBookingNumber()*/);
                    HomeActivity activity = ((HomeActivity) getActivity());
                    setETAtime(mDuration);

                    if (activity != null) {
                        activity.getTravelBetwTwoLocations(mDriver.getLatitude() + "," + mDriver.getLongitude(), false);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }

    public void setETAtime(String duration) {
        try {
            mDuration = duration;

            if (mDuration.isEmpty() && mDuration.length() <= 0)
                mDuration = "0";

            if (eTA_Time_TV != null) {
                eTA_Time_TV.setText(String.format("ETA:%s", mDuration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                ToastHelper.showToastLenShort(getContext(), "Please give permission to make calls");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setDriverRating(int rating) {
        Log.e(TAG, "rating" + rating);
        String rate = "";
        try {
            try {

                if (rating != 0) {
                    rate = String.valueOf(rating);
                } else {
                    rate = "4";
                }

                if (getActivity() != null) {
                    CredentialManager.setDriverRating(getActivity().getApplicationContext(), rate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mRatingTV.setText(rate);
            rtBar.setRating(Float.parseFloat(rate));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tryAgain() {
        ToastHelper.showToastLenShort(getContext(), getString(R.string.driver_rating));
    }

    @Override
    public void bookingCanceled(String message, Boolean isCancelled) {

        if (isCancelled) {
            ToastHelper.showToastLenShort(getContext(), message);
            mListener.bookingCancelledSuccessfully();
        }
    }

    @Override
    public void createDriverRoute(DriverCurLatLng driverCurLatLng, boolean isReachedDeliveryLoc) {

        if (mListener != null) {
            mListener.createDriverRoute(new LatLng(driverCurLatLng.getCurrentLat(), driverCurLatLng.getCurrentLong()), true, isReachedDeliveryLoc);//false
        }

    }

    @Override
    public void monitorDriver(final DriverLocationUpdates driverCurrentLocation) {
        try {

            if (driverCurrentLocation != null) {
                mListener.monitorDriver(driverCurrentLocation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DriverListener) {
            mListener = (DriverListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        if (!PermissionChecker.checkPermission(activity, READ_WRITE_PERMISSIONS)) {
            PermissionChecker.reqPermissions(activity, READ_WRITE_PERMISSIONS);
        }

        Bundle bundle = getArguments();
        HomeActivity.mOnBackPressStatus = FINISH_ACTIVITY;

        if (bundle != null) {
            mDriver = (ConfirmedDriver) bundle.getSerializable(CONFIRM_BOOKING_INFO_BUNDLE_KEY);

            if (mDriver != null) {
                driverReached = mDriver.isReachedPickupLocation();
                mIsTripStarted = mDriver.isInTrip();

                if (mCancelLayout != null) {

                    if (!mIsTripStarted)
                        mCancelLayout.setVisibility(View.VISIBLE);
                    else
                        mCancelLayout.setVisibility(View.GONE);
                }
            }
        }

        try {

            if (driverReached) {
                pickTVAR.setText(R.string.pick_arrived);
                TextToSpeechHelper.speakOut(getActivity(), "Pick up arrived");
                setETAtime("");
            }

            if (mIsTripStarted) {
                mTopLayout.setVisibility(View.GONE);

                if (mCancelLayout != null)
                    mCancelLayout.setVisibility(View.GONE);
            }

            mDriverDetailsPresnter = new DriverDetailsPresenterImpl(getActivity(), this);

            if (mDriver != null && mDriver.getDriverId() != null && mDriver.getDriverId().length() > 0) {
                setDriverData();

                if (mDriverDetailsPresnter != null) {
                    mDriverId = mDriver.getDriverId();
                    mDriverDetailsPresnter.getDriverAvgRating(mDriver.getDriverId());
                }
            } else {
                ToastHelper.showToastLenLong(getActivity(), "Driver details are not available");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onTripStartedByDriverListener() {
        try {
            mIsTripStarted = true;
            driverReached = true;

            if (mTopLayout != null) {
                mTopLayout.setVisibility(View.GONE);
            }

            if (mCancelLayout != null)
                mCancelLayout.setVisibility(View.GONE);

            if (mRatingTV != null) {
                TextToSpeechHelper.speakOut(getActivity(), getString(R.string.trip_started_driver));
                showSnackBar(mRatingTV, getString(R.string.trip_started_driver));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onTripEndByDriverListener() {
        try {
            if (mRatingTV != null) {
                TextToSpeechHelper.speakOut(getActivity(), getString(R.string.trip_completed_by_driver));
                showSnackBar(mRatingTV, getString(R.string.trip_completed_by_driver));
            }
            mTimer = null;
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onTripEndByDriverListener :- Exception" + e.getMessage());

        }
    }


    public void onDriverReachedPickupLocationListener() {
        try {
            //  driverReached=true;
            if (mRatingTV != null) {
                TextToSpeechHelper.speakOut(getActivity(), getString(R.string.driver_reached_pick));
                showSnackBar(mRatingTV, getString(R.string.driver_reached_pick));
            }

            pickTVAR.setText(R.string.pick_up_arrived);
            TextToSpeechHelper.speakOut(getActivity(), getString(R.string.pick_up_arrived));
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onDriverReachedPickupLocationListener :- Exception" + e.getMessage());
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        if (mTimer == null) {
            mTimer = new Timer();

            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    String driverId = "";

                    if (mDriver != null && mDriver.getDriverId() != null && mDriver.getDriverId().length() > 0) {
                        driverId = mDriver.getDriverId();

                    }
                    if (mDriverDetailsPresnter != null) {
                        mDriverDetailsPresnter.monitorDriver(driverId);
                    } else {
                        LogUtils.appendLog(getActivity(), TAG + " mDriver :- Exception" + new Gson().toJson(mDriver));
                    }
                }
            }, 0, TIMER_DELAY_FIVE_SECS);
        }


        if (!driverReached) {
            if (getActivity() != null) {

                if (CredentialManager.isInTrip(getActivity())) {
                    driverReached = true;
                }
            }
        }

        if (driverReached) {

            if (mTopLayout != null && mTopLayout.getVisibility() != View.GONE) {
                mTopLayout.setVisibility(View.GONE);
            }

            if (mCancelLayout != null && mCancelLayout.getVisibility() != View.GONE)
                mCancelLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onPause() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onPause();

    }

    public void onDriverAboutToReachedPickupLocationListener() {
        try {
            pickTVAR.setText(R.string.pickup_arriving);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onDriverAboutToReachedPickupLocationListener :- Exception" + e.getMessage());
        }
    }

    public void onDriverReachedDropedLocationListener() {
        try {
            if (mRatingTV != null) {
                TextToSpeechHelper.speakOut(getActivity(), getString(R.string.reached_de));
                showSnackBar(mRatingTV, getString(R.string.reached_de));
            }

            String driverId = "";

            if (mDriver != null && mDriver.getDriverId().length() > 0) {
                driverId = mDriver.getDriverId();

            }
            if (mDriverDetailsPresnter != null) {
                mDriverDetailsPresnter.getDriverCurrentLatLngOnReachedPickUp(driverId, true);
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onDriverReachedDropedLocationListener :- Exception" + String.valueOf(e));
        }
    }

    @Override
    public void onInit(int status) {

    }


    public interface DriverListener {
        void bookingCancelledSuccessfully();

        void createDriverRoute(LatLng latLng, boolean b, boolean isReachedDeliveryLocation);

        void monitorDriver(DriverLocationUpdates driverCurrentLocation);
    }


}
