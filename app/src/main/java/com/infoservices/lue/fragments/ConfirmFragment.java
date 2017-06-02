package com.infoservices.lue.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment implements View.OnClickListener{
    Button button_confirm;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    public static final String Name = "name";
    public static final String Unitno = "unit_no";
    public static final String Address = "address";
    public static final String Image = "image";
    TextView confirm_name,confirm_unit_no,confirm_address;
    String name,unitno,address;
    ImageView confirm_image;
    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_confirm=(Button)getActivity().findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(this);
        confirm_name=(TextView)getActivity().findViewById( R.id.confirm_name );
        confirm_unit_no=(TextView)getActivity().findViewById( R.id.confirm_unit_no );
        confirm_address=(TextView)getActivity().findViewById( R.id.confirm_address );
        confirm_image=(ImageView)getActivity().findViewById( R.id.confirm_image );
        sharedPreferences=getActivity().getSharedPreferences( MyLoginPREFERENCES, Context.MODE_PRIVATE );
        name= sharedPreferences.getString( Name,"" );
        unitno= sharedPreferences.getString( Unitno,"" );
        address= sharedPreferences.getString( Address,"" );
        confirm_name.setText( name );
        confirm_unit_no.setText( unitno );
        confirm_address.setText( address );
        Picasso.with(getActivity())
                .load("http://api.condoassist2u.com/"+sharedPreferences.getString( Image,"" ))
                .placeholder(R.drawable.hotel)   // optional
                .error(R.drawable.error)     // optional
                .into(confirm_image);
    }

    @Override
    public void onClick(View v) {
        TextView sh2=(TextView)getActivity().findViewById(R.id.sh2);
        sh2.setBackgroundResource(R.drawable.circulerview);
        TextView sh3=(TextView)getActivity().findViewById(R.id.sh3);
        sh3.setBackgroundResource(R.drawable.circuler_primary_light);
        MakePaymentFragment fragment=new  MakePaymentFragment();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_proceed, fragment).commit();
    }
}
