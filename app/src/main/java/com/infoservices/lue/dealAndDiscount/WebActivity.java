package com.infoservices.lue.dealAndDiscount;
 

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.infoservices.lue.condomanagement.R;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends Activity{
	ProgressDialog progressBar;// = new ProgressDialog(this);
	protected void onCreate(Bundle bun)
	{
		super.onCreate(bun);
		setContentView(R.layout.web_layout);
		
		WebView webView = (WebView) findViewById(R.id.webview);
	
		 progressBar= new ProgressDialog(this);
		 progressBar.setMessage(""+getString(R.string.pleasewait));
		 progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		 progressBar.setCancelable(false);
		 progressBar.show();
		 webView.setWebViewClient(new myWebClient());
		 webView.getSettings().setJavaScriptEnabled(true);
		   Bundle extra = getIntent().getExtras();
		   Log.v("UUURRRLLL",extra.getString("Share"));
//		    if(extra.getBoolean("isFacebook", true))
//		    {
		    	//var facebook_url = "http://www.facebook.com/sharer.php?u=http://google.com/?q=bla";
		    	webView.loadUrl(extra.getString("Share"));	
		    	//getIntent().putExtra("isFacebook", false);
//		    }
//		    else if(extra.getBoolean("isTwitter", true))
//		    {
//		    	webView.loadUrl("http://twitter.com/share?url="+extra.getString("Share"));
//		    	getIntent().putExtra("isTwitter", false);
//		    }
//		    else
//		    {
//		    	//Toast.makeText(getApplicationContext(), "Google+", Toast.LENGTH_SHORT).show();
//		    	webView.loadUrl("https://plus.google.com/share?url="+extra.getString("Share"));
//		    	getIntent().putExtra("isGoogle", false);
//		    }
	}
	 public class myWebClient extends WebViewClient
	    {
	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	            // TODO Auto-generated method stub
	            super.onPageStarted(view, url, favicon);
	        }
	 
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	 
	            view.loadUrl(url);
	            return true;
	 
	        }
	 
	        @Override
	        public void onPageFinished(WebView view, String url) {
	            // TODO Auto-generated method stub
	            super.onPageFinished(view, url);
	            progressBar.dismiss();
	            //progressBar.setVisibility(View.GONE);
	        }
	    }
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	int pid = android.os.Process.myPid(); 
	Log.v("Count", String.valueOf(pid));
	android.os.Process.killProcess(pid); 
}
}
