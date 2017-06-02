package com.infoservices.lue.dealAndDiscount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.infoservices.lue.condomanagement.LoginActivity;
import com.infoservices.lue.condomanagement.MainActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.condomanagement.SplashActivity;
import com.infoservices.lue.utills.Utils;

public class SplashDD extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCESDD = "loginpreferencedd" ;
    public static final String Login = "login";
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_dd);
        call();
    }
    private void call(){
        ImageView spaceshipImage = (ImageView) findViewById(R.id.splash_img2);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences(MyLoginPREFERENCESDD, Context.MODE_PRIVATE);
                String loginstring=sharedPreferences.getString( Login,"" );
                if (!Utils
                        .isNetworkAvailable(SplashDD.this)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashDD.this);
                    alertDialogBuilder.setMessage("No Internet Connection ! Enable your Connection First !!! ");

                    alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

                    alertDialog = alertDialogBuilder.create();
                    alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                                       @Override
                                                       public void onShow(DialogInterface arg0) {
                                                           alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                           alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                       }
                                                   }
                    );
                    alertDialog.show();
                }
                else if (!Utils
                        .isGpsEnable(SplashDD.this)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashDD.this);
                    alertDialogBuilder.setMessage("No GPS Enabled ! Enable your Connection First !!! ");

                    alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                                       @Override
                                                       public void onShow(DialogInterface arg0) {
                                                           alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                           alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                                       }
                                                   }
                    );
                    alertDialog.show();
                }
                else if (loginstring.equals( "yes" )){
                    Intent mainIntent = new Intent(SplashDD.this,MainActivity.class);
                    SplashDD.this.startActivity(mainIntent);
                    SplashDD.this.finish();
                }else{
                    Intent mainIntent = new Intent(SplashDD.this, com.infoservices.lue.dealAndDiscount.LoginActivity.class);
                    SplashDD.this.startActivity(mainIntent);
                    SplashDD.this.finish();
                }

            }
        }, 2800);
    }

}
