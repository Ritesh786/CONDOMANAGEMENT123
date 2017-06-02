package com.infoservices.lue.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakePaymentFragment extends Fragment implements View.OnClickListener{

Button button_make_payment;
    public MakePaymentFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_payment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_make_payment=(Button)getActivity().findViewById(R.id.button_make_payment);
        button_make_payment.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        TextView sh3=(TextView)getActivity().findViewById(R.id.sh3);
        sh3.setBackgroundResource(R.drawable.circulerview);
        TextView sh4=(TextView)getActivity().findViewById(R.id.sh4);
        sh4.setBackgroundResource(R.drawable.circuler_primary_light);
        ReturnToHomeFragment fragment=new ReturnToHomeFragment();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_proceed, fragment).commit();
    }
}
