package com.infoservices.lue.GCM;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/*import com.adobe.mobile.Config;*/
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.infoservices.lue.condomanagement.CommadsAndSuggesationActivity;
import com.infoservices.lue.condomanagement.LoginActivity;
import com.infoservices.lue.condomanagement.MainActivity;
import com.infoservices.lue.condomanagement.MenuOptionSelectedActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.MyNotificationHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GCMIntentService extends IntentService{
    Context context;
    Set<String> setx= new HashSet<String>();
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "GCM Demo";
    public static final int notifyID = 9001;
    String id;
    String mgnt_id;
    String title;
    String image;
    String description;
    String status;
    String created;
    String msg,type;
    Intent resultIntent;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    int count=0;
    ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
    public GCMIntentService() {
        super("GcmIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Bundle extras = intent.getExtras();
        type= extras.getString("type");
        msg= extras.getString("Notice");
        //msg=msg+":"+type;
        setx= NewAbstract.setx;
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;
        try {
            token = instanceID.getToken(getString( R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
     //  Config.setPushIdentifier(token);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(),"");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(),"");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
               // sendNotification("Received: " + extras.toString(),type);

                if(!setx.contains(msg)) {
                    setx.add(msg);
                    sendNotification(msg, type);
                    NewAbstract.setx.add(msg);
                }else{

                }
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    private void sendNotification(String msg,String type) {
        /** on Click notification go to landing Activity **/
        if(type.equals( "bulletin_notices" )||type.equals( "regulation" )||type.equals( "application_form" )) {
            try {
                notificationEntities.clear();
                count=0;
                JSONArray jsonArray = new JSONArray( msg );
                JSONObject jsonObject = jsonArray.getJSONObject( 0 );
                id = jsonObject.getString( "id" );
                mgnt_id = jsonObject.getString( "mgnt_id" );
                image = jsonObject.getString( "image" );
                description = jsonObject.getString( "description" );
                status = jsonObject.getString( "status" );
                created = jsonObject.getString( "created" );
                title = jsonObject.getString( "title" );
            } catch (JSONException e) {
                e.printStackTrace();
            }
               if (type.equals( "bulletin_notices"  )){
                   MyNotificationHelper myNotificationHelper=new MyNotificationHelper(getApplicationContext() );
                   notificationEntities=myNotificationHelper.getAllNotifications();
                   myNotificationHelper.insertnotification( id, mgnt_id,title, image, description, status,created );
                   resultIntent = new Intent( this, MenuOptionSelectedActivity.class );
                   resultIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                   resultIntent.putExtra( "msg", msg );
                   resultIntent.putExtra( "from", "Bulatin and Notices" );
                       }
               else if (type.equals( "regulation"  )){
                   //="A new form is added rule management !!";
                   resultIntent = new Intent( this, CommadsAndSuggesationActivity.class );
                   resultIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                   resultIntent.putExtra( "msg", msg );
                   resultIntent.putExtra( "from", "regulation" );
                       }
               else if (type.equals( "application_form"  )){
                  //title="A new form is added form management !!";
                   resultIntent = new Intent( this, CommadsAndSuggesationActivity.class );
                   resultIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
                   resultIntent.putExtra( "msg", msg );
                   resultIntent.putExtra( "from", "form" );
               }

        }else if(type.equals( "events" )){
            try {
                JSONArray jsonArray = new JSONArray( msg );
                JSONObject jsonObject = jsonArray.getJSONObject( 0 );
                id = jsonObject.getString( "id" );
                mgnt_id = jsonObject.getString( "mgnt_id" );
                image = jsonObject.getString( "image" );
                description = jsonObject.getString( "description" );
                status = jsonObject.getString( "status" );
                created = jsonObject.getString( "created" );
                title = jsonObject.getString( "title" );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultIntent = new Intent( this, MenuOptionSelectedActivity.class );
            resultIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
            resultIntent.putExtra( "from", "Calender" );
        }else if(type.equals( "approved_facility" )){
            title="Your booked facility is approved !!";
            String yaa=getSharedPreferences(MyLoginPREFERENCES,Context.MODE_PRIVATE).getString("login","");
            if (yaa.equals("yes")) {
                resultIntent = new Intent(this, MainActivity.class);
            }else{
                resultIntent = new Intent(this, LoginActivity.class);
            }
            resultIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK );
        }
       PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if(alarmSound == null){
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if(alarmSound == null){
                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name) )
                .setContentText(title)
                .setSmallIcon(R.drawable.lobo2).setSound(alarmSound);

        // Set pending intent
        /**Enable when pending intent is in working**/
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        defaults = defaults | Notification.DEFAULT_ALL;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentTitle("CondoAssist");
        mNotifyBuilder.setContentText(title);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());


    }

}