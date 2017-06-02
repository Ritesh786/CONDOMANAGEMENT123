package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.SharedPrefs;


public class Settings extends Activity  {

    TextView   privacy_policy_txt, terms_of_use,profile,change_password,logout;

    ImageView imageViewleft;

    private OnClickListener mclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.imageViewleft:
                    finish();
                    break;
                case R.id.privacy_policy_txt:

                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    intent.putExtra("Share", getString(R.string.privacyurl));
                    startActivity(intent);
                    break;

                case R.id.terms_of_use:
                    Intent intent1 = new Intent(getApplicationContext(), WebActivity.class);
                    intent1.putExtra("Share",
                            getString(R.string.tcurl));
                    startActivity(intent1);

                    break;

                case R.id.change_password: 
                    Intent intent2 = new Intent(getApplicationContext(), ChangePassword.class);
                    startActivity(intent2);

                    break;

                case R.id.profile:

                    Intent intent3 = new Intent(getApplicationContext(), ProfileDetails.class);
                    startActivity(intent3);

                    break;


                case R.id.logout:

                    SharedPrefs.clear(Settings.this);
                    Session session = new Session(Settings.this);
                    session.clearAll();
                    Intent logoutIntent = new Intent(Settings.this,Logindd.class);

                    startActivity(logoutIntent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SharedPrefs.setLanguage(getBaseContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_activity);

        init();

        initData();
        setListner();

    }

    public void init() {



        privacy_policy_txt = (TextView) findViewById(R.id.privacy_policy_txt);
        imageViewleft = (ImageView) findViewById(R.id.imageViewleft);


        terms_of_use = (TextView) findViewById(R.id.terms_of_use);
        profile = (TextView) findViewById(R.id.profile);
        change_password = (TextView) findViewById(R.id.change_password);
        logout = (TextView) findViewById(R.id.logout);


    }

    public void initData() {

    }


    //{"apicall":"NotificationSetting","notification_setting":"1/0","user_id":"2097"}

    public void setListner() {
        imageViewleft.setOnClickListener(mclick);
        logout.setOnClickListener(mclick);
        profile.setOnClickListener(mclick);
        terms_of_use.setOnClickListener(mclick);
        privacy_policy_txt.setOnClickListener(mclick);
        change_password.setOnClickListener(mclick);

    }


}
