package com.infoservices.lue.condomanagement.ClassifAdvetisment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.infoservices.lue.condomanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdsShow extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView mlistads;
    ImageView mbackimage;
    private ProgressDialog pDialog;
    private AdsAdapter adapter;
    JSONArray array= null;
    List<pojo> movieList = new ArrayList<pojo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_show);

        mlistads = (ListView) findViewById(R.id.list_ads);
        mbackimage = (ImageView) findViewById(R.id.back_ads);
        mbackimage.setOnClickListener(this);

        Intent intent = getIntent();

        String jsonArray = intent.getStringExtra("mylist");

        try {
             array = new JSONArray(jsonArray);
            System.out.println(array.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < array.length(); i++) {


            JSONObject obj = null;
            try {
                obj = array.getJSONObject(i);
                String imagestr = obj.getString("image");
                String[] parts = imagestr.split(",");
                String part1 = parts[0];
                String part2 = parts[1];

                String imagrurl = "http://api.condoassist2u.com/classified_ad/";
                String imagrurl1 = imagrurl+part1;
                String imagrurl2 = imagrurl+part2;

                pojo movie = new pojo();
                movie.setTitle(obj.getString("description"));
                movie.setThumbnailUrl(imagrurl1);
                movie.setRating(obj.getString("state_name"));

                movie.setYear(obj.getString("cat_name"));
                movie.setGenre(obj.getString("area_name"));
                movie.setImage2(imagrurl2);

                movieList.add(movie);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
            Log.d("value1230", String.valueOf(movieList));
        adapter = new AdsAdapter(AdsShow.this, movieList);
        adapter.notifyDataSetChanged();
        mlistads.setAdapter(adapter);

        mlistads.setOnItemClickListener(this);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        pojo mo123 = (pojo) parent.getItemAtPosition(position);

        Intent adsdetailintnt = new Intent(getApplicationContext(),AdsDetailShow.class);
        adsdetailintnt.putExtra("image1",mo123.getThumbnailUrl());
        adsdetailintnt.putExtra("image2",mo123.getImage2());
        adsdetailintnt.putExtra("description",mo123.getTitle());
        adsdetailintnt.putExtra("statename",mo123.getRating());
        adsdetailintnt.putExtra("catname",mo123.getYear());
        adsdetailintnt.putExtra("areaname",mo123.getGenre());
        //  newsdetailintnt.putExtra("caption",movie.);
        startActivity(adsdetailintnt);


    }

    @Override
    public void onClick(View v) {

        AdsShow.this.finish();

    }





}
