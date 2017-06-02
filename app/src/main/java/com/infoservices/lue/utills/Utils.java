package com.infoservices.lue.utills;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = ( ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
				connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		}else{
			return false;
		}

	}
	public static boolean isGpsEnable(Context context){
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (statusOfGPS==true){
			return true;
		}else{
			return false;
		}
	}
	public static Bitmap decodeSampledBitmapFromPath(String pathName, int reqWidth, int reqHeight) {
	     // First decode with inJustDecodeBounds=true to check dimensions
	     final BitmapFactory.Options options = new BitmapFactory.Options();
	     options.inJustDecodeBounds = true;
	     BitmapFactory.decodeFile(pathName, options);

	     // Calculate inSampleSize
	     options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	     // Decode bitmap with inSampleSize set
	     options.inJustDecodeBounds = false;
	     return BitmapFactory.decodeFile(pathName, options);
	 }
	 public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	     // Raw height and width of image
	     final int height = options.outHeight;
	     final int width = options.outWidth;
	     int inSampleSize = 1;
	 
	     if (height > reqHeight || width > reqWidth) {
	         if (width > height) {
	             inSampleSize = Math.round((float)height / (float)reqHeight);
	         } else {
	             inSampleSize = Math.round((float)width / (float)reqWidth);
	         }
	     }
	     return inSampleSize;
	 }


}
