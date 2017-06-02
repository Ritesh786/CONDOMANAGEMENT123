/**
 * 
 */
package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.socialMedia.Callback;
import com.infoservices.lue.socialMedia.FacebookProcess;
import com.infoservices.lue.socialMedia.SocialMedia;
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
public final class LoginActivity extends Activity implements Callback<SocialMedia> {
	private EditText mUserName_Edt, mPassword_Edt;
	private Facebook mFacebook;
	 private static final String APP_ID = "1589864331276091";
	//private static final String APP_ID = "914859735331080";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPrefs.setLanguage(getBaseContext());
		 
	    setContentView(R.layout.login_layout);
		Button signin_btn = (Button) findViewById(R.id.signin_btn);
		TextView forgot_btn = (TextView) findViewById(R.id.forgot_pwd);
		mUserName_Edt = (EditText) findViewById(R.id.username);
		mPassword_Edt = (EditText) findViewById(R.id.password);
		
		mPassword_Edt.setTypeface(Typeface.DEFAULT);
		
		signin_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isEmailAndPwdValid())
					new Login();
			}
		});
		
		forgot_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
				mUserName_Edt.setText("");
				mPassword_Edt.setText("");
				mUserName_Edt.setError(null);
				mPassword_Edt.setError(null);
			}
		});
		
	}
	
	private boolean isEmailAndPwdValid(){
		
		boolean isValid = true;
        String msg = "Enter The Password";
		String ms1 = "Enter The Email Id";
		String msg2 = "Enter the valid email id";
		String msg3 = "Password length should be minimum 4 characters";
		
		if(mUserName_Edt != null && mPassword_Edt != null){
			
			String email = mUserName_Edt.getText().toString().trim();
			String pwd = mPassword_Edt.getText().toString().trim();
			
			if(email.length() <=0 ){
				//username_edttext.setError("Enter the username");
				//StringUtil.setError(mUserName_Edt,""+getString(R.string.emailval));

				mUserName_Edt.setError(Html.fromHtml("<font color='orange'>"+ ms1 + "</font>"));    //Done By Ritesh

				isValid = false;
			}else if(!StringUtil.isValidEmail(email)){
				//username_edttext.setError("Enter the valid email id");
			//	StringUtil.setError(mUserName_Edt, ""+getString(R.string.emailidval));

				mUserName_Edt.setError(Html.fromHtml("<font color='orange'>"+ msg2 + "</font>"));   //Done By Ritesh

				isValid = false;
				
			}if(pwd.length() <= 0){
				//password_edttext.setError("Enter the password");
                mPassword_Edt.setError(Html.fromHtml("<font color='orange'>"+ msg + "</font>"));    //Done By Ritesh

				isValid = false;
				
			}else if(pwd.length() < 4){
				//password_edttext.setError("Password length should be minimum 4 characters");
				//StringUtil.setError(mPassword_Edt, ""+getString(R.string.passval2));
				mPassword_Edt.setError(Html.fromHtml("<font color='orange'>"+ msg3 + "</font>"));    //Done By Ritesh

				isValid = false;
			}
			return isValid;
		}
		return !isValid;
	}

	@Override
	public void onSuccess(SocialMedia socialMedia) {

	}

	@Override
	public void onError(String errorMessage) {

	}

	private class Login implements ApiResponse {
		
		public Login() {
			// TODO Auto-generated constructor stub
			String email = mUserName_Edt.getText().toString().trim();
			String pwd = mPassword_Edt.getText().toString().trim();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("email", email));
		    nameValuePairs.add(new BasicNameValuePair("password", pwd));
			new ApiService(LoginActivity.this, this, true, nameValuePairs).execute("users/login.html");
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
				try {
					JSONObject obj = new JSONObject(result).getJSONObject("response");
					final String HTTPCODE = obj.getString("httpCode");
					final String MSG = obj.getString("Message");
					if(HTTPCODE.equals("202")){
						final String USERID = obj.getJSONObject("UserData").getString("user_id"); 
						Session session = new Session(LoginActivity.this);
						session.saveUserId(USERID);
						startActivity(new Intent(LoginActivity.this, TabActivity.class));
						Logger.showCenterToast(""+getString(R.string.loginsuccess), LoginActivity.this);
						finish();
					}else{
						Logger.showCenterToast(MSG, LoginActivity.this);
					}
				} catch (JSONException e) {}
			}
		}
	}
	public void sign_up_click(View v)
	{
		Intent inter=new Intent(getApplicationContext(), SignupDDActivity.class);
		startActivity(inter);
	}
	
	@SuppressWarnings("deprecation")
	public void signinWithFacebook(View v){
		new FacebookProcess(LoginActivity.this,LoginActivity.this).proceedLogin();
//		mFacebook = new Facebook(APP_ID);
//		if(!mFacebook.isSessionValid()){
//			mFacebook.authorize(this, new String[] {"publish_stream","email","user_groups","read_stream","user_about_me","offline_access"},Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
//		}else{
//			 JSONObject json;
//			try {
//				//http://192.168.1.74:1008/api/facebook.html/?first_name=venkat&last_name=raja&email=k.t.venkatraja@gmail.com&facebook_id=100000280426247&fb_access_token=CAADBbM65qEsBAOUZAW7AGCYDWw7gytov7JlMi1iUatA5l1GTrIWRt3XAp7cWwZCp15qRgZA8qigDRZC1ZB7YuHZAmkpM3ocrTuVYtZBdvJInpvgXWaZAxTWfkehxPYgTEN7iTk4vQDt9HucheUE3kYMmVVf3tSjGigtZAwCvDThE1E5J4jlHaHHKp
//				json = Util.parseJson(mFacebook.request("me"));
//				String facebookID = json.getString("id");
//	              String facebookEmail = json.getString("email");
//	              String firstname = json.getString("first_name");
//	              String lastname = json.getString("last_name");
//	              String accesstoken = mFacebook.getAccessToken();
//	              new fbSignup(firstname, lastname, facebookEmail, facebookID, accesstoken);
//			} catch (FacebookError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//
//		}
	}

	final class LoginDialogListener implements DialogListener
{
    public void onComplete(Bundle values)
    {
        try 
        {
            JSONObject json = Util.parseJson(mFacebook.request("me"));
            String facebookID = json.getString("id");
           String facebookEmail = json.getString("email");
           String firstname = json.getString("first_name");
           String lastname = json.getString("last_name");
           String accesstoken = mFacebook.getAccessToken();
           new fbSignup(firstname, lastname, facebookEmail, facebookID, accesstoken);
        }
        catch( Exception error ) 
        {
            Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onFacebookError(FacebookError error) {
        Toast.makeText( LoginActivity.this, R.string.loginerror+""+error.toString(), Toast.LENGTH_LONG).show();
    }

    public void onError(DialogError error) {
        Toast.makeText( LoginActivity.this, R.string.loginerror+""+error.toString(), Toast.LENGTH_LONG).show();
    } 

    public void onCancel() {
        Toast.makeText( LoginActivity.this, R.string.loginerror, Toast.LENGTH_LONG).show();
    }
    /******  Facebook Login End  *******/

}
	class fbSignup implements ApiResponse
	{
	  public fbSignup(String firstname, String lastname, String email, String fId, String accessToken)
	  {
		  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		    nameValuePairs.add(new BasicNameValuePair("email", email));
		    nameValuePairs.add(new BasicNameValuePair("first_name", firstname));
		    nameValuePairs.add(new BasicNameValuePair("last_name", lastname));
		    nameValuePairs.add(new BasicNameValuePair("facebook_id", fId));
		    nameValuePairs.add(new BasicNameValuePair("fb_access_token", accessToken));
		    //firstname..Selva lastname...Raman id..100001461528717 accessTokenn..CAACDdzOKoD8BAOOLgY9fKu7D0ZARXI9ZBl3ZBQSXkKM2yYutJc3pAfxr0brFANHAVgryBcZCRZBjyr3gedtfOvsNYeA1ThLQxK33KOCSJezZAGNJ87wcxkSrxJS0333IZCTDZB0YYHzXN9m663twTT7xFa3GLnO9h6qSegIZAE0JeBwD7wsomRaicbvClFbZBBRL4ZD
		    String url = "facebook.html/?first_name=" +  firstname + "&last_name=" +  lastname + "&email=" + email + "&facebook_id=" + fId + "&fb_access_token="+ accessToken;
	 Logger.Log("fb details..", "firstname.."+firstname+" lastname..."+lastname+" id.."+fId+" accessTokenn.."+accessToken);
			//new ApiService(LoginActivity.this, this, true, nameValuePairs).execute("facebook.html/");
			new ApiService(LoginActivity.this, this).execute(url);
	  }
		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
				try {
					JSONObject obj = new JSONObject(result).getJSONObject("response");
					String httpcode = obj.getString("httpCode");
					String msg = obj.getString("Message");
					Logger.showCenterToast(msg, LoginActivity.this);
					if(httpcode.equals("202")){
						final String USERID = obj.getJSONObject("login_details").getString("user_id"); 
						Session session = new Session(LoginActivity.this);
						session.saveUserId(USERID);
						startActivity(new Intent(LoginActivity.this, TabActivity.class));
						finish();
					}
					
				} catch (JSONException e) {	}
				
			}else{
				Logger.showCenterToast(result, LoginActivity.this);
			}
		}
		
	}
}