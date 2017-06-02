package com.infoservices.lue.condomanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.entity.CalenderEvensEntity;
import com.infoservices.lue.entity.NotificationEntity;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationView extends AppCompatActivity {
ImageView image_notice;
    TextView tital_notification,by,description,timing3;
    View view1;
    NotificationEntity notificationEntity;
    CalenderEvensEntity calenderEvensEntity;
    String from="",s1,s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_scrolling );
        //Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        //setSupportActionBar( toolbar );
        Intent intent=getIntent();
        from=intent.getStringExtra( "from" );
        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction( "Action", null ).show();*/
                finish();
            }
        } );
        view1=findViewById( R.id. view1 );
        tital_notification=(TextView) view1.findViewById( R.id.tital_notification );
        by=(TextView) view1.findViewById( R.id.by );
        description=(TextView) view1.findViewById( R.id.description );
        timing3=(TextView) view1.findViewById( R.id.timing3 );
        image_notice=(ImageView)findViewById( R.id.image_notice );
        if (from.equals( "notice" )){
            notificationEntity=(NotificationEntity)intent.getSerializableExtra(  "object" );
        Picasso.with(this)
                .load("http://api.condoassist2u.com/"+notificationEntity.getImage())
                .placeholder(R.drawable.hotel)   // optional
                .error(R.drawable.error)     // optional
                .into(image_notice);
        tital_notification.setText( ""+notificationEntity.getTitle() );
        by.setText( "By Management");
        description.setText( Html.fromHtml(notificationEntity.getDescription()));
        }else if(from.equals( "calender" )){
            calenderEvensEntity=(CalenderEvensEntity) intent.getSerializableExtra(  "object" );
            tital_notification.setText( ""+calenderEvensEntity.getTitle() );
            by.setText( ""+ calenderEvensEntity.getPosted_by()+"\n Date:"+calenderEvensEntity.getEvent_date());
            description.setText( Html.fromHtml(calenderEvensEntity.getDescription()));
            /*try {
                DateFormat f1 = new SimpleDateFormat("HH:mm:ss");
                Date d = f1.parse(calenderEvensEntity.getFrom_time());
                Date d2 = f1.parse(calenderEvensEntity.getTo_time());
                DateFormat f2 = new SimpleDateFormat("h:mma");
                s1=f2.format(d).toLowerCase();
                s2=f2.format(d2).toLowerCase();
            }catch(Exception e){
                e.printStackTrace();
            }*/
            timing3.setText( "(Timing) From:- "+calenderEvensEntity.getFrom_time()+" To:- "+calenderEvensEntity.getTo_time());
            Picasso.with(this)
                    .load("http://api.condoassist2u.com/"+calenderEvensEntity.getImage())
                    .placeholder(R.drawable.lobo2)   // optional
                    .error(R.drawable.lobo2)     // optional
                    .into(image_notice);
        }
    }
}
