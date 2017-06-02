package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.AddEvent;
import com.infoservices.lue.condomanagement.ListPreRegistration;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.utills.LoadApi;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreRegistationFragment extends Fragment implements View.OnClickListener{
    EditText id1,id2,id3,id4,id5,id6,visitor_name2,edit_passport_no;
    TextView tanent_name,unit_no_pre;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    public static final String Name = "name";
    public static final String MGNT_ID = "mgnt_id";
    public static final String Unitno = "unit_no";
    TextView pre_from_time,pre_to_time,pre_date;
    TimePickerDialog mTimePicker;
    DatePickerDialog datePickerDialog;
    LinearLayout ll_pre_process,lll_nric;
    Button apply_for_pre_button,list_pre_resi;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    String ftime="00:00";
    public PreRegistationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_registation, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ll_pre_process=(LinearLayout)getActivity().findViewById(R.id.ll_pre_process);
        lll_nric=(LinearLayout)getActivity().findViewById(R.id.lll_nric);
        ll_pre_process.setVisibility(View.GONE);
        sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        tanent_name=(TextView)getActivity().findViewById(R.id.tanent_name);
        unit_no_pre=(TextView)getActivity().findViewById(R.id.unit_no_pre);
        tanent_name.setText(sharedPreferences.getString(Name,""));
        unit_no_pre.setText("Unit Number : "+sharedPreferences.getString(Unitno,""));
        visitor_name2=(EditText)getActivity().findViewById(R.id.visitor_name2);
        edit_passport_no=(EditText)getActivity().findViewById(R.id.edit_passport_no);
        edit_passport_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    lll_nric.setVisibility(View.GONE);
                }else{
                    lll_nric.setVisibility(View.VISIBLE);
                }
            }
        });
        id1=(EditText)getActivity().findViewById(R.id.id1);
        id2=(EditText)getActivity().findViewById(R.id.id2);
        //id3=(EditText)getActivity().findViewById(R.id.id3);
        id4=(EditText)getActivity().findViewById(R.id.id4);
        id4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0 && id5.getText().toString().equals("") && id6.getText().toString().equals("")){
                    edit_passport_no.setVisibility(View.VISIBLE);
                }else{
                    edit_passport_no.setVisibility(View.GONE);
                }
            }
        });
        id5=(EditText)getActivity().findViewById(R.id.id5);
        id5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0 && id4.getText().toString().equals("") && id6.getText().toString().equals("")){
                    edit_passport_no.setVisibility(View.VISIBLE);
                }else{
                    edit_passport_no.setVisibility(View.GONE);
                }
            }
        });
        id6=(EditText)getActivity().findViewById(R.id.id6);
        id6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0 && id4.getText().toString().equals("") && id5.getText().toString().equals("")){
                    edit_passport_no.setVisibility(View.VISIBLE);
                }else{
                    edit_passport_no.setVisibility(View.GONE);
                }
            }
        });
        apply_for_pre_button=(Button) getActivity().findViewById(R.id.apply_for_pre_button);
        list_pre_resi=(Button) getActivity().findViewById(R.id.list_pre_resi);
        pre_date=(TextView)getActivity().findViewById(R.id.pre_date);
        pre_from_time=(TextView)getActivity().findViewById(R.id.pre_from_time);
        pre_to_time=(TextView)getActivity().findViewById(R.id.pre_to_time);
        pre_date.setOnClickListener(this);
        pre_from_time.setOnClickListener(this);
        pre_to_time.setOnClickListener(this);
        apply_for_pre_button.setOnClickListener(this);
        list_pre_resi.setOnClickListener(this);
       /* id1.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        id2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        id3.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        id4.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        id5.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        id6.setFilters(new InputFilter[]{new InputFilter.AllCaps()});*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==pre_date.getId()){
            datePickerDialog=new DatePickerDialog( getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                                    @Override
                                                    public void onShow(DialogInterface arg0) {
                                                        datePickerDialog.getButton( AlertDialog.BUTTON_NEGATIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                        datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                    }
                                                }
            );
            datePickerDialog.show();
        }
        else  if (v.getId()==pre_from_time.getId()){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            new TimePickerDialog(getActivity(), mTimeSetListener, hour, minute, false).show();

        }
        else  if (v.getId()==pre_to_time.getId()) {
            if (pre_from_time.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Please Select From Time First !!", Toast.LENGTH_LONG).show();
            } else {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String timemy = pre_from_time.getText().toString();
                        String[] tokens = timemy.split(":");
                        int ho = Integer.parseInt(tokens[0]);
                        int min = Integer.parseInt(tokens[1]);
                        if (selectedHour < ho) {
                            Toast.makeText(getActivity(), "Back time not allowed !!", Toast.LENGTH_LONG).show();
                        } else if (selectedHour == ho && selectedMinute < min) {
                            Toast.makeText(getActivity(), "Back time not allowed !!", Toast.LENGTH_LONG).show();
                        } else if (selectedMinute < 10) {
                            pre_to_time.setText(selectedHour + ":0" + selectedMinute);
                        } else {
                            pre_to_time.setText(selectedHour + ":" + selectedMinute);
                        }

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.setOnShowListener(new DialogInterface.OnShowListener() {
                                                  @Override
                                                  public void onShow(DialogInterface arg0) {
                                                      mTimePicker.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                                      mTimePicker.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                                  }
                                              }
                );
                mTimePicker.show();
            }
        }else if (v.getId()==apply_for_pre_button.getId()){
            if (visitor_name2.getText().toString().equals("")||visitor_name2.getText().toString().length()<5){
                Toast.makeText(getActivity(), "Enter the valid  name must be al least 5 character !!", Toast.LENGTH_LONG).show();
            }/*else if (id1.getText().toString().equals("")||id1.getText().toString().length()<6){
                Toast.makeText(getActivity(), "Enter the valid car no in block 1 must be al least 2 character !!", Toast.LENGTH_LONG).show();
            }*/ else if(edit_passport_no.getText().toString().equals("")){
                if (id4.getText().toString().equals("")||id4.getText().toString().length()!=6){
                    Toast.makeText(getActivity(), "Enter the valid NRIC No in block-I,must be 6 character !!", Toast.LENGTH_LONG).show();
                }else if (id5.getText().toString().equals("")||id5.getText().toString().length()<2){
                    Toast.makeText(getActivity(), "Enter the valid NRIC No in block-II,must be al least 8 character !!", Toast.LENGTH_LONG).show();
                }else if (id6.getText().toString().equals("")||id6.getText().toString().length()!=4){
                    Toast.makeText(getActivity(), "Enter the valid NRIC No in block-III,must be 4 character !!", Toast.LENGTH_LONG).show();
                }
                else if (pre_from_time.getText().toString().equals("-- Select From Time --")){
                    Toast.makeText(getActivity(), "Please Select From Time !!", Toast.LENGTH_LONG).show();
                }
                else if (pre_date.getText().toString().equals("-- Select Date --")){
                    Toast.makeText(getActivity(), "Please select date !!", Toast.LENGTH_LONG).show();
                }else{
                    loadData();
                }
            }else if (id4.getText().toString().equals("")&&id5.getText().toString().equals("")&& id6.getText().toString().equals("")){
                    if(edit_passport_no.getText().toString().equals("")||edit_passport_no.getText().toString().length()<3){
                        Toast.makeText(getActivity(), "Enter the valid Passport Number!!", Toast.LENGTH_LONG).show();
                    }
                    else if (pre_from_time.getText().toString().equals("-- Select From Time --")){
                        Toast.makeText(getActivity(), "Please Select From Time !!", Toast.LENGTH_LONG).show();
                    }
                    else if (pre_date.getText().toString().equals("-- Select Date --")){
                        Toast.makeText(getActivity(), "Please select date !!", Toast.LENGTH_LONG).show();
                    }else{
                        loadData();
                    }
                }

            }

        else if (v.getId()==list_pre_resi.getId()){
            Intent intent=new Intent(getActivity(), ListPreRegistration.class);
            startActivity(intent);
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
                pre_date.setText("");
            }
        }

    };
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here MM
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        pre_date.setText(sdf.format(myCalendar.getTime()));
    }
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
                    pre_from_time.setText(aTime);
                }
            };
    @SuppressLint("NewApi")
    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // switch case
            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    //showProgDialog();
                    ll_pre_process.setVisibility(View.VISIBLE);
                    break;

                case HIDE_PROG_DIALOG:
                    //hideProgDialog();
                    ll_pre_process.setVisibility(View.GONE);
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
                res=api.preRegistation( params );
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
            Log.d("result ==",""+result);
            try {
                JSONObject jobject1 = new JSONObject(result);
                if (jobject1.getString( "status" ).equals( "1" )){
                    Toast.makeText( getActivity(),"Request for pre-registration Submitted !!",Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText( getActivity(),"Request for Pre-registration not Submitted!!",Toast.LENGTH_LONG ).show();
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

    @SuppressLint("NewApi")
    private void loadData() {
        Log.d("inside ", " load data");
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            Log.d("inside ", " load data inside async");
            sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            asyncLoad.execute(
                    sharedPreferences.getString( Name,"" ),
                    sharedPreferences.getString( MGNT_ID,"" ),
                    sharedPreferences.getString( Id,"" ),
                    id4.getText().toString().toUpperCase()+"-"+id5.getText().toString().toUpperCase()+"-"+id6.getText().toString().toUpperCase(),
                    id1.getText().toString().toUpperCase().toUpperCase(),
                    pre_from_time.getText().toString(),
                    pre_date.getText().toString(),
                    visitor_name2.getText().toString(),
                    edit_passport_no.getText().toString().toUpperCase()
                    );

        }
    }

}
