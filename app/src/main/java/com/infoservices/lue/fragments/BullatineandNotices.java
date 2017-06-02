package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.infoservices.lue.adapters.NotificationAdapter;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.NotificationEntity;
import com.infoservices.lue.utills.LoadApi;
import com.infoservices.lue.utills.MyNotificationHelper;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import java.util.ArrayList;

public class BullatineandNotices extends Fragment {
    MyPagerAdapter adapterViewPager;
    //public static TabLayout tabLayout;
    DotIndicator dotIndicator;
    public static ViewPager viewPager;
   public static ListView listView;
    TextView text_no_notice;
    ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
    ArrayList<NotificationEntity> notificationEntities2=new ArrayList<NotificationEntity>();
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    int count=0;

    public BullatineandNotices() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bullatineand_notices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
       // loadData();
        try {
            notificationEntities.clear();
            notificationEntities=new MyNotificationHelper( getActivity() ).getAllNotifications();
            notificationEntities2.clear();
            for (int k=0;k<notificationEntities.size();k++){
                if(notificationEntities.get( k ).getStatus().equals( "1" )){
                    notificationEntities2.add( notificationEntities.get( k ) );
                }
            }
            if (notificationEntities.size() != 0) {
                Log.d("inside", " onPost");
                listView = (ListView) getActivity().findViewById(R.id.list_bulatin_and_notice);
                listView.setAdapter(new NotificationAdapter(getActivity(),notificationEntities));
                viewPager = (ViewPager) getActivity().findViewById( R.id.viewpager );
                //tabLayout = (TabLayout) getActivity().findViewById( R.id.sliding_tabs );
                dotIndicator=(DotIndicator)getActivity().findViewById( R.id.indicator );
                text_no_notice = (TextView) getActivity().findViewById( R.id.text_no_notice );
                if (notificationEntities2.size() != 0) {
                    adapterViewPager = new MyPagerAdapter( getActivity().getSupportFragmentManager(), notificationEntities2.size() );
                    viewPager.setAdapter( adapterViewPager );
                    dotIndicator.setNumberOfItems(  notificationEntities2.size() );
                    dotIndicator.setSelectedItem( 0,false );
                        /*tabLayout.setupWithViewPager( viewPager );
                        for (int i = 0; i < notificationEntities2.size(); i++) {
                            View tab = ((ViewGroup) tabLayout.getChildAt( 0 )).getChildAt( i );
                            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                            p.setMargins( 5, 0, 5, 0 );
                            tab.requestLayout();
                            tabLayout.getTabAt( i ).setIcon( R.drawable.circuler_gray );
                        }*/
                    viewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {

                        // This method will be invoked when a new page becomes selected.
                        @Override
                        public void onPageSelected(int position) {
                        /* Toast.makeText(getActivity(),
                    "Selected page position: " + menu_list.get(position).getId(), Toast.LENGTH_SHORT).show();*/

                        }

                        // This method will be invoked when the current page is scrolled
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            // Code goes here
                               /* for (int i = 0; i < notificationEntities2.size(); i++) {
                                    if (i == position) {
                                        tabLayout.getTabAt( i ).setIcon( R.drawable.circuler_white );
                                        //adapterViewPager.notifyDataSetChanged();
                                    } else {
                                        tabLayout.getTabAt( i ).setIcon( R.drawable.circuler_gray );
                                        // adapterViewPager.notifyDataSetChanged();
                                    }
                                }*/
                            dotIndicator.setSelectedItem( position,false );
                        }

                        // Called when the scroll state changes:
                        // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                        @Override
                        public void onPageScrollStateChanged(int state) {
                            // Code goes here
                        }
                    } );
                }else{
                    viewPager.setVisibility( View.GONE );
                    //tabLayout.setVisibility( View.GONE );
                    dotIndicator.setVisibility( View.GONE );
                    text_no_notice.setVisibility( View.VISIBLE );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static class MyPagerAdapter extends FragmentStatePagerAdapter {


        int mNumOfTabs;

        public MyPagerAdapter(FragmentManager fm,int numTabs) {
            super(fm);
            this.mNumOfTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {
        return PagerItemFragment.newInstance(position, ""+position);
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
    @SuppressLint("NewApi")
    Handler mHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // switch case
            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    hideProgDialog();
                    break;

                case LOAD_QUESTION_SUCCESS:

                    // GotoData();

                    break;

                default:
                    break;
            }

            return false;
        }
    });
    class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;

        @Override
        protected Void doInBackground(String... params) {

            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi api = new LoadApi();

            // if(flagValue==0){
            try {
                // rest_id="76";
                // Log.d("rest_id back", " " + rest_id);
                api.notificationLoader(getActivity());
                Log.d("notice_list_size", " " + notificationEntities.size());
            } catch (Exception e) {
                // result1="Please select image";
                e.printStackTrace();

            }
            // }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Void result) {

            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);


        }

    }


    @SuppressLint("InlinedApi")
    private void showProgDialog () {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(getActivity());
            } else {
                progress_dialog = new ProgressDialog(getActivity());
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // hide progress

    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void loadData() {
        Log.d("inside ", " load data");
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            Log.d("inside ", " load data inside async");
            asyncLoad.execute();
        }
    }

}
