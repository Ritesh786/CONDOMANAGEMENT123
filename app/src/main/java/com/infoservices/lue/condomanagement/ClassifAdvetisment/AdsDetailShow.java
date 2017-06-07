package com.infoservices.lue.condomanagement.ClassifAdvetisment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;
import com.squareup.picasso.Picasso;

public class AdsDetailShow extends AppCompatActivity implements View.OnClickListener {

    ImageView mimage1,mimage2;
    TextView mdescription,mstatename,mcatname,mareaname;
    ImageView mback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_detail_show);

        mimage1 = (ImageView) findViewById(R.id.imageview1);
        mimage2 = (ImageView) findViewById(R.id.imageview2);
        mback = (ImageView) findViewById(R.id.back_ads);
        mback.setOnClickListener(this);

        mdescription = (TextView) findViewById(R.id.description);
        mstatename = (TextView) findViewById(R.id.statename);
        mcatname = (TextView) findViewById(R.id.catgname);
        mareaname = (TextView) findViewById(R.id.areaname);

        Intent intent = getIntent();
        String image1  = intent.getStringExtra("image1");
        String image2  = intent.getStringExtra("image2");
        String description  = intent.getStringExtra("description");
        String statename  = intent.getStringExtra("statename");
        String catname  = intent.getStringExtra("catname");
        String areaname  = intent.getStringExtra("areaname");

        Picasso.with(getApplicationContext()).load(String.valueOf(image1)).resize(800, 800).into(mimage1);
        Picasso.with(getApplicationContext()).load(String.valueOf(image2)).resize(800, 800).into(mimage2);
        mdescription.setText(description);
        mstatename.setText(statename);
        mcatname.setText(catname);
        mareaname.setText(areaname);

    }

    @Override
    public void onClick(View v) {

        AdsDetailShow.this.finish();

    }
}
