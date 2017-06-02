package com.infoservices.lue.arrayadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.util.ImageDownloader;

import java.util.ArrayList;

public class DealListAdapter extends BaseAdapter{
	
	private ArrayList <String> mDealName = new ArrayList<String>();
	private ArrayList <String> mDealDescription = new ArrayList<String>();
	private ArrayList <String> mDealCredits = new ArrayList<String>();
	private ArrayList <String> mDealKms = new ArrayList<String>();
	private ArrayList <String> mDealImg = new ArrayList<String>();
	private ArrayList <String> mDealId = new ArrayList<String>();
	private ImageDownloader mImgDownloader = new ImageDownloader();
	
	public DealListAdapter(ArrayList<String> name, ArrayList<String> dscrptn, ArrayList<String> credtis, ArrayList<String> kms, ArrayList<String> img, ArrayList<String> id){
		mDealName = name;
		mDealDescription = dscrptn;
		mDealCredits = credtis;
		mDealKms = kms;
		mDealImg = img;
		mDealId = id;
	}
	
	public void update(ArrayList<String> name, ArrayList<String> dscrptn, ArrayList<String> credtis, ArrayList<String> kms, ArrayList<String> img, ArrayList<String> id){
		mDealName = name;
		mDealDescription = dscrptn;
		mDealCredits = credtis;
		mDealKms = kms;
		mDealImg = img;
		mDealId = id;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDealName.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDealId.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View view, final ViewGroup vgroup) {
		// TODO Auto-generated method stub
		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(vgroup.getContext());
			view = inflater.inflate(R.layout.deallist_layout, vgroup, false);
			
		}
		final TextView dealName = (TextView) view.findViewById(R.id.title);
		final TextView dealDescrptn = (TextView) view.findViewById(R.id.description);
		final TextView dealCrdts = (TextView) view.findViewById(R.id.credits);
		final TextView dealKms = (TextView) view.findViewById(R.id.kms);
		final ImageView imgView = (ImageView) view.findViewById(R.id.image);
		
		dealName.setText(mDealName.get(arg0));
		dealDescrptn.setText(mDealDescription.get(arg0));
		dealCrdts.setText(mDealCredits.get(arg0));
		dealKms.setText(mDealKms.get(arg0));
		
		mImgDownloader.download(mDealImg.get(arg0), imgView);
		if(((arg0 % 2) == 0)){
			view.setBackgroundResource(R.color.caldroid_gray);
		}else
			view.setBackgroundResource(R.color.caldroid_white);
		return view;
	}
}
