package com.prts.pickcustomer.about;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;


import com.prts.pickcustomer.BuildConfig;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.trucks.TrucksGridAdapter;
import com.prts.pickcustomer.trucks.VehicleGroupType;
import com.prts.pickcustomer.trucks.VehicleType;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            PickCCustomTextVIew textVIew = (PickCCustomTextVIew) findViewById(R.id.versionTV);
            TextView titleTv = (TextView) findViewById(R.id.title_appName_textView);
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            textVIew.setText(String.format("Version %s", versionName));

            try {
                Typeface limeLightFontFace = Typeface.createFromAsset(getAssets(), "fonts/limelight.ttf");
                titleTv.setTypeface(limeLightFontFace);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
