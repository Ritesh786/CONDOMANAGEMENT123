package com.infoservices.lue.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.infoservices.lue.condomanagement.MenuOptionSelectedActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.MyNotificationHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFregment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String MYNOTIFICATION_PREFERENCE="notification_preferences";
    public static final String DO_NOT_DITURB="do_not_disturb";
    public static final String NOTIFICATION_RIGTONE="notification_ringtone";
    public static final String SOUND="sound";
    public static final String VIBRATE="vibrate";
    ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
    static ArrayList<NotificationEntity> notificationEntities2=new ArrayList<NotificationEntity>();
    RelativeLayout rel_notification2;
    TextView text_notification_freg;
    Intent intent;
    ToggleButton chkdisturb,chksound,chkvibrate;
    public NotificationFregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_fregment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        sharedPreferences=getActivity().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
        updatenotification(getActivity());
        rel_notification2=(RelativeLayout)getActivity().findViewById( R.id.rel_notification2 );
        rel_notification2.setOnClickListener( this );
        chkdisturb=(ToggleButton)getActivity().findViewById(R.id.chkdisturb);
        chksound=(ToggleButton)getActivity().findViewById(R.id.chksound);
        if (sharedPreferences.getString(SOUND,"").equals("yes")){
            chkdisturb.setChecked(true);
            chksound.setChecked(true);
        }else{
            chkdisturb.setChecked(false);
            chksound.setChecked(false);
        }
        chkvibrate=(ToggleButton)getActivity().findViewById(R.id.chkvibrate);
        if (sharedPreferences.getString(VIBRATE,"").equals("yes")){
            chkvibrate.setChecked(true);
        }else{
            chkvibrate.setChecked(false);
        }

        chkdisturb.setOnCheckedChangeListener(this);
        chksound.setOnCheckedChangeListener(this);
        chkvibrate.setOnCheckedChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        updatenotification(getActivity());
    }

    public void updatenotification(Activity activity){
        notificationEntities.clear();
        notificationEntities=new MyNotificationHelper(activity).getAllNotifications();
        notificationEntities2.clear();
        for (int k=0;k<notificationEntities.size();k++){
            if(notificationEntities.get( k ).getStatus().equals( "1" )){
                notificationEntities2.add( notificationEntities.get( k ) );
            }
        }
        text_notification_freg=(TextView)activity.findViewById( R.id.text_notification_freg2);
        text_notification_freg.setText( ""+notificationEntities2.size() );
    }

    @Override
    public void onClick(View v) {
        int noofItems=new MyNotificationHelper(getActivity()).numberOfRows();
        if (v.getId()==rel_notification2.getId()&&noofItems>0){
            intent=new Intent( getActivity(), MenuOptionSelectedActivity.class );
            intent.putExtra( "from","Bulatin and Notices" );
            startActivity( intent );
        }else{
            Toast.makeText( getActivity(),"No Notification now !!!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int bid=buttonView.getId();
        sharedPreferences=getActivity().getSharedPreferences(MYNOTIFICATION_PREFERENCE, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        //editor.putString("fcm", ""+refreshedToken);
        //editor.commit();
        switch (bid){
            case R.id.chkdisturb:
                if (isChecked){
                    chksound.setChecked(true);
                    editor.putString(SOUND, "yes");
                    editor.commit();
                }else{
                    chksound.setChecked(false);
                    editor.putString(SOUND, "no");
                    editor.commit();
                }
                break;
            case R.id.chksound:
                if (isChecked){
                    chkdisturb.setChecked(true);
                    editor.putString(SOUND, "yes");
                    editor.commit();
                }else{
                    chkdisturb.setChecked(false);
                    editor.putString(SOUND, "no");
                    editor.commit();
                }
                break;
            case R.id.chkvibrate:
                if (isChecked){
                    editor.putString(VIBRATE, "yes");
                    editor.commit();
                }else{
                    editor.putString(VIBRATE, "no");
                    editor.commit();
                }
                break;
        }
    }
}
