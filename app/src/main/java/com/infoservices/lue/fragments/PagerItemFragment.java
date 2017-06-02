package com.infoservices.lue.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.MyNotificationHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerItemFragment extends Fragment {
    private  ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
    private ArrayList<NotificationEntity> notificationEntities2=new ArrayList<NotificationEntity>();
    int page;
    String title;
    TextView item_det,by,timing;
    ImageView imageView;
    public PagerItemFragment() {}
    public static PagerItemFragment newInstance(int page, String title) {
        PagerItemFragment fragmentFirst = new PagerItemFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt",0);
        title = getArguments().getString("someTitle");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager_item, container, false);
        imageView=(ImageView)view.findViewById( R.id. pager_imgs);
        item_det=(TextView)view.findViewById( R.id.item_det );
        by=(TextView)view.findViewById( R.id.by );
        timing=(TextView)view.findViewById( R.id.timing2 );
        notificationEntities=new MyNotificationHelper( getActivity() ).getAllNotifications();
        for (int k=0;k<notificationEntities.size();k++){
            if(notificationEntities.get( k ).getStatus().equals( "1" )){
                notificationEntities2.add( notificationEntities.get( k ) );
                Log.d( "ck",""+notificationEntities.get( k )  );
            }
        }
        Picasso.with(getActivity())
                .load("http://api.condoassist2u.com/"+notificationEntities2.get(page).getImage())
                .placeholder(R.drawable.lobo2)   // optional
                .error(R.drawable.lobo2)     // optional
                .into(imageView);
        item_det.setText( notificationEntities2.get(page).getTitle());
        timing.setText( notificationEntities2.get(page).getCreated());
        by.setText("By Mangement"  );
        return view;

    }


}
