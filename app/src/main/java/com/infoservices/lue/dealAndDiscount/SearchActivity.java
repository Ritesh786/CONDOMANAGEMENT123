package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends Activity implements OnItemSelectedListener{
	private ArrayList<String>categorylist = new ArrayList<String>();
	private ArrayList<String>Category_id = new ArrayList<String>();
	private ArrayList<String>statelist = new ArrayList<String>();
	private ArrayList<String>State_id = new ArrayList<String>();
	private ArrayList<String>merchantlist = new ArrayList<String>();
	private ArrayList<String>Merchant_id = new ArrayList<String>();
	private ArrayList<String>shoppinglist = new ArrayList<String>();
	private ArrayList<String>Shopping_id = new ArrayList<String>();
	private Spinner catgory_spinner,state_spinner,merchant_spinner,shoping_spinner;
	private String cid = "", sid = "",mid = "", smid = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_layout);
	    catgory_spinner = (Spinner)findViewById(R.id.search_spinner_catogry);
	    state_spinner = (Spinner)findViewById(R.id.search_spinner_state);
	    merchant_spinner = (Spinner)findViewById(R.id.search_spinner_merchant);
	    shoping_spinner = (Spinner)findViewById(R.id.search_spinner_shoping_mail);
	    Button search_btn = (Button)findViewById(R.id.search_btn);
	    
	    /*state_spinner.setVisibility(View.GONE);
	    merchant_spinner.setVisibility(View.GONE);
	    shoping_spinner.setVisibility(View.GONE);*/
		String[] items = getResources().getStringArray(R.array.category_list);
		ArrayAdapter<String>categoryadapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, items);
		categoryadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		catgory_spinner.setAdapter(categoryadapter);
	    
	    catgory_spinner.setOnItemSelectedListener(this);
	    state_spinner.setOnItemSelectedListener(this);
	    merchant_spinner.setOnItemSelectedListener(this);
	    shoping_spinner.setOnItemSelectedListener(this);
	    
	    search_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//if(catgory_spinner.getSelectedItemPosition() > 0 ){
				   // cid = getResources().getStringArray(R.array.your_array)[position];
					Intent intent = new Intent(SearchActivity.this, TabActivity.class);
					String URL = "?category="+cid+"&state="+sid+"&merchant="+mid+"&store="+smid;
					intent.putExtra("SEARCH_URL", URL);
					startActivity(intent);
					finish();
				/*}else{
					Logger.showCenterToast("Select category", SearchActivity.this);
				}*/
			}
		});
	  
	    
	    
	    merchantlist.add(""+getString(R.string.statedrop));
		Merchant_id.add("0");
		
		ArrayAdapter<String>merchantadapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, merchantlist);
		merchantadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		merchant_spinner.setAdapter(merchantadapter);
		
		shoppinglist.add(""+getString(R.string.marchantdrop));
		Shopping_id.add("0");
		
		ArrayAdapter<String>shopadapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, shoppinglist);
		shopadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		shoping_spinner.setAdapter(shopadapter);
		
	    new StateResponse(cid);
	}
	

	class StateResponse implements ApiResponse {
   
		public StateResponse(String catogery_id) {
			// TODO Auto-generated constructor stub
			String URL = "state.html/?category=" + catogery_id;
			new ApiService(SearchActivity.this, this).execute(URL);
		}
		 
		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject jsn_obj = obj.getJSONObject("response");
					JSONArray jsn_array = jsn_obj.getJSONArray("state_list");
					statelist.clear();
					statelist.add(""+getString(R.string.statedrop1)); 
					State_id.add("0");
					for (int i = 0; i < jsn_array.length(); i++) {
						String state_name = jsn_array.getJSONObject(i).getString("state_name");
						String state_id = jsn_array.getJSONObject(i).getString("state_id");
						State_id.add(state_id);
						statelist.add(state_name);
					}
					int size = statelist.size();
					String sList[] = new String[size];
					for (int i = 0; i < size; i++) {
						sList[i] = statelist.get(i);
					}
					ArrayAdapter<String>stateadapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, sList);
					stateadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
					state_spinner.setAdapter(stateadapter);
				} catch (Exception e) {}
			}else{
				Logger.showCenterToast(result, SearchActivity.this);
			}
		}
		
	}
	class merchantresponse implements ApiResponse{

		public merchantresponse(String sid) {
			// TODO Auto-generated constructor stub
			String URL = "merchant.html/?state=" + sid;
			new ApiService(SearchActivity.this, this).execute(URL);
		}
		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
				try {
					JSONObject obj = new JSONObject(result);
					Log.v("obj", "obj"+obj);
					JSONObject jsn_obj = obj.getJSONObject("response");
					JSONArray jsn_array = jsn_obj.getJSONArray("merchant_list");
					merchantlist.clear();
					merchantlist.add(""+getString(R.string.statedrop));
					Merchant_id.add("0");
					for (int i = 0; i < jsn_array.length(); i++) {
						String first_name = jsn_array.getJSONObject(i).getString("firstname");
						String merchant_id = jsn_array.getJSONObject(i).getString("user_id");
						Merchant_id.add(merchant_id);
						merchantlist.add(first_name);
					}
					int size = merchantlist.size();
					String mList[] = new String[size];
					for (int i = 0; i < size; i++) {
						mList[i] = merchantlist.get(i);
					}
					ArrayAdapter<String>merchantadapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, mList);
					merchantadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
					merchant_spinner.setAdapter(merchantadapter);
				} catch (Exception e) {}
			}else{
				Logger.showCenterToast(result, SearchActivity.this);
			}
		}
		
	}
	class shopingresponse implements ApiResponse{

		public shopingresponse(String merchant_id) {
			// TODO Auto-generated constructor stub
			String URL = "shopping-mall.html/?merchant=" + merchant_id;
			new ApiService(SearchActivity.this, this).execute(URL);
		}
		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if(isSuccess){
			try {
				JSONObject obj = new JSONObject(result);
				JSONObject jsn_obj = obj.getJSONObject("response");
				JSONArray jsn_array = jsn_obj.getJSONArray("shopping_mall_list");
				shoppinglist.clear();
				shoppinglist.add(""+getString(R.string.marchantdrop));
				Shopping_id.add("0");
				for (int i = 0; i < jsn_array.length(); i++) {
					String shoping_name = jsn_array.getJSONObject(i).getString("store_name");
					String store_id = jsn_array.getJSONObject(i).getString("store_id");
					shoppinglist.add(shoping_name);
					Shopping_id.add(store_id);
				}
				int size = shoppinglist.size();
				String shopList[] = new String[size];
				for (int i = 0; i < size; i++) {
					shopList[i] = shoppinglist.get(i);
					
				}
				ArrayAdapter<String>shopadapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, shopList);
				shopadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
				shoping_spinner.setAdapter(shopadapter);
			} catch (Exception e) {}
			}else{
				Logger.showCenterToast(result, SearchActivity.this);
			}
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == catgory_spinner) {
		    if (catgory_spinner.getSelectedItemPosition() > 0) {
				cid = getResources().getStringArray(R.array.category_ids)[pos];
				state_spinner.setVisibility(View.VISIBLE);
				new StateResponse(cid);
			}
			 
		}else if(arg0 == state_spinner){
			if (state_spinner.getSelectedItemPosition() > 0) {
				sid = State_id.get(pos);
				merchant_spinner.setVisibility(View.VISIBLE);
				new merchantresponse(sid);
			}
		
		}else if(arg0 == merchant_spinner){
			if(merchant_spinner.getSelectedItemPosition() > 0){
				mid = Merchant_id.get(pos);
				shoping_spinner.setVisibility(View.VISIBLE);
				new shopingresponse(mid);
			}
			 
		}else if(arg0 == shoping_spinner){
			if (shoping_spinner.getSelectedItemPosition() > 0) {
			}
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


}
