package com.infoservices.lue.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.FacilitiesEntity;
import com.infoservices.lue.utills.LoadApi;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FcilityBookingFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView mph_rate,mph_notice,mph_date,mph_time,mph_time2;
    ImageView mph_img;
    FacilitiesEntity facilitiesEntity;
    LinearLayout linearLayout;

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
    public static final String MGNT_ID = "mgnt_id";
    public static final String Status = "status";
    public static final String Created = "created";
    public static final String Updated = "updated";
    public static final String RegistationID = "updated";
    public static final String Address = "address";
    public static final String Image = "image";
    TextView mph_full_name,mph_unit_no;
    Button mph_make_payment;
    private final String TAG="chandan";
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    Intent intent;
    AlertDialog alertDialog;
    TimePickerDialog mTimePicker;
    DatePickerDialog datePickerDialog;
    public FcilityBookingFragment() {
        // Required empty public constructor
    }


    public static FcilityBookingFragment newInstance(String param1, String param2) {
        FcilityBookingFragment fragment = new FcilityBookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getActivity().getIntent();
        facilitiesEntity=(FacilitiesEntity) intent.getSerializableExtra( "object" );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facility_book, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        linearLayout=(LinearLayout)getActivity().findViewById( R.id.ll_mph_process ) ;
        linearLayout.setVisibility( View.GONE );
        mph_img=(ImageView)getActivity().findViewById( R.id.mph_img );
        mph_rate=(TextView)getActivity().findViewById( R.id.mph_rate );
        mph_notice=(TextView)getActivity().findViewById( R.id.mph_notice );
        mph_rate.setText( "RM "+facilitiesEntity.getRate() );
        mph_notice.setText( facilitiesEntity.getBooking_notice() );
        Picasso.with(getActivity())
                .load("http://api.condoassist2u.com/"+facilitiesEntity.getPhoto())
                .placeholder(R.drawable.lobo2)   // optional
                .error(R.drawable.lobo2)     // optional
                .into(mph_img);
        mph_date=(TextView)getActivity().findViewById( R.id.mph_date );
        mph_time=(TextView)getActivity().findViewById( R.id.mph_time );
        mph_time2=(TextView)getActivity().findViewById( R.id.mph_time2 );
        mph_full_name=(TextView)getActivity().findViewById( R.id.mph_full_name );
        mph_unit_no=(TextView)getActivity().findViewById( R.id.mph_unit_no );
        mph_unit_no.setText( ""+sharedPreferences.getString(Unitno,"" ) );
        mph_full_name.setText( ""+sharedPreferences.getString( Name,"" ) );
        mph_make_payment=(Button)getActivity().findViewById( R.id.mph_make_payment );
        mph_make_payment.setOnClickListener( this );
        mph_date.setOnClickListener(this);
        mph_time.setOnClickListener(this);
        mph_time2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == mph_date.getId()) {

            datePickerDialog=new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable( getResources().getColor( R.color.colorPrimary )));
            datePickerDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                               @Override
                                               public void onShow(DialogInterface arg0) {
                                                   datePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                   datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                               }
                                           }
            );
            datePickerDialog.show();

        }else if (v.getId() == mph_time.getId()){
          Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            new TimePickerDialog(getActivity(), mTimeSetListener, hour, minute, false).show();
        }
        else if (v.getId() == mph_time2.getId()){
            if (mph_time.getText().toString().equals("")){
                Toast.makeText( getActivity(),"Select From Time First!!",Toast.LENGTH_LONG ).show();
            }else {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                new TimePickerDialog(getActivity(), mTimeSetListener2, hour, minute, false).show();
            }
        }
        else if (v.getId()==mph_make_payment.getId()){
            if (mph_full_name.getText().toString().equals( "" )){
                Toast.makeText( getActivity(),"Enter Name !!",Toast.LENGTH_LONG ).show();
            }else if (mph_unit_no.getText().toString().equals( "" )){
                Toast.makeText( getActivity(),"Enter Unit No !!",Toast.LENGTH_LONG ).show();
            }else if (mph_time.getText().toString().equals( "" )){
                Toast.makeText( getActivity(),"Select From Time !!",Toast.LENGTH_LONG ).show();
            }
            else if (mph_time2.getText().toString().equals( "" )){
                Toast.makeText( getActivity(),"Select To Time !!",Toast.LENGTH_LONG ).show();
            }
            else if (mph_date.getText().toString().equals( "" )){
                Toast.makeText( getActivity(),"Select Date !!",Toast.LENGTH_LONG ).show();
            }else{
                loadData();
            }
        }

    }
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date date=new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (year>=cal.get(Calendar.YEAR)&& monthOfYear==cal.get(Calendar.MONTH)&&dayOfMonth>=cal.get(Calendar.DAY_OF_MONTH)){
                updateLabel();
            }
            else if (year>=cal.get(Calendar.YEAR)&& monthOfYear>cal.get(Calendar.MONTH)){
                updateLabel();
            }else{
                Toast.makeText( getActivity(),"Back date not allowed !!!",Toast.LENGTH_LONG ).show();
                mph_date.setText("");
            }
        }

    };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // TODO Auto-generated method stub
                    int hour = hourOfDay;
                    int minutes = minute;
                    String timeSet = "";
                    if (hour > 12) {
                        hour -= 12;
                        timeSet = "PM";
                    } else if (hour == 0) {
                        hour += 12;
                        timeSet = "AM";
                    } else if (hour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }

                    String min = "";
                    if (minutes < 10)
                        min = "0" + minutes ;
                    else
                        min = String.valueOf(minutes);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(hour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    mph_time.setText(aTime);
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2 =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // TODO Auto-generated method stub
                    int hour = hourOfDay;
                    int minutes = minute;
                    String timeSet = "";
                    if (hour > 12) {
                        hour -= 12;
                        timeSet = "PM";
                    } else if (hour == 0) {
                        hour += 12;
                        timeSet = "AM";
                    } else if (hour == 12){
                        timeSet = "PM";
                    }else{
                        timeSet = "AM";
                    }
                    String min = "";
                    if (minutes < 10)
                        min = "0" + minutes ;
                    else
                        min = String.valueOf(minutes);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(hour).append(':')
                            .append(min ).append(" ").append(timeSet).toString();
                    String sn=mph_time.getText().toString();
                    compareDates(sn,aTime);

                }
            };
    private void compareDates(String from,String to){
        Date dateCompareOne = parseDate(from);
        Date dateCompareTwo = parseDate(to);

        if ( dateCompareOne.before( dateCompareTwo )) {
            mph_time2.setText(to);
        }else {
            Toast.makeText(getActivity(),"Back time not Allowed",Toast.LENGTH_LONG).show();
        }
    }
    public static final String inputFormat = "h:mm a";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }
   /* private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            boolean isPM = (hourOfDay >= 12);
            tvTime.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
        }
    };*/
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here MM
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mph_date.setText(sdf.format(myCalendar.getTime()));
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
            String res=null;
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi api = new LoadApi();
            try {
                res=api.facilityBooking( params );
            } catch (Exception e) {

                e.printStackTrace();

            }
            // }
            return res;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            try {
                JSONObject jobject1 = new JSONObject(result);
                if (jobject1.getString( "status" ).equals( "1" )){
                    //Toast.makeText( getActivity()," Your booking has been Submitted !! !!",Toast.LENGTH_LONG ).show();
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("Your booking has been Submitted !! Please wait for the Conformation !! ");
                    alertDialogBuilder.setCancelable( true );
                    alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
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
                   /* Intent intent=new Intent( getActivity(), PayWebView.class );
                   *//* intent.putExtra( "price",facilitiesEntity.getRate() );
                    intent.putExtra( "from","facility" );*//*
                    intent.putExtra( "product_id",facilitiesEntity.getProduct_id() );
                    startActivity( intent );*/
                }else{
                    //Toast.makeText( getActivity(),"No Facility avalable on this date !!",Toast.LENGTH_LONG ).show();
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("No Facility avalable on this time  !!\n Please check on another Date. ");
                    alertDialogBuilder.setCancelable( true );
                    alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
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
        intent=getActivity().getIntent();
        facilitiesEntity=(FacilitiesEntity) intent.getSerializableExtra( "object" );
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            Log.d("inside ", " load data inside async");
            asyncLoad.execute(sharedPreferences.getString( Id,"" )
                    ,sharedPreferences.getString( "mgnt_id","" )
                    ,mph_time.getText().toString()
                    ,mph_date.getText().toString()
                    ,facilitiesEntity.getId()
                    ,mph_time2.getText().toString()
                    );

        }
    }

}
