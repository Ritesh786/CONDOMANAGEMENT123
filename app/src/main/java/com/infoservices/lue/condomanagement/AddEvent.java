package com.infoservices.lue.condomanagement;

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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.infoservices.lue.utills.LoadApi;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class AddEvent extends AppCompatActivity implements View.OnClickListener{
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

    Button add_event_button;
    private final String TAG="chandan";
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    View header;
    ImageView go_back,logout;
    TextView head,to_time_event_ae,from_time_event_ae,date_event_ae;
    EditText title_event_ae,purpose_event_ae,description_event_ae;
    JSONObject jobject1;
    TimePickerDialog mTimePicker;
    DatePickerDialog datePickerDialog;

    ImageView event_img,edit_img_addevent;
    private static final int SELECT_PICTURE = 0;
    String ImageDecode,ftime="00:00",ttime="00:00";
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_event );
        header=findViewById( R.id.header_add_event );
        title_event_ae=(EditText)findViewById( R.id.title_event_ae );
        purpose_event_ae=(EditText)findViewById( R.id.purpose_event_ae );
        description_event_ae=(EditText)findViewById( R.id.description_event_ae );
        date_event_ae=(TextView)findViewById( R.id.date_event_ae );
        from_time_event_ae=(TextView)findViewById( R.id.from_time_event_ae );
        to_time_event_ae=(TextView)findViewById( R.id.to_time_event_ae );
        add_event_button=(Button)findViewById( R.id.add_event_button );
        event_img=(ImageView)findViewById(R.id.event_img);
        edit_img_addevent=(ImageView)findViewById(R.id.edit_img_add_event);
        edit_img_addevent.setOnClickListener( this );
        add_event_button.setOnClickListener( this );
        date_event_ae.setOnClickListener( this );
        from_time_event_ae.setOnClickListener( this );
        to_time_event_ae.setOnClickListener( this );
        header();
   }
    private void header() {
        go_back=(ImageView)header.findViewById(R.id.go_back);
        logout=(ImageView)header.findViewById(R.id.logout);
        head=(TextView) header.findViewById(R.id.header_text);
        head.setText( "ADD EVENT" );
        logout.setVisibility( View.GONE );
        go_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
       if (v.getId()==go_back.getId()){
                finish();
       }
       else if(v.getId()==edit_img_addevent.getId()){
           pickPhoto();
       }
       else if(v.getId()==add_event_button.getId()){
                  if (title_event_ae.getText().toString().equals( "" )){
                      Toast.makeText( getApplicationContext(),"Enter Title!!",Toast.LENGTH_LONG ).show();
                  } else if (purpose_event_ae.getText().toString().equals( "" )){
                      Toast.makeText( getApplicationContext(),"Enter purpose!!",Toast.LENGTH_LONG ).show();
                  } else if (date_event_ae.getText().toString().equals( "" )){
                      Toast.makeText( getApplicationContext(),"Select Date!!",Toast.LENGTH_LONG ).show();
                  } else if (from_time_event_ae.getText().toString().equals( "" )){
                      Toast.makeText( getApplicationContext(),"Select time From!!",Toast.LENGTH_LONG ).show();
                  } else if (to_time_event_ae.getText().toString().equals( "" )){
                      Toast.makeText( getApplicationContext(),"Select time To !!",Toast.LENGTH_LONG ).show();
                  } else if (description_event_ae.getText().toString().equals( "" )){
                      Toast.makeText( getApplicationContext(),"Enter Description !!",Toast.LENGTH_LONG ).show();
                  }else if (bitmap==null){
                      Toast.makeText( getApplicationContext(),"Select an image !!",Toast.LENGTH_LONG ).show();
                  }else{
                      loadData();
                  }
          }
          else  if (v.getId()==date_event_ae.getId()){
           datePickerDialog=new DatePickerDialog( AddEvent.this, date, myCalendar
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
          else  if (v.getId()==from_time_event_ae.getId()){
           Calendar mcurrentTime = Calendar.getInstance();
           int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
           int minute = mcurrentTime.get(Calendar.MINUTE);
           new TimePickerDialog(AddEvent.this, mTimeSetListener1, hour, minute, false).show();

       }
       else  if (v.getId()==to_time_event_ae.getId()) {
           Calendar mcurrentTime = Calendar.getInstance();
           int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
           int minute = mcurrentTime.get(Calendar.MINUTE);
           new TimePickerDialog(AddEvent.this, mTimeSetListener2, hour, minute, false).show();
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
            String res=null;
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi api = new LoadApi();
            try {
                res=api.addEvent( params );
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
                jobject1 = new JSONObject(result);
                if (jobject1.getString( "status" ).equals( "1" )){
                    Toast.makeText( getApplicationContext(),"Event Posted Successfully !!",Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText( getApplicationContext(),"Event not Posted !!",Toast.LENGTH_LONG ).show();
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
                progress_dialog = new ProgressDialog(AddEvent.this,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(AddEvent.this);
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
            sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            asyncLoad.execute(sharedPreferences.getString( Id,"" )
                    ,title_event_ae.getText().toString()
                    ,purpose_event_ae.getText().toString()
                    ,date_event_ae.getText().toString()
                    ,from_time_event_ae.getText().toString()
                    ,to_time_event_ae.getText().toString()
                    ,"no pax Avalable.."
                    ,description_event_ae.getText().toString()
                    ,sharedPreferences.getString( MGNT_ID,"" )
            ,getBase64(bitmap).toString());
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
                Toast.makeText( getApplicationContext(),"Back date not allowed !!!",Toast.LENGTH_LONG ).show();
                date_event_ae.setText("");
            }
        }

    };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener1 =
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
                    from_time_event_ae.setText(aTime);
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
                    String sn=from_time_event_ae.getText().toString();
                    compareDates(sn,aTime);

                }
            };
    public static final String inputFormat = "h:mm a";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
    private void compareDates(String from,String to){
        Date dateCompareOne = parseDate(from);
        Date dateCompareTwo = parseDate(to);

        if ( dateCompareOne.before( dateCompareTwo )) {
            to_time_event_ae.setText(to);
        }else {
            Toast.makeText(getApplicationContext(),"Back time not Allowed",Toast.LENGTH_LONG).show();
        }
    }
    private Date parseDate(String date) {
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here MM
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_event_ae.setText(sdf.format(myCalendar.getTime()));
    }
    public void pickPhoto() {
        //TODO: launch the photo picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity. RESULT_OK) {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                event_img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private String getBase64( Bitmap bitmap3){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        if (bitmap3!=null){
            bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

    }
}