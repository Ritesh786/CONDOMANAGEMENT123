package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.arrayadapter.DealListAdapter;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.location.GPSTracker;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends Activity{
	private ArrayList <String> mDealName = new ArrayList<String>();
	private ArrayList <String> mDealDescription = new ArrayList<String>();
	private ArrayList <String> mDealCredits = new ArrayList<String>();
	private ArrayList <String> mDealKms = new ArrayList<String>();
	private ArrayList <String> mDealImg = new ArrayList<String>();
	private ArrayList <String> mDealId = new ArrayList<String>();
	private ArrayList <String> mDealKey = new ArrayList<String>();
	private ListView mList;
	private DealListAdapter mAdapter;
	private TextView new_tab, atoz_tab, nearby_tab;
	private int index = 3; //nearby tab 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPrefs.setLanguage(getBaseContext());
		setContentView(R.layout.home_layout);
		new_tab = (TextView) findViewById(R.id.new_tab);
		atoz_tab = (TextView) findViewById(R.id.atoz_tab);
		nearby_tab = (TextView) findViewById(R.id.nearby_tab);
		mList = (ListView) findViewById(R.id.deal_listview);
		
		TextView nodata_txt = (TextView) findViewById(R.id.empty);
		mList.setEmptyView(nodata_txt);
		
		mAdapter = new DealListAdapter(mDealName, mDealDescription, mDealCredits, mDealKms, mDealImg, mDealId);
		mList.setAdapter(mAdapter);
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent redeem_intent = new Intent(HomeActivity.this, RedeemActivity.class);
				redeem_intent.putExtra("DEALID", mDealId.get(arg2).toString());
				redeem_intent.putExtra("DEALKEY", mDealKey.get(arg2).toString());
				startActivity(redeem_intent);
			}
		});
		
		TextView changeLanugae = (TextView) findViewById(R.id.home_change_language);
		changeLanugae.setOnClickListener(new OnClickListener() {

            public void onClick(final View arg0) {
            	changeLanguageDialog();
            }
		});
		
		new_tab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 1;
				changeImage();
			}
		});
		
		atoz_tab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 2;
				changeImage();
			}
		});

		nearby_tab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = 3;
				changeImage();
			}
		});
		
		String url = getIntent().getStringExtra("SEARCH_URL");
		if(url != null && url.length() > 0){
			index = 4;
			//new DealList(url);
		}
		
		new MyProfile();
		
	}
	
	public void onChangeLanguage(View ve){
		changeLanguageDialog();
	}
	private class DealList implements ApiResponse {
		
		public DealList(String type){
			new ApiService(HomeActivity.this, this).execute("deals.html"+type);
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
				try {
					
					mDealName.clear();
					mDealDescription.clear();
					mDealCredits.clear();
					mDealKms.clear();
					mDealImg.clear();
					mDealId.clear();
					mDealKey.clear();
					mAdapter.update(mDealName, mDealDescription, mDealCredits, mDealKms, mDealImg, mDealId);
					
					JSONObject jobj = new JSONObject(result);
					JSONArray jary = jobj.getJSONObject("response").getJSONArray("deal_detail");
					 String user_credits = jobj.getJSONObject("response").getString("user_credit_balance");
                     TabActivity.user_credits = user_credits;
                     TextView crdts_txt = (TextView) findViewById(R.id.home_usercredits_txt);
             		crdts_txt.setText(TabActivity.user_credits + " " + getString(R.string.available_credits));
					int size = jary.length();
					for(int i = 0; i < size; i++){
						final String DNAME = jary.getJSONObject(i).getString("deal_title");
						final String DDSCRPTN = jary.getJSONObject(i).getString("city_name") + 
							", " + jary.getJSONObject(i).getString("state_name") + ", " 
								+ jary.getJSONObject(i).getString("country_name");
						final String DCREDITS = jary.getJSONObject(i).getString("minimum_credit_value");
						final String DKMS = jary.getJSONObject(i).getString("lat_long_distance");
						final String DIMAGE = jary.getJSONObject(i).getString("image_src");
						final String DId = jary.getJSONObject(i).getString("deal_id");
						final String Dkey = jary.getJSONObject(i).getString("deal_key");
						mDealName.add(DNAME);
						mDealDescription.add(DDSCRPTN);
						mDealCredits.add(DCREDITS + " Credits");
						mDealKms.add(DKMS + " km");
						mDealImg.add(DIMAGE);
						mDealId.add(DId);
						mDealKey.add(Dkey);
					}
					mAdapter.update(mDealName, mDealDescription, mDealCredits, mDealKms, mDealImg, mDealId);
					TextView nodata_txt = (TextView) findViewById(R.id.empty);
					if(size > 0){
						mList.setSelection(0);
						nodata_txt.setText("");
					}else{
						nodata_txt.setText(R.string.nodata);
					}
					mList.invalidate();
					
				} catch (JSONException e) {}
				
			}else{
				Logger.showCenterToast(result, HomeActivity.this);
			}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		changeImage();
	}
	
	private void changeImage(){
		
		GPSTracker gpstracker = new GPSTracker(HomeActivity.this);
		if(gpstracker.canGetLocation()){
			double lat = gpstracker.getLatitude();
			double lng = gpstracker.getLongitude();
			Session sess=new Session(HomeActivity.this);
			String uid = sess.getUSerId();
			
			//lat = 3.187308f; //near by testing
			//lng = 101.703697f;
		
		switch (index) {
		
		case 1: //new tab
			//new_tab.setBackgroundResource(R.drawable.white_button);
			//atoz_tab.setBackgroundResource(R.drawable.white_button);
			//nearby_tab.setBackgroundResource(R.drawable.white_button);
			new DealList("/?deal_filter=new"+"&latitude=" + lat + "&longtitude=" + lng + "&user_id=" + uid);
			break;

		case 2: //a-z tab
			//new_tab.setBackgroundResource(R.drawable.white_button);
			//atoz_tab.setBackgroundResource(R.drawable.white_button);
			//nearby_tab.setBackgroundResource(R.drawable.white_button);
			new DealList("/?deal_filter=a-z"+"&latitude=" + lat + "&longtitude=" + lng+ "&user_id=" + uid);
			break;

		case 3: //nearby tab
			/*//get the current gps location.
			GPSTracker gpstracker = new GPSTracker(HomeActivity.this);
			if(gpstracker.canGetLocation()){
				double lat = gpstracker.getLatitude();
				double lng = gpstracker.getLongitude();*/
				new DealList("/?latitude=" + lat + "&longtitude=" + lng + "&user_id=" + uid+ "&distance=100"+"&deal_filter=distance");
				//new_tab.setBackgroundResource(R.drawable.white_button);
				//atoz_tab.setBackgroundResource(R.drawable.white_button);
				//nearby_tab.setBackgroundResource(R.drawable.white_button);
			/*}else{
				gpstracker.showSettingsAlert();
			}*/
			break;
	
		case 4: //from search activity
			String url = getIntent().getStringExtra("SEARCH_URL");
			if(url != null && url.length() > 0){
				///new_tab.setBackgroundResource(R.drawable.white_button);
				//atoz_tab.setBackgroundResource(R.drawable.white_button);
				//nearby_tab.setBackgroundResource(R.drawable.white_button);
				new DealList(url + "&latitude=" + lat + "&longtitude=" + lng+ "&user_id=" + uid);
			}
			break;
		}
		}else{
			gpstracker.showSettingsAlert();
		}
	}
	
	private void changeLanguageDialog()
    {
		final AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
		String[] Language = new String[3];
		Language[0] =getString(R.string.lang1); 
		Language[1] =getString(R.string.lang2);
		Language[2] =getString(R.string.lang3); 
		alert.setTitle(R.string.changelanugae);
		LinearLayout layout = new LinearLayout(HomeActivity.this);
	    layout.setOrientation(LinearLayout.VERTICAL);
	    alert.setMessage(R.string.selectLanguage);
	    
	    //final EditText table = new EditText(Order.this);
	    //table.setHint("Enter table no");
	    final Spinner languageList = new Spinner(HomeActivity.this); 
	    final ArrayAdapter<String> adp = new ArrayAdapter<String>(HomeActivity.this,
	            android.R.layout.simple_spinner_item, Language);
	    adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
	    languageList.setAdapter(adp); 
	   
	    layout.addView(languageList);

	    alert.setView(layout);
	    
	    
		alert.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
							if(languageList.getSelectedItemPosition() == 0)
						      setLocale("en");
							else if(languageList.getSelectedItemPosition() == 1)
								 setLocale("in");
							else if(languageList.getSelectedItemPosition() == 2)
								 setLocale("zh");
							}
				});

		alert.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						// Canceled.
						dialog.cancel();
					}
				});

		alert.show();
    }
	public void setLocale(String lang) {
    	SharedPrefs.setLanguage(getBaseContext(),lang);
       Locale myLocale = new Locale(lang);
       Resources res = getResources();
       DisplayMetrics dm = res.getDisplayMetrics();
       Configuration conf = res.getConfiguration();
       conf.locale = myLocale;
       res.updateConfiguration(conf, dm);
       Intent refresh = new Intent(this, TabActivity.class);
       startActivity(refresh);
       finish();
    }
	private class MyProfile implements ApiResponse{
		
		public MyProfile() {
			// TODO Auto-generated constructor stub
			Session session = new Session(HomeActivity.this);
			final String USERID = session.getUSerId(); 
			final String URL = "user_profile.html/?id=" + USERID;
			new ApiService(HomeActivity.this, this).execute(URL);
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
				try {
					JSONObject jobj = new JSONObject(result).getJSONObject("response").getJSONObject("user_detail");
					final String USER_CREDITS = jobj.getString("credit_balance");
					final String RWRD_POINTS = jobj.getString("reward_balance");
					final String FIRST_NAME = jobj.getString("firstname");
					
					TabActivity.reward_points = RWRD_POINTS; 
					TabActivity.user_credits = USER_CREDITS;
					TabActivity.user_name = FIRST_NAME;
					
					TextView crdts_txt = (TextView) findViewById(R.id.home_usercredits_txt);
					crdts_txt.setText(USER_CREDITS + " Credits " );
					
				} catch (JSONException e) {}
			}
		}
	}
	
}
