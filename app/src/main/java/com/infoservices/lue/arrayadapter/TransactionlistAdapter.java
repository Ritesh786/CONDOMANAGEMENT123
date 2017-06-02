package com.infoservices.lue.arrayadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.infoservices.lue.adapterdata.TransactionData;
import com.infoservices.lue.condomanagement.R;

import java.util.ArrayList;

public class TransactionlistAdapter extends BaseAdapter {
	private ArrayList<TransactionData> transaction_list = new ArrayList<TransactionData>();
	
	public TransactionlistAdapter(ArrayList<TransactionData>transaction_list) {
		this.transaction_list = transaction_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return transaction_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return transaction_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.transaction_list_layout, null);
		}
		TextView transaction_time = (TextView)convertView.findViewById(R.id.transaction_time_txt);
		TextView transaction_myr = (TextView)convertView.findViewById(R.id.transaction_myr_txt);
		TextView transaction_credits = (TextView)convertView.findViewById(R.id.transaction_credits_txt);
		TextView transaction_id = (TextView)convertView.findViewById(R.id.transaction_id_txt);
		
		transaction_time.setText(transaction_list.get(position).getDate());
		transaction_myr.setText(transaction_list.get(position).getPrice());
		transaction_credits.setText(transaction_list.get(position).getCredit_points());
		transaction_id.setText(transaction_list.get(position).getTransaction_id());
		return convertView;
	}

}
