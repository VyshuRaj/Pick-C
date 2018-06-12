package com.prts.pickcustomer.referral;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.prts.pickcustomer.R;

public class ReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);

    }

    public void termsAndCond(View view) {

        Intent intent=new Intent(ReferralActivity.this,AboutReferralActivity.class);
        startActivity(intent);
    }

    public void addRefery(View view) {
        Intent intent=new Intent(ReferralActivity.this,AddFriendActivity.class);
        startActivity(intent);
    }
}
