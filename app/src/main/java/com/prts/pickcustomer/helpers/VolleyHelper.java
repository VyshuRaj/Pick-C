package com.prts.pickcustomer.helpers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyHelper {
    private static RequestQueue mRequestQueue = null;

    public static RequestQueue getVolleyInstance(Context context) {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }
}
