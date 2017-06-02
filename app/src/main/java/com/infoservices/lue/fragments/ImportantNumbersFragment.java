package com.infoservices.lue.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.infoservices.lue.adapters.FacilitiesAdapter;
import com.infoservices.lue.adapters.ImportanNumberAdapter;
import com.infoservices.lue.condomanagement.DownloadPdfActivity;
import com.infoservices.lue.condomanagement.Manifest;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.ImportantContactEntity;
import com.infoservices.lue.utills.LoadApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ImportantNumbersFragment extends Fragment{
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    ArrayList<ImportantContactEntity> importantContactEntities=new ArrayList<ImportantContactEntity>();
    ArrayList<ImportantContactEntity> importantContactEntities1=new ArrayList<ImportantContactEntity>();
    ArrayList<ImportantContactEntity> importantContactEntities2=new ArrayList<ImportantContactEntity>();
    ListView list_contact1,list_contact2;

    private int STORAGE_PERMISSION_CODE = 1;
    private View view;

    public ImportantNumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate( R.layout.fragment_important_numbers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          loadData();
        if(isPhoneCallAllowed()!=true){
            //If the app has not the permission then asking for the permission
            requestStoragePermission();
        }

    }

    @SuppressLint("NewApi")
    Handler mHandler = new Handler( new Handler.Callback() {

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

                    // GotoData();

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
                progress_dialog = new ProgressDialog(getActivity()
                        ,R.style.AppCompatAlertDialogStyle);
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
          String str=null;
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi api = new LoadApi();
            try {
                str = api.getImportantContact(params);
                Log.d("menu_list", " " + importantContactEntities.size());
            } catch (Exception e) {
                // result1="Please select image";
                e.printStackTrace();

            }
            // }
            return str;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {

            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            JSONObject jobject1 = null;
            try {
                jobject1 = new JSONObject(result);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jobject1.getString("info"));
            Log.d("contact list", " below");
            Log.d("jsonArray", "  " + jsonArray.toString());
            Log.d("sizeeeem", " kk no size");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobject = jsonArray.getJSONObject(i);
                Log.d("111sizeeeem", "kk1111 " + jobject.length());
                ImportantContactEntity importantContactEntity = new ImportantContactEntity();
                importantContactEntity.setId(jobject.getString("id"));
                importantContactEntity.setName(jobject.getString("name"));
                importantContactEntity.setNumber(jobject.getString("number"));
                importantContactEntity.setNum_type(jobject.getString("num_type"));
                importantContactEntity.setCreated(jobject.getString("created"));
                importantContactEntity.setUpdated(jobject.getString("updated"));
                importantContactEntity.setDesignation(jobject.getString("designation"));
                importantContactEntities.add(importantContactEntity);}
            } catch (JSONException e) {
                    e.printStackTrace();
                }

              for (int i=0;i<importantContactEntities.size();i++){
                  if (importantContactEntities.get( i ).getNum_type().equals( "1" )){
                      importantContactEntities1.add( importantContactEntities.get(i));
                  }else{
                      importantContactEntities2.add( importantContactEntities.get(i));
                  }
              }
            try {

                if (importantContactEntities.size() != 0 && getActivity() != null) {
                    list_contact1 = (ListView) getActivity().findViewById(R.id.list_contact1);
                    list_contact1.setAdapter(new ImportanNumberAdapter(getActivity(),importantContactEntities1));list_contact2 = (ListView) getActivity().findViewById(R.id.list_contact2);
                    list_contact2.setAdapter(new ImportanNumberAdapter(getActivity(),importantContactEntities2));

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

    private boolean isPhoneCallAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),android.Manifest.permission.CALL_PHONE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.CALL_PHONE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(getActivity(),"Permission granted now you can CALL !!",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(),"Oops you just denied the permission !!",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }
}


