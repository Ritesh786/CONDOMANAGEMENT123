package com.infoservices.lue.condomanagement;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.fragments.PagerItemFragment;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import java.util.ArrayList;


public class ViewpagerAct extends AppCompatActivity {
    ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
    static ArrayList<NotificationEntity> notificationEntities2=new ArrayList<NotificationEntity>();
    MyPagerAdapter2 adapterViewPager;
    ViewPager viewPager;
    DotIndicator dotIndicator;
    //TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_viewpager );
        viewPager = (ViewPager) findViewById( R.id.viewpager2 );
       // tabLayout = (TabLayout) findViewById( R.id.sliding_tabs );
        //text_no_notice = (TextView) getActivity().findViewById( R.id.text_no_notice );
        if (notificationEntities2.size() == 0) {
            adapterViewPager = new MyPagerAdapter2( getSupportFragmentManager(),3 );
            viewPager.setAdapter( adapterViewPager );
          //  tabLayout.setupWithViewPager( viewPager );
           /* for (int i = 0; i < notificationEntities2.size(); i++) {
                View tab = ((ViewGroup) tabLayout.getChildAt( 0 )).getChildAt( i );
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins( 5, 0, 5, 0 );
                tab.requestLayout();
                tabLayout.getTabAt( i ).setIcon( R.drawable.circuler_gray );
            }*/
        }}
        class MyPagerAdapter2 extends FragmentStatePagerAdapter {


            int mNumOfTabs;

            public MyPagerAdapter2(FragmentManager fm, int numTabs) {
                super( fm );
                this.mNumOfTabs = numTabs;
            }

            @Override
            public Fragment getItem(int position) {
                return PagerItemFragment.newInstance( position, "" + position );
            }

            @Override
            public int getCount() {
                return mNumOfTabs;
            }
        }
    }