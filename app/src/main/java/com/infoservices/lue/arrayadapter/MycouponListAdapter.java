package com.infoservices.lue.arrayadapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.infoservices.lue.adapterdata.CouponlistData;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.dealAndDiscount.MyCouponsDetailsActivity;
import com.infoservices.lue.dealAndDiscount.RedeemActivity;

import java.util.ArrayList;


public class MycouponListAdapter extends BaseAdapter{
	
	private ArrayList<CouponlistData> Mycoupon_list = new ArrayList<CouponlistData>();

	public MycouponListAdapter(ArrayList<CouponlistData>Mycoupon_list){
		this.Mycoupon_list = Mycoupon_list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Mycoupon_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Mycoupon_list.get(position).getTransid();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		final Context context = parent.getContext();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.mycoupon_list_layout, null);
		}
		TextView coupon_details = (TextView)convertView.findViewById(R.id.coupon_detail_txt);
		TextView coupon_time = (TextView)convertView.findViewById(R.id.coupon_time_txt);
		TextView coupon_credits = (TextView)convertView.findViewById(R.id.coupon_credit_txt);
		TextView coupon_serial = (TextView)convertView.findViewById(R.id.coupon_serial_txt);
		
		Button redeem_btn = (Button) convertView.findViewById(R.id.coupon_redeem_btn);
		
		coupon_details.setText(Mycoupon_list.get(position).getCoupon_details());
		coupon_time.setText(Mycoupon_list.get(position).getTime());
		coupon_credits.setText(Mycoupon_list.get(position).getCredits());
		coupon_serial.setText(Mycoupon_list.get(position).getSerial());
		
		convertView.setBackgroundResource(android.R.color.white);
		/*if( (position %2) == 0){
			convertView.setBackgroundResource(android.R.color.white);
		}else{
			convertView.setBackgroundColor(0xD0D1CF);
		}*/
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MyCouponsDetailsActivity.class);
				intent.putExtra("TRANSID", Mycoupon_list.get(position).getTransid());
				Log.v("##check TRANS  id ##", ""+getItem(position).toString());
				context.startActivity(intent);
			}
		});
		
		redeem_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent redeem_intent = new Intent(context, RedeemActivity.class);
				redeem_intent.putExtra("DEALID", Mycoupon_list.get(position).getDealid());
				Log.v("##check deal  id ##", ""+Mycoupon_list.get(position).getDealid());
				context.startActivity(redeem_intent);
			}
		});
		return convertView;
	}

}
