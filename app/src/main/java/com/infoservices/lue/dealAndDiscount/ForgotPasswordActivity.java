/**
 * 
 */
package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.util.SharedPrefs;
import com.infoservices.lue.util.StringUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ndot
 *
 */
public final class ForgotPasswordActivity extends Activity implements ApiResponse {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password_layout);
		SharedPrefs.setLanguage(getBaseContext());
		final EditText email_edt = (EditText) findViewById(R.id.email_edt);
		Button submit_btn = (Button) findViewById(R.id.forgot_submit);
		Button cancel_btn = (Button) findViewById(R.id.forgot_cancel);
		ImageView back_forget_pass = (ImageView) findViewById(R.id.back_forget_pass);
		back_forget_pass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		final ApiResponse apiResponse = this;
		submit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String EMAIL_TXT = email_edt.getText().toString().trim();
				if(EMAIL_TXT.length() <=0 ){
					StringUtil.setError(email_edt,""+R.string.emailval);
					
				}else if(!StringUtil.isValidEmail(EMAIL_TXT)){
					StringUtil.setError(email_edt, ""+R.string.emailidval);
				
				}else{
					String email = email_edt.getText().toString().trim();
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				    nameValuePairs.add(new BasicNameValuePair("email", email));
					new ApiService(ForgotPasswordActivity.this, apiResponse, true, nameValuePairs).execute("forget-password.html");
				}
			}
		});
		
		cancel_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	public void getResult(boolean isSuccess, String result) {
		// TODO Auto-generated method stub
		if(isSuccess){
			Logger.Log("REsult", result);
			try {
				JSONObject obj = new JSONObject(result).getJSONObject("response");
				final String MSG = obj.getString("Message");
				final String HTTPCODE = obj.getString("httpCode");
				Logger.showCenterToast(MSG, ForgotPasswordActivity.this);
				if(HTTPCODE.equals("202")){
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Logger.showCenterToast(result, ForgotPasswordActivity.this);
		}
	}

}
