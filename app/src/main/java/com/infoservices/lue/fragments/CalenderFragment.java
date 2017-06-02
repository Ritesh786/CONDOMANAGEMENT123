package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.infoservices.lue.condomanagement.AddEvent;
import com.infoservices.lue.condomanagement.LoginActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.utills.LoadApi;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class CalenderFragment extends Fragment {
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
    //CustomCalendarView calendarView;
    Button addevent;
    private final String TAG="chandan";
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    static  String get_date="",get_time="";
   TextView text_month,text_year;
    Button getAddevent;
    public CalenderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calender, container, false);
        /*HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        CalendarView cv = ((CalendarView)getActivity().findViewById(R.id.calendar_view));
        cv.updateCalendar(events);*/


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        text_month=(TextView)getActivity().findViewById( R.id.text_month );
        text_year=(TextView)getActivity().findViewById( R.id.text_year );
        getAddevent=(Button)getActivity().findViewById( R.id.add_event ) ;

        Calendar cal = Calendar.getInstance();
        Date date=new Date(  );
        text_month.setText( ""+getMonthForInt(date.getMonth()) );
        text_year.setText( ""+cal.get(Calendar.YEAR) );
        final CompactCalendarView compactCalendar = (CompactCalendarView)getActivity(). findViewById(R.id.compactcalendar_view);
        getAddevent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getActivity(), AddEvent.class );
                startActivity( intent );
            }
        } );
        /*Date date2=new Date(  );
        date2.setDate( 15 );
        date2.setMonth(8  );
        date2.setYear( 2016 );
        Date date3=new Date(  );
        date3.setDate( 20 );
        date3.setMonth(8  );
        date3.setYear( 2016 );*/
        //And now set the selected date in the calendar view by doing
        //compactCalendar.setCurrentDate( date2 );
       // compactCalendar.addEvent(new Event( Color.GREEN,1433701251000L,date3  ));
       /* // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1= new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendar.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendar.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendar.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: " + events);*/

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date date) {
                Log.d(TAG, "Month was scrolled to: " + date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                text_month.setText( ""+getMonthForInt(date.getMonth()) );
                text_year.setText( ""+cal.get(Calendar.YEAR));

            }
        });
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
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
               api.addEvent( params );
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
             sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            asyncLoad.execute(sharedPreferences.getString( "mgnt_id","" ));

        }
    }

}

   /* String date = "22/3/2014";
    String parts[] = date.split("/");

    int day = Integer.parseInt(parts[0]);
    int month = Integer.parseInt(parts[1]);
    int year = Integer.parseInt(parts[2]);

    Calendar calendar = Calendar.getInstance();
calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milliTime = calendar.getTimeInMillis();
        And now set the selected date in the calendar view by doing

        mCalendarView.setDate (milliTime, true, true); */