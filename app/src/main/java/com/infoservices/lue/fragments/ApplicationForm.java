package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.DownloadPdfActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.ApplicationFormEntity;
import com.infoservices.lue.entity.ImportantContactEntity;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.LoadApi;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationForm extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    SnapedPhotoAdapter snapedPhotoAdapter;
    GridView gridView;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    ArrayList<ApplicationFormEntity> applicationForms= new ArrayList<ApplicationFormEntity>();
    SwipeRefreshLayout swipeRefreshLayout;
    public ApplicationForm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_form, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView=(GridView)getActivity().findViewById( R.id.gridview2 );
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout2);

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        loadData();
                                    }
                                }
        );
    }
    @Override
    public void onRefresh() {
        applicationForms.clear();
        loadData();
    }

    public class SnapedPhotoAdapter extends BaseAdapter {
        private Activity mContext;
        ArrayList<ApplicationFormEntity> applicationForms;
        private  LayoutInflater inflater=null;
        // Constructor
        public SnapedPhotoAdapter(Activity context, ArrayList<ApplicationFormEntity> applicationForms) {
            mContext = context;
            this.applicationForms=applicationForms;
            inflater = ( LayoutInflater )mContext.
                    getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return applicationForms.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }
        public class Holder
        {
            //TextView tv;
            TextView inflate_text;
        }
        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.form_item, null);
            // holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            holder.inflate_text=(TextView) rowView.findViewById(R.id.inflate_text);
            //holder.tv.setText(result[position]);
            holder.inflate_text.setText(""+applicationForms.get( position ).getTital());
            rowView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent( v.getContext(), DownloadPdfActivity.class );
                    intent.putExtra("object",applicationForms.get( position ));
                    startActivity( intent );
                }
            } );
            return rowView;
        }

    }


    @SuppressLint("NewApi")
    Handler mHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // switch case
            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    //showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    //hideProgDialog();
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case LOAD_QUESTION_SUCCESS:

                    // GotoData();

                    break;

                default:
                    break;
            }

            return false;
        }
    });

    class AsyncLoadData extends AsyncTask<String, Void, String> {
        boolean flag = false;

        @Override
        protected String doInBackground(String... params) {
            String sss=null;
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi loadApi=new LoadApi();
            // if(flagValue==0){
            try {

                sss=loadApi.getApplicationForms(params);
            } catch (Exception e) {
                e.printStackTrace();

            }
            // }
            return sss;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            Log.d("resultAppform====",""+result);
            try {
                JSONObject jobject1 = new JSONObject(result);
                if (jobject1.getString("status").equals("1")) {
                JSONArray jsonArray = jobject1.getJSONArray("info");
                Log.d("application list", " below");
                Log.d("jsonArray", "  " + jsonArray.toString());
                Log.d("sizeeeem", " kk no size"+jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jobject = jsonArray.getJSONObject(i);
                        Log.d("111sizeeeem", "kk1111 " + jobject.toString());
                        ApplicationFormEntity applicationFormEntity = new ApplicationFormEntity();
                        applicationFormEntity.setId(jobject.getString("id"));
                        applicationFormEntity.setTital(jobject.getString("title"));
                        applicationFormEntity.setMgnt_id(jobject.getString("mgnt_id"));
                        applicationFormEntity.setImage(jobject.getString("image"));
                        applicationFormEntity.setDescription(jobject.getString("description"));
                        applicationFormEntity.setCreated(jobject.getString("created"));
                        applicationForms.add(applicationFormEntity);
                    }
                    if (applicationForms.size()>0){
                        snapedPhotoAdapter = new SnapedPhotoAdapter(getActivity(),
                                applicationForms);
                        gridView.setAdapter(snapedPhotoAdapter);
                    }else {
                        Toast.makeText( getActivity(), "No application Form Available!!", Toast.LENGTH_LONG ).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            asyncLoad.execute(sharedPreferences.getString( "mgnt_id","" ));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad.cancel(true);
        }
    }
}
