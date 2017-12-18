package com.infoservices.lue.dealAndDiscount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.infoservices.lue.condomanagement.R;

public class TabActivity extends android.app.TabActivity{

	public static String user_credits = "0";
	public static String reward_points = "0";
	public static String user_name = "";

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout);

		TabHost tabhost = getTabHost();
	  tabhost.getTabWidget().setBackgroundResource(R.drawable.tab_bg);

		TabSpec home_tab = tabhost.newTabSpec("HOME");
		home_tab.setIndicator(createTabView(this, R.drawable.tab_home_selector));
		Intent home_intent = new Intent(this, HomeActivity.class);
		// check here is from search tab
		String url = getIntent().getStringExtra("SEARCH_URL");
		if(url != null && url.length() > 0)
			home_intent.putExtra("SEARCH_URL", url);

		home_tab.setContent(home_intent);

		TabSpec search_tab = tabhost.newTabSpec("SEARCH");
		search_tab.setIndicator(createTabView(this, R.drawable.tab_search_selector));
		Intent search_intent = new Intent(this, SearchActivity.class);
		search_tab.setContent(search_intent);

		TabSpec mycoupon_tab = tabhost.newTabSpec("MY COUPON");
		mycoupon_tab.setIndicator(createTabView(this, R.drawable.tab_mycoupon_selector));
		Intent mycoupon_intent = new Intent(this, MycouponListActivity.class);
		mycoupon_tab.setContent(mycoupon_intent);

		TabSpec setting_tab = tabhost.newTabSpec("SETTINGS");
		setting_tab.setIndicator(createTabView(this, R.drawable.tab_setting_selector));
		Intent setting_intent = new Intent(this, Settings.class);
		setting_tab.setContent(setting_intent);

		tabhost.addTab(home_tab);
		tabhost.addTab(search_tab);
		tabhost.addTab(mycoupon_tab);
		tabhost.addTab(setting_tab);

		tabhost.setCurrentTab(0);

	}

	private static View createTabView(final Context context, final int drawable) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
		ImageView img = (ImageView) view.findViewById(R.id.tabsLayout);
		img.setImageResource(drawable);
		return view;
	}


}

          