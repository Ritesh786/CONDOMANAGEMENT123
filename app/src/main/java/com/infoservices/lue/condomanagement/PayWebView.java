package com.infoservices.lue.condomanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class PayWebView extends AppCompatActivity {
    Intent intent;
    View header;
    TextView header_text;
    ImageView back,logout;
    String product_id;
    WebView myWebView;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Name = "name";
    public static final String Phone = "mobile";
    public static final String Email = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_web_view);
        sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        intent=getIntent();
        product_id=intent.getStringExtra( "product_id" );
        header=findViewById( R.id.header_payweb );
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
        header_text.setText( "Pay Now");
        try{
            myWebView = (WebView) findViewById(R.id.payweb_view);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
                myWebView.loadUrl( "https://app.senangpay.my/payment/"+product_id+"?name="+sharedPreferences.getString(Name,"")+"=>id-"+sharedPreferences.getString("id","")+"&email="+sharedPreferences.getString(Email,"")+"&phone="+sharedPreferences.getString(Phone,""));
            }catch (Exception e){
            e.printStackTrace();
        }
    }
    }

