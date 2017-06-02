package com.infoservices.lue.condomanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {
Intent intent;
    View header;
    TextView header_text;
    ImageView back,logout;
    String title;
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_web_view );
        intent=getIntent();
        title=intent.getStringExtra( "from" );
        header=findViewById( R.id.header_web );
        header_text=(TextView)header.findViewById( R.id.header_text);
        back=(ImageView)header.findViewById( R.id.go_back);
        logout=(ImageView)header.findViewById( R.id.logout);
        logout.setVisibility( View.GONE );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        header_text.setText( ""+ title);
        try{
         myWebView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if(title.equals( "About Us" )) {
            myWebView.loadUrl( "http://condoassist2u.com/about/" );
        }else if (title.equals( "Official Website" )){
            myWebView.loadUrl( "http://condoassist2u.com/" );
        }}catch (Exception e){
            e.printStackTrace();
        }
    }
}
