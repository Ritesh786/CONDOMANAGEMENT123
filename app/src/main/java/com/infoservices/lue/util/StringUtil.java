package com.infoservices.lue.util;

import android.graphics.Point;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.widget.EditText;

public class StringUtil {
	public final static boolean isValidEmail(String email) {
		if (email == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
	}

	public static Point getDisplaySize(final Display display) {
		Point point = new Point();
		point.x = display.getWidth();
		point.y = display.getHeight();
		return point;
		/*
		 * if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){ //API
		 * LEVEL 13 display.getSize(point); }else{ point.x = display.getWidth();
		 * point.y = display.getHeight(); } return point;
		 */
	}
	
	public static void setError(EditText edtText, String msg){
		edtText.setError(Html.fromHtml("<font color='black'>" +  msg + "</font>"));
	}


}
