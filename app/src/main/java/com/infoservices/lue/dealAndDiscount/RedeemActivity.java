package com.infoservices.lue.dealAndDiscount;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.ImageDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class RedeemActivity extends FragmentActivity implements ApiResponse, OnMapReadyCallback {
	private TextView txtviewdes, txtviewterms, mDealTitle, mDiscountTxt,
			mCrditTxt, mRwrdsTxt, mcrdt_balTxt, txttimer,txtviewtermscondition;
	private ArrayList<String> mDeal_url = new ArrayList<String>();
	ImageDownloader downloader;// = new ImageDownloader();
	WebView txtshow;
	private ImageView mImgView;
	long dsecs, dminutes, dhours;
	private  String mDescrptn, mTerms_Condtn, DEAL_KEY, DEAL_ID, sharelink,mTermscondition;
	private GoogleMap googleMap;
	private String mCredit = "0", limit, minlimit, remaindeal, reward;
	Timer timer = new Timer();
	boolean mapavailable = false;
	//private SocialAuthAdapter social;
	
	private int nolimitstatus=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redeem_layout);
		downloader = new ImageDownloader();
		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		// Showing status
		//social = new SocialAuthAdapter(new ResponseListener());
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			/* * int requestCode = 10; Dialog dialog =
			 * GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			 * dialog.show();*/

			txtviewtermscondition=(TextView)findViewById(R.id.tterms);
			txtviewdes = (TextView) findViewById(R.id.txtdes);
			mDealTitle = (TextView) findViewById(R.id.deal_title);
			txtviewterms = (TextView) findViewById(R.id.txtterms);
			txtshow = (WebView) findViewById(R.id.showdesandterms);
			mImgView = (ImageView) findViewById(R.id.imgredeem);
			mDiscountTxt = (TextView) findViewById(R.id.discount);
			mCrditTxt = (TextView) findViewById(R.id.credits);
			mRwrdsTxt = (TextView) findViewById(R.id.rewards);
			txttimer = (TextView) findViewById(R.id.time);
			mcrdt_balTxt = (TextView) findViewById(R.id.redeem_txtavailablecredit);
			mcrdt_balTxt.setText(TabActivity.user_credits + " "
					+ getString(R.string.available_credits));
			TextView redeem_btn = (TextView) findViewById(R.id.redeemnow_btn);

			txtviewdes.setClickable(false);
			txtviewterms.setClickable(true);
			txtviewtermscondition.setClickable(true);
			
			txtviewdes.setBackgroundResource(R.color.caldroid_white);
			txtviewterms
					.setBackgroundResource(R.color.caldroid_gray);
			//DEAL_ID = getIntent().getStringExtra("DEALID");
			//DEAL_KEY = getIntent().getStringExtra("DEALKEY");

			redeem_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(RedeemActivity.this,
							RedeemNowActivity.class);
					Log.d("deal id===",""+DEAL_ID);
					intent.putExtra("DEALID", DEAL_ID);
					intent.putExtra("DEALKEY", DEAL_KEY);
					intent.putExtra("CREDITPOINT", mCredit);
					intent.putExtra("MAXLIMIT", limit);
					intent.putExtra("MINLIMIT", minlimit);
					intent.putExtra("DEALAVAIL", remaindeal);
					intent.putExtra("DEALTITLE", mDealTitle.getText()
							.toString().trim());
					startActivity(intent);
				}
			});

			// String url = "deal-detail.html/?id=" + DEAL_ID;
			// new ApiService(this, this).execute(url);
		} else { // Google Play Services are available
			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			fm.getMapAsync(RedeemActivity.this);
			mapavailable = true;
			txtviewdes = (TextView) findViewById(R.id.txtdes);
			mDealTitle = (TextView) findViewById(R.id.deal_title);
			txtviewterms = (TextView) findViewById(R.id.txtterms);
			txtshow = (WebView) findViewById(R.id.showdesandterms);
			mImgView = (ImageView) findViewById(R.id.imgredeem);
			mDiscountTxt = (TextView) findViewById(R.id.discount);
			mCrditTxt = (TextView) findViewById(R.id.credits);
			mRwrdsTxt = (TextView) findViewById(R.id.rewards);
			txttimer = (TextView) findViewById(R.id.time);
			txtviewtermscondition=(TextView)findViewById(R.id.tterms);
			mcrdt_balTxt = (TextView) findViewById(R.id.redeem_txtavailablecredit);
			mcrdt_balTxt.setText(TabActivity.user_credits + " "
					+ getString(R.string.available_credits));

			TextView redeem_btn = (TextView) findViewById(R.id.redeemnow_btn);

			txtviewdes.setClickable(false);
			txtviewterms.setClickable(true);
			txtviewtermscondition.setClickable(true);
			txtviewdes.setBackgroundResource(R.color.caldroid_white);
			txtviewterms
					.setBackgroundResource(R.color.caldroid_gray);
			txtviewtermscondition.setBackgroundResource(R.color.caldroid_gray);

			// final String DEAL_ID = getIntent().getStringExtra("DEALID");
			// final String DEAL_KEY = getIntent().getStringExtra("DEALKEY");

			redeem_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(nolimitstatus==0){
					if (remaindeal != "0") {
						Intent intent = new Intent(RedeemActivity.this,
								RedeemNowActivity.class);
						intent.putExtra("DEALID", DEAL_ID);
						intent.putExtra("DEALKEY", DEAL_KEY);
						intent.putExtra("CREDITPOINT", mCredit);
						intent.putExtra("MAXLIMIT", limit);
						intent.putExtra("MINLIMIT", minlimit);
						intent.putExtra("DEALAVAIL", remaindeal);
						// intent.putExtra("REWARD", reward);
						// Toast.makeText(getApplicationContext(), reward,
						// Toast.LENGTH_SHORT).show();
						intent.putExtra("DEALTITLE", mDealTitle.getText()
								.toString().trim());
						startActivity(intent);
					}
					
					else
					{
						Toast.makeText(getApplicationContext(),
								R.string.dealnor,
								Toast.LENGTH_SHORT).show();
					}
					
				}
				else{
						
					
					Intent intent = new Intent(RedeemActivity.this,
							RedeemNowActivity.class);
					intent.putExtra("DEALID", DEAL_ID);
					intent.putExtra("DEALKEY", DEAL_KEY);
					intent.putExtra("CREDITPOINT", mCredit);
					intent.putExtra("MAXLIMIT", limit);
					intent.putExtra("MINLIMIT", minlimit);
					intent.putExtra("DEALAVAIL", remaindeal);
					// intent.putExtra("REWARD", reward);
					// Toast.makeText(getApplicationContext(), reward,
					// Toast.LENGTH_SHORT).show();
					intent.putExtra("DEALTITLE", mDealTitle.getText()
							.toString().trim());
					startActivity(intent);
					
					}
					
				}
			});

			// String url = "deal-detail.html/?id=" + DEAL_ID;
			// new ApiService(this, this).execute(url);
		}
	}

	public void descrip_click(View v) {
		txtviewdes.setClickable(false);
		txtviewterms.setClickable(true);
		txtviewtermscondition.setClickable(true);
		txtviewdes.setBackgroundResource(R.color.caldroid_white);
		txtviewterms.setBackgroundResource(R.color.caldroid_gray);
		txtviewtermscondition.setBackgroundResource(R.color.caldroid_gray);
		// txtshow.loadData(mDescrptn, "text/html", "urf-8");
		txtshow.loadDataWithBaseURL(null, mDescrptn, "text/html", "UTF-8", null);
	}
	public void termscondition_click(View v) {
		txtviewtermscondition.setClickable(false);
		txtviewdes.setClickable(true);
		txtviewterms.setClickable(true);
		
		txtviewdes.setBackgroundResource(R.color.caldroid_gray);
		txtviewterms.setBackgroundResource(R.color.caldroid_gray);
		txtviewtermscondition.setBackgroundResource(R.color.caldroid_white);
		// txtshow.loadData(mDescrptn, "text/html", "urf-8");
		txtshow.loadDataWithBaseURL(null,mTermscondition, "text/html", "UTF-8", null);
	}

	public void terms_click(View v) {
		txtviewterms.setClickable(false);
		txtviewdes.setClickable(true);
		txtviewtermscondition.setClickable(true);
		
		txtviewdes.setBackgroundResource(R.color.caldroid_gray);
		txtviewterms.setBackgroundResource(R.color.caldroid_white);
		txtviewtermscondition.setBackgroundResource(R.color.caldroid_gray);
		// txtshow.loadData(mTerms_Condtn, "text/html", "urf-8");
		txtshow.loadDataWithBaseURL(null, mTerms_Condtn, "text/html", "UTF-8",
				null);
	}

	public void fb_click(View v) {
		// Intent intent = new Intent(getApplicationContext(),
		// WebActivity.class);
		// intent.putExtra("isFacebook", true);
		// intent.putExtra("isTwitter", false);
		// intent.putExtra("isGoogle", false);
		// String passurl = "http://www.facebook.com/sharer.php?u=" + sharelink;
		// intent.putExtra("Share", passurl);
		// startActivity(intent);
		Log.d("facebookworking","facebookworking");
		
		
	//	
		try {
			Intent intent1 = new Intent();
			intent1.setClassName("com.facebook.katana",
					"com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
			Log.i("exception1","---------->");
			intent1.setAction("android.intent.action.SEND");
			Log.i("exception2","---------->");
			intent1.setType("text/plain");
			Log.i("exception3","---------->");
			intent1.putExtra("android.intent.extra.TEXT", sharelink);
			Log.i("exception4","---------->");
			startActivity(intent1);
			// finish();
		} catch (Exception e) {
			// If we failed (not native FB app installed), try share through
			// SEND
try{
			Intent intent = new Intent(Intent.ACTION_SEND);
			String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u="
					+ sharelink;
		//Intent  intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
			startActivity(intent);
}catch(ActivityNotFoundException afe){
	
}
			// finish();
		}
		//social.authorize(RedeemActivity.this, Provider.FACEBOOK);
	}

	public void twitter_click(View v) {
		// Intent intent = new Intent(getApplicationContext(),
		// WebActivity.class);
		// String passurl = "http://twitter.com/share?url=" + sharelink;
		// intent.putExtra("isFacebook", false);
		// intent.putExtra("isTwitter", true);
		// intent.putExtra("isGoogle", false);
		// intent.putExtra("Share", sharelink);
		// startActivity(intent);
		Log.d("twitterworking","twitterworking");
		//social.authorize(RedeemActivity.this, Provider.TWITTER);

	}

	public void google_click(View v) {
		Intent intent = new Intent(getApplicationContext(), WebActivity.class);
		String passurl = "https://plus.google.com/share?url=" + sharelink;
		// intent.putExtra("isFacebook", false);
		// intent.putExtra("isTwitter", false);
		// intent.putExtra("isGoogle", true);
		intent.putExtra("Share", sharelink);
		startActivity(intent);

		
	}

	/*private final class ResponseListener implements Facebook.DialogListener {
		@Override
		public void onBack() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onComplete(Bundle arg0) {
			// TODO Auto-generated method stub
			//social.updateStatus(sharelink, new MessageListener(), true);
		}

		@Override
		public void onFacebookError(FacebookError e) {

		}

		@Override
		public void onError(DialogError e) {

		}

		@Override
		public void onError(SocialAuthError arg0) {
			// TODO Auto-generated method stub
		}
	}

	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onError(SocialAuthError arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onExecute(String arg0, Integer t) {
			// TODO Auto-generated method stub
			Integer status = t;
			Log.i("Status ", "status :" + t);
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204) {
				Toast.makeText(RedeemActivity.this, R.string.sharesuccess,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(RedeemActivity.this, R.string.shareerror,
						Toast.LENGTH_LONG).show();
			}
		}
	}
*/
	@Override
	public void getResult(boolean isSuccess, String result) {
		// TODO Auto-generated method stub
		if (isSuccess) {
			try {
				JSONObject jobj = new JSONObject(result).getJSONObject(
						"response").getJSONObject("deal_list");
				String user_credits = new JSONObject(result).getJSONObject(
						"response").getString("user_credit_balance");
				reward = new JSONObject(result).getJSONObject("response")
						.getString("user_reward_balance");
				TabActivity.user_credits = user_credits;
				TabActivity.reward_points = reward;
				mcrdt_balTxt.setText(TabActivity.user_credits + " "
						+ getString(R.string.available_credits));
				final String DEAL_TITLE = jobj.getString("deal_title");
				mDescrptn = jobj.getString("deal_description");
				DEAL_KEY = jobj.getString("deal_key");
				DEAL_ID = jobj.getString("deal_id");
				// mTerms_Condtn = jobj.getString("terms_conditions");
				mTerms_Condtn = jobj.getString("highlights");
				mTermscondition=jobj.getString("terms_conditions");
				sharelink = jobj.getString("image_link");
				String[] date = jobj.getString("enddate_format2").split(":");
				final String DEAL_IMG = jobj.getString("image_src");
				JSONArray img_ary = jobj.getJSONArray("deal_images");
				// deal_multiple_img =
				mDeal_url.add(DEAL_IMG);
				int dealsize = img_ary.length();
				if (img_ary != null && dealsize > 0) {
					for (int i = 0; i < dealsize; i++) {
						String deal_img = img_ary.get(i).toString();

						  mDeal_url.add(
						  "http://192.168.1.74:1008/images/deals/510_330/K1HhAmlw_2.png"
						 ); mDeal_url.add(
						 "http://192.168.1.74:1008/images/deals/510_330/K1HhAmlw_3.png"
						 );


						 String[] img_list = deal_img.split(",");
						 if(img_list.length > 0 )
						 mDeal_url.add(Arrays.toString(img_list));

						mDeal_url.add(deal_img);
					}
				}
				final String DEAL_DSCUNT = jobj.getString("discount");
				final String DEAL_RWRDS = jobj.getString("rewards");
				mCredit = jobj.getString("minimum_credit_value");
				limit = jobj.getString("maximum_deals_limit");
				minlimit = jobj.getString("minimum_deals_limit");
				remaindeal = jobj.getString("coupons_available");
				nolimitstatus=jobj.getInt("no_limit");
				JSONArray jary = jobj.getJSONArray("storeresult");

				int size = jary.length();
				for (int i = 0; i < size; i++) {

					String lat = jary.getJSONObject(i).getString("latitude");
					String lng = jary.getJSONObject(i).getString("longitude");
					String shopname = jary.getJSONObject(i).getString(
							"shopname");
					String address1 = jary.getJSONObject(i).getString(
							"address1");
					String address2 = jary.getJSONObject(i).getString(
							"address2");
					String city_name = jary.getJSONObject(i).getString(
							"city_name");
					 String country_name =
					 jary.getJSONObject(i).getString("country_name");
					if (mapavailable == true) {
						if (!address1.endsWith(","))
							address1 += ",";

						double latitude = 0;
						double longitude = 0;
						try {
							latitude = Double.parseDouble(lat);
							longitude = Double.parseDouble(lng);
							LatLng latLng = new LatLng(latitude, longitude);
							googleMap.addMarker(new MarkerOptions()
									.position(latLng)
									.title(shopname)
									.snippet(
											address1 + address2 + ","
													+ city_name
																 +","+country_name
																 ));
							if (i == 0)
								googleMap.moveCamera(CameraUpdateFactory
										.newLatLng(latLng));
						} catch (Exception e) {
						}
						googleMap.animateCamera(CameraUpdateFactory.zoomTo(6));
					}

				}
				mapavailable = false;
				mDealTitle.setText(DEAL_TITLE);// deal title
				// txtshow.loadData(mDescrptn, "text/html",
				// "utf-8");//description
				txtshow.loadDataWithBaseURL(null, mDescrptn, "text/html",
						"UTF-8", null);
				mRwrdsTxt.setText( DEAL_RWRDS);
				mCrditTxt.setText(mCredit);
				mDiscountTxt.setText( DEAL_DSCUNT);
				Calendar calendar1 = Calendar.getInstance();
				Calendar calendar2 = Calendar.getInstance();
				String timeStamp = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss")
						.format(Calendar.getInstance().getTime());
				String[] cdate = timeStamp.split(":");
				calendar1.set(Integer.parseInt(cdate[0]),
						Integer.parseInt(cdate[1]), Integer.parseInt(cdate[2]),
						Integer.parseInt(cdate[3]), Integer.parseInt(cdate[4]),
						Integer.parseInt(cdate[5]));
				calendar2.set(Integer.parseInt(date[0]),
						Integer.parseInt(date[1]), Integer.parseInt(date[2]),
						Integer.parseInt(date[3]), Integer.parseInt(date[4]),
						Integer.parseInt(date[5]));
				long milsecs1 = calendar1.getTimeInMillis();
				long milsecs2 = calendar2.getTimeInMillis();
				long diff = milsecs2 - milsecs1;
				dsecs = (diff / 1000) % 60;
				dminutes = diff / (60 * 1000) % 60;
				dhours = diff / (60 * 60 * 1000);
				txttimer.setText(dhours + ":" + dminutes + ":" + dsecs);
				timer.schedule(new firstTask(), 0, 1000);

				//downloader.download(DEAL_IMG, mImgView);
				Picasso.with(RedeemActivity.this)
						.load(DEAL_IMG)
						.placeholder(R.drawable.lobo2)   // optional
						.error(R.drawable.lobo2)     // optional
						.into(mImgView);
				startSlideShow();

			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Logger.showCenterToast(result, RedeemActivity.this);
		}
	}

	int index = 0;

	private void startSlideShow() {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				index++;
				if (index >= mDeal_url.size())
					index = 0;
				final String url = mDeal_url.get(index).toString();
				System.out.println("url....." + url);
				if (url != null && url.length() > 0) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//downloader.download(url, mImgView);
							Picasso.with(RedeemActivity.this)
									.load(url)
									.placeholder(R.drawable.lobo2)   // optional
									.error(R.drawable.lobo2)     // optional
									.into(mImgView);
						}
					});
				}
			}
		};

		if (mDeal_url.size() > 1)
			timer.scheduleAtFixedRate(task, 3000, 3000);

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}

	class firstTask extends TimerTask {

		public void run() {
			// Log.v("%%In Hours%%", ""+dhours+":"+dminutes+":"+dsecs);
			dsecs = dsecs - 1;
			if (dsecs == 0) {
				if (dminutes > 0) {
					dminutes = dminutes - 1;
					dsecs = 59;
				}
			}
			if (dminutes == 0) {
				// dsecs=59;
				if (dhours > 0) {
					dminutes = 59;
					dhours = dhours - 1;
				}
			}
			if (dhours == 0 && dminutes == 0 && dsecs == 0) {
				timer.cancel();
				timer = null;
				// timer.purge();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					txttimer.setText(dhours + ":" + dminutes + ":" + dsecs);

				}
			});
			// h2.sendEmptyMessage(0);
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		timer = null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		final String DEAL_ID = getIntent().getStringExtra("DEALID");
		Session sess = new Session(getApplicationContext());
		// final String DEAL_KEY = getIntent().getStringExtra("DEALKEY");
		String url = "deal-detail.html/?id=" + DEAL_ID + "&user_id="
				+ sess.getUSerId();
		System.out.println("dealdetailurl"+url);

		new ApiService(RedeemActivity.this,this).execute(url);


		/* * if(mcrdt_balTxt != null)
		 * mcrdt_balTxt.setText(TabActivity.user_credits + " " +
		 * getString(R.string.available_credits));*/

	}
}
