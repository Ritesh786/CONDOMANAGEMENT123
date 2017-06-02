package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.infoservices.lue.adapterdata.CouponlistData;
import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.arrayadapter.MycouponListAdapter;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.session.Session;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MycouponListActivity extends Activity {
	private ArrayList<CouponlistData>Mycoupon_list = new ArrayList<CouponlistData>();
	private ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.my_coupon_list_layout);
	    list = (ListView)findViewById(R.id.mycoupon_listView);
	    Button transaction_btn = (Button)findViewById(R.id.transaction_btn);
		ImageView back_mycou = (ImageView) findViewById(R.id.back_mycou);
		back_mycou.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	    
	    transaction_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent transaction_intent = new Intent(MycouponListActivity.this, TransactionHistoryActivity.class);
			startActivity(transaction_intent);
			}
		});
	    
	   // new Mycouponresponse();
	}
	class Mycouponresponse implements ApiResponse {
		
		public Mycouponresponse(){
			
			Session session = new Session(MycouponListActivity.this);
			final String USERID = session.getUSerId(); 
			String URl = "user-coupon.html/?id=" + USERID;
			new ApiService(MycouponListActivity.this, this).execute(URl);
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			Mycoupon_list.clear();
			try {
				JSONObject obj = new JSONObject(result);
				JSONObject jsn_obj = obj.getJSONObject("response");
				String user_credits = obj.getJSONObject("response").getString("user_credit_balance");
                TabActivity.user_credits = user_credits;
                TextView creditBalTxt = (TextView) findViewById(R.id.mycoupon_credit_balance);
        	    creditBalTxt.setText(" " + TabActivity.user_credits);
				JSONArray jsn_array = jsn_obj.getJSONArray("coupon_list");
				int size = jsn_array.length();
				for (int i = 0; i < size; i++) {
					CouponlistData couponlist = new CouponlistData();
					couponlist.setCoupon_details(jsn_array.getJSONObject(i).getString("deal_title"));
					couponlist.setTime(jsn_array.getJSONObject(i).getString("transactiondate"));
					couponlist.setCredits(jsn_array.getJSONObject(i).getString("credits"));
					couponlist.setSerial(jsn_array.getJSONObject(i).getString("coupon_code"));
					couponlist.setDealid(jsn_array.getJSONObject(i).getString("deal_id"));
					couponlist.setTransid(jsn_array.getJSONObject(i).getString("transaction_id"));
					Mycoupon_list.add(couponlist);
				}
				MycouponListAdapter adapter = new MycouponListAdapter(Mycoupon_list);
				list.setAdapter(adapter);
				TextView nodata_txt = (TextView) findViewById(R.id.mycoupon_empty_txt);
				if(size > 0){
					nodata_txt.setText("");
				}else{
					nodata_txt.setText(R.string.nodata);
				}
			} catch (Exception e) {}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Mycouponresponse();
	}
}
