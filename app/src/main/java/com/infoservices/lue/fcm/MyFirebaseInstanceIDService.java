package com.infoservices.lue.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by lue on 25-08-2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public static final String MyLoginPREFERENCES = "loginpreference" ;

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d("log", "Refreshed token: " + refreshedToken);
        SharedPreferences sharedPreferences=getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("fcm", ""+refreshedToken);
        editor.commit();
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}