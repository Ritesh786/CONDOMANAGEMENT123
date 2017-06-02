package com.infoservices.lue.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFregment extends Fragment implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    public static final String UserName = "username";
    public static final String Name = "name";
    public static final String Salt = "salt";
    public static final String Phone = "mobile";
    public static final String Email = "email";
    public static final String Unitno = "unit_no";
    public static final String HouseContact = "house_phone";
    public static final String EmergencyContact = "emergency_contact";
    public static final String Relationship = "relationship";
    public static final String Status = "status";
    public static final String Created = "created";
    public static final String Updated = "updated";
    public static final String RegistationID = "updated";
    public static final String Address = "address";
    public static final String Image = "image";

    TextView profile_name,profile_email,unit_no_profile,house_phone_profile,mobile_profile,emer_con_profile,relation_profile;
    ImageView profile_image_main,back;
    LinearLayout edit_profile;
    android.support.v4.app.FragmentManager fragmentManager;
    View header;


    public ProfileFregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_fregment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        profile_name=(TextView)getActivity().findViewById( R.id.profile_name );
        profile_email=(TextView)getActivity().findViewById( R.id.profile_email );
        unit_no_profile=(TextView)getActivity().findViewById( R.id.unit_no_profile );
        house_phone_profile=(TextView)getActivity().findViewById( R.id.house_phone_profile );
        mobile_profile=(TextView)getActivity().findViewById( R.id.mobile_profile );
        emer_con_profile=(TextView)getActivity().findViewById( R.id.emer_con_profile );
        relation_profile=(TextView)getActivity().findViewById( R.id.relation_profile );
        profile_image_main=(ImageView)getActivity().findViewById( R.id.profile_image_main );
        profile_image_main.setOnClickListener( this );
        edit_profile=(LinearLayout) getActivity().findViewById( R.id.edit_profile );
        edit_profile.setOnClickListener( this );
        header=getActivity().findViewById( R.id.header_comm_and_sugg );
        back=(ImageView)header.findViewById( R.id.go_back );
       // back.setOnClickListener( this );
        if (!sharedPreferences.getString(Name,"").equals("null"))profile_name.setText( sharedPreferences.getString(Name,"") );
        if (!sharedPreferences.getString(Email,"").equals("null"))profile_email.setText( sharedPreferences.getString(Email,""  ) );
        if (!sharedPreferences.getString(Unitno,"").equals("null"))unit_no_profile.setText( sharedPreferences.getString(Unitno,"") );
        if (!sharedPreferences.getString(HouseContact,"").equals("null"))house_phone_profile.setText( sharedPreferences.getString( HouseContact,"" ) );
        if (!sharedPreferences.getString(Phone,"").equals("null"))mobile_profile.setText( sharedPreferences.getString(Phone,""  ) );
        if (!sharedPreferences.getString(EmergencyContact,"").equals("null"))emer_con_profile.setText( sharedPreferences.getString( EmergencyContact,"" ) );
        if (!sharedPreferences.getString(Relationship,"").equals("null"))relation_profile.setText( sharedPreferences.getString( Relationship,"" ) );
        if (!sharedPreferences.getString(Image,"").equals("null"))
        Picasso.with(getActivity())
                .load("http://api.condoassist2u.com/"+sharedPreferences.getString( Image,"" ))
                .placeholder(R.drawable.userprofileicon2)   // optional
                .error(R.drawable.error)     // optional
                .into(profile_image_main);
    }

    @Override
    public void onClick(View v) {
      if (v.getId()==edit_profile.getId()){
          //tv.setText("PROFILE");
          EditProfileFregment fragment = new EditProfileFregment();
          fragmentManager = getActivity().getSupportFragmentManager();
          fragmentManager.beginTransaction().replace(R.id.content_command_sugg, fragment).commit();
      }else if(v.getId()==back.getId()){
          ProfileFregment fragment2 = new ProfileFregment();
          fragmentManager = getActivity().getSupportFragmentManager();
          fragmentManager.beginTransaction().replace(R.id.content_command_sugg, fragment2).commit();
      }
    }

}
