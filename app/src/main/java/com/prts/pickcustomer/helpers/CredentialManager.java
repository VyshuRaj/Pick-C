package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.prts.pickcustomer.home.HomeActivity.ANNOUNCEMENT_SHARED_PREF_KEY;

/**
 * Created by Uday on 15-08-2016.
 */
public class CredentialManager {

    private static final String CREDNTIALS_SHARED_PREF_NAME = "CRED_SHARE_PREF";
    private static final String AUTH_TOKEN_SHARED_PREF_KEY = "Auth_token_key";
    private static final String MOBILE_NO_SHARED_PREF_KEY = "Mobile_No_key";

    public static final String AUTH_TOKEN_HEADER_KEY = "AUTH_TOKEN";
    public static final String MOBILE_NO_HEADER_KEY = "MOBILENO";
    private static final String TAG = "CredentialManager";


    private static final String BOOKING_NO_SHARED_PREF_KEY = "booking_no";
    private static final String TRIP_ID_SHARED_PREF_KEY = "trip_id";

    private static final String NAME_SHARED_PREF_KEY = "name";
    private static final String MAIL_ID_SHARED_PREF_KEY = "email";


    private static final String DRIVER_ID_SHARED_PREF_KEY = "driver_id";
    private static final String TO_LAT_SHARED_PREF_KEY = "to_lat";
    private static final String TO_LONG_SHARED_PREF_KEY = "to_long";
    private static final String PASSWORD_SHARED_PREF_KEY = "password";
    private static final String DEVICE_ID_SHARED_PREF_KEY = "device_id";

    public static String getAuthToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(AUTH_TOKEN_SHARED_PREF_KEY, null);
    }

    public static void setVolumeStatus(Context context, boolean isOnMode) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ANNOUNCEMENT_SHARED_PREF_KEY, isOnMode);
        editor.apply();
    }

    public static boolean getVolumeStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(ANNOUNCEMENT_SHARED_PREF_KEY, false);
    }


    public static String getMobileNO(Context context) {
        if (context == null) {
            return null;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(MOBILE_NO_SHARED_PREF_KEY, null);
    }

    public static void setAuthToken(Context context, @NonNull String authToken) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN_SHARED_PREF_KEY, authToken);
        editor.apply();
    }

    public static void setMobileNumber(Context context, @NonNull String mobileNo) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MOBILE_NO_SHARED_PREF_KEY, mobileNo);
        editor.apply();
    }

    public static void clearAuthToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN_SHARED_PREF_KEY, null);
        editor.apply();
    }

    public static void clearMobileNumber(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MOBILE_NO_SHARED_PREF_KEY, null);
        editor.apply();
    }

    public static void setDriverName(Context context, String driverName) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("driverName", driverName);
        editor.apply();
    }

    public static String getDriverName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString("driverName", "");
    }

    public static Map<String, String> getHeaders(Context context/*, boolean isContentTypeRequired*/) {
        Map<String, String> params = new HashMap<>();
        params.put(AUTH_TOKEN_HEADER_KEY, getAuthToken(context));
        params.put(MOBILE_NO_HEADER_KEY, getMobileNO(context));
        params.put("Content-Type", "application/json");
        Log.e("TAG", "getHeaders(mContext) " + params);
        return params;
    }

    public static Map<String, String> getContentType() {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json");
        return params;
    }

    public static void setBookingNo(Context context, @NonNull String bookingNO) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOOKING_NO_SHARED_PREF_KEY, bookingNO);
        editor.apply();
    }

    public static String getBookingNO(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(BOOKING_NO_SHARED_PREF_KEY, null);
    }

    public static void setTripID(Context context, @NonNull String tripID) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TRIP_ID_SHARED_PREF_KEY, tripID);
        editor.apply();
    }

    public static String getTripID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(TRIP_ID_SHARED_PREF_KEY, null);
    }

    public static void setName(Context context, @NonNull String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME_SHARED_PREF_KEY, name);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(NAME_SHARED_PREF_KEY, null);
    }

    public static void setEmailId(Context context, String email) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MAIL_ID_SHARED_PREF_KEY, email);
        editor.apply();
    }

    public static String getEmailId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(MAIL_ID_SHARED_PREF_KEY, null);
    }

    public static void setDriverId(Context context, @NonNull String driverId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DRIVER_ID_SHARED_PREF_KEY, driverId);
        editor.apply();
    }

    public static String getDriverId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(DRIVER_ID_SHARED_PREF_KEY, null);
    }

    public static void setToLat(Context context, @NonNull double toLat) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TO_LAT_SHARED_PREF_KEY, toLat + "");
        editor.apply();
    }

    public static double getToLat(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String toLat = sharedPreferences.getString(TO_LAT_SHARED_PREF_KEY, null);
        if (toLat != null) {
            return Double.parseDouble(toLat);
        } else {
            return 0;
        }
    }

    public static void setToLong(Context context, @NonNull double toLong) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TO_LONG_SHARED_PREF_KEY, toLong + "");
        editor.apply();
    }

    public static double getToLong(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String toLong = sharedPreferences.getString(TO_LONG_SHARED_PREF_KEY, null);
        if (toLong != null) {
            return Double.parseDouble(toLong);
        } else {
            return 0;
        }
    }

    public static void setPassword(Context context, @NonNull String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD_SHARED_PREF_KEY, password);
        editor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(PASSWORD_SHARED_PREF_KEY, null);
    }


    public static void setSelectedVehicleGroupID(Context context, @NonNull int SelectedVehicleGroupID) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SelectedVehicleGroupID", SelectedVehicleGroupID);
        editor.apply();
    }

    public static int getSelectedVehicleGroupID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getInt("SelectedVehicleGroupID", 0);
    }

    public static void setSelectedVehicleTypeID(Context context, @NonNull int SelectedVehicleTypeID) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SelectedVehicleTypeID", SelectedVehicleTypeID);
        editor.apply();
    }

    public static int getSelectedVehicleTypeID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getInt("SelectedVehicleTypeID", 0);
    }

    public static void setSelectedTruckWeightDesc(Context context, @NonNull String selectedTruckWeightDesc) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SelectedTruckWeightDesc", selectedTruckWeightDesc);
        editor.apply();
    }

    public static String getSelectedTruckWeightDesc(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString("SelectedTruckWeightDesc", null);
    }

    public static void setIsBookingLater(Context context, @NonNull boolean isBookingLater) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsBookingLater", isBookingLater);
        editor.apply();
    }

    public static boolean getIsBookingLater(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean("IsBookingLater", false);
    }

    public static void setLoadingUnloadingStatus(Context context, @NonNull int SelectedVehicleTypeID) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("LoadingUnloadingStatus", SelectedVehicleTypeID);
        editor.apply();
    }

    public static int getLoadingUnloadingStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getInt("LoadingUnloadingStatus", 0);
    }

    public static void setCallBookNowAPI(Context context, @NonNull boolean callBookNowAPI) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CallBookNowAPI", callBookNowAPI);
        editor.apply();
    }

    public static boolean getCallBookNowAPI(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean("CallBookNowAPI", false);
    }

    public static void setFromoLat(Context context, @NonNull double fromLat) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FROMLAT", fromLat + "");
        editor.apply();
    }

    public static double getFromoLat(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String fromlat = sharedPreferences.getString("FROMLAT", null);
        if (fromlat != null) {
            return Double.parseDouble(fromlat);
        } else {
            return 0;
        }
    }

    public static void setFromLong(Context context, @NonNull double fromLong) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FROMLNG", fromLong + "");
        editor.apply();
    }

    public static double getFromLong(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String fromlng = sharedPreferences.getString("FROMLNG", null);
        if (fromlng != null) {
            return Double.parseDouble(fromlng);
        } else {
            return 0;
        }
    }

    public static void setVehicleType(Context context, @NonNull int vehicleType) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("VEHCILETYPE", vehicleType);
        editor.apply();
    }

    public static int getVehicleType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getInt("VEHCILETYPE", 1300);
    }

    public static void setPresentState(Context context, @NonNull boolean show) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("splash", show);
        editor.apply();
    }


    public static boolean getPresentState(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean("splash", false);

    }

    public static void setShowingLiveUpdateMarker(Context context, @NonNull boolean show) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d(TAG, "getShowingLiveUpdateMarker: " + show);
        editor.putBoolean("ShowingLiveUpdateMarker", show);
        editor.apply();
    }

    public static boolean getShowingLiveUpdateMarker(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean("ShowingLiveUpdateMarker", false);
    }


    public static void setDriverRating(Context context, String rating) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DRIVER_RATING", rating);
        editor.apply();
    }

    public static String getDriverRating(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString("DRIVER_RATING", "0");
    }

    public static void setLatlNgs(Context context, ArrayList<LatLng> mPolyLines) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latlngs", mPolyLines.toString());
        editor.apply();
    }


    public static void setAppInBackground(Context context, boolean b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isInBg", b);
        editor.apply();
    }

    public static boolean isAppInBackground(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean("isInBg", false);

    }

    public static void setIsInTrip(Context context, boolean b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isInTrip", b);
        editor.apply();
    }

    public static boolean isInTrip(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean("isInTrip", false);

    }

    public static void setFirstTimeEntryCount(Context context, int cnt) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cnt", cnt);
        editor.apply();
    }

    public static int getEntryCount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getInt("cnt", 1);

    }

    public static void setBookingState(Context context, int mBookingState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("bookingState", mBookingState);
        editor.apply();
    }

    public static int getBookingState(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getInt("bookingState", 0);

    }


}




