package com.infoservices.lue.api;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;


import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.util.NetworkUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ApiService extends AsyncTask<String, String, String> {
	// private ProgressDialog mProgressDialog;
	private Dialog mProgressDialog;
	private Context mContext;
	private ApiResponse response;
	private boolean isSuccess = true;
	private boolean isGetMethod = true;
	private List<NameValuePair> data;

	// private final String DOMAIN = "http://192.168.1.108:1006/api/"; //local
	// private final String DOMAIN = "http://192.168.1.74:1008/api/"; //local 1
	// private final String DOMAIN = "http://192.168.1.200:1198/api/"; //QA
	// private final String DOMAIN = "http://demo6.know3.com/api/"; //Demo

//private final String DOMAIN ="http://monzter.my/api/";
private final String DOMAIN ="http://monzter2u.com/api/";



	// LIVE URL
//private final String DOMAIN = "http://192.168.1.39:1000/monzter-PII/api/";
	
	public ApiService(Context context, ApiResponse response) {
		this.mContext = context;
		this.response = response;
	}

	public ApiService(Context context, ApiResponse response,
					  boolean isPostMethod, List<NameValuePair> data) {
		this.mContext = context;
		this.response = response;
		this.data = data;
		isGetMethod = false;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		View view = View.inflate(mContext, R.layout.progressdialog, null);
		// mProgressDialog = new ProgressDialog(mContext, R.style.NewDialog);
		mProgressDialog = new Dialog(mContext, R.style.NewDialog);
		mProgressDialog.setContentView(view);
		// mProgressDialog.setTitle(mContext.getString(R.string.app_name));
		// mProgressDialog.setIcon(R.drawable.app_icon_small);
		// mProgressDialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(android.graphics.Color.TRANSPARENT));
		/*
		 * mProgressDialog.setMessage("Please wait...");
		 * mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		 * mProgressDialog.setProgress(0);
		 * mProgressDialog.setIndeterminate(false);
		 */
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (!(NetworkUtil.isOnline(mContext))) {
			isSuccess = false;
			return "Check your internet connection.";
		}
		String api_response = "";
		try {

			// params[0] = params[0].replace("\\+", "%20");
			params[0] = params[0].replace(" ", "%20");
			final String URL = (DOMAIN + params[0]);
			Logger.Log("url..", URL);

			InputStream in = null;
			// Get Method
			if (isGetMethod) {
				URL u = new URL(URL);
				URLConnection c = u.openConnection();
				c.connect();
				in = c.getInputStream();
				/*
				 * int totalSize = c.getContentLength();
				 * 
				 * int len1 = 0; int downloadedSize = 0;
				 */
				// PostMethod
			} else {
				in = postData(URL, data);
			}

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder builder = new StringBuilder();
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				/*
				 * len1 = temp.length(); downloadedSize += len1;
				 * publishProgress((int) ((downloadedSize * 100) / totalSize) +
				 * "");
				 */
				builder.append(temp);
			}
			api_response = builder.toString();

		} catch (Exception e) {
			Logger.Log("Api Error...", e.getMessage());
			isSuccess = false;
			api_response = e.getMessage();
		}
		return api_response;
	}

	protected void onProgressUpdate(String... progress) {
		// mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	}

	protected void onPostExecute(String result) {
		mProgressDialog.dismiss();
		response.getResult(isSuccess, result);
	}

	/**
	 * @param Url
	 * @param data
	 * @return
	 */
	private InputStream postData(String Url, List<NameValuePair> data) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(data));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			return entity.getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
