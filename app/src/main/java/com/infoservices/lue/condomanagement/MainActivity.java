package com.infoservices.lue.condomanagement;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.infoservices.lue.dealAndDiscount.*;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.LoadApi;
import com.infoservices.lue.utills.MyNotificationHelper;

import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    RelativeLayout rel_pay_man_fee, rel_fac_booking, rel_bul_notice, rel_full_report, rel_tran_his, rel_cal,
            rel_imp_num, rel_comm_sugg,rel_dis_deal,rel_classads;
    RelativeLayout foot_forms, foot_regulations, foot_notifications, profile, foot_settings;

    View header, footer;
    ImageView go_back, logout;
    Intent intent;

    public static final String MYNOTIFICATION_PREFERENCE="notification_preferences";
    public static final String SOUND="sound";
    public static final String VIBRATE="vibrate";

    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference";
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
    public static final String Status = "status";
    public static final String Created = "created";
    public static final String Updated = "updated";
    public static final String RegistationID = "updated";
    public static final String Address = "address";
    public static final String Login = "login";
    String loaddata="";
    ArrayList<NotificationEntity> notificationEntities = new ArrayList<NotificationEntity>();
    ArrayList<NotificationEntity> notificationEntities2 = new ArrayList<NotificationEntity>();
    AlertDialog alertDialog;
    private static final int REQUEST_PERMISSION = 10;
    private SparseIntArray mErrorString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mErrorString = new SparseIntArray();
        requestAppPermissions(new String[]{
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.READ_PHONE_STATE},
                R.string.request_perm, REQUEST_PERMISSION);
        header = findViewById(R.id.header_main);
        go_back = (ImageView) header.findViewById(R.id.go_back);
        logout = (ImageView) header.findViewById(R.id.logout);
        go_back.setOnClickListener(this);
        logout.setOnClickListener(this);
        TextView text_notice_bullet = (TextView) findViewById(R.id.text_notice_bullet);
        text_notice_bullet.setText("Bulletins & Notices");
        TextView tv = (TextView) header.findViewById(R.id.header_text);
        tv.setText("Home");
        rel_pay_man_fee = (RelativeLayout) findViewById(R.id.rel_pay_man_fee);
        rel_fac_booking = (RelativeLayout) findViewById(R.id.rel_fac_booking);
        rel_bul_notice = (RelativeLayout) findViewById(R.id.rel_bul_notice);
        rel_full_report = (RelativeLayout) findViewById(R.id.rel_full_report);
        rel_tran_his = (RelativeLayout) findViewById(R.id.rel_tran_his);
        rel_cal = (RelativeLayout) findViewById(R.id.rel_cal);
        rel_imp_num = (RelativeLayout) findViewById(R.id.rel_imp_num);
        rel_comm_sugg = (RelativeLayout) findViewById(R.id.rel_comm_sugg);
        rel_dis_deal = (RelativeLayout) findViewById(R.id.rel_dis_deal);
        rel_classads = (RelativeLayout) findViewById(R.id.rel_class_ads);  // Done By Ritesh
        rel_classads.setOnClickListener(this);                             // Done By Ritesh
        rel_pay_man_fee.setOnClickListener(this);
        rel_fac_booking.setOnClickListener(this);
        rel_bul_notice.setOnClickListener(this);
        rel_full_report.setOnClickListener(this);
        rel_tran_his.setOnClickListener(this);
        rel_cal.setOnClickListener(this);
        rel_imp_num.setOnClickListener(this);
        rel_comm_sugg.setOnClickListener(this);
        rel_dis_deal.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        footerSelect();
    }

    void footerSelect() {
        footer = findViewById(R.id.footer_main);
        foot_forms = (RelativeLayout) footer.findViewById(R.id.foot_forms);
        foot_regulations = (RelativeLayout) footer.findViewById(R.id.foot_regulations);
        foot_notifications = (RelativeLayout) footer.findViewById(R.id.foot_notifications);
        profile = (RelativeLayout) footer.findViewById(R.id.profile);
        foot_settings = (RelativeLayout) footer.findViewById(R.id.foot_settings);
        foot_forms.setOnClickListener(this);
        foot_regulations.setOnClickListener(this);
        profile.setOnClickListener(this);
        foot_settings.setOnClickListener(this);
        foot_notifications.setOnClickListener(this);
        notificationEntities.clear();
        notificationEntities = new MyNotificationHelper(MainActivity.this).getAllNotifications();
        notificationEntities2.clear();
        for (int k = 0; k < notificationEntities.size(); k++) {
            if (notificationEntities.get(k).getStatus().equals("1")) {
                notificationEntities2.add(notificationEntities.get(k));
            }
        }
        TextView note_main = (TextView) footer.findViewById(R.id.note_main);
        if (notificationEntities2.size() > 0) {
            note_main.setText("" + notificationEntities2.size());
        } else {
            note_main.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        notificationEntities.clear();
        notificationEntities2.clear();
        notificationEntities = new MyNotificationHelper(MainActivity.this).getAllNotifications();
        footer = findViewById(R.id.footer_main);
        for (int k = 0; k < notificationEntities.size(); k++) {
            if (notificationEntities.get(k).getStatus().equals("1")) {
                notificationEntities2.add(notificationEntities.get(k));
            }
        }
        TextView note_main = (TextView) footer.findViewById(R.id.note_main);
        if (notificationEntities2.size() > 0) {
            note_main.setText("" + notificationEntities2.size());
        } else {
            note_main.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == go_back.getId()) {
            finish();
        } else if (v.getId() == logout.getId()) {

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure want to logout ?");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    loaddata="logout";
                    loadData();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
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

        } else if (v.getId() == rel_pay_man_fee.getId()) {
            intent = new Intent(getApplicationContext(), PayWebView.class);
            intent.putExtra("product_id","149034779220");
            startActivity(intent);
        } else if (v.getId() == rel_fac_booking.getId()) {
            intent = new Intent(getApplicationContext(), MenuOptionSelectedActivity.class);
            intent.putExtra("from", "Facilities Booking");
            startActivity(intent);
        } else if (v.getId() == rel_bul_notice.getId()) {
            int noofItems = new MyNotificationHelper(MainActivity.this).numberOfRows();
            if (noofItems > 0) {
                intent = new Intent(getApplicationContext(), MenuOptionSelectedActivity.class);
                intent.putExtra("from", "Bulatin and Notices");
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "No Notification now !!!", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == rel_full_report.getId()) {
           /* intent =new Intent(getApplicationContext(),MenuOptionSelectedActivity.class);
            intent.putExtra("from","Fault Report");
            startActivity(intent);*/
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure want to notify the Admin ?");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Notify", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    loaddata="panic";
                    loadData();
           /* Toast.makeText(MainActivity.this, "Latitude and Longitude " + currentLatitude + "::: " +
                    currentLatitude, Toast.LENGTH_SHORT).show();*/
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
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

        } else if (v.getId() == rel_tran_his.getId()) {
            intent = new Intent(getApplicationContext(), MenuOptionSelectedActivity.class);
            intent.putExtra("from", "Transaction History");
            startActivity(intent);
        } else if (v.getId() == rel_cal.getId()) {
            intent = new Intent(getApplicationContext(), MenuOptionSelectedActivity.class);
            intent.putExtra("from", "Calendar");
            startActivity(intent);
        } else if (v.getId() == rel_imp_num.getId()) {
            /*intent = new Intent(getApplicationContext(), PayNowActivity.class);
            startActivity(intent);*/
            intent = new Intent(getApplicationContext(), MenuOptionSelectedActivity.class);
            intent.putExtra("from", "Important Number");
            startActivity(intent);
        } else if (v.getId() == rel_comm_sugg.getId()) {
            intent = new Intent(getApplicationContext(), CommadsAndSuggesationActivity.class);
            intent.putExtra("from", "Comments and Suggestions");
            startActivity(intent);
        } else if (v.getId() == rel_dis_deal.getId()) {
            intent = new Intent(getApplicationContext(), com.infoservices.lue.dealAndDiscount.SplashActivity.class);
            startActivity(intent);
        }else if (v.getId() == foot_forms.getId()) {
            intent = new Intent(getApplicationContext(), CommadsAndSuggesationActivity.class);
            intent.putExtra("from", "form");
            startActivity(intent);
        } else if (v.getId() == foot_regulations.getId()) {
            intent = new Intent(getApplicationContext(), CommadsAndSuggesationActivity.class);
            intent.putExtra("from", "regulation");
            startActivity(intent);
        } else if (v.getId() == profile.getId()) {
            intent = new Intent(getApplicationContext(), CommadsAndSuggesationActivity.class);
            intent.putExtra("from", "profile");
            startActivity(intent);
        } else if (v.getId() == foot_settings.getId()) {
            intent = new Intent(getApplicationContext(), CommadsAndSuggesationActivity.class);
            intent.putExtra("from", "setting");
            startActivity(intent);
        } else if (v.getId() == foot_notifications.getId()) {
            intent = new Intent(getApplicationContext(), CommadsAndSuggesationActivity.class);
            intent.putExtra("from", "notification");
            startActivity(intent);
        }else if(v.getId() == R.id.rel_class_ads){   // Done By Ritesh

            intent = new Intent(getApplicationContext(), MenuOptionSelectedActivity.class);
            intent.putExtra("from", "Classified Ads");
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
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
  /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    class AsyncLoadData extends AsyncTask<String, Void, String> {
        boolean flag = false;

        @Override
        protected String doInBackground(String... params) {
            String return_res="";
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            LoadApi api = new LoadApi();
            try {
                if (loaddata.equals("logout")) {
                    progress_dialog_msg = "Logging off...";
                    return_res = api.changeLoginStatus(params);
                }else{
                    progress_dialog_msg = "Sending message for alarm ...";
                    return_res = api.generateAlarm(params);
                }
            } catch (Exception e) {
                // result1="Please select image";
                e.printStackTrace();

            }
            // }
            return return_res;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            try{
                Log.d( "result",result );
                JSONObject jobject=new JSONObject(result);
                //status
                if(jobject.getString("status").equals("0")){
                    if (loaddata.equals("logout")) {
                        Toast.makeText(getApplicationContext(), "Logout not Successfull !!!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Not Successfull !!!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    if (loaddata.equals("logout")) {

                    MyNotificationHelper myNotificationHelper = new MyNotificationHelper( MainActivity.this );
                    myNotificationHelper.deleteAll();

                        SharedPreferences sharedPreferences2=getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putString(VIBRATE, "no");
                        editor2.putString(SOUND, "no");
                        editor2.commit();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString( Id, "" );
                    editor.putString( UserName, "" );
                    editor.putString( Name, "" );
                    editor.putString( Salt, "" );
                    editor.putString( Phone, "" );
                    editor.putString( Email, "" );
                    editor.putString( Unitno, "" );
                    editor.putString( HouseContact, "" );
                    editor.putString( EmergencyContact, "" );
                    editor.putString( Relationship, "" );
                    editor.putString( Status, "" );
                    editor.putString( Created, "" );
                    editor.putString( Updated, "" );
                    editor.putString( RegistationID, "" );
                    editor.putString( Address, "" );
                    editor.putString( Login, "" );
                    editor.commit();
                    Toast.makeText( getApplicationContext(), "Successfully Logout !!!", Toast.LENGTH_LONG ).show();
                    intent = new Intent( getApplicationContext(), LoginActivity.class );
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra( "from", "Pay Maintainance Fee" );
                    startActivity( intent );
                    }else{
                        Toast.makeText(getApplicationContext(), "Successfully Sent !!!", Toast.LENGTH_LONG).show();
                    }
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
                progress_dialog = new ProgressDialog(MainActivity.this,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(MainActivity.this);
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
            if (loaddata.equals("logout")) {
                asyncLoad.execute(sharedPreferences.getString("mgnt_id", ""), sharedPreferences.getString(Id, ""));
            }else{
                asyncLoad.execute(sharedPreferences.getString("mgnt_id", ""), sharedPreferences.getString(Id, ""),Double.toString(currentLatitude),Double.toString(currentLongitude));
            }
        }
    }
    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermissions = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermissions = showRequestPermissions || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (showRequestPermissions) {
                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permisson : grantResults) {
            permissionCheck = permissionCheck + permisson;
        }

        if ((grantResults.length > 0) && PackageManager.PERMISSION_GRANTED == permissionCheck) {
            // Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
        } else {
            //Display message when contain some Dangerous permisson not accept
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }).show();
        }
    }


}

