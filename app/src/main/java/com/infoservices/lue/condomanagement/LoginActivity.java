package com.infoservices.lue.condomanagement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.infoservices.lue.utills.LoadApi;
import com.infoservices.lue.utills.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText user_name,password;
    Button button_login,button_signup;
    SharedPreferences sharedPreferences;

    public static final String MYNOTIFICATION_PREFERENCE="notification_preferences";
    public static final String SOUND="sound";
    public static final String VIBRATE="vibrate";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
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
    /**GCM Notification**/
    public static final String ERROR_SERVICE_NOT_AVAILABLE =
            "SERVICE_NOT_AVAILABLE";
    private int PLAY_SERVICES_RESOLUTION_REQUEST;
    GoogleCloudMessaging gcm;
    static String regid="";
    private String TAG;
    public static final String PROJECT_NUMBER = "577298480579";
    private ProgressDialog pd;
    String gcmid;
    CheckBox checkBox;
    String check2="login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name=(EditText)findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.password);
        button_login=(Button)findViewById(R.id.button_login);
        button_signup=(Button)findViewById(R.id.button_signup);
        button_login.setOnClickListener(this);
        button_signup.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        checkBox=(CheckBox)findViewById( R.id.check_show_password );
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked){
                                                        if (!password.getText().toString().equals( "" )){
                                                            password.setTransformationMethod(null);
                                                            checkBox.setText( "Hide Password" );
                                                        }
                                                    }else{
                                                        if (!password.getText().toString().equals( "" )){
                                                        password.setTransformationMethod(new PasswordTransformationMethod());
                                                            checkBox.setText( "Show Password" );
                                                        }
                                                    }
                                                }
                                            }
        );
       // getGCMRegId();

    }

    private void getGCMRegId() {
        //check if already registered have GCM Reg.Id
        getRegId();
    }

    private void getRegId() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                pd = new ProgressDialog(LoginActivity.this);
                pd.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        pd.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                        pd.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                    }
                } );
                pd.setCancelable( false );
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setMessage("Registering with gcm....");
                pd.show();

            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                if (checkPlayServices()) {

                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                        }
                        regid = gcm.register(PROJECT_NUMBER);
                        sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("gcm", regid);
                        editor.commit();
                        msg = "Device registered, registration ID=" + regid;
                        Log.i("GCM", msg);


                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();

                    }


                } else {
                    Log.e(TAG, "No valid Google Play Services APK found.");
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                pd.dismiss();
                /**    save in Shared Preferance**/
                 Toast.makeText( getApplicationContext(),"Registered !!",Toast.LENGTH_LONG ).show();
                //etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                try {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "This device is not supported.");
                finish();
            }

            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==button_login.getId()){
            if (user_name.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Please Enter Your Username",Toast.LENGTH_LONG).show();
            }else if(password.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Please Enter Your Password",Toast.LENGTH_LONG).show();
            }else{

                name=user_name.getText().toString();
                pass=password.getText().toString();
                sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
                gcmid=sharedPreferences.getString( "fcm","" );
                if (!Utils
                        .isNetworkAvailable(LoginActivity.this)) {
                    Toast.makeText(getApplicationContext(),
                            "Please check your internet connection", Toast.LENGTH_SHORT)
                            .show();
                }
             else if (gcmid==null){
                    Toast.makeText(getApplicationContext(),
                            "fcmid is null !!", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    check2="login";
                    sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
                    gcmid=sharedPreferences.getString( "fcm","" );
                    Log.d("inside:-", " flag value 0 above"+gcmid);
                    loadData(name,pass,gcmid);

                }


            }
        }else{
            Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
        }
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
            if (check2.equals("login")) {
                asyncLoad.execute(name1, pass1, regid1, "android");
            }else{
                asyncLoad.execute(name1);
            }
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
            LoadApi api = new LoadApi();
            try {
                if (check2.equals("login")) {
                    sret = api.postData(params);
                }else{
                    sret = api.logoutFromOtherDevice(params);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            // }
            return sret;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            Log.d("result", "" + result);
            if (check2.equals("login")) {
                try {
                    JSONObject jobject = new JSONObject(result);
                    //status
                    if (jobject.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), "Wrong user name or password or Account not Activated !!!", Toast.LENGTH_LONG).show();
                    } else if (jobject.getString("status").equals("2")) {
                        //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        final EditText input = new EditText(LoginActivity.this);
                        alertDialogBuilder.setView(input);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setMessage("You are already Loged in with Other Device ! Would you like to logout ? \n Enter Email Id");
                        alertDialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //getGCMRegId();
                                if (input.getText().toString().equals("") || !input.getText().toString().matches(emailPattern)) {
                                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                                } else {
                                    check2 = "logout";
                                    loadData("" + input.getText().toString(), "", "");
                                }
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        alertDialog = alertDialogBuilder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                          @Override
                                                          public void onShow(DialogInterface arg0) {
                                                              alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                                              alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                                          }
                                                      }
                        );
                        alertDialog.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You are Successfully Login", Toast.LENGTH_LONG).show();
                        JSONArray jsonArray = new JSONArray(jobject.getString("info"));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.d("After Login", "" + jsonObject.toString());

                        SharedPreferences sharedPreferences2 = getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putString(VIBRATE, "no");
                        editor2.putString(SOUND, "yes");
                        editor2.commit();

                        sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Id, jsonObject.getString("id"));
                        editor.putString(UserName, jsonObject.getString("username"));
                        editor.putString(Name, jsonObject.getString("name"));
                        editor.putString(Salt, jsonObject.getString("salt"));
                        editor.putString(Phone, jsonObject.getString("mobile"));
                        editor.putString(Email, jsonObject.getString("email"));
                        editor.putString(Unitno, jsonObject.getString("unit_no"));
                        editor.putString(HouseContact, jsonObject.getString("house_phone"));
                        editor.putString(EmergencyContact, jsonObject.getString("emergency_contact"));
                        editor.putString(Relationship, jsonObject.getString("relationship"));
                        editor.putString(Status, jsonObject.getString("status"));
                        editor.putString(Created, jsonObject.getString("created"));
                        editor.putString(Updated, jsonObject.getString("updated"));
                        editor.putString(RegistationID, jsonObject.getString("registration_id"));
                        editor.putString(Image, jsonObject.getString("image"));
                        editor.putString(Address, jsonObject.getString("address"));
                        editor.putString(MGNT_ID, jsonObject.getString("mgnt_id"));
                        editor.putString("maintenance_fee", jsonObject.getString("maintenance_fee"));
                        editor.putString("car_no_plate", jsonObject.getString("car_no_plate"));
                        editor.putString(Login, "yes");
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                check2="login";
                alertDialog.dismiss();
                try{
                    JSONObject jobject = new JSONObject(result);
                    String msg_log=jobject.getString("info");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage(""+msg_log);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                      @Override
                                                      public void onShow(DialogInterface arg0) {
                                                          alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                                      }
                                                  }
                    );
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(LoginActivity.this
                        ,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(LoginActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
/* // Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }*/
