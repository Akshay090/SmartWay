package com.idealtechcontrivance.ashish.smartway.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.idealtechcontrivance.ashish.smartway.R;

public class OfferDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        String url =getIntent().getStringExtra("offerId");
        Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
    }
}
