package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.prts.pickcustomer.invoice.FromAddress;
import com.prts.pickcustomer.invoice.ToAddress;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by satya on 16-Dec-17.
 */

public class GoogleMapHelper {

    public static List<LatLng> decodeLines(String encoded) {
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

    public static FromAddress getFromAddress(Context context, LatLng latLng) {
        FromAddress fromAddress = null;
        List<Address> addresses = getAdress(context,latLng);

        if (addresses != null && addresses.size() > 0) {
            fromAddress = new FromAddress();
            fromAddress.setAddres(addresses.get(0).getSubLocality());
            fromAddress.setCityName(addresses.get(0).getLocality());
            fromAddress.setStateName(addresses.get(0).getAdminArea());
            fromAddress.setGetPostalCode(addresses.get(0).getPostalCode());
        }
        return fromAddress;


    }

    public static ToAddress getToAddress(Context context, LatLng latLng) {
        ToAddress toAddress = null;

        List<Address> addresses = getAdress(context,latLng);

        if (addresses != null && addresses.size() > 0) {
            toAddress = new ToAddress();
            toAddress.setAddres(addresses.get(0).getSubLocality());
            toAddress.setCityName(addresses.get(0).getLocality());
            toAddress.setStateName(addresses.get(0).getAdminArea());
            toAddress.setGetPostalCode(addresses.get(0).getPostalCode());
        }
        return toAddress;

    }

    public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
       // Log.d(TAG, StartP + " CalculationByDistance: " + EndP);
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "CalculationByDistance " + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        //return Radius * c;
        return kmInDec;
    }

    public static List<Address> getAdress(Context context, LatLng latLng) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            return addresses;

        } catch (IOException e) {
            return null;
        }


    }
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";





    public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
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

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
