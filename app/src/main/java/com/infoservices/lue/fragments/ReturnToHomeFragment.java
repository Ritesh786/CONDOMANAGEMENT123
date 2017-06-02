package com.infoservices.lue.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReturnToHomeFragment extends Fragment {
Button button_retn_home;

    public ReturnToHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_to_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        button_retn_home=(Button)getActivity().findViewById( R.id.button_retn_home );
        button_retn_home.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getActivity(),"Payment made Successfully!!!",Toast.LENGTH_LONG ).show();
                getActivity().finish();
            }
        } );
    }
}
