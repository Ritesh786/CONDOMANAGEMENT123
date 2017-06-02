package com.infoservices.lue.condomanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.infoservices.lue.utills.Utils;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Login = "login";
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_splash);
        call();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        call();
    }

    private void call(){
        ImageView spaceshipImage = (ImageView) findViewById(R.id.splash_img);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        new Handler().postDelayed(new Runnable(){
        @Override
        public void run() {
            sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            String loginstring=sharedPreferences.getString( Login,"" );
            if (!Utils
                    .isNetworkAvailable(SplashActivity.this)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
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
                    .isGpsEnable(SplashActivity.this)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
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
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }else{
                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }
    }, 2800);
}


}
