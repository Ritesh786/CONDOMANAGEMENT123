package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.util.ImageDownloader;

import org.json.JSONObject;

public class MyCouponsDetailsActivity extends Activity{
	
	TextView coupon_code,holder_name,Xtend_username,coupon_date,email_id,mycoupontitle,store_name,
	         coupon_address1,coupon_address2,coupon_city_name,coupon_zip_code,coupon_phn;
	ImageView coupon_barcode_img,coupon_store_img,my_deal_and_dis_back;
	WebView coupon_terms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_coupons_layout);
		coupon_code = (TextView)findViewById(R.id.mycoupon_code_id);
		holder_name = (TextView)findViewById(R.id.mycoupon_holdername_txt);
		Xtend_username = (TextView)findViewById(R.id.mycoupon_xtend_username_txt);
		coupon_date = (TextView)findViewById(R.id.mycoupon_date_of_printing_txt);
		email_id = (TextView)findViewById(R.id.mycoupon_holder_email_txt);
		mycoupontitle = (TextView)findViewById(R.id.mycoupon_title);
		store_name = (TextView)findViewById(R.id.mycoupon_store_name);
		coupon_address1 = (TextView)findViewById(R.id.mycoupon_address1);
		coupon_address2 = (TextView)findViewById(R.id.mycoupon_address2);
		coupon_city_name = (TextView)findViewById(R.id.mycoupon_city_name);
		coupon_zip_code = (TextView)findViewById(R.id.mycoupon_zipcode);
		coupon_phn = (TextView)findViewById(R.id.mycoupon_phonenumber);
		coupon_terms = (WebView)findViewById(R.id.mycoupon_terms);
		coupon_barcode_img = (ImageView)findViewById(R.id.profile_img);
		coupon_store_img = (ImageView)findViewById(R.id.mycoupon_image);
		my_deal_and_dis_back = (ImageView)findViewById(R.id.my_deal_and_dis_back);
		my_deal_and_dis_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		new MycouponResponse();
	}
	class MycouponResponse implements com.infoservices.lue.interfaces.ApiResponse{
		
		public MycouponResponse(){
			
			String URL = "coupon_detail.html/?id=" + getIntent().getStringExtra("TRANSID");
			new ApiService(MyCouponsDetailsActivity.this, this).execute(URL);
			Log.v("", ""+URL);
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			try {
				JSONObject obj = new JSONObject(result);
				JSONObject jsn_obj = obj.getJSONObject("response").getJSONObject("coupon_detail");
				for (int i = 0; i < jsn_obj.length(); i++) {
					String coupn_code = jsn_obj.getString("coupon_code");
					String barcode_image = jsn_obj.getString("qrcode_image");
					String store_img = jsn_obj.getString("store_image_path");
					String holdr_name = jsn_obj.getString("coupon_holder_name");
					String username = jsn_obj.getString("coupon_xtend_username");
					String transaction_date = jsn_obj.getString("transactiondate");
					String holder_email = jsn_obj.getString("coupon_holder_email");
					String Coupon_heading = jsn_obj.getString("deal_title");
					String Store_name = jsn_obj.getString("store_name");
					String city_name = jsn_obj.getString("city_name");
					String terms = jsn_obj.getString("terms_conditions");
					String address1 = jsn_obj.getString("address1");
					String address2 = jsn_obj.getString("address2");
					String Phone_number = jsn_obj.getString("phone_number");
					String Zip_code = jsn_obj.getString("zipcode");
					coupon_code.setText(coupn_code);
					holder_name.setText(holdr_name);
					Xtend_username.setText(username.trim());
					coupon_date.setText(transaction_date);
					email_id.setText(holder_email);
					mycoupontitle.setText(Coupon_heading);
					store_name.setText(Store_name);
					coupon_address1.setText(address1);
					coupon_address2.setText(address2);
					coupon_city_name.setText(city_name);
					if(terms.trim().length()<=0){
						LinearLayout layout = (LinearLayout) findViewById(R.id.terms_linearlayout);
						layout.setVisibility(View.GONE);
						TextView terms_txt = (TextView) findViewById(R.id.terms_txt);
						terms_txt.setVisibility(View.GONE);
					}else
					//coupon_terms.loadData(terms, "text/html", "utf-8");
					coupon_terms.loadDataWithBaseURL(null, terms, "text/html", "UTF-8", null);
					coupon_zip_code.setText("Pin : "+Zip_code);
					coupon_phn.setText("Tel : "+Phone_number);
					
					if(barcode_image == ""){
						
						
					}else{
					ImageDownloader imagedownloader = new ImageDownloader();
					imagedownloader.download(barcode_image, coupon_barcode_img);
					}if (store_img == "") {
						
					} else {
						ImageDownloader imagedownload = new ImageDownloader();
						imagedownload.download(store_img, coupon_store_img);
					}
				}
			} catch (Exception e) {}
			
			
		}
		
	}


}
