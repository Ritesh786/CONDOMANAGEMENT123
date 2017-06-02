package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.NetworkUtil;
import com.infoservices.lue.util.SharedPrefs;

public class SplashActivity extends Activity {

	// used to know if the back button was pressed in the splash screen activity
	// and avoid opening the next activity
	private boolean mIsBackButtonPressed;
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPrefs.setLanguage(getBaseContext());
		setContentView(R.layout.splash_layout);

		new Handler().postDelayed(new Runnable() {

			// Showing splash screen with a timer.
			@Override
			public void run() {
				// This method will be executed once the timer is over
				if (!mIsBackButtonPressed) {
					if (!NetworkUtil.isOnline(SplashActivity.this)) {
						Logger.showCenterToast( getString(R.string.interneterror1),
								SplashActivity.this);
						// finish();
					} else {
						Session session = new Session(SplashActivity.this);
						String userid = session.getUSerId();
						if (userid != null && userid.length() > 0) {
							startActivity(new Intent(SplashActivity.this,
									com.infoservices.lue.dealAndDiscount.TabActivity.class));
						} else {
							startActivity(new Intent(SplashActivity.this,
									com.infoservices.lue.dealAndDiscount.LoginActivity.class));
						}
					}
				}
				// make sure we close the splash screen so the user won't come
				// back
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	@Override
	public void onBackPressed() {
		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}
}
