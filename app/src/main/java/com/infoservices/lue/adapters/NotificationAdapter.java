package com.infoservices.lue.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.condomanagement.NotificationView;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.MyNotificationHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lue on 29-07-2016.
 */
public class NotificationAdapter extends BaseAdapter {
    ArrayList<NotificationEntity> notificationEntities;
    Activity context;
    String from;
    Holder holder;
    MyNotificationHelper myNotificationHelper;
    private static LayoutInflater inflater=null;
    public NotificationAdapter(Activity mainActivity,  ArrayList<NotificationEntity> notificationEntities) {
        // TODO Auto-generated constructor stub
        this.notificationEntities=notificationEntities;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return notificationEntities.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder=new Holder();
        View rowView;
        rowView = inflater.inflate( R.layout.notification_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.notifi_detail);
        holder.tv1=(TextView) rowView.findViewById(R.id.point1);
        holder.tv2=(TextView) rowView.findViewById(R.id.point2);
        holder.tv3=(TextView) rowView.findViewById(R.id.point3);
        holder.img=(ImageView) rowView.findViewById(R.id.image_notification2);
        holder.image_notice_bullet=(ImageView) rowView.findViewById(R.id.image_notice_bullet);
        if (notificationEntities.get( position ).getStatus().equals( "1" )){
            holder.image_notice_bullet.setBackgroundResource( R.drawable.radio_button_0n );
        }else if(notificationEntities.get(position).getStatus().equals( "0" )){
            holder.image_notice_bullet.setBackgroundResource( R.drawable.radio_button_off );
        }
        holder.tv.setText(notificationEntities.get(position).getTitle());
        Picasso.with(context)
                .load("http://api.condoassist2u.com/"+notificationEntities.get(position).getImage())
                .placeholder(R.drawable.lobo2)   // optional
                .error(R.drawable.lobo2)     // optional
                .into(holder.img);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d( "Clicked","row" );
                //Toast.makeText(context,""+notificationEntities.get( position ).getStatus(),Toast.LENGTH_LONG).show();
                 myNotificationHelper=new MyNotificationHelper(context);
                SQLiteDatabase db =  myNotificationHelper.getWritableDatabase();
                notificationEntities=myNotificationHelper.getAllNotifications();
                 myNotificationHelper.updateNotification( Integer.parseInt( notificationEntities.get( position ).getId()),notificationEntities.get( position ).getNotification_id(), notificationEntities.get( position ).getMgnt_id(),
                        notificationEntities.get( position ).getTitle(), notificationEntities.get( position ).getImage(), notificationEntities.get( position ).getDescription()
                        , "0", notificationEntities.get( position ).getCreated() );
                holder.image_notice_bullet=(ImageView) v.findViewById(R.id.image_notice_bullet);
                holder.image_notice_bullet.setBackgroundResource( R.drawable.radio_button_off );
               footerUpdate( context );
                Intent intent=new Intent( context,NotificationView.class );
                intent.putExtra( "object",notificationEntities.get( position ) );
                intent.putExtra( "from","notice" );
               context. startActivity(intent);


            }
        });
        return rowView;
    }

    public class Holder
    {
        TextView tv,tv1,tv2,tv3;
        ImageView img,image_notice_bullet;
    }
    public void  footerUpdate(Activity activity){
        ArrayList<NotificationEntity> notificationEntities2=new ArrayList<NotificationEntity>(  );
        ArrayList<NotificationEntity> notificationEntities3=new ArrayList<NotificationEntity>(  );
        notificationEntities3.clear();
        notificationEntities2.clear();
        notificationEntities2=new MyNotificationHelper(activity).getAllNotifications();
        View footer=activity.findViewById(R.id.footer_menuoption);
        for (int k=0;k<notificationEntities2.size();k++){
            if(notificationEntities2.get( k ).getStatus().equals( "1" )){
                notificationEntities3.add( notificationEntities.get( k ) );
            }
        }
        TextView note_main=(TextView)footer.findViewById( R.id.note_main );
        if(notificationEntities3.size()>0){
            note_main.setText( ""+notificationEntities3.size() );
        }else{
            note_main.setVisibility( View.GONE );
        }
    }

}