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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.ClassifAdvetisment.AdsFragment;
import com.infoservices.lue.entity.FacilitiesEntity;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.fragments.BullatineandNotices;
import com.infoservices.lue.fragments.EventCalender;
import com.infoservices.lue.fragments.FacilitiesListFragment;
import com.infoservices.lue.fragments.FaultReportFregment;
import com.infoservices.lue.fragments.ImportantNumbersFragment;
import com.infoservices.lue.fragments.FcilityBookingFragment;
import com.infoservices.lue.fragments.PreRegistationFragment;
import com.infoservices.lue.fragments.TransactionHistoryFragment;
import com.infoservices.lue.utills.LoadApi;
import com.infoservices.lue.utills.MyNotificationHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class MenuOptionSelectedActivity extends AppCompatActivity implements View.OnClickListener{
    View header,footer;
    RelativeLayout foot_forms,foot_regulations,foot_notifications,profile,foot_settings,foot_home;
    ImageView go_back,logout;
    String from;
    TextView tv;
    AlertDialog alertDialog;

    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;

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
    public static final String Status = "status";
    public static final String Created = "created";
    public static final String Updated = "updated";
    public static final String RegistationID = "updated";
    public static final String Address = "address";
    public static final String Login = "login";
    Intent intent;
    ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
    static ArrayList<NotificationEntity> notificationEntities2=new ArrayList<NotificationEntity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_option_selected);
        header=findViewById(R.id.header_menuoption);
        tv=(TextView)header.findViewById(R.id.header_text);
        intent=getIntent();
        from=intent.getStringExtra("from");
        if(from.equals("Facilities Booking")){
            tv.setText("FACILITIES");
            FacilitiesListFragment fragment = new FacilitiesListFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();
        }
        else if(from.equals("Bulatin and Notices")){
            tv.setText("BULLETINS & NOTICES");
            BullatineandNotices fragment = new BullatineandNotices();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();
        }
        else if(from.equals("Fault Report")){
            tv.setText("FAULT REPORT");
            FaultReportFregment fragment = new FaultReportFregment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();
        }
        else if(from.equals("Transaction History")){
            tv.setText("TRANSACTION HISTORY");
            TransactionHistoryFragment fragment = new TransactionHistoryFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();
        }
        else if(from.equals("Calendar")){
            tv.setText("EVENT CALENDAR");
            EventCalender fragment = new EventCalender();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();
        }
        else if(from.equals("Important Number")){
            tv.setText("IMPORTANT NUMBERS");
            ImportantNumbersFragment fragment = new ImportantNumbersFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();
        }
        else if(from.equals("Multipurpose Hall")){
            FacilitiesEntity facilitiesEntity=(FacilitiesEntity) intent.getSerializableExtra( "object" );
            tv.setText(""+facilitiesEntity.getTitle());
            FcilityBookingFragment fragment = new FcilityBookingFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();

        }else if(from.equals("Classified Ads")){  // Done By Ritesh Jha

            tv.setText("Classified Ads");
            AdsFragment fragment = new AdsFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();

        }
        else{
            //tv.setText("Comments & Suggestions");
            //CommandAndSuggetionFragment fragment = new CommandAndSuggetionFragment();
            tv.setText("Pre-Registration");
            PreRegistationFragment fragment = new PreRegistationFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_menuoption, fragment).commit();
        }
        header();
        footer();
        sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        footerUpdate();
    }

    private void footer() {
        footer=findViewById(R.id.footer_menuoption);
        foot_forms=(RelativeLayout)footer.findViewById(R.id.foot_forms);
        foot_regulations=(RelativeLayout)footer.findViewById(R.id.foot_regulations);
        foot_notifications=(RelativeLayout)footer.findViewById(R.id.foot_notifications);
        profile=(RelativeLayout)footer.findViewById(R.id.profile);
        foot_settings=(RelativeLayout)footer.findViewById(R.id.foot_settings);
        foot_home=(RelativeLayout)footer.findViewById(R.id.foot_home);
        foot_forms.setOnClickListener(this);
        foot_regulations.setOnClickListener(this);
        profile.setOnClickListener(this);
        foot_settings.setOnClickListener(this);
        foot_notifications.setOnClickListener(this);
        foot_home.setOnClickListener(this);
        footerUpdate();
    }
    private void header() {
        go_back=(ImageView)header.findViewById(R.id.go_back);
        logout=(ImageView)header.findViewById(R.id.logout);
        go_back.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
public void  footerUpdate(){
    notificationEntities2.clear();
    notificationEntities=new MyNotificationHelper(MenuOptionSelectedActivity.this).getAllNotifications();
    footer=findViewById(R.id.footer_menuoption);
    for (int k=0;k<notificationEntities.size();k++){
        if(notificationEntities.get( k ).getStatus().equals( "1" )){
            notificationEntities2.add( notificationEntities.get( k ) );
        }
    }
    TextView note_main=(TextView)footer.findViewById( R.id.note_main );
    if(notificationEntities2.size()>0){
        note_main.setText( ""+notificationEntities2.size() );
    }else{
        note_main.setVisibility( View.GONE );
    }
}
    @Override
    public void onClick(View v) {
        if (v.getId()==go_back.getId()){
            finish();
        }else if(v.getId()==logout.getId()){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout ?");
            alertDialogBuilder.setCancelable( true );
            alertDialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    loadData();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

             alertDialog = alertDialogBuilder.create();
            alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                               @Override
                                               public void onShow(DialogInterface arg0) {
                                                   alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                   alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                               }
                                           }
            );
            alertDialog.show();
        }
        else if(v.getId()==foot_forms.getId()){
            intent =new Intent(getApplicationContext(),CommadsAndSuggesationActivity.class);
            intent.putExtra("from","form");
            startActivity(intent);
        }
        else if(v.getId()==foot_regulations.getId()){
            intent =new Intent(getApplicationContext(),CommadsAndSuggesationActivity.class);
            intent.putExtra("from","regulation");
            startActivity(intent);
        }
        else if(v.getId()==profile.getId()){
            intent =new Intent(getApplicationContext(),CommadsAndSuggesationActivity.class);
            intent.putExtra("from","profile");
            startActivity(intent);
        }
        else if(v.getId()==foot_home.getId()){
            intent =new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(v.getId()==foot_settings.getId()){
            intent =new Intent(getApplicationContext(),CommadsAndSuggesationActivity.class);
            intent.putExtra("from","setting");
            startActivity(intent);
        }
        else if(v.getId()==foot_notifications.getId()){
            intent =new Intent(getApplicationContext(),CommadsAndSuggesationActivity.class);
            intent.putExtra("from","notification");
            startActivity(intent);
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
            String return_res="";
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "Logging off...";
            LoadApi api = new LoadApi();
            try {

                return_res=api.changeLoginStatus(params);
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
                JSONObject jobject=new JSONObject(result);
                if(jobject.getString("status").equals("0")){
                    Toast.makeText(getApplicationContext(),"Logout not Successfull !!!",Toast.LENGTH_LONG).show();
                }else {
                    MyNotificationHelper myNotificationHelper = new MyNotificationHelper( MenuOptionSelectedActivity.this );
                    myNotificationHelper.deleteAll();
                    sharedPreferences = getSharedPreferences( MyLoginPREFERENCES, Context.MODE_PRIVATE );
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
                progress_dialog = new ProgressDialog(MenuOptionSelectedActivity.this,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(MenuOptionSelectedActivity.this);
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
            asyncLoad.execute(sharedPreferences.getString( "mgnt_id","" ),sharedPreferences.getString( Id,"" ));
        }
    }

}


