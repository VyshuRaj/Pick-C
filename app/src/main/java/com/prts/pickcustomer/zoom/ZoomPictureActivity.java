package com.prts.pickcustomer.zoom;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.prts.pickcustomer.R;
import com.squareup.picasso.Picasso;

import static com.prts.pickcustomer.trucks.TruckCateogeoriesFragment.getTruckSelectedIconForRateCard;

public class ZoomPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Window window = this.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getSupportActionBar().hide();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        try {
            setContentView(R.layout.activity_zoom_picture);
            this.setFinishOnTouchOutside(false);
            Intent intent = getIntent();
            int selectedTruckID = intent.getIntExtra("selectedTruckID", 0);
            int selectedvehicleTypeID = intent.getIntExtra("selectedvehicleTypeID", 0);
            String url = getTruckSelectedIconForRateCard(selectedTruckID, selectedvehicleTypeID);
            ImageView  iv = (ImageView)findViewById(R.id.fullimage);
            ImageView  closeView = (ImageView) findViewById(R.id.close_rate_card_imageVIew);
            Picasso.with(ZoomPictureActivity.this).load(url).into(iv);
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
