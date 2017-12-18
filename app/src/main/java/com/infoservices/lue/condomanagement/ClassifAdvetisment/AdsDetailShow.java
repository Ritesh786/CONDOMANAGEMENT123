package com.infoservices.lue.condomanagement.ClassifAdvetisment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.adapters.AndroidImageAdapternew;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.dealAndDiscount.RedeemActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdsDetailShow extends AppCompatActivity implements View.OnClickListener {

    ViewPager mimage1,mimage2;
    TextView mdescription,mstatename,mcatname,mareaname;
    ImageView mback;
    ArrayList<String> imgurl = new ArrayList<>();
    int index = 0;

    private int delay = 5000;
    private int page = 0;
    Handler handler1;
    Runnable runnable;

    AndroidImageAdapternew adapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_detail_show);

        mimage1 = (ViewPager) findViewById(R.id.imageview1);
        //  mimage2 = (ImageView) findViewById(R.id.imageview2);
        mback = (ImageView) findViewById(R.id.back_ads);
        mback.setOnClickListener(this);
        handler1=new Handler();
        mdescription = (TextView) findViewById(R.id.description);
        mstatename = (TextView) findViewById(R.id.statename);
        mcatname = (TextView) findViewById(R.id.catgname);
     //   mareaname = (TextView) findViewById(R.id.areaname);

        Intent intent = getIntent();
        String image1 = intent.getStringExtra("image1");
        String image2 = intent.getStringExtra("image2");
        String description = intent.getStringExtra("description");
        String statename = intent.getStringExtra("statename");
        String catname = intent.getStringExtra("catname");
       // String areaname = intent.getStringExtra("areaname");

        String imgstrurl = image2.replace("[", "");
        String newimgurl = imgstrurl.replace("]","");
        String newimguurrll = newimgurl.replace(" ","");

     //   final String s = "text1,text2,text3".replace(",", " ");

        imgurl = new ArrayList<String>(Arrays.asList(newimguurrll.split(",")));
//        imgurl.add("http://cloud9.condoassist2u.com/api/uploads/slideshow/slide1.jpg");
//        imgurl.add("http://cloud9.condoassist2u.com/api/uploads/slideshow/slide2.jpg");


        Log.d("kbnonon", "" + imgurl.toString());
        Log.d("kbnonon1234", "" + newimguurrll);

        //  Picasso.with(getApplicationContext()).load(String.valueOf(image1)).resize(800, 800).into(mimage1);
        //   Picasso.with(getApplicationContext()).load(String.valueOf(image2)).resize(800, 800).into(mimage2);
        mdescription.setText(description);
        mstatename.setText(statename);
        mcatname.setText(catname);
     //   mareaname.setText(areaname);


        mimage1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                page = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (imgurl.size() > 0) {
            //    listView.setVisibility(View.VISIBLE);
            //  nonet_ll_cot.setVisibility(View.GONE);
            //   Log.d("cotegory_list", "size: " + cotegory_list.size());
            adapterView = new AndroidImageAdapternew(getApplicationContext(), imgurl);
            mimage1.setAdapter(adapterView);
            runnable = new Runnable() {
                public void run() {
                    if (adapterView.getCount() == page) {
                        page = 0;
                    } else {
                        page++;
                    }
                    mimage1.setCurrentItem(page, true);
                    handler1.postDelayed(this, delay);
                }
            };


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler1.removeCallbacks(runnable);
    }


    @Override
    public void onResume() {
        super.onResume();
        handler1.postDelayed(runnable, delay);
    }


    @Override
    public void onClick(View v) {

        AdsDetailShow.this.finish();

    }

//    private void startSlideShow() {
////		// TODO Auto-generated method stub
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                index++;
//                if (index >= imgurl.size())
//                    index = 0;
//                final String url = imgurl.get(index).toString();
//                System.out.println("url....." + url);
//                if (url != null && url.length() > 0) {
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            //downloader.download(url, mImgView);
//                            Picasso.with(AdsDetailShow.this)
//                                    .load(url)
//                                    //	.placeholder(R.drawable.lobo2)   // optional
//                                    .error(R.drawable.lobo2)     // optional
//                                    .into(mimage1);
//
//                        }
//                    });
//                }
//            }
//        };
//
//        if (imgurl.size() > 1)
//            timer.scheduleAtFixedRate(task, 3000, 3000);
//
//
//    }

}
