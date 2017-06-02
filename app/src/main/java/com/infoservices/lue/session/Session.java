package com.infoservices.lue.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {
	// Sharedpref file name
	private final String PREF_NAME = "MONZTER";
	// Shared Preferences
	private SharedPreferences mSharedpreference;
	// Editor for Shared preferences
	private Editor mEditor;
	// Context
	private Context mContext;
	// Shared pref mode
    final int PRIVATE_MODE = 0;
    //Userid
    private final String KEY_USERID = "USERID";
	
	public Session(Context context){
		mContext = context;
		mSharedpreference = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		mEditor = mSharedpreference.edit();
	}
	
	public void saveUserId(String userid){
		mEditor.putString(KEY_USERID, userid);
		mEditor.commit();
	}
	
	public String getUSerId(){
		return mSharedpreference.getString(KEY_USERID, "");
	}
	
	public void clearAll(){
		mEditor.clear();
		mEditor.commit();
	}
}
