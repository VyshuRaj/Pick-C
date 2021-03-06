package com.prts.pickcustomer.home;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by LOGICON on 02-01-2018.
 */

public interface LatLngInterpolator {
    LatLng interpolate(float fraction, LatLng a, LatLng b);
    class LinearFixed implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
            double lngDelta = b.longitude - a.longitude;
            // Take the shortest path across the 180th meridian.
            if (Math.abs(lngDelta) > 180) {
                lngDelta -= Math.signum(lngDelta) * 360;
            }
            double lng = lngDelta * fraction + a.longitude;
            return new LatLng(lat, lng);
        }
    }


}
