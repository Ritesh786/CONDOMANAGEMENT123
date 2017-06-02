package com.infoservices.lue.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class SharedPrefs {

	public static final String PREFS_NAME = "MyPrefsFile";  
	private static String PREF_LANGUAGE = "uNameS";
	 
	private static SharedPreferences mPreferences;
 
    
	public static void setLanguage(final Context context,final String language) {
		mPreferences = context.getSharedPreferences("MyPrefsFile", 0);
		SharedPreferences.Editor editor1 = mPreferences.edit();
		editor1.putString(PREF_LANGUAGE, language);
		editor1.commit();
	}
	 

	public static void clear(final Context context) {
		mPreferences = context.getSharedPreferences("MyPrefsFile", 0);
		SharedPreferences.Editor editor1 = mPreferences.edit();
		editor1.clear();
		editor1.commit();
	}

	public static void setLanguage(final Context context) {
		 
		mPreferences = context.getSharedPreferences("MyPrefsFile", 0);
		String language = mPreferences.getString(PREF_LANGUAGE, "");
		if(language == null || language.equals("")){
			 
		}
		else
		setLocale(context,language); 
	}

	 public static void setLocale(final Context context,String lang) {
	       Locale myLocale = new Locale(lang);
	       Resources res = context.getResources();
	       DisplayMetrics dm = res.getDisplayMetrics();
	       Configuration conf = res.getConfiguration();
	       conf.locale = myLocale;
	       res.updateConfiguration(conf, dm); 
	    }
}
