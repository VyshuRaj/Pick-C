package com.prts.pickcustomer.home;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.firebase.iid.FirebaseInstanceId;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.base.BaseActivity;
import com.prts.pickcustomer.booking.BookingFragment;
import com.prts.pickcustomer.customviews.AVLoadingIndicatorView;
import com.prts.pickcustomer.customviews.PickCCustomEditText;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.customviews.VerticalSeekBar;
import com.prts.pickcustomer.driver.ConfirmedDriver;
import com.prts.pickcustomer.driver.DriverDetailsFragment;
import com.prts.pickcustomer.driver.DriverLocationUpdates;
import com.prts.pickcustomer.fcm.ObservableObject;
import com.prts.pickcustomer.helpers.AlertDialogActivity;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.GoogleMapHelper;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.MapHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.PermissionChecker;
import com.prts.pickcustomer.helpers.TextToSpeechHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.Utility;
import com.prts.pickcustomer.helpers.ViewHelper;
import com.prts.pickcustomer.helpers.VolleyHelper;
import com.prts.pickcustomer.payment.PaymentActivity;
import com.prts.pickcustomer.trucks.TruckCateogeoriesFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.google.android.gms.maps.model.JointType.ROUND;
import static com.prts.pickcustomer.helpers.Constants.ABOUTTOREACHPICKUPLOCATION;
import static com.prts.pickcustomer.helpers.Constants.BODY_KEY;
import static com.prts.pickcustomer.helpers.Constants.BOOKING_CANCELLED_BY_DRIVER;
import static com.prts.pickcustomer.helpers.Constants.BOOKING_CONFIRMED;
import static com.prts.pickcustomer.helpers.Constants.BOOKING_FAILED;
import static com.prts.pickcustomer.helpers.Constants.BOOKING_NO_KEY;
import static com.prts.pickcustomer.helpers.Constants.DISABLE_ON_BACK_PRESS;
import static com.prts.pickcustomer.helpers.Constants.DRIVER_REACHED_DROP_LOCATION;
import static com.prts.pickcustomer.helpers.Constants.DRIVER_REACHED_PICK_UP_LOCATION;
import static com.prts.pickcustomer.helpers.Constants.ENABLE_BOOKING;
import static com.prts.pickcustomer.helpers.Constants.FINISH_ACTIVITY;
import static com.prts.pickcustomer.helpers.Constants.GENERATE_INVOICE;
import static com.prts.pickcustomer.helpers.Constants.INVOICE_GENERATED;
import static com.prts.pickcustomer.helpers.Constants.TRIP_END;
import static com.prts.pickcustomer.helpers.Constants.TRIP_STARTED;
import static com.prts.pickcustomer.helpers.PermissionChecker.LOCATION_PERMISSIONS;
import static com.prts.pickcustomer.invoice.InvoiceActivity.BOOKING_NO;


public class HomeActivity extends BaseActivity implements BookingFragment.BookingListener, TextToSpeech.OnInitListener, TruckCateogeoriesFragment.OnTruckSelectedListner, HomeView, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, CountDownListener, DriverDetailsFragment.DriverListener, Observer {

    private static final int FROM_PLACE_PICKER_REQUEST = 100;
    private static final int TO_PLACE_PICKER_REQUEST = 200;
    public static String mOnBackPressStatus = FINISH_ACTIVITY;
    @BindView(R.id.pinIV)
    ImageView pinIV;
    @BindView(R.id.from_location_imageVIew)
    ImageView mZoomPickUpLocationIV;
    @BindView(R.id.to_location_imageVIew)
    ImageView mZoomDropLocationIV;
    @BindView(R.id.location_LLL)
    public LinearLayout mLocationLLL;
    @BindView(R.id.map_linear)
    LinearLayout mMapLL;
    public static final String DefaultSrc = "Cargo pick up location";
    public static final String DefaultDes = "Cargo drop location";
    private static final int MAX_PADDING = 15;
    private static final int MIN_PADDING = 1;
    private static final String UN_SELECTED_COLOR = "#e8ffffff";
    public static final String ANNOUNCEMENT_SHARED_PREF_KEY = "announcenment";
    public GoogleMap mMap;
    static String TAG = "HomeActivity";
    @BindView(R.id.progressBar_activity_center)
    public ProgressBar mProgressDialog;
    @BindView(R.id.progressBar_for_time)
    public ProgressBar mProgressBarForTime;
    @BindView(R.id.textView_for_time)
    public PickCCustomTextVIew mTimeOneTV;
    @BindView(R.id.textView_for_time_2)
    public PickCCustomTextVIew mTimeTwoTV;
    public String mPickUpOrDropLocationSelected = PICK_UP;
    @BindView(R.id.lock_from_location_imageVIew)
    public ImageView mLockPickUpLocationIV;
    @BindView(R.id.lock_to_location_imageVIew)
    ImageView mLockDropLocationIV;
    @BindView(R.id.from_location_relativeLayout)
    public RelativeLayout fromLocationRL;
    @BindView(R.id.to_location_relativeLayout)
    RelativeLayout toLocationRL;
    @BindView(R.id.setLocationMarkertext)
    PickCCustomTextVIew mSetLocationMarkertext;
    private static final float MAP_CAMERA_ZOOM_ANIMATION = 18;
    public static String mFromAddress = "";
    public static String mToAddress = "";
    public static final String PICK_UP = "from";
    public static final String DROP = "to";
    public static LatLng mPickUpLatLng = null;
    public static LatLng mDropLatLng = null;
    MapFragment mMapFragment;
    @BindView(R.id.from_lcation_textView)
    public PickCCustomEditText mPickUpLocationTV;
    @BindView(R.id.to_lcation_textView)
    public PickCCustomEditText mDropLocationTV;
    @BindView(R.id.seekBar)
    VerticalSeekBar mZoomControlSkbr;
    public static AVLoadingIndicatorView mIndicatorView;
    private static final String[] INDICATORS = new String[]{"BallPulseIndicator"};
    private static boolean mIsPickUpLocationLocked = false;
    private static boolean mIsDropLocationLocked = false;
    private int mSelectedTruckId = 1000;
    private int mSelectedClosedOrOpenTypeId = 1300;
    private int mSelectedLoadUnLoadId = 1373;
    private HomeViewPresenter mHomeViewPresenter;
    private Snackbar mSnackbar;
    private Marker mFromMarker;
    BitmapDescriptor mSourceIcon;
    BitmapDescriptor mDestinationIcon;
    private TruckCateogeoriesFragment mTCFragment;
    private BookingFragment mBookingFragment;
    private BitmapDescriptor mTruckIcon;
    private boolean mTruckAvailable;
    MyCountDownTimer mCountDownTimer;
    private Location mPreviousLocation;
    LatLngInterpolator mLatLngInterpolator = null;
    private float mBearing = 0;
    private ArrayList<LatLng> mPolyLines;
    private boolean mBookNowIsClicked;
    private List<TruckInNearLocation> mNearTrucks;
    private boolean mIsDriverDetailsAvailable = false;
    private DriverDetailsFragment mDriverDetailsFragment = null;
    private List<DriversWithMarkers> mDriverWithMarkers = new ArrayList<>();
    private Marker mToMarker;
    private Marker mTruckMarker = null;
    private int mSelectedTruckPosition = 0;
    private boolean mIsTripStartedByDriver;
    private float mZoomingValue = 17.5f;
    private static final int TILT_THIRTY = 30;
    private static final int ROTATION_ANGLE = 45;
    private int mBookingState = 0;
    private boolean mIsReBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!PermissionChecker.checkPermission(HomeActivity.this, LOCATION_PERMISSIONS)) {
            PermissionChecker.reqPermissions(HomeActivity.this, LOCATION_PERMISSIONS);
        }

        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            Log.e(TAG, "Exception1 " + e.getMessage());
        }
        try {
            mIndicatorView = findViewById(R.id.indicator);
            mIndicatorView.setIndicator(INDICATORS[0]);
        } catch (Exception e) {
            Log.e(TAG, "Exception2 " + e.getMessage());
        }

        ObservableObject.getInstance().addObserver(this);

        try {
            try {
                mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                mMapFragment.getMapAsync(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mHomeViewPresenter = new HomeViewPresenterImpl(HomeActivity.this, this);

            if (!NetworkHelper.hasNetworkConnection(HomeActivity.this)) {
                ToastHelper.noInternet(HomeActivity.this);
                return;
            }

            mHomeViewPresenter.sendTokenAndMobileToServer(FirebaseInstanceId.getInstance().getToken(), CredentialManager.getMobileNO(HomeActivity.this));
            initializeTheViews();
            Log.e(TAG, "token " + CredentialManager.getAuthToken(HomeActivity.this));

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, "Exception " + e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            TextToSpeechHelper.getInstance(HomeActivity.this, this);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + "onStart");
        }
    }

    @Override
    protected void onStop() {
        try {
            CredentialManager.setBookingState(HomeActivity.this, mBookingState);
            TextToSpeechHelper.destroyTTS();
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + "onStop");
        }

        super.onStop();
    }

    private BitmapDescriptor getDrawableIcon(int id) {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(id);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }

    private void initializeTheViews() {
        try {

            mOnBackPressStatus = FINISH_ACTIVITY;
            mIsPickUpLocationLocked = false;
            mIsDropLocationLocked = false;
            mIsDriverDetailsAvailable = false;

            navDrawerOnCreate();

            if (mCountDownTimer != null) {
                mCountDownTimer.isRunning = false;
                mCountDownTimer = null;
            }

            mSourceIcon = getDrawableIcon(R.drawable.green_flag);
            mDestinationIcon = getDrawableIcon(R.drawable.red_flag);

            createSnackbar();

            mLocationLLL.setVisibility(GONE);
            pinIV.setVisibility(GONE);
            mSetLocationMarkertext.setVisibility(GONE);

            if (mHomeViewPresenter != null) {
                mHomeViewPresenter.checkIsCustomerInTrip();
            }

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, "Exception" + e.getMessage());
        }
    }

    private void handleThread() {
        try {

            mTimeOneTV.setVisibility(GONE);
            mTimeTwoTV.setVisibility(GONE);
            mProgressDialog.setVisibility(View.VISIBLE);
            mProgressBarForTime.setVisibility(GONE);
            mProgressBarForTime.setBackgroundColor(Color.TRANSPARENT);
            mTimeOneTV.setVisibility(GONE);
            mTimeTwoTV.setVisibility(GONE);
            mLockPickUpLocationIV.setImageResource(R.drawable.unlock);
            mLockDropLocationIV.setImageResource(R.drawable.unlock);
            pinIV.setImageResource(R.drawable.source2);
            mLocationLLL.setVisibility(View.VISIBLE);
            pinIV.setVisibility(View.VISIBLE);
            mSetLocationMarkertext.setVisibility(View.VISIBLE);
            resetEverythingfromMap();

            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                mTCFragment = new TruckCateogeoriesFragment();
                fragmentTransaction.add(R.id.gridLayout_fragment, mTCFragment);

                mBookingFragment = new BookingFragment();
                fragmentTransaction.add(R.id.booknow_linear_fragment, mBookingFragment);
                fragmentTransaction.commitAllowingStateLoss();
                mIsDriverDetailsAvailable = false;

                if (mDriverDetailsFragment != null) {
                    removeFragment(mDriverDetailsFragment);
                    mDriverDetailsFragment = null;
                }


                if (!mIsDropLocationLocked && mToMarker != null) {
                    mToMarker.remove();
                    mToMarker = null;
                }

                if (!mIsPickUpLocationLocked && mFromMarker != null) {
                    mFromMarker.remove();
                    mFromMarker = null;
                }

            } catch (Exception e) {
                LogUtils.appendLog(HomeActivity.this, TAG + "handleThread" + ":- Exception" + e.getMessage());
            }
            try {

                if (mBookingFragment != null) {
                    mBookingFragment.selectedTruckInformation(mSelectedTruckId, mSelectedClosedOrOpenTypeId, mSelectedLoadUnLoadId, 0);
                }

                inVisibleSnackbar();
            } catch (Exception e) {
                LogUtils.appendLog(HomeActivity.this, TAG + ":- Exception" + e.getMessage());
            }

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + ":- Exception" + e.getMessage());
        }
    }

    private void inVisibleSnackbar() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mProgressDialog.getVisibility() == View.VISIBLE) {
                    mProgressDialog.setVisibility(GONE);
                }
                if (mSnackbar != null && mSnackbar.isShown()) {
                    mSnackbar.dismiss();
                }

            }
        }, 2000);

    }

    private void enableGPS() {

        try {
            LogUtils.appendLog(HomeActivity.this, "enableGPS");
            LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (null != mLocationManager) {
                PackageManager pm = getPackageManager();
                boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
                boolean isGPSEnabled = false;
                boolean isNetworkEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (hasGps) {
                    isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                }

                if (!isNetworkEnable && !isGPSEnabled)
                    buildAlertMessageNoGps();

                if (PermissionChecker.checkPermission(HomeActivity.this, LOCATION_PERMISSIONS)) {

                    Location lastKnownLocation=null;
                    if (hasGps) {
                         lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (lastKnownLocation != null) {
                            mPickUpLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                    }else{
                        lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (lastKnownLocation != null) {
                            mPickUpLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                    }

                    if (mPickUpLatLng == null) {
                        onMyLocationButtonClick();
                    } else {
                        Log.e(TAG, "Current Location " + mPickUpLatLng.latitude + "," + mPickUpLatLng.longitude);
                        showMarkerOnMap(mPickUpLatLng, true);
                    }

                }
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + ":- Exception" + e.getMessage());
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppDialogTheme1);
        builder.setMessage(R.string.enable_gps)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void createSnackbar() {
        try {
            mSnackbar = Snackbar.make(mProgressDialog, R.string.getting_ur_loc, Snackbar.LENGTH_INDEFINITE);
            View snackView = mSnackbar.getView();
            TextView tv = snackView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(getResources().getColor(R.color.appThemeYellow));
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(0, 0, 0, 250);
            snackView.setLayoutParams(params);
            mSnackbar.show();
        } catch (Resources.NotFoundException e) {
            LogUtils.appendLog(HomeActivity.this, TAG + "creatSnackBar" + ":- Exception" + e.getMessage());
        }
    }

    @Override
    public void initializeTheTrucksAndBookingLatouyts() {
        try {
            mIsPickUpLocationLocked = false;
            mIsDropLocationLocked = false;
            mIsDriverDetailsAvailable = false;

            if (mDriverDetailsFragment != null) {
                removeFragment(mDriverDetailsFragment);
            }

            if (mTCFragment != null) {
                removeFragment(mTCFragment);
            }

            if (mBookingFragment != null) {
                removeFragment(mBookingFragment);
            }


            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        handleThread();
                    } catch (Exception ise) {
                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        finish();
                    }
                }
            }, 1000);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + ":- Exception" + e.getMessage());
        }
    }

    @Override
    public void setNearByTrucksOnMap(List<TruckInNearLocation> truckInNearLocations) {
        mNearTrucks = truckInNearLocations;
        showNearByTrucksonMap(truckInNearLocations);
        mTruckAvailable = true;

        if (mBookNowIsClicked) {
            createDriverRoute(mDropLatLng, false, false);
        }

        if (HomeActivity.mDropLatLng != null && mTruckAvailable) {
            TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.now_proc_for_booking));
        }
    }

    private Marker getTruckMarker(LatLng latLng) {
        Bitmap bitmap;
        Marker marker = null;

        switch (mSelectedClosedOrOpenTypeId) {
            case 1301:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.closed_truck);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.open_truck);
                break;
        }

        //   bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
        mTruckIcon = BitmapDescriptorFactory.fromBitmap(bitmap);

        try {
            if (mMap != null) {
                marker = mMap.addMarker(new MarkerOptions().position(latLng).flat(true).icon(mTruckIcon));
            }
        } catch (Exception e) {
            Log.e(TAG, "Marker is not created Exception " + e.getMessage());
        }

        return marker;
    }


    private void showNearByTrucksonMap(List<TruckInNearLocation> truckInNearLocations) {

        if (mMap != null) {
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            removePreviousSelectionTrucks();
        }

        //  ArrayList<String> driverIds = new ArrayList<>();

        for (int i = 0; i < truckInNearLocations.size(); i++) {
            // driverIds.add(truckInNearLocations.get(i).getDriverID());
            double lat = truckInNearLocations.get(i).getLatitude();
            double longi = truckInNearLocations.get(i).getLongitude();
            LatLng location = new LatLng(lat, longi);

            if (mDriverWithMarkers == null)
                mDriverWithMarkers = new ArrayList<>();

            DriversWithMarkers driversAndMarkers = new DriversWithMarkers();
            driversAndMarkers.setDriverId(truckInNearLocations.get(i).getDriverID());
            driversAndMarkers.setMarker(getTruckMarker(location));
            driversAndMarkers.setLatLng(location);
            mDriverWithMarkers.add(driversAndMarkers);
            getTravelBetwTwoLocations(mPickUpLatLng.latitude + "," + mPickUpLatLng.longitude, lat + "," + longi, false);
        }

    }


    private void removePreviousSelectionTrucks() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mDriverWithMarkers != null) {

                    for (int i = 0; i < mDriverWithMarkers.size(); i++) {
                        mDriverWithMarkers.get(i).getMarker().remove();
                    }
                    mDriverWithMarkers.clear();
                }
            }
        });

    }

    public void getTravelBetwTwoLocations(String fromLat, String toLat, boolean fa) {
        try {
            if (mHomeViewPresenter != null) {
                mHomeViewPresenter.getTravelTimeBwSourceAndDest(fromLat, toLat, fa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTravelBetwTwoLocations(String toLat, boolean fa) {
        if (mHomeViewPresenter != null)
            mHomeViewPresenter.getTravelTimeBwSourceAndDest(mPickUpLatLng.latitude + "," + mPickUpLatLng.longitude, toLat, fa);
    }


    private void resetEverythingfromMap() {
        try {
            mTruckAvailable = false;

            if (mMap != null) {
                mMap.clear();

                if (mFromMarker != null) {
                    mFromMarker.remove();
                    mFromMarker = null;

                }

                if (mToMarker != null) {
                    mToMarker.remove();
                    mToMarker = null;
                }


                if (mMapFragment != null) {
                    mMapFragment.getMapAsync(this);
                }

                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);

            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " resetEverythingfromMap:- Exception" + e.getMessage());
        }
    }

    @Override
    public void setETAToSelectedForTruck(String duration, String distance) {
        if (mTCFragment != null) {
            mTCFragment.setETAToSelectedTextView(duration, mSelectedTruckPosition);
        }
    }

    @Override
    public void setETAToSelectedForDriver(String duration, String distance) {
        if (mDriverDetailsFragment != null)
            mDriverDetailsFragment.setETAtime(duration);
    }

    @Override
    public void tryAgain() {
        //  ToastHelper.showToastLenShort(HomeActivity.this, "Trucks not available");
    }


    @Override
    public void navigateTOPaymentActivty(String result) {
        Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
        intent.putExtra(BOOKING_NO, result);
        startActivity(intent);
        finish();
    }

    @Override
    public void showDriverDetailsOnMap(ConfirmedDriver confirmedDriver) {
        try {

            if (mDriverDetailsFragment != null) {
                removeFragment(mDriverDetailsFragment);
                mDriverDetailsFragment = null;
            }

            if (mMap != null)
                mMap.getUiSettings().setMapToolbarEnabled(false);

            mIsDriverDetailsAvailable = true;
            //mZoomControlSkbr.setVisibility(View.VISIBLE);
            mIsPickUpLocationLocked = true;
            mIsDropLocationLocked = true;
            mOnBackPressStatus = FINISH_ACTIVITY;
            setStopProgressTime();
            mIsTripStartedByDriver = confirmedDriver.isInTrip();
            inVisibleSnackbar();
            clearLocationMarkersTrucksAndBookingLayouts();


        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showDriverDetailsOnMap:- Exception" + e.getMessage());
        }

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mDriverDetailsFragment = new DriverDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("confirm_bundle_key", confirmedDriver);
            CredentialManager.setDriverId(HomeActivity.this, confirmedDriver.getDriverId());

            mDriverDetailsFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.driver_fragment, mDriverDetailsFragment);
            fragmentTransaction.commitAllowingStateLoss();

            pinIV.setVisibility(GONE);
            mSetLocationMarkertext.setVisibility(GONE);

            if (mMap != null) {
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }

            mFromMarker = mMap.addMarker(new MarkerOptions()
                    .position(mPickUpLatLng)
                    .title(mFromAddress)
                    .icon(mSourceIcon));

            mToMarker = mMap.addMarker(new MarkerOptions()
                    .position(mDropLatLng)
                    .title(mToAddress)
                    .icon(mDestinationIcon));

            LatLng latLng = new LatLng(confirmedDriver.getLatitude(), confirmedDriver.getLongitude());
            mSelectedClosedOrOpenTypeId = confirmedDriver.getVehicleType();


            if (mDriverWithMarkers != null) {
                mDriverWithMarkers.clear();
                mDriverWithMarkers = null;
            }

            mTruckMarker = getTruckMarker(latLng);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)                        // Center Set
                    .zoom(mZoomingValue)                  // Zoom
                    .bearing(ROTATION_ANGLE)                         // Orientation of the camera to east
                    .tilt(TILT_THIRTY)                  // Tilt of the camera to 30 degrees
                    .build();

            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } catch (Exception e) {

            LogUtils.appendLog(HomeActivity.this, TAG + " showDriverDetailsOnMap:- Exception" + e.getMessage());

        }
    }

    @Override
    public void noTruckFound() {
        mTruckAvailable = false;
        removePreviousSelectionTrucks();

        if (mTCFragment != null) {
            mTCFragment.setETAToSelectedTextView("No trucks", mSelectedTruckPosition);
        }
    }


    @Override
    public void setBookingData(BookingInfo bookingInfo1) {
        try {

            mIsDropLocationLocked = true;
            mIsPickUpLocationLocked = true;
            // mLocationIndicator.setVisibility(GONE);
            mFromAddress = bookingInfo1.getLocationFrom();
            mToAddress = bookingInfo1.getLocationTo();
            mPickUpLatLng = new LatLng(bookingInfo1.getLatitude(), bookingInfo1.getLongitude());
            mDropLatLng = new LatLng(bookingInfo1.getToLatitude(), bookingInfo1.getToLongitude());
            mSelectedClosedOrOpenTypeId = bookingInfo1.getVehicleType();
            mSelectedTruckId = bookingInfo1.getVehicleGroup();
            mSelectedLoadUnLoadId = bookingInfo1.getLoadingUnLoading();
            mSourceIcon = getDrawableIcon(R.drawable.green_flag);
            mDestinationIcon = getDrawableIcon(R.drawable.red_flag);
        } catch (Resources.NotFoundException e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " setBookingData:- Exception" + e.getMessage());
        }
    }


    private void clearLocationMarkersTrucksAndBookingLayouts() {

        try {
            if (mTCFragment != null) {
                removeFragment(mTCFragment);
                mTCFragment = null;
            }
            if (mBookingFragment != null) {
                removeFragment(mBookingFragment);
                mBookingFragment = null;
            }
            if (mMap != null) {
                mMap.clear();

                if (PermissionChecker.checkPermission(HomeActivity.this, LOCATION_PERMISSIONS))
                    mMap.setMyLocationEnabled(false);
            }
            if (mFromMarker != null) {
                mFromMarker.remove();
                mFromMarker = null;
            }

            if (mToMarker != null) {
                mToMarker.remove();
                mToMarker = null;
            }

            if (mLocationLLL != null) {
                mLocationLLL.setVisibility(GONE);
            }
            if (pinIV != null) {
                pinIV.setVisibility(GONE);
            }


            if (mSetLocationMarkertext != null)
                mSetLocationMarkertext.setVisibility(GONE);

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " clearLocationMarkersTrucksAndBookingLayouts:- Exception" + e.getMessage());

        }
    }

    private void removeFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commitAllowingStateLoss();

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " removeFragment:- Exception" + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            ToastHelper.showToastLenShort(HomeActivity.this, "Please give permission to access locations");
            PermissionChecker.reqPermissions(HomeActivity.this,LOCATION_PERMISSIONS);
        } else {
            enableGPS();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            if (CredentialManager.getEntryCount(HomeActivity.this) == 1) {
                CredentialManager.setVolumeStatus(HomeActivity.this, true);
                CredentialManager.setFirstTimeEntryCount(HomeActivity.this, 2);
            }

            boolean announcementStatus = CredentialManager.getVolumeStatus(HomeActivity.this);

            int id = 0;

            if (announcementStatus) {
                id = R.drawable.announcements_on;
            } else {
                id = R.drawable.announcements_off;
            }

            menu.add(0, 1, 1, "").setIcon(id/*R.drawable.announcements_on*/).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onCreateOptionsMenu:- Exception" + e.getMessage());

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        try {
            MenuItem menuItem = menu.getItem(0);
            boolean announcementStatus = CredentialManager.getVolumeStatus(HomeActivity.this);
            if (announcementStatus) {
                menuItem.setIcon(R.drawable.announcements_on);
            } else {
                menuItem.setIcon(R.drawable.announcements_off);
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onPrepareOptionsMenu:- Exception" + e.getMessage());

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            boolean announcementStatus = CredentialManager.getVolumeStatus(HomeActivity.this);
            CredentialManager.setVolumeStatus(HomeActivity.this, !announcementStatus);
            announcementStatus = CredentialManager.getVolumeStatus(HomeActivity.this);

            if (announcementStatus) {
                item.setIcon(R.drawable.announcements_on);
            } else {
                item.setIcon(R.drawable.announcements_off);
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onOptionsItemSelected:- Exception" + e.getMessage());

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        try {

            if (mMap != null) {
                Location myLocation = mMap.getMyLocation();

                if (myLocation != null) {

                    if (!mIsPickUpLocationLocked) {
                        mPickUpLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        showMarkerOnMap(mPickUpLatLng, true);
                        getNearByTrucks();
                    } else if (!mIsDropLocationLocked) {
                        mDropLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        showMarkerOnMapForToLocation(mDropLatLng, true);
                    } else {
                        if (mMap != null)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), MAP_CAMERA_ZOOM_ANIMATION));

                    }
                }

            }/*else{
                mMapFragment.getMapAsync(this);
            }*/

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onMyLocationButtonClick:- Exception" + e.getMessage());

        }
        return true;
    }


    public void getNearByTrucks() {

        try {
            if (!NetworkHelper.hasNetworkConnection(HomeActivity.this)) {
                ToastHelper.noInternet(HomeActivity.this);
                return;
            }
            try {
                if (mPickUpLatLng != null && mPickUpLatLng.longitude != 0.0 && mPickUpLatLng.latitude != 0.0) {
                    NearestData nearestData = new NearestData();
                    nearestData.setLatitude(mPickUpLatLng.latitude);
                    nearestData.setLongitude(mPickUpLatLng.longitude);
                    nearestData.setTruckId("" + mSelectedTruckId);
                    nearestData.setOpenCloseId("" + mSelectedClosedOrOpenTypeId);

                    if (mHomeViewPresenter != null) {
                        mHomeViewPresenter.getTrucksNearByLocation(nearestData);
                    }

                }
            } catch (Exception e) {
                LogUtils.appendLog(HomeActivity.this, TAG + " getNearByTrucks:- Exception" + e.getMessage());

            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " getNearByTrucks:- Exception" + e.getMessage());
        }
    }

    private void showSelectedAddressOnMap(@NonNull LatLng location, boolean callGetAdreessASync) {
        try {
            pinIV.setImageResource(R.drawable.source2);
            mHomeViewPresenter.getAddressFromLatLng(location.latitude + "," + location.longitude);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showSelectedAddressOnMap:- Exception" + e.getMessage());
        }
    }

    private void showSelectedAddressOnMapForToLocation(@NonNull LatLng location, boolean callGetAdreessASync) {

        try {
            pinIV.setImageResource(R.drawable.destination2);
            mHomeViewPresenter.getAddressFromLatLng(location.latitude + "," + location.longitude);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showSelectedAddressOnMapForToLocation:- Exception" + e.getMessage());
        }

    }

    @Override
    public void onInit(int status) {

    }

    public void unLockPickUpLocation(View view) {
        try {
            unLockPickUpLocation();

            if (mPickUpLatLng != null) {
                showMarkerOnMap(mPickUpLatLng, false);
            }

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " unLockPickUpLocation:- Exception" + e.getMessage());
        }
    }


    public void unLockDropUpLocation(View view) {
        try {
            unLockDropUpLocation();

            if (mDropLatLng != null) {
                showMarkerOnMapForToLocation(mDropLatLng, false);
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " unLockDropUpLocation:- Exception" + e.getMessage());
        }
    }


    @Override
    protected void onDestroy() {
        try {
            TextToSpeechHelper.destroyTTS();
            super.onDestroy();
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onDestroy:- Exception" + e.getMessage());

        }
    }

    public void zoomDropLocation(View view) {
        try {
            if (mMap != null && mDropLatLng != null) {
                mDropLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                mPickUpLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                toLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                fromLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                mPickUpOrDropLocationSelected = DROP;

                if (!mIsDropLocationLocked)
                    pinIV.setImageResource(R.drawable.destination2);

                showMarkerOnMapForToLocation(mDropLatLng, false);
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " zoomDropLocation:- Exception" + e.getMessage());

        }
    }

    public void zoomPickUpLocation(View view) {

        try {
            if (mMap != null && mPickUpLatLng != null) {
                mPickUpLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                mDropLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                fromLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                toLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                mPickUpOrDropLocationSelected = PICK_UP;

                if (!mIsPickUpLocationLocked) {
                    pinIV.setImageResource(R.drawable.source2);
                }
                showMarkerOnMap(mPickUpLatLng, false);
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " zoomPickUpLocation:- Exception" + e.getMessage());

        }
    }

    @Override
    public void onTruckSelected(int truckId, int openClosedId, int loadUnloadId, int selectedTruckPosition) {
        try {

            if (!mIsReBook) {
                mSelectedTruckId = truckId;
                mSelectedClosedOrOpenTypeId = openClosedId;
                mSelectedLoadUnLoadId = loadUnloadId;

                mSelectedTruckPosition = selectedTruckPosition;

                if (mBookingFragment != null) {
                    mBookingFragment.selectedTruckInformation(truckId, openClosedId, loadUnloadId, selectedTruckPosition);
                }
                getNearByTrucks();

            } else {
                mIsReBook = false;
            }


        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onTruckSelected:- Exception" + e.getMessage());

        }
    }

    @Override
    public void visibleRateCard() {

        mOnBackPressStatus = ENABLE_BOOKING;

        if (mTCFragment != null) {
            mTCFragment.visibleRateCard();
            mTCFragment.inVisibleTruckLayout();
        }
    }


    @Override
    public void invisibleRateCardLayout(boolean isBookingLater) {

        mOnBackPressStatus = DISABLE_ON_BACK_PRESS;

        if (mTCFragment != null) {
            mTCFragment.inVisibleRateCardLayout();
            mTCFragment.setAllRequiredValues(isBookingLater, mSelectedLoadUnLoadId);
        }

    }

    @Override
    public void setStartProgressTime(boolean isBookingLater) {
        try {
            showMarkerOnMap(mPickUpLatLng, false);
            int repeatTimes = 2;
            int totalTimeSec = 60;
            long totalTime = totalTimeSec * 1000;
            mTimeOneTV.setVisibility(GONE);
            mTimeOneTV.setText(String.format("%d s", totalTimeSec));
            mTimeTwoTV.setVisibility(GONE);
            mTimeTwoTV.setText("1/2");
            mCountDownTimer = new MyCountDownTimer(totalTime, 1000, repeatTimes, totalTime, HomeActivity.this, this);
            mCountDownTimer.start();
            mCountDownTimer.isRunning = true;
            Animation an = new RotateAnimation(0.0f, 0.0f, 0f, 0f);
            an.setFillAfter(true);
            mIndicatorView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " setStartProgressTime:- Exception" + e.getMessage());
        }
    }

    public void setStopProgressTime() {
        try {
            if (mCountDownTimer == null) {
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mCountDownTimer != null && mCountDownTimer.isRunning) {
                        mCountDownTimer.cancel();
                    }

                }
            }, 100);


            if (mCountDownTimer != null) {
                mCountDownTimer.isRunning = false;
            }
            mIndicatorView.setVisibility(GONE);
            mIndicatorView.setBackgroundColor(Color.TRANSPARENT);
            mTimeOneTV.setText("");
            mTimeOneTV.setVisibility(GONE);
            mTimeTwoTV.setText("");
            mTimeTwoTV.setVisibility(GONE);
            mCountDownTimer = null;
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " setStopProgressTime:- Exception" + e.getMessage());
        }
    }


    @Override
    public void createDriverRoute(final LatLng driverCurLatLng, final boolean canShowDriverLocation, final boolean isReachedDeliveryLocation) {
        // mIsDriverReachedDeliveryLoc = isReachedDeliveryLocation;

        if (!NetworkHelper.hasNetworkConnection(HomeActivity.this)) {
            ToastHelper.noInternet(HomeActivity.this);
            return;
        }
        String requestUrl = MapHelper.getDirectionUrl(mPickUpLatLng, driverCurLatLng);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response + "");
                        mBookNowIsClicked = false;
                        try {
                            JSONArray jsonArray = response.getJSONArray("routes");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject route = jsonArray.getJSONObject(i);
                                JSONObject poly = route.getJSONObject("overview_polyline");
                                String polyline = poly.getString("points");
                                mPolyLines = GoogleMapHelper.decodePoly(polyline);
                            }

                            if (mPolyLines != null && mPolyLines.size() > 0) {
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                                for (LatLng latLng : mPolyLines) {
                                    builder.include(latLng);
                                }
                                PolylineOptions polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.BLUE);
                                polylineOptions.width(10);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.endCap(new SquareCap());
                                polylineOptions.jointType(ROUND);
                                polylineOptions.addAll(mPolyLines);

                                if (mMap != null) {
                                    LatLngBounds bounds = builder.build();
                                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                                    mMap.animateCamera(mCameraUpdate);
                                    mMap.addPolyline(polylineOptions);
                                }

                                if (canShowDriverLocation) {
                                    showBothMarkersIncludingDriverOnMap(driverCurLatLng);
                                } else {
                                    showBothFromTOLocationOnMapThroughMarkers(mPickUpLatLng, mDropLatLng);
                                }
                            }

                        } catch (Exception e) {
                            LogUtils.appendLog(HomeActivity.this, TAG + " createDriverRoute:- Exception" + e.getMessage());

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                mBookNowIsClicked = false;
                LogUtils.appendLog(HomeActivity.this, TAG + " createDriverRoute:- Exception" + e.getMessage());


            }
        });
        RequestQueue requestQueue = VolleyHelper.getVolleyInstance(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean checkBothLocation() {

        try {
            String pick = ViewHelper.getString(mPickUpLocationTV);
            String drop = ViewHelper.getString(mDropLocationTV);

            if (pick.equalsIgnoreCase(DefaultSrc) || pick.isEmpty()) {
                TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.select_pick_up));
                ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.select_pick_up));
                return false;
            }


            if (!mIsPickUpLocationLocked) {
                TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.lock_pick_location));
                ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.lock_pick_location));
                return false;
            }

            if (drop.equalsIgnoreCase(DefaultDes) || drop.isEmpty()) {
                TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.select_drop_location));
                ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.select_drop_location));
                return false;
            }


            if (!mIsDropLocationLocked) {
                TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.lock_drop_location));
                ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.lock_drop_location));
                return false;
            }

            if (pick.equalsIgnoreCase(drop)) {
                TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.pick_drop_not_same));
                ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.pick_drop_not_same));
                return false;
            }

            if (mBookingFragment != null) {

                if (!mBookingFragment.mIsBookingLater) {
                    if (!mTruckAvailable) {
                        TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.trucks_are_not_available));
                        ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.trucks_are_not_available));
                        return false;
                    }
                }
            }

            mLocationLLL.setVisibility(GONE);

            if (!setSrcDestText())
                return false;

            if (mTCFragment != null) {
                mTCFragment.inVisibleTruckLayout();
                mTCFragment.visibleRateCard();
            }

            CredentialManager.setFromoLat(HomeActivity.this, mPickUpLatLng.latitude);
            CredentialManager.setFromLong(HomeActivity.this, mPickUpLatLng.longitude);
            CredentialManager.setToLat(HomeActivity.this, mDropLatLng.latitude);
            CredentialManager.setToLong(HomeActivity.this, mDropLatLng.longitude);

            mBookNowIsClicked = true;

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " checkBothLocation:- Exception" + e.getMessage());

        }

        return true;
    }


    @Override
    public void drawDirectionBetweenSourceToDestination() {
        String requestUrl = MapHelper.getDirectionUrl(mPickUpLatLng, mDropLatLng);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response + "");

                        try {
                            JSONArray jsonArray = response.getJSONArray("routes");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject route = jsonArray.getJSONObject(i);
                                JSONObject poly = route.getJSONObject("overview_polyline");
                                String polyline = poly.getString("points");
                                mPolyLines = GoogleMapHelper.decodePoly(polyline);
                            }

                            if (mPolyLines != null && mPolyLines.size() > 0) {
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng latLng : mPolyLines) {
                                    builder.include(latLng);
                                }
                                if (mMap == null)
                                    return;

                                //  mMap.clear();

                                LatLngBounds bounds = builder.build();
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                                mMap.animateCamera(mCameraUpdate);
                                mMap.addMarker(new MarkerOptions().position(/*mPolyLines.get(0)*/mPickUpLatLng).title(mFromAddress).icon(mSourceIcon));
                                mMap.addMarker(new MarkerOptions().position(mDropLatLng/*mPolyLines.get(mPolyLines.size() - 1)*/).title(mToAddress).icon(mDestinationIcon));

                                PolylineOptions polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.BLUE);
                                polylineOptions.width(10);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.endCap(new SquareCap());
                                polylineOptions.jointType(ROUND);
                                polylineOptions.addAll(mPolyLines);
                                final Polyline greyPolyLine = mMap.addPolyline(polylineOptions);

                                PolylineOptions blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.width(5);
                                blackPolylineOptions.color(Color.BLUE/*Color.parseColor("#fff200")*/);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(ROUND);
                                final Polyline blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                                polylineAnimator.setDuration(3000);
                                polylineAnimator.setInterpolator(new LinearInterpolator());
                                polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        List<LatLng> points = greyPolyLine.getPoints();
                                        int percentValue = (int) valueAnimator.getAnimatedValue();
                                        int size = points.size();
                                        int newPoints = (int) (size * (percentValue / 100.0f));
                                        List<LatLng> p = points.subList(0, newPoints);
                                        blackPolyline.setPoints(p);
                                    }
                                });

                                polylineAnimator.start();
                            }
                        } catch (Exception e) {
                            LogUtils.appendLog(HomeActivity.this, TAG + " drawDirectionBetweenSourceToDestination:- Exception" + e.getMessage());

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                LogUtils.appendLog(HomeActivity.this, TAG + " drawDirectionBetweenSourceToDestination:- Exception" + e.getMessage());


            }
        });

        RequestQueue requestQueue = VolleyHelper.getVolleyInstance(this);//Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    private void unLockDropUpLocation() {
        try {
            if (!mIsDropLocationLocked) {
                return;
            }

            if (!mBookNowIsClicked) {
                mIsDropLocationLocked = false;
                mLockDropLocationIV.setImageResource(R.drawable.unlock);
                pinIV.setVisibility(View.VISIBLE);
                mSetLocationMarkertext.setVisibility(View.VISIBLE);
                mDropLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                mPickUpLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                toLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                fromLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                mPickUpOrDropLocationSelected = DROP;
                pinIV.setImageResource(R.drawable.destination2);
            }

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " unLockDropUpLocation:- Exception" + e.getMessage());

        }
    }

    private void unLockPickUpLocation() {
        try {
            if (!mIsPickUpLocationLocked) {
                return;
            }

            if (!mBookNowIsClicked) {
                mLockPickUpLocationIV.setImageResource(R.drawable.unlock);
                mIsPickUpLocationLocked = false;
                pinIV.setVisibility(View.VISIBLE);
                mSetLocationMarkertext.setVisibility(View.VISIBLE);
                mPickUpLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                mDropLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                fromLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                toLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                mPickUpOrDropLocationSelected = PICK_UP;
                pinIV.setImageResource(R.drawable.source2);
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " unLockPickUpLocation:- Exception" + e.getMessage());
        }
    }

    public boolean setSrcDestText() {
        try {
            mFromAddress = mPickUpLocationTV.getText().toString();
            mToAddress = mDropLocationTV.getText().toString();

            if (mFromAddress.equalsIgnoreCase(DefaultSrc) || mFromAddress.isEmpty()) {
                TextToSpeechHelper.speakOut(HomeActivity.this, "Select the pick up location");
                ToastHelper.showToastLenShort(HomeActivity.this, "Select the pick up location");
                return false;
            }

            if (mToAddress.equalsIgnoreCase(DefaultDes) || mToAddress.isEmpty()) {
                TextToSpeechHelper.speakOut(HomeActivity.this, "Select the drop location");
                ToastHelper.showToastLenShort(HomeActivity.this, "Select the drop location");
                return false;
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " setSrcDestText:- Exception" + e.getMessage());
            return false;
        }

        return true;

    }

    public void showBothMarkersIncludingDriverOnMap(LatLng driverLatLng) {

        try {
            GoogleMap googleMap = mMap;
            mTruckMarker.setPosition(driverLatLng);

            Marker[] markers = {mTruckMarker, mFromMarker, mToMarker};
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }

            LatLngBounds bounds = builder.build();

            int padding = 50;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.moveCamera(cu);
            googleMap.animateCamera(cu);

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showBothMarkersIncludingDriverOnMap:- Exception" + e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showBothFromTOLocationOnMapThroughMarkers(final LatLng fromLocation, final LatLng toLocation) {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mMapLL.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.location_LLL);
        try {
            if (mMap != null) {
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Marker> markersAL = new ArrayList<>();

        if (fromLocation != null) {
            markersAL.add(showMarkerOnMap(fromLocation, true));
        }

        if (mIsDropLocationLocked && mIsPickUpLocationLocked) {
            if (toLocation != null) {
                markersAL.add(showMarkerOnMapForToLocation(toLocation, true));
                //distance = CalculationByDistance(markersAL.get(0).getPosition(), markersAL.get(1).getPosition());
            }
        }
        try {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Marker marker : markersAL) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 50; // offset from edges of the mHashMap in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            try {
                if (mMap != null)
                    mMap.animateCamera(cu);
            } catch (Exception e) {
                LogUtils.appendLog(HomeActivity.this, TAG + " showBothFromTOLocationOnMapThroughMarkers:- Exception" + e.getMessage());

            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showBothFromTOLocationOnMapThroughMarkers:- Exception" + e.getMessage());

        }

        try {
            if (mMapFragment != null) {
                final View mapView = mMapFragment.getView();
                if (mapView != null && mapView.getViewTreeObserver().isAlive() && toLocation != null) {
                    try {
                        mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onGlobalLayout() {
                                LatLngBounds.Builder bld = new LatLngBounds.Builder();
                                if (fromLocation != null) {
                                    bld.include(fromLocation);
                                }
                                if (toLocation != null) {
                                    bld.include(toLocation);
                                }
                                LatLngBounds bounds = bld.build();

                                if (mMap != null)
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));

                                mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                params.addRule(RelativeLayout.RIGHT_OF, 0);
                            }
                        });
                    } catch (Exception e) {
                        LogUtils.appendLog(HomeActivity.this, TAG + " showBothFromTOLocationOnMapThroughMarkers:- Exception" + e.getMessage());

                    }
                }
            }

            params.addRule(RelativeLayout.RIGHT_OF, 0);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showBothFromTOLocationOnMapThroughMarkers:- Exception" + e.getMessage());

        }
    }


    @Override
    public void setTimerValues(String t1, String t2) {
        try {
            mTimeTwoTV.setText(t2);
            mTimeOneTV.setText(t1);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " setTimerValues:- Exception" + e.getMessage());
        }
    }

    @Override
    public void hideTheViews() {
        try {
            if (mIndicatorView != null)
                mIndicatorView.hide();

            CredentialManager.setCallBookNowAPI(this, false);

            mTimeOneTV.setText("");
            mTimeTwoTV.setVisibility(GONE);
            mTimeOneTV.setVisibility(GONE);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " hideTheViews:- Exception" + e.getMessage());
        }
    }

    @Override
    public void retryBookingDialog() {
        try {

            if (!mIsPickUpLocationLocked && !mIsDropLocationLocked)
                return;

            if (mIsDriverDetailsAvailable)
                return;

            //  mIsRetryDialogShown=true;
            AlertDialogActivity.showAlertDialogActivity(HomeActivity.this, getString(R.string.trucks_are_not_available),
                    getString(R.string.high_demand), getString(R.string.retry), getString(R.string.cancel),
                    new AlertDialogActivity.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick() {

                            if (!mIsDriverDetailsAvailable && mIsPickUpLocationLocked && mIsDropLocationLocked) {
                                mBookingFragment.callBookNowAPI();
                            }

                        }
                    }, new AlertDialogActivity.OnNegativeBrnClickListener() {
                        @Override
                        public void onNegativeBtnClick() {
                            if (mBookingFragment != null && !mIsDriverDetailsAvailable && mIsPickUpLocationLocked && mIsDropLocationLocked) {
                                mBookingFragment.cancelBooking(CredentialManager.getBookingNO(HomeActivity.this), getString(R.string.time_limit_exceed));
                            }

                        }
                    });

            TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.trucks_not_found_c_r));
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " retryBookingDialog:- Exception" + e.getMessage());
        }
    }

    @Override
    public void bookingCancelledSuccessfully() {

        try {


            CredentialManager.setDriverId(HomeActivity.this, "");
            setStopProgressTime();
            mIsDriverDetailsAvailable = false;

            if (mDriverDetailsFragment != null)
                removeFragment(mDriverDetailsFragment);

            TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.your_booking_is_cancelled));

            mDropLatLng = null;
            mPickUpLatLng = null;
            mIsPickUpLocationLocked = false;
            mIsDropLocationLocked = false;
            mBookNowIsClicked = false;
            mSelectedTruckId = 1000;
            mPickUpLocationTV.setText(DefaultSrc);
            mDropLocationTV.setText(DefaultDes);
            mSelectedClosedOrOpenTypeId = 1300;
            mPickUpOrDropLocationSelected = PICK_UP;
            resetEverythingfromMap();
            mFromAddress = "";
            mToAddress = "";
            pinIV.setImageResource(R.drawable.source2);
            mSetLocationMarkertext.setVisibility(View.VISIBLE);
            mLocationLLL.setVisibility(View.VISIBLE);
            pinIV.setVisibility(View.VISIBLE);
            mIndicatorView.setVisibility(GONE);
            initializeTheViews();
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " bookingCancelledSuccessfully:- Exception" + e.getMessage());
        }
    }


    @Override
    public void latestLocation() {
        mFromAddress = ViewHelper.getString(mPickUpLocationTV);
        mToAddress = ViewHelper.getString(mDropLocationTV);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            mMap = googleMap;
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);


            if (PermissionChecker.checkPermission(HomeActivity.this, LOCATION_PERMISSIONS))
                mMap.setMyLocationEnabled(true);


            enableGPS();
            mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int i) {
                    // mIsCameraMoveStarted = true;
                    switch (mPickUpOrDropLocationSelected) {
                        case PICK_UP:
                            if (!mIsPickUpLocationLocked) {
                                mPickUpLocationTV.setText(DefaultSrc);
                            }
                            break;
                        case DROP:

                            if (!mIsDropLocationLocked) {
                                mDropLocationTV.setText(DefaultDes);
                            }

                            break;
                    }
                }
            });

            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    try {
                        if (!mIsDriverDetailsAvailable) {
                            try {
                                switch (mPickUpOrDropLocationSelected) {
                                    case PICK_UP:

                                        if (!mIsPickUpLocationLocked) {
                                            mPickUpLatLng = mMap.getCameraPosition().target;
                                            showSelectedAddressOnMap(mPickUpLatLng, true);
                                        }

                                        break;
                                    case DROP:
                                        if (!mIsDropLocationLocked) {
                                            mDropLatLng = mMap.getCameraPosition().target;
                                            showSelectedAddressOnMapForToLocation(mDropLatLng, true);
                                        }
                                        break;
                                }
                            } catch (Exception e) {
                                LogUtils.appendLog(HomeActivity.this, TAG + " onMapReady setOnCameraIdleListener:- Exception" + e.getMessage());
                            }
                        }


                    } catch (Exception e) {
                        LogUtils.appendLog(HomeActivity.this, TAG + " onMapReady:- Exception" + e.getMessage());

                    }
                }

            });

            mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
                @Override
                public void onCameraMoveCanceled() {
                }
            });

            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    CameraPosition cameraPosition = mMap.getCameraPosition();
                    mZoomingValue = cameraPosition.zoom;
                }
            });

            try {
                View mapView = mMapFragment.getView();

                if (mapView != null && mapView.findViewById(1) != null) {

                    @SuppressLint("ResourceType") View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    layoutParams.setMargins(0, 0, 30, 30);
                }
                mMap.setOnMyLocationButtonClickListener(this);

                if(!mIsDriverDetailsAvailable && mPickUpLatLng==null)
                onMyLocationButtonClick();

            } catch (Exception e) {
                LogUtils.appendLog(HomeActivity.this, TAG + " onMapReady :- Exception" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Marker showMarkerOnMap(@NonNull LatLng location, boolean callGetAdreessASync) {

        try {
            pinIV.setImageResource(R.drawable.source2);

            if (location.latitude != 0.0 && location.longitude != 0.0) {

                if (callGetAdreessASync) {
                    mHomeViewPresenter.getAddressFromLatLng(location.latitude + "," + location.longitude);
                }

                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), MAP_CAMERA_ZOOM_ANIMATION));

                    MarkerOptions mMarkerOptions = new MarkerOptions();
                    mMarkerOptions.position(location);
                    mMarkerOptions.title(mFromAddress);
                    mMarkerOptions.icon(mSourceIcon);

                    if (mFromMarker != null) {
                        mFromMarker.remove();
                    }

                    if (mIsPickUpLocationLocked) {//16042018
                        mFromMarker = mMap.addMarker(mMarkerOptions);
                    }

                }
            }

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showMarkerOnMap :- Exception" + e.getMessage());
        }

        return mFromMarker;
    }


    @Override
    public void setCurrentAddress(String address) {

        if (address != null && !address.isEmpty()) {
            setAddressToMarker(address);
        } else {
            try {

                if (mIsPickUpLocationLocked && mIsDropLocationLocked)
                    return;

                try {
                    GetAddressTask getAddress = new GetAddressTask();
                    getAddress.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    LogUtils.appendLog(HomeActivity.this, TAG + " GetAddressTask :- Exception" + e.getMessage());
                }


            } catch (Exception e) {
                LogUtils.appendLog(HomeActivity.this, TAG + " MapHelper.getAddress :- Exception" + e.getMessage());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class GetAddressTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                if (mPickUpOrDropLocationSelected.equals(PICK_UP)) {
                    return MapHelper.getAddress(HomeActivity.this, mPickUpLatLng);
                } else {
                    return MapHelper.getAddress(HomeActivity.this, mDropLatLng);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                if (s != null && s.length() > 0) {
                    setAddressToMarker(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setAddressToMarker(String address) {
        if (mPickUpOrDropLocationSelected.equals(PICK_UP)) {
            if (!mIsPickUpLocationLocked) {
                mFromAddress = address;
                mPickUpLocationTV.setText(mFromAddress);

                if (mFromMarker != null) {
                    mFromMarker.setTitle(mFromAddress);
                }
            }
        } else {
            if (!mIsDropLocationLocked) {
                mToAddress = address;
                mDropLocationTV.setText(mToAddress);

                if (mToMarker != null) {
                    mToMarker.setTitle(mToAddress);
                }
            }
        }
    }

    public void pickUpSrcLocation(View view) {
        try {
            if (mIsPickUpLocationLocked) {
                return;
            }

            if (mPickUpLatLng != null) {

                if (mPickUpOrDropLocationSelected.equalsIgnoreCase(DROP)) {
                    mPickUpLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                    mDropLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                    fromLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                    toLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                    mPickUpOrDropLocationSelected = PICK_UP;
                    pinIV.setImageResource(R.drawable.source2);
                    showMarkerOnMap(mPickUpLatLng, false);
                }
            }


            callPlaceActivity(FROM_PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " pickUpSrcLocation :- Exception" + e.getMessage());

        }
    }

    public void pickDropLocation(View view) {

        try {
            if (mIsDropLocationLocked) {
                return;
            }
            if (mDropLatLng != null) {
                if (mPickUpOrDropLocationSelected.equalsIgnoreCase(PICK_UP)) {
                    mPickUpLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                    mDropLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                    toLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                    fromLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                    mPickUpOrDropLocationSelected = DROP;
                    pinIV.setImageResource(R.drawable.destination2);
                    showMarkerOnMapForToLocation(mDropLatLng, false);
                }
            }

            callPlaceActivity(TO_PLACE_PICKER_REQUEST);

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " pickDropLocation :- Exception" + e.getMessage());
        }


    }

    private void callPlaceActivity(int requestCode) {
        try {
            if (!NetworkHelper.hasNetworkConnection(HomeActivity.this)) {
                ToastHelper.noInternet(HomeActivity.this);
                return;

            }
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(HomeActivity.this);
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " callPlaceActivity :- Exception" + e.getMessage());

        }
    }

    //lock the selected location
    public void lockSelectedLocation(View view) {

        try {
            if (mPickUpOrDropLocationSelected.equals(PICK_UP)) {

                if (mPickUpLatLng == null) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.select_pick_up));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.select_pick_up));
                    return;
                }

                if (mPickUpLocationTV.getText().toString().equals(DefaultSrc)) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.select_pick_up));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.select_pick_up));
                    return;
                }

                mFromAddress = mPickUpLocationTV.getText().toString();
                mIsPickUpLocationLocked = true;
                mLockPickUpLocationIV.setImageResource(R.drawable.lock);
                showMarkerOnMap(mPickUpLatLng, true);//22022018

                if (!mIsDropLocationLocked && !mBookNowIsClicked) {

                    mDropLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                    mPickUpLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                    toLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                    fromLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                    mPickUpOrDropLocationSelected = DROP;
                    pinIV.setImageResource(R.drawable.destination2);

                }


                if (mDropLatLng == null) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.select_drop_loc));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.select_drop_loc));
                    return;
                }

                if (!mIsDropLocationLocked) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.loc_drop_loc));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.loc_drop_loc));
                    return;
                }

                TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.now_u_can_proceed_for_nooking));

            } else if (mPickUpOrDropLocationSelected.equals(DROP)) {

                if (mDropLatLng == null) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.select_drop_loc));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.select_drop_loc));
                    return;
                }

                if (mDropLocationTV.getText().toString().equals(DefaultDes)) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.loc_drop_loc));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.loc_drop_loc));
                    return;
                }

                mToAddress = mDropLocationTV.getText().toString();
                mIsDropLocationLocked = true;
                mLockDropLocationIV.setImageResource(R.drawable.lock);
                showMarkerOnMapForToLocation(mDropLatLng, true);//22022018

                if (!mIsPickUpLocationLocked && !mBookNowIsClicked) {

                    mPickUpLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                    mDropLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                    fromLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                    toLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                    mPickUpOrDropLocationSelected = PICK_UP;
                    pinIV.setImageResource(R.drawable.source2);
                    // showMarkerOnMap(mPickUpLatLng, true);
                }

                if (mPickUpLatLng == null) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.select_pick_pick));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.select_pick_pick));
                    return;
                }

                if (!mIsPickUpLocationLocked) {
                    TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.lock_pick));
                    ToastHelper.showToastLenShort(HomeActivity.this, getString(R.string.lock_pick));
                    return;
                }

                TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.now_u_can_proceed_for_nooking));
            }

            if (mIsPickUpLocationLocked && mIsDropLocationLocked && !mBookNowIsClicked) {
                mSetLocationMarkertext.setVisibility(GONE);
                pinIV.setVisibility(GONE);
                showBothMarkersOnMap();
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " lockSelectedLocation :- Exception" + e.getMessage());

        }
    }

    public void showBothMarkersOnMap() {

        try {
            if (mMap != null) {
                GoogleMap googleMap = mMap;
                googleMap.clear();
                List<Marker> markers = new ArrayList<>();

                if (mDriverWithMarkers != null && mDriverWithMarkers.size() > 0) {

                    for (int i = 0; i < mDriverWithMarkers.size(); i++) {

                        if (mDriverWithMarkers.get(i).getMarker() != null) {
                            markers.add(mDriverWithMarkers.get(i).getMarker());
                        } else {
                            Marker mark = mMap.addMarker(new MarkerOptions().icon(mTruckIcon).position(mDriverWithMarkers.get(i).getLatLng()).flat(true));
                            mDriverWithMarkers.get(i).setMarker(mark);
                            markers.add(mark);
                        }
                    }
                }

                markers.add(showMarkerOnMap(mPickUpLatLng, false));
                markers.add(showMarkerOnMapForToLocation(mDropLatLng, false));

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                try {
                    for (Marker marker : markers) {
                        try {
                            builder.include(marker.getPosition());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LatLngBounds bounds = builder.build();
                int padding = 50;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                googleMap.moveCamera(cu);
                googleMap.animateCamera(cu);
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showBothMarkersOnMap :- Exception" + e.getMessage());

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {

                if (requestCode == FROM_PLACE_PICKER_REQUEST) {

                    try {
                        Place place = PlacePicker.getPlace(data, this);
                        mPickUpLatLng = place.getLatLng();
                        TextToSpeechHelper.speakOut(HomeActivity.this, getString(R.string.src_loc_selected));
                        mPickUpLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                        mDropLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                        fromLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                        toLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                        pinIV.setImageResource(R.drawable.source2);
                        mFromAddress = place.getAddress().toString();
                        mPickUpLocationTV.setText(mFromAddress);
                        TextToSpeechHelper.speakOut(HomeActivity.this, "Lock the pick up location");
                        showMarkerOnMap(mPickUpLatLng, false);
                    } catch (Exception e) {
                        LogUtils.appendLog(HomeActivity.this, TAG + " onActivityResult :- Exception" + e.getMessage());
                    }

                } else if (requestCode == TO_PLACE_PICKER_REQUEST) {

                    try {
                        Place place = PlacePicker.getPlace(data, this);
                        mDropLatLng = place.getLatLng();
                        TextToSpeechHelper.speakOut(HomeActivity.this, "Drop location is selected");
                        mDropLocationTV.setPadding(MAX_PADDING, MAX_PADDING, MAX_PADDING, MAX_PADDING);
                        mPickUpLocationTV.setPadding(MIN_PADDING, MIN_PADDING, MIN_PADDING, MIN_PADDING);
                        toLocationRL.setBackgroundResource(R.drawable.selected_location_rounded_corner_map);
                        fromLocationRL.setBackgroundColor(Color.parseColor(UN_SELECTED_COLOR));
                        pinIV.setImageResource(R.drawable.destination2);
                        mToAddress = place.getAddress().toString();
                        mDropLocationTV.setText(mToAddress);
                        showMarkerOnMapForToLocation(mDropLatLng, false);//true;
                        TextToSpeechHelper.speakOut(HomeActivity.this, "Lock the drop location");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onActivityResult :- Exception" + e.getMessage());

        }

    }

    public Marker showMarkerOnMapForToLocation(@NonNull LatLng location, boolean callGetAdreessASync) {
        try {
            pinIV.setImageResource(R.drawable.destination2);
            // pinIV.setVisibility(View.VISIBLE);
            // mLocationLLL.setVisibility(View.VISIBLE);


            if (location.latitude != 0.0 && location.longitude != 0.0) {

                if (callGetAdreessASync) {
                    mHomeViewPresenter.getAddressFromLatLng(location.latitude + "," + location.longitude);

                }


                try {

                    if (mMap != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), MAP_CAMERA_ZOOM_ANIMATION));

                        if (mToMarker != null) {
                            mToMarker.remove();
                        }

                        MarkerOptions markerForToLocation = new MarkerOptions();
                        markerForToLocation.position(location);
                        markerForToLocation.title(mToAddress);
                        markerForToLocation.icon(mDestinationIcon);

                        if (mIsDropLocationLocked) {//22022018
                            mToMarker = mMap.addMarker(markerForToLocation);
                            //return mToMarker;
                        }


/*
                       if(mToMarker==null){

                           if(mIsDropLocationLocked){
                               MarkerOptions markerForToLocation = new MarkerOptions();
                               markerForToLocation.position(location);
                               markerForToLocation.title(mToAddress);
                               markerForToLocation.icon(mDestinationIcon);
                               mToMarker = mMap.addMarker(markerForToLocation);

                           }
                       }else{
                           mToMarker.setPosition(location);
                           mToMarker.setTitle(mToAddress);
                       }*/
                    }
                } catch (Exception e) {
                    LogUtils.appendLog(HomeActivity.this, TAG + " showMarkerOnMapForToLocation1 :- Exception" + e.getMessage());
                }

            }

        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " showMarkerOnMapForToLocation :- Exception" + e.getMessage());

        }

        return mToMarker;
    }

    @Override
    public void onBackPressed() {

        try {
            if (isNavDrawerOpened) {
                super.onBackPressed();
                return;
            }
            if (mProgressDialog.getVisibility() == View.VISIBLE) {
                return;
            }

            if (mOnBackPressStatus != null) {
                switch (mOnBackPressStatus) {
                    case FINISH_ACTIVITY:
                        Utility.existanceApplication(HomeActivity.this);
                        break;
                    case ENABLE_BOOKING:

                        rebook();

                        break;
                    case DISABLE_ON_BACK_PRESS:

                        break;
                }
            }
        } catch (Exception e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " onBackPressed :- Exception" + e.getMessage());
        }
    }


    @Override
    public void update(Observable o, final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = (Map<String, String>) arg;
                final String bodyMessage = map.get(BODY_KEY);
                final String bookingNo = map.get(BOOKING_NO_KEY);

                try {
                    if(bodyMessage!=null && bodyMessage.length()>0){
                        switch (bodyMessage) {
                            case BOOKING_CONFIRMED:

                                try {
                                    bookingConfirmed(bookingNo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case BOOKING_FAILED:
                                break;
                            case BOOKING_CANCELLED_BY_DRIVER:
                                try {
                                    if (mIsDriverDetailsAvailable) {
                                        bookingCancelledByDriver(bookingNo);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case TRIP_STARTED:
                                try {
                                    if (mIsDriverDetailsAvailable)
                                        tripStarted(bookingNo);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case TRIP_END:
                                try {
                                    if (mIsDriverDetailsAvailable)
                                        tripEnd(bookingNo);
                                } catch (Exception e) {
                                    Log.e(TAG, "Exception " + e.getMessage());
                                }

                                break;
                            case DRIVER_REACHED_PICK_UP_LOCATION:

                                if (mIsDriverDetailsAvailable)
                                    reachedPickup();

                                break;
                            case DRIVER_REACHED_DROP_LOCATION:

                                if (mIsDriverDetailsAvailable)
                                    reachedDeliverLoc();
                                break;
                            case INVOICE_GENERATED:
                                break;
                            case ABOUTTOREACHPICKUPLOCATION:
                                if (mIsDriverDetailsAvailable)
                                    aboouToReach();
                                break;
                            case GENERATE_INVOICE:
                                try {
                                    ToastHelper.showToastLenShort(HomeActivity.this, "Payment done successfully");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void bookingCancelledByDriver(String bno) {

        try {
            mBookingState = 0;
            CredentialManager.setBookingState(HomeActivity.this, 0);
            rebook();

        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        if (mBookingState != 106)*/
        {
            //   mBookingState = 106;
            CredentialManager.setBookingNo(HomeActivity.this, "");

            AlertDialogActivity.showAlertDialogActivity(HomeActivity.this, "Cancelled",
                    "Your booking with " + bno + " is cancelled by driver. Sorry for the inconvenience. Please try again", "Ok", null,
                    new AlertDialogActivity.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick() {

                        }
                    }, null);
        }

    }

    private void rebook() {

        mIsReBook = true;

        if (mTCFragment == null && mBookingFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mTCFragment = new TruckCateogeoriesFragment();
            fragmentTransaction.add(R.id.gridLayout_fragment, mTCFragment);
            mBookingFragment = new BookingFragment();
            fragmentTransaction.add(R.id.booknow_linear_fragment, mBookingFragment);
            fragmentTransaction.commitAllowingStateLoss();

            //getNearByTrucks();


        }


        if (mTCFragment != null) {
            mTCFragment.visibleTruckLayout();
            mTCFragment.inVisibleRateCardLayout();
        }

        if (mBookingFragment != null) {
            mBookingFragment.visibleBookNowAndLater();
            mBookingFragment.inVisibleConfirmBooking();
            mBookingFragment.inVisibleCancelBooking();
        }

        mLocationLLL.setVisibility(View.VISIBLE);

        if (mDriverDetailsFragment != null)
            removeFragment(mDriverDetailsFragment);

        if (mMap != null) {
            mMap.clear();

            if (mNearTrucks != null)
                showNearByTrucksonMap(mNearTrucks);

            mBookNowIsClicked = false;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mPickUpLatLng);
            markerOptions.title(mFromAddress);
            markerOptions.icon(mSourceIcon);
            mMap.addMarker(markerOptions);
            markerOptions.position(mDropLatLng);
            markerOptions.title(mToAddress);
            markerOptions.icon(mDestinationIcon);
            mMap.addMarker(markerOptions);
        }

    }


    private void tripEnd(final String bno) {

        try {
            mBookingState = 105;

            CredentialManager.setShowingLiveUpdateMarker(HomeActivity.this, false);
            CredentialManager.setIsInTrip(HomeActivity.this, false);

            if (mDriverDetailsFragment != null) {
                mDriverDetailsFragment.onTripEndByDriverListener();
            }

            AlertDialogActivity.showAlertDialogActivity(HomeActivity.this, "Trip completed",
                    "Trip completed successfully. Please proceed for payment options.", "Ok", null,
                    new AlertDialogActivity.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick() {
                            try {
                                mBookingState = 0;
                                CredentialManager.setDriverId(HomeActivity.this, "");
                                CredentialManager.setBookingState(HomeActivity.this, 0);
                                Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
                                intent.putExtra("bookingNo", bno);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                Log.e(TAG, "Exception " + e.getMessage());
                            }
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void reachedDeliverLoc() {

        try {
            mBookingState = 104;
            CredentialManager.setShowingLiveUpdateMarker(HomeActivity.this, false);

            if (mDriverDetailsFragment != null)
                mDriverDetailsFragment.onDriverReachedDropedLocationListener();

            try {
                AlertDialogActivity.showAlertDialogActivity(HomeActivity.this, "Delivery location",
                        "Driver reached delivery location", "Ok", null,
                        new AlertDialogActivity.OnPositiveBtnClickListener() {
                            @Override
                            public void onPositiveBtnClick() {
                            }
                        }, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tripStarted(String bno) {

        try {
            /*  if (mBookingState != 103)*/
            {
                mBookingState = 103;
                CredentialManager.setIsInTrip(HomeActivity.this, true);

                if (mDriverDetailsFragment != null)
                    mDriverDetailsFragment.onTripStartedByDriverListener();

                CredentialManager.setShowingLiveUpdateMarker(HomeActivity.this, true);
                //mWatchDriverComing = true;
                mIsTripStartedByDriver = true;

                AlertDialogActivity.showAlertDialogActivity(HomeActivity.this, "Trip started",
                        bno + " Trip started and the vehicle is on the way to delivery location.", "Ok", null,
                        new AlertDialogActivity.OnPositiveBtnClickListener() {
                            @Override
                            public void onPositiveBtnClick() {
                            }
                        }, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reachedPickup() {
        try {
            /*  if (mBookingState != 102)*/
            {
                mBookingState = 102;
                if (mDriverDetailsFragment != null)
                    mDriverDetailsFragment.onDriverReachedPickupLocationListener();
                try {

                    AlertDialogActivity.showAlertDialogActivity(HomeActivity.this, "Pick up location",
                            "Driver has reached pick up location", "Ok", null,
                            new AlertDialogActivity.OnPositiveBtnClickListener() {
                                @Override
                                public void onPositiveBtnClick() {
                                }
                            }, null);
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bookingConfirmed(String bno) {
        /* if (mBookingState != 100)*/
        {
            CredentialManager.setIsInTrip(HomeActivity.this, false);
            mBookingState = 100;
            CredentialManager.setBookingNo(HomeActivity.this, bno);
            mHomeViewPresenter.getDriverDetails(bno, false, true);

        }
    }

    private void aboouToReach() {
        try {
            mBookingState = 101;
            CredentialManager.setShowingLiveUpdateMarker(HomeActivity.this, true);
            //mWatchDriverComing = true;
            mIsTripStartedByDriver = false;

            if (mDriverDetailsFragment != null)
                mDriverDetailsFragment.onDriverAboutToReachedPickupLocationListener();

            AlertDialogActivity.showAlertDialogActivity(HomeActivity.this, "Pick up location",
                    "Driver is about to reach pick up location", "Ok", null,
                    new AlertDialogActivity.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClick() {

                        }
                    }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void monitorDriver(DriverLocationUpdates driverCurrentLocation) {
        try {
            Location location = new Location("");
            double lat = driverCurrentLocation.getDriverMonitorList().getCurrentLat();
            double lng = driverCurrentLocation.getDriverMonitorList().getCurrentLong();
            location.setLatitude(lat);
            location.setLongitude(lng);
            onTruckLocationUpdated(location);


            if (!mIsTripStartedByDriver) {
                if (mPickUpLatLng != null && mPickUpLatLng.latitude != 0.0 && mPickUpLatLng.longitude != 0.0) {
                    getTravelBetwTwoLocations(mPickUpLatLng.latitude + "," + mPickUpLatLng.longitude, lat + "," + lng, true);
                }
            }
        } catch (NumberFormatException e) {
            LogUtils.appendLog(HomeActivity.this, TAG + " updateDriverLocation:- Exception" + e.getMessage());

        }
    }


    private void onTruckLocationUpdated(final Location location) {

        if (location != null) {

            if (mPreviousLocation != null && mPreviousLocation.getLatitude() == location.getLatitude() && mPreviousLocation.getLongitude() == location.getLongitude())
                return;

            final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            LogUtils.appendLog(HomeActivity.this, "Current Lat Lng " + location.getLatitude() + "," + location.getLongitude());

            if (mPreviousLocation == null) {
                mPreviousLocation = location;
            } else {
                mBearing = mPreviousLocation.bearingTo(location);
                mPreviousLocation = location;
            }

            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            animateMarkerLocationToGBP(mTruckMarker, latLng/*locationTOLatLng(location)*/);
                        } catch (Exception e) {
                            LogUtils.appendLog(HomeActivity.this, TAG + " onTruckLocationUpdated1:- Exception" + e.getMessage());

                        }
                    }
                });
            } catch (Exception e) {
                LogUtils.appendLog(HomeActivity.this, TAG + " onTruckLocationUpdated1:- Exception" + e.getMessage());
            }
        }
    }

    private void animateMarkerLocationToGBP(final Marker marker, final LatLng finalPosition) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();
        final float durationInMs = 2500;

        handler.post(new Runnable() {
            long elapsed;
            float time;
            float v;

            @Override
            public void run() {

                try {
                    elapsed = SystemClock.uptimeMillis() - start;
                    time = elapsed / durationInMs;
                    v = interpolator.getInterpolation(time);

                    LatLng currentPosition = new LatLng(startPosition.latitude * (1 - time) + finalPosition.latitude * time, startPosition.longitude * (1 - time) + finalPosition.longitude * time);
                    marker.setPosition(currentPosition/*latLngInterpolator.interpolate(v, startPosition, finalPosition)*/);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(finalPosition)
                            .zoom(mZoomingValue)
                            .tilt(TILT_THIRTY)
                            .bearing(ROTATION_ANGLE/*mBearing*/)
                            .build();

                    if (mMap != null)
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    // marker.setRotation(mBearing + 75);
                    marker.setRotation(mBearing);

                    // marker.setRotation(getRotation(finalPosition, startPosition));

                    if (time < 1) {
                        /* LogUtils.appendLog(HomeActivity.this, TAG + " time " + time);*/
                        handler.postDelayed(this, 32);
                    }

                } catch (Exception e) {
                    LogUtils.appendLog(HomeActivity.this, TAG + " animateMarkerLocationToGBP:- Exception" + e.getMessage());
                }
            }
        });
    }

}
