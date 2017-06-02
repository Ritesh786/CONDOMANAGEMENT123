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
public class ProceedFragment extends Fragment implements View.OnClickListener{
Button button_proceed;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    public static final String Name = "name";
    public static final String Unitno = "unit_no";
    public static final String Image = "image";
    TextView proceed_name,proceed_unitno;
    ImageView proceed_image;
    String name,unitno;
    public ProceedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proceed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_proceed=(Button)getActivity().findViewById(R.id.button_proceed);
        button_proceed.setOnClickListener(this);
        proceed_name=(TextView)getActivity().findViewById( R.id.proceed_name );
        proceed_unitno=(TextView)getActivity().findViewById( R.id.proceed_unitno );
        proceed_image=(ImageView)getActivity().findViewById( R.id.proceed_image );
        sharedPreferences=getActivity().getSharedPreferences( MyLoginPREFERENCES, Context.MODE_PRIVATE );
        name= sharedPreferences.getString( Name,"" );
        unitno= sharedPreferences.getString( Unitno,"" );
        proceed_name.setText( name );
        proceed_unitno.setText( unitno );
        Picasso.with(getActivity())
                .load("http://api.condoassist2u.com/"+sharedPreferences.getString( Image,"" ))
                .placeholder(R.drawable.lobo2)   // optional
                .error(R.drawable.lobo2)     // optional
                .into(proceed_image);
    }

    @Override
    public void onClick(View v) {
        TextView sh1=(TextView)getActivity().findViewById(R.id.sh1);
        sh1.setBackgroundResource(R.drawable.circulerview);
        TextView sh2=(TextView)getActivity().findViewById(R.id.sh2);
        sh2.setBackgroundResource(R.drawable.circuler_primary_light);
        ConfirmFragment fragment=new ConfirmFragment();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_proceed, fragment).commit();
    }
}
