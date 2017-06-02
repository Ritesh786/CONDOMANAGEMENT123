package com.infoservices.lue.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.condomanagement.WebViewActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFregment extends Fragment implements View.OnClickListener{

RelativeLayout about_condo,go_to_official_web;
    Intent intent;
    public SettingsFregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_fregment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        about_condo=(RelativeLayout)getActivity().findViewById( R.id.about_condo );
        go_to_official_web=(RelativeLayout)getActivity().findViewById( R.id.go_to_official_web );
        about_condo.setOnClickListener( this );
        go_to_official_web.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        intent=new Intent( getActivity(), WebViewActivity.class );
        if (v.getId()==about_condo.getId()){
            intent.putExtra( "from","About Us" );
        }
        else if (v.getId()==go_to_official_web.getId()){
            intent.putExtra( "from","Official Website" );
        }
        startActivity( intent );
    }
}
