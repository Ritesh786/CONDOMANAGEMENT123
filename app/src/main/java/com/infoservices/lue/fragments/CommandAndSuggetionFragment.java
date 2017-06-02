package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.utills.LoadApi;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommandAndSuggetionFragment extends Fragment implements View.OnClickListener{

EditText edit_name_cs,edit_sub,edit_email_cs,edit_coment;
    Button comment;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    public static final String UserName = "username";
    public static final String Name = "name";
    public static final String Salt = "salt";
    public static final String Phone = "mobile";
    public static final String Email = "email";
    public static final String Unitno = "unit_no";
    public static final String HouseContact = "house_phone";
    public static final String EmergencyContact = "emergency_contact";
    public static final String Relationship = "relationship";
    public static final String Address = "address";
    public final static String IMAGE ="image";
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public CommandAndSuggetionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_command_and_suggetion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        edit_name_cs=(EditText)getActivity().findViewById( R.id.edit_name_cs );
        edit_sub=(EditText)getActivity().findViewById( R.id.edit_sub );
        edit_email_cs=(EditText)getActivity().findViewById( R.id.edit_email_cs );
        edit_coment=(EditText)getActivity().findViewById( R.id.edit_coment );
        comment=(Button)getActivity().findViewById( R.id.comment );
        comment.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
  if (v.getId()==comment.getId()){
      if (edit_name_cs.getText().toString().equals( "" )){
          Toast.makeText( getActivity(),"Enter name",Toast.LENGTH_LONG ).show();
      }
      else if (edit_sub.getText().toString().equals( "" )){
          Toast.makeText( getActivity(),"Enter Subject",Toast.LENGTH_LONG ).show();
      }
      else if (edit_email_cs.getText().toString().equals( "" )){
          Toast.makeText( getActivity(),"Enter Email",Toast.LENGTH_LONG ).show();
      }
      else if (!edit_email_cs.getText().toString().matches( emailPattern )){
          Toast.makeText( getActivity(),"Invalid Email !!!",Toast.LENGTH_LONG ).show();
      }
      else if (edit_coment.getText().toString().equals( "" )){
          Toast.makeText( getActivity(),"Enter Comment",Toast.LENGTH_LONG ).show();
      }else{
          loadData();
      }
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

    class AsyncLoadData extends AsyncTask<String, Void, String> {
        boolean flag = false;

        @Override
        protected String doInBackground(String... params) {
            String sret="";
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi loadApi=new LoadApi();
            try {

                sret=loadApi.sentComment(params);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return sret;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            try {
                JSONObject jobject1=new JSONObject(result);
                //status
                if(jobject1.getString("status").equals("1")){
                    Toast.makeText(getActivity(),"Comment Successfully Sent !!",Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(getActivity(),"Profile not updated ! Try again !!",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


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

    @SuppressLint("NewApi")
    private void loadData() {
        Log.d("inside ", " load data");
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            Log.d("inside ", " load data inside async");
            asyncLoad.execute(sharedPreferences.getString( "id","" )
            ,sharedPreferences.getString( "mgnt_id","" )
                    ,edit_name_cs.getText().toString()
                    ,edit_email_cs.getText().toString()
                    ,edit_sub.getText().toString()
                    ,edit_coment.getText().toString()
            );

        }
    }
}

