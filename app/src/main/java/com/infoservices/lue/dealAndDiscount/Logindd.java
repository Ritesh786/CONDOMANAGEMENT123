package com.infoservices.lue.dealAndDiscount;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.LoginActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.utills.LoadApi;
import com.infoservices.lue.utills.LoadApi2;
import com.infoservices.lue.utills.Utils;

import org.json.JSONObject;

public class Logindd extends AppCompatActivity implements View.OnClickListener{
    EditText user_name,password;
    Button button_login,button_signup;
    SharedPreferences sharedPreferences;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String MyLoginPREFERENCESDD = "loginpreferencedd" ;
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
    public static final String MGNT_ID = "mgnt_id";
    public static final String Status = "status";
    public static final String Created = "created";
    public static final String Updated = "updated";
    public static final String RegistationID = "updated";
    public static final String Address = "address";
    public static final String Image = "image";
    public static final String Login = "login";
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AlertDialog alertDialog;
    String name,pass,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logindd);
        user_name=(EditText)findViewById(R.id.email_dd);
        password=(EditText)findViewById(R.id.passworddd);
        button_login=(Button)findViewById(R.id.button_login_dd);
        button_signup=(Button)findViewById(R.id.button_signup_dd);
        button_login.setOnClickListener(this);
        button_signup.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(MyLoginPREFERENCESDD, Context.MODE_PRIVATE);
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

                    // GotoData();

                    break;

                default:
                    break;
            }

            return false;
        }
    });

    private AsyncLoadData asyncLoad;

    @SuppressLint("NewApi")
    private void loadData(String name1,String pass1,String regid1) {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            Log.d("inside ", " load data inside async");
                asyncLoad.execute(name1, pass1);
        }
    }
    // asynchronous task
    class AsyncLoadData extends AsyncTask<String, Void, String> {
        boolean flag = false;
        String sret=null;
        @Override
        protected String doInBackground(String... params) {

            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "Logging...";
            LoadApi2 api = new LoadApi2();
            try {
                    sret = api.loginDeal(params);
            } catch (Exception e) {
                e.printStackTrace();

            }
            // }
            return sret;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            Log.d("result==",""+s);
            try{
                JSONObject jsonObject=new JSONObject(s);
                if (jsonObject.getJSONObject("response").getInt("httpCode")==401){
                    Toast.makeText(getApplicationContext(),""+jsonObject.getJSONObject("response").getString("Message"),Toast.LENGTH_LONG).show();
                }	else if(jsonObject.getJSONObject("response").getInt("httpCode")==202){
                    final String USERID = jsonObject.getJSONObject("UserData").getString("user_id");
                    Session session = new Session(Logindd.this);
                    session.saveUserId(USERID);
                    startActivity(new Intent(Logindd.this, TabActivity.class));
                    Logger.showCenterToast(""+getString(R.string.loginsuccess), Logindd.this);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),""+jsonObject.getJSONObject("response").getString("Message"),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),MainddActivity.class);
                    startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==button_login.getId()){
            if (user_name.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Please Enter Your Username",Toast.LENGTH_LONG).show();
            }else if(!user_name.getText().toString().matches(emailPattern)){
                Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_LONG).show();
            }else if(password.getText().toString().equals("")){
                password.setError("invalid msg");
                Toast.makeText(getApplicationContext(),"Please Enter Your Password",Toast.LENGTH_LONG).show();
            }else{
                name=user_name.getText().toString();
                pass=password.getText().toString();
                sharedPreferences = getSharedPreferences(MyLoginPREFERENCESDD, Context.MODE_PRIVATE);

                if (!Utils.isNetworkAvailable(Logindd.this)) {
                    Toast.makeText(getApplicationContext(),
                            "Please check your internet connection", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                   loadData(name,pass,"");

                }


            }
        }else if (v.getId()==button_signup.getId()){
            Intent intent=new Intent(getApplicationContext(),SignupDDActivity.class);
            startActivity(intent);
        }
    }
    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(Logindd.this
                        ,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(Logindd.this);
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
}
