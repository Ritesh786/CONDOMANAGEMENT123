package com.infoservices.lue.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infoservices.lue.condomanagement.CommadsAndSuggesationActivity;
import com.infoservices.lue.condomanagement.LoginActivity;
import com.infoservices.lue.condomanagement.MainActivity;
import com.infoservices.lue.condomanagement.MenuOptionSelectedActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.condomanagement.SplashActivity;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.MyNotificationHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by lue on 25-08-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String MYNOTIFICATION_PREFERENCE="notification_preferences";
    public static final String SOUND="sound";
    public static final String VIBRATE="vibrate";

    SharedPreferences sharedPreferences3;
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


    private static final String TAG = "MyFirebaseMsgService";
    NotificationManager notificationManager;
    int count=0,id=0;
    static String title="Unknown",subTitle="Unknown is published",type="",id_notification="",message="",mgnt_id="",image="",description="",status="",created="";
    boolean is_not_avalable=false;
    Intent intent;
    public static int badgeCount = 0;
    ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       Log.d("log","received");
        Map<String, String> params = remoteMessage.getData();
        Log.d(TAG, "Notification Message Body: " + params.toString());
        sendNotification(params);

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(Map<String, String> params) {
        //Normal fcm topicwise messaging
        //sharedPreferences=getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE);
        //badgeCount++;
        //ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4+
        //ShortcutBadger.applyCountOrThrow(getApplicationContext(),badgeCount);
        try{
            JSONObject jsonObject=new JSONObject(params);
            id_notification=jsonObject.getString("id");
            type=jsonObject.getString("type");
            message=jsonObject.getString("message");

        }catch(Exception e){
            e.printStackTrace();
        }
        if(type.equals( "bulletin-notices" )||type.equals( "regulation" )||type.equals( "application-form" )) {
            try {
                notificationEntities.clear();
                badgeCount=0;
                JSONObject jsonObject = new JSONObject(message);
                id_notification = jsonObject.getString( "id" );
                mgnt_id = jsonObject.getString( "mgnt_id" );
                image = jsonObject.getString( "image" );
                description = jsonObject.getString( "description" );
                status = jsonObject.getString( "status" );
                created = jsonObject.getString( "created" );
                title = jsonObject.getString( "title" );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (type.equals("bulletin-notices")) {
                //{subTitle=Magazine post, id=2, type=magazine, title=magazine post}
                MyNotificationHelper myNotificationHelper = new MyNotificationHelper(getApplicationContext());
                myNotificationHelper.insertnotification(id_notification, mgnt_id, title, image, description, status, created);
                intent = new Intent(this, MenuOptionSelectedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("msg", message);
                intent.putExtra("from", "Bulatin and Notices");
                //notificationEntities = myNotificationHelper.getAllNotifications();
                //badgeCount=notificationEntities.size();
                //ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4+
                //ShortcutBadger.applyCountOrThrow(getApplicationContext(),badgeCount);
                //soundNotification();
                SharedPreferences sharedPreferences =getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
                if (sharedPreferences.getString(VIBRATE,"").equals("yes") && sharedPreferences.getString(SOUND,"").equals("yes")){
                    soundandVibrateNotification();
                }else if (sharedPreferences.getString(VIBRATE,"").equals("yes")){
                    vibrateNotification();
                }
                else if (sharedPreferences.getString(SOUND,"").equals("yes")){
                    soundNotification();
                }else{
                    soundlessNotification();
                }
            }
            else if (type.equals( "regulation"  )){
                //="A new form is added rule management !!";
                intent = new Intent( this, CommadsAndSuggesationActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                intent.putExtra( "msg", message );
                intent.putExtra( "from", "regulation" );
                //soundNotification();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
                if (sharedPreferences.getString(VIBRATE,"").equals("yes") && sharedPreferences.getString(SOUND,"").equals("yes")){
                    soundandVibrateNotification();
                }
                else if (sharedPreferences.getString(VIBRATE,"").equals("yes")){
                    vibrateNotification();
                }
                else if (sharedPreferences.getString(SOUND,"").equals("yes")){
                    soundNotification();
                }
                else{
                    soundlessNotification();
                }
            }
            else if (type.equals( "application-form"  )){
                //title="A new form is added form management !!";
                intent = new Intent( this, CommadsAndSuggesationActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                intent.putExtra( "msg", message );
                intent.putExtra( "from", "form" );
               // soundNotification();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
                if (sharedPreferences.getString(VIBRATE,"").equals("yes") && sharedPreferences.getString(SOUND,"").equals("yes")){
                    soundandVibrateNotification();
                }else if (sharedPreferences.getString(VIBRATE,"").equals("yes")){
                    vibrateNotification();
                }
                else if (sharedPreferences.getString(SOUND,"").equals("yes")){
                    soundNotification();
                }else{
                    soundlessNotification();
                }
            }

        }else if(type.equals( "events" )){
            try {
                JSONObject jsonObject = new JSONObject(message);
                id_notification = jsonObject.getString( "id" );
                mgnt_id = jsonObject.getString( "mgnt_id" );
                image = jsonObject.getString( "image" );
                description = jsonObject.getString( "description" );
                status = jsonObject.getString( "status" );
                created = jsonObject.getString( "created" );
                title = jsonObject.getString( "title" );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent = new Intent( this, MenuOptionSelectedActivity.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            intent.putExtra( "from", "Calender" );
            //soundNotification();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
            if (sharedPreferences.getString(VIBRATE,"").equals("yes") && sharedPreferences.getString(SOUND,"").equals("yes")){
                soundandVibrateNotification();
            }else if (sharedPreferences.getString(VIBRATE,"").equals("yes")){
                vibrateNotification();
            }
            else if (sharedPreferences.getString(SOUND,"").equals("yes")){
                soundNotification();
            }else{
                soundlessNotification();
            }
        }
        else if(type.equals( "approved-facility" )){
            title="Your facility booking has been Confirmed.Please make payment at management office(if any).";
            intent = new Intent(this, MainActivity.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            //soundNotification();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
            if (sharedPreferences.getString(VIBRATE,"").equals("yes") && sharedPreferences.getString(SOUND,"").equals("yes")){
                soundandVibrateNotification();
            }else if (sharedPreferences.getString(VIBRATE,"").equals("yes")){
                vibrateNotification();
            }
            else if (sharedPreferences.getString(SOUND,"").equals("yes")){
                soundNotification();
            }else{
                soundlessNotification();
            }
        }
        else if(type.equals( "panic-alarm" )){
            title="Your request for panic alarm is approved !!";
                intent = new Intent(this, MainActivity.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
           // soundNotification();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
            if (sharedPreferences.getString(VIBRATE,"").equals("yes") && sharedPreferences.getString(SOUND,"").equals("yes")){
                soundandVibrateNotification();
            }else if (sharedPreferences.getString(VIBRATE,"").equals("yes")){
                vibrateNotification();
            }
            else if (sharedPreferences.getString(SOUND,"").equals("yes")){
                soundNotification();
            }else{
                soundlessNotification();
            }
        }
        else if(type.equals( "pre-registration" )){
            title="Your request for pre-registration is approved !!";
                intent = new Intent(this, MainActivity.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            // soundNotification();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
            if (sharedPreferences.getString(VIBRATE,"").equals("yes") && sharedPreferences.getString(SOUND,"").equals("yes")){
                soundandVibrateNotification();
            }else if (sharedPreferences.getString(VIBRATE,"").equals("yes")){

                vibrateNotification();
            }
            else if (sharedPreferences.getString(SOUND,"").equals("yes")){

                soundNotification();

            }else{
                soundlessNotification();
            }
        }
        else if (type.equals( "logout"  )){
            title="Your have logged in with other device !!";
            MyNotificationHelper myNotificationHelper = new MyNotificationHelper(this);
            myNotificationHelper.deleteAll();

            SharedPreferences sharedPreferences2=getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString(VIBRATE, "no");
            editor2.putString(SOUND, "no");
            editor2.commit();
            sharedPreferences3 = getApplicationContext().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor3 = sharedPreferences3.edit();
            editor3.putString( Id, "" );
            editor3.putString( UserName, "" );
            editor3.putString( Name, "" );
            editor3.putString( Salt, "" );
            editor3.putString( Phone, "" );
            editor3.putString( Email, "" );
            editor3.putString( Unitno, "" );
            editor3.putString( HouseContact, "" );
            editor3.putString( EmergencyContact, "" );
            editor3.putString( Relationship, "" );
            editor3.putString( Status, "" );
            editor3.putString( Created, "" );
            editor3.putString( Updated, "" );
            editor3.putString( RegistationID, "" );
            editor3.putString( Address, "" );
            editor3.putString( Login, "" );
            editor3.commit();
            intent = new Intent(this, SplashActivity.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            soundNotification();
        }
    }

    private void soundNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this,Integer.parseInt(id_notification), intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.lobo2)
                .setContentTitle("CondoAssist")
                .setContentText(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        notificationManager.notify(Integer.parseInt(id_notification), notificationBuilder.build());
    }
    private void soundlessNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(id_notification), intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.lobo2)
                .setContentTitle("CondoAssist")
                .setContentText(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        notificationManager.notify(Integer.parseInt(id_notification), notificationBuilder.build());
    }
    private void soundandVibrateNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(id_notification), intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.lobo2)
                .setContentTitle("CondoAssist")
                .setContentText(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        notificationManager.notify(Integer.parseInt(id_notification), notificationBuilder.build());
    }
    private void vibrateNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(id_notification), intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.lobo2)
                .setContentTitle("CondoAssist")
                .setContentText(title)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        notificationManager.notify(Integer.parseInt(id_notification), notificationBuilder.build());
    }

}