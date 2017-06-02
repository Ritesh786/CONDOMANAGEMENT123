package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.infoservices.lue.adapterdata.TransactionData;
import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.arrayadapter.TransactionlistAdapter;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.session.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionHistoryActivity extends Activity {
	
   private ArrayList<TransactionData>transactionDatas = new ArrayList<TransactionData>();
   private ListView list;
	ImageView back_trasaction_his;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_history_layout);
		list = (ListView)findViewById(R.id.listView1);
		back_trasaction_his = (ImageView) findViewById(R.id.back_trasaction_his);
		back_trasaction_his.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		transactionDatas.clear();
		new Transactionhistory();
	}
	 class Transactionhistory implements ApiResponse {
		public Transactionhistory(){
			Session session = new Session(TransactionHistoryActivity.this);
			final String USERID = session.getUSerId(); 
			String URL = "credits-transaction.html/?id=" + USERID;
			new ApiService(TransactionHistoryActivity.this, this).execute(URL);
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
			try {
				JSONObject obj = new JSONObject(result);
				JSONObject jsn_obj = obj.getJSONObject("response");
				String user_credits = obj.getJSONObject("response").getString("user_credit_balance");
                TabActivity.user_credits = user_credits;
                TextView cr_balTxt = (TextView) findViewById(R.id.trans_history_crdit_bal_txt);
    			cr_balTxt.setText(" " + TabActivity.user_credits);
				JSONArray jsn_array = jsn_obj.getJSONArray("credit_list");
				int size = jsn_array.length();
				for (int i = 0; i < size; i++) {
					TransactionData transactionvalues = new TransactionData();
					 transactionvalues.setDate(jsn_array.getJSONObject(i).getString("created_date"));
					 transactionvalues.setPrice(jsn_array.getJSONObject(i).getString("price"));
					 transactionvalues.setCredit_points(jsn_array.getJSONObject(i).getString("credit_points"));
					 transactionvalues.setTransaction_id(jsn_array.getJSONObject(i).getString("transaction_id"));
					 transactionDatas.add(transactionvalues);

				}
				TransactionlistAdapter adapter = new TransactionlistAdapter(transactionDatas);
				list.setAdapter(adapter);
				TextView nodata_txt = (TextView) findViewById(R.id.transaction_empty_txt);
				if(size > 0){
					nodata_txt.setText("");
				}else{
					nodata_txt.setText(R.string.nodata);
					nodata_txt.setTextColor(TransactionHistoryActivity.this.getResources().getColor(R.color.caldroid_white));
				}
			} catch (Exception e) {}
		}else{
			Logger.showCenterToast(result, TransactionHistoryActivity.this);
		}
	 }
	 }
	 @Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume(); 
			TextView cr_balTxt = (TextView) findViewById(R.id.trans_history_crdit_bal_txt);
			cr_balTxt.setText("" + TabActivity.user_credits);
		}
}
