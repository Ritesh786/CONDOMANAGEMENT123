package com.infoservices.lue.log;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Logger {
	public final static void Log(String tag, String msg){
		Log.i(tag, msg);
	}
	
	public final static void showCenterToast(String msg, Context context){
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public final static void showTopToast(String msg, Context context){
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.show();
	}
	
	public final static void showToast(String msg, Context context){
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
