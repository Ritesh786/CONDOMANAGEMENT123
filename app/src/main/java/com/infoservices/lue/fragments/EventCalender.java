package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.infoservices.lue.condomanagement.AddEvent;
import com.infoservices.lue.condomanagement.MainActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.condomanagement.NotificationView;
import com.infoservices.lue.entity.CalenderEvensEntity;
import com.infoservices.lue.utills.LoadApi;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class EventCalender extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    TextView text_month,text_year;
    Button getAddevent;

    public static final String MyLoginPREFERENCES = "loginpreference" ;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    public static final String CITY_LOCATION = "citylocation";
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AlertDialog alertDialog;
    AsyncLoadData asyncLoad;
    //SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<CalenderEvensEntity> calenderEvensEntities=new ArrayList<CalenderEvensEntity>();
    public EventCalender() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        /*
        hashMap.put(date,R.drawable.circuler_primary_light  );*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_event_calender, container, false );


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        text_month=(TextView)getActivity().findViewById( R.id.text_month2 );
        text_year=(TextView)getActivity().findViewById( R.id.text_year2 );
        getAddevent=(Button)getActivity().findViewById( R.id.add_event2 ) ;
        loadData();
        //swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout_calender);

       // swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
       /* swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        loadData();
                                    }
                                }
        );*/

    }
    String getMonthForInt(int num) {
        String month = "wrong";
        Log.d( "num",""+num );
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
                    //swipeRefreshLayout.setRefreshing(true);
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    //swipeRefreshLayout.setRefreshing(false);
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


/* @Override
    public void onRefresh() {
        calenderEvensEntities.clear();
        loadData();
    }
*/
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

                sts = api.showEvent( params );
                Log.d("menu_list", " " + calenderEvensEntities.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return sts;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {

            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            try {
                JSONObject jobject1 = new JSONObject(result);
                JSONArray jsonArray = null;
                jsonArray = new JSONArray(jobject1.getString("info"));
                Log.d("facility list", " below");
                Log.d("jsonArray", "  " + jsonArray.toString());
                Log.d("sizeeeem", " kk no size");
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Log.d("111sizeeeem", "kk1111 "+jsonArray.length());
                    Log.d("111sizeeeem", "kk1111 " + jsonArray.length());
                    JSONObject jobject = jsonArray.getJSONObject(i);
                    CalenderEvensEntity calenderEvensEntity = new CalenderEvensEntity();
                    calenderEvensEntity.setId(jobject.getString("id"));
                    calenderEvensEntity.setTitle(jobject.getString("title"));
                    calenderEvensEntity.setMgnt_id(jobject.getString("mgnt_id"));
                    calenderEvensEntity.setMember_id(jobject.getString("member_id"));
                    calenderEvensEntity.setEvent_purpose(jobject.getString("event_purpose"));
                    calenderEvensEntity.setEvent_date(jobject.getString("event_date"));
                    calenderEvensEntity.setFrom_time(jobject.getString("from_time"));
                    calenderEvensEntity.setTo_time(jobject.getString("to_time"));
                    calenderEvensEntity.setTargeted_paxs(jobject.getString("targeted_paxs"));
                    calenderEvensEntity.setPosted_by(jobject.getString("posted_by"));
                    calenderEvensEntity.setDescription(jobject.getString("description"));
                    calenderEvensEntity.setCreated(jobject.getString("created"));
                    calenderEvensEntity.setImage(jobject.getString("image"));
                    calenderEvensEntities.add(calenderEvensEntity);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            Date datec=new Date(  );
            text_month.setText( ""+getMonthForInt(datec.getMonth()-1) );
            text_year.setText( ""+cal.get(Calendar.YEAR) );
            final CompactCalendarView compactCalendar = (CompactCalendarView)getActivity(). findViewById(R.id.compactcalendar_view);
            getAddevent.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent( getActivity(), AddEvent.class );
                    startActivity( intent );
                }
            } );
            CaldroidFragment caldroidFragment = new CaldroidFragment();
            Bundle args = new Bundle();
            Calendar cal2 = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal2.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal2.get( Calendar.YEAR));
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
           if (calenderEvensEntities.size()>0){

            for (int j=0;j<calenderEvensEntities.size();j++){
                CalenderEvensEntity calenderEvensEntity=calenderEvensEntities.get( j );
                String getDate=calenderEvensEntity.getEvent_date();
                String[] tokens=getDate.split( "-" );
                int myday=Integer.parseInt( tokens[0] );
                int mymonth=Integer.parseInt( tokens[1] );
                int myyear=Integer.parseInt( tokens[2] );
                Calendar myCalendar = new GregorianCalendar(myyear, (mymonth-1), myday);
                Date myDate = myCalendar.getTime();
                caldroidFragment.setBackgroundDrawableForDate( blue, myDate);
                caldroidFragment.setTextColorForDate( R.color.green, myDate);
            }
            caldroidFragment.setArguments(args);
            final CaldroidListener listener = new CaldroidListener() {

                @Override
                public void onSelectDate(Date date, View view) {
                    String nowdate="";
                    int position=0;
                     try {
                         SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                         Date parsedDate = sdf.parse( String.valueOf( date ) );
                         SimpleDateFormat print = new SimpleDateFormat("dd-MM-yyyy");
                         nowdate=print.format(parsedDate).toString();
                         Log.d("nowdate",""+nowdate);
                         for(int k=0;k<calenderEvensEntities.size();k++){
                             if (nowdate.equals( calenderEvensEntities.get( k ).getEvent_date() )){
                                 Log.d("loopdate1",""+calenderEvensEntities.get( k ).getEvent_date());
                                 position=k;
                                 break;
                             }else {
                                 position=500000;
                                 Log.d("loopdate2",""+calenderEvensEntities.get( k ).getEvent_date());
                             }
                         }
                         if (position!=500000){
                         Intent intent=new Intent( getActivity(), NotificationView.class );
                         intent.putExtra( "object",calenderEvensEntities.get( position ) );
                         intent.putExtra( "from","calender" );
                         startActivity( intent );
                         }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onChangeMonth(int month, int year) {
                    Calendar cal = Calendar.getInstance();
                    cal.set( year,month,15 );
                    Calendar myCalendar = new GregorianCalendar(year, month, 15);
                    Date myDate = myCalendar.getTime();
                    text_month.setText( ""+getMonthForInt(myDate.getMonth()-1) );
                    text_year.setText( ""+cal.get(Calendar.YEAR));
                }

                @Override
                public void onLongClickDate(Date date, View view) {

                }

                @Override
                public void onCaldroidViewCreated() {

                }

            };

            caldroidFragment.setCaldroidListener(listener);
            //set fregment here

        }else{
               Toast.makeText( getActivity(),"Low Internate Connection!!!",Toast.LENGTH_LONG ).show();
           }
            FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
            t.replace(R.id.cal_fra, caldroidFragment);
            t.commit();
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

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}

