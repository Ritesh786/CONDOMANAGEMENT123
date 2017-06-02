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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infoservices.lue.adapters.FacilitiesAdapter;
import com.infoservices.lue.condomanagement.AddEvent;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.FacilitiesEntity;
import com.infoservices.lue.utills.LoadApi;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RulesAndRegFragment extends Fragment {
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    private final String TAG="chandan";
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    TextView terms_of_service,recent;
    public RulesAndRegFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules_and_reg, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        terms_of_service=(TextView)getActivity().findViewById(R.id.terms_of_service);
        terms_of_service.setText("");
        recent=(TextView)getActivity().findViewById(R.id.recent);
        recent.setText("");
        loadData();
    }

    @SuppressLint("NewApi")
    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // switch case
            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:

                    hideProgDialog();
                    break;

                case LOAD_QUESTION_SUCCESS:

                    break;

                default:
                    break;
            }

            return false;
        }
    });
    @SuppressLint("InlinedApi")
    private void showProgDialog () {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // hide progress

    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            try {
                sts = api.rulesAndRegulations(params);
            } catch (Exception e) {
                // result1="Please select image";
                e.printStackTrace();
            }
            return sts;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
              Log.d("result===",""+result);
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            try {
                JSONObject jobject1 = new JSONObject(result);
                  if(jobject1.getString("status").equals("1")){
                      JSONArray jsonArray=jobject1.getJSONArray("info");
                      JSONObject jsonObject=jsonArray.getJSONObject(0);
                      terms_of_service.setText(Html.fromHtml(jsonObject.getString("description")));
                      //recent.setText(Html.fromHtml(jsonObject.getString("recent")));
                  }else{
                      terms_of_service.setText(jobject1.getString("msg"));
                      //recent.setText(jobject1.getString("msg"));
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
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            asyncLoad.execute(sharedPreferences.getString( "mgnt_id","" ));
        }
    }

}
