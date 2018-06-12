package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by LOGICON on 04-01-2018.
 */

public class MapHelper {
    final static String DIRECTION_KEY = "AIzaSyBt7Y0WDy9nka0JrV_gZkLIt-zTRfbf_ZE";//"AIzaSyBt7Y0WDy9nka0JrV_gZkLIt-zTRfbf_ZE";
    final static String DISTANCE_API_KEY = "AIzaSyDZzsUIdzyATngclxURzHTX9ijd_GlpuV0";
    private static List<LatLng> latLngs;

    public static String getDirectionUrl(LatLng pickUp, LatLng drop) {
        String requestUrl = null;
        try {
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"
                    + "transit_routing_preference=less_driving&"
                    + "origin=" + pickUp.latitude + "," + pickUp.longitude + "&"
                    + "destination=" + drop.latitude + "," + drop.longitude + "&"
                    + "key=" + DIRECTION_KEY;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestUrl;
    }

    public static String getNearByPlacesUrl(LatLng pickUp, LatLng drop) {
        String requestUrl = null;
        try {
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"
                    + "transit_routing_preference=less_driving&"
                    + "origin=" + pickUp.latitude + "," + pickUp.longitude + "&"
                    + "destination=" + drop.latitude + "," + drop.longitude + "&"
                    + "key=" + DIRECTION_KEY;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestUrl;
    }

    public static Location getLocation(LatLng pickUp) {
        Location location = new Location("");
        location.setLatitude(pickUp.latitude);
        location.setLatitude(pickUp.longitude);
        return location;
    }


    public static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static PolylineOptions getPolyLine(List<LatLng> latLngs) {

        PolylineOptions polylineOptions = new PolylineOptions();
        if (latLngs != null && latLngs.size() > 0) {
            polylineOptions.addAll(latLngs);
            polylineOptions.width(10);
            polylineOptions.color(Color.parseColor("#fff200"));
        }
        return polylineOptions;
    }

    public static String getAddress(Context context, LatLng latLng) {
        String address = null;
        try {
            List<Address> addresses = getAdress(context, latLng);

            address = "";
            if (addresses != null && addresses.size() > 0) {


                address=addresses.get(0).getAddressLine(0);

                /*
                *
                * [{"mAddressLines":{"0":"201, Jai Hind Gandhi Rd, VIP Hills, Jaihind Enclave, Madhapur, Hyderabad,
                * Telangana 500081, India"},"mAdminArea":"Telangana","mCountryCode":"IN","mCountryName":"India",
                * "mFeatureName":"201","mHasLatitude":true,"mHasLongitude":true,"mLatitude":17.4507718,
                * "mLocale":"en_US","mLocality":"Hyderabad","mLongitude":78.3872829,"mMaxAddressLineIndex":0,
                * "mPostalCode":"500081","mSubAdminArea":"Ranga Reddy",
                * "mSubLocality":"Madhapur","mSubThoroughfare":"201","mThoroughfare":"Jai Hind Gandhi Road"}]*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;


    }

    private static List<Address> getAdress(Context context, LatLng latLng) {
        try {
            List<Address> addresses = null;
            if (latLng!=null) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
            }

            return addresses;

        } catch (IOException e) {
            return null;
        }


    }
}
