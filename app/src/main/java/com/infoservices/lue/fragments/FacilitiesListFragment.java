package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.infoservices.lue.adapters.FacilitiesAdapter;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.FacilitiesEntity;
import com.infoservices.lue.utills.LoadApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacilitiesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView listView;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    public static final String CITY_LOCATION = "citylocation";
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AlertDialog alertDialog;
    AsyncLoadData asyncLoad;
    SwipeRefreshLayout swipeRefreshLayout;
ArrayList<FacilitiesEntity> facilitiesEntities=new ArrayList<FacilitiesEntity>();
    public FacilitiesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facilities_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        loadData();
                                    }
                                }
        );

    }



        @SuppressLint("NewApi")
        Handler mHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                // switch case
                switch (msg.what) {
                    case SHOW_PROG_DIALOG:
                        swipeRefreshLayout.setRefreshing(true);
                        break;

                    case HIDE_PROG_DIALOG:
                        swipeRefreshLayout.setRefreshing(false);

                        break;

                    case LOAD_QUESTION_SUCCESS:

                        break;

                    default:
                        break;
                }

                return false;
            }
        });

    @Override
    public void onRefresh() {
        facilitiesEntities.clear();
        loadData();
    }


    class AsyncLoadData extends AsyncTask<String, Void, String> {
            boolean flag = false;

            @Override
            protected String doInBackground(String... params) {
                String sts="";
                Log.d("inside ", " do in back above");

                mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
                progress_dialog_msg = "loading...";
                LoadApi api = new LoadApi();

                // if(flagValue==0){
                try {
                    // rest_id="76";
                    // Log.d("rest_id back", " " + rest_id);
                    sts = api.facilitiesLoader(params);
                    Log.d("menu_list", " " + facilitiesEntities.size());
                } catch (Exception e) {
                    // result1="Please select image";
                    e.printStackTrace();

                }
                // }
                return sts;
            }

            @TargetApi(Build.VERSION_CODES.DONUT)
            @SuppressLint("NewApi")
            @Override
            protected void onPostExecute(String result) {

                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

                try {
                    JSONObject jobject1 = new JSONObject(result);
                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(jobject1.getString("info"));
                    Log.d("facility list", " below");
                    Log.d("jsonArray", "  " + jsonArray.toString());
                    Log.d("sizeeeem", " kk no size");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Log.d("111sizeeeem", "kk1111 "+jsonArray.length());
                        Log.d("111sizeeeem", "kk1111 " + jsonArray.length());
                        JSONObject jobject = jsonArray.getJSONObject(i);
                        FacilitiesEntity facilitiesEntity = new FacilitiesEntity();
                        facilitiesEntity.setId(jobject.getString("id"));
                        facilitiesEntity.setTitle(jobject.getString("title"));
                        facilitiesEntity.setPoint_1(jobject.getString("point_1"));
                        facilitiesEntity.setPoint_2(jobject.getString("point_2"));
                        facilitiesEntity.setPoint_3(jobject.getString("point_3"));
                        facilitiesEntity.setPhoto(jobject.getString("photo"));
                        facilitiesEntity.setProduct_id(jobject.getString("product_id"));
                        facilitiesEntity.setBooking_notice(jobject.getString("booking_notice"));
                        facilitiesEntity.setRate(jobject.getString("rate"));
                        facilitiesEntities.add(facilitiesEntity);
                    }
                    if (facilitiesEntities.size() != 0) {
                        Log.d("inside", " onPost");
                        listView = (ListView) getActivity().findViewById(R.id.list_facilities);
                        listView.setAdapter(new FacilitiesAdapter(getActivity(),facilitiesEntities));
                    }else{
                        Toast.makeText(getActivity(),"No facilities Avalable !!!",Toast.LENGTH_LONG  ).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Something went wrong !!!",Toast.LENGTH_LONG  ).show();
                }

            }

        }

    @SuppressLint("NewApi")
    private void loadData() {
        Log.d("inside ", " load data");
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            Log.d("inside ", " load data inside async");
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            asyncLoad.execute(sharedPreferences.getString( "mgnt_id","" ));
        }
    }

}
