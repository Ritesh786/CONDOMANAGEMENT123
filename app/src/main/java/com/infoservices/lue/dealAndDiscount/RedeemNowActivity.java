/**
 * 
 */
package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.log.Logger;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ndot
 * 
 */
public final class RedeemNowActivity extends Activity implements OnClickListener{
	ImageView plus,minus;
	TextView qty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redeemnow_layout);
		TextView crdt_balTxt = (TextView) findViewById(R.id.redeennow_usercredits_txt);
		crdt_balTxt.setText(TabActivity.user_credits + " "
				+ getString(R.string.available_credits));
		TextView usernameTxt = (TextView) findViewById(R.id.redeemnow_username);
		usernameTxt.setText(TabActivity.user_name);

		Button cancelBtn = (Button) findViewById(R.id.redeemnow_cancel);
		Button printBtn = (Button) findViewById(R.id.redeemnow_print);

		 plus = (ImageView) findViewById(R.id.plus);
		 minus = (ImageView) findViewById(R.id.minus);
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);

		 printBtn = (Button) findViewById(R.id.redeemnow_print);

		qty = (TextView) findViewById(R.id.redeem_now_qty_edt);

		final TextView totalCreditsTxt = (TextView) findViewById(R.id.total_crdts);
		TextView deal_titleTxt = (TextView) findViewById(R.id.deal_title);
		TextView deal_crdtsTxt = (TextView) findViewById(R.id.credits);
		ImageView back_radee_lay = (ImageView) findViewById(R.id.back_radee_lay);
		back_radee_lay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Bundle bundle = getIntent().getExtras();
		final String DEALTITLE = bundle.getString("DEALTITLE");
		String CREDITPOINT = bundle.getString("CREDITPOINT");
		final String deallimit = bundle.getString("MAXLIMIT");
		final String mindeal = bundle.getString("MINLIMIT");
		final String avail = bundle.getString("DEALAVAIL");
		// final String rbal=bundle.getString("REWARD");
		// Toast.makeText(getApplicationContext(), rbal,
		// Toast.LENGTH_SHORT).show();
		// TabActivity.reward_points=rbal;
		TextView rewardTxt = (TextView) findViewById(R.id.reward_txt);
		rewardTxt.setText(""+ TabActivity.reward_points);
		int temp = 0;
		try {
			temp = Integer.parseInt(CREDITPOINT);
		} catch (Exception e) {
		}

		final int CPOINT = temp;

		qty.setText(mindeal);
		deal_crdtsTxt.setText(String.valueOf(CREDITPOINT));
		totalCreditsTxt.setText(String.valueOf(CREDITPOINT));
		deal_titleTxt.setText(DEALTITLE);

		/*qty.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				String count = qty.getText().toString().trim();
				if (count.length() > 0) {
					try {

						int quanity = Integer.parseInt(count);
						if ((quanity < Integer.parseInt(mindeal))) {
							Toast.makeText(getApplicationContext(),
									R.string.dealminerror,
									Toast.LENGTH_SHORT).show();
							qty.setText(mindeal);
						}
						else if (quanity <= Integer.parseInt(deallimit)) {
							totalCreditsTxt.setText(String
									.valueOf((CPOINT * quanity)));
						} else if (deallimit.equals("0")) {
							Log.i("run", "runworking" + deallimit);
							totalCreditsTxt.setText(String
									.valueOf((CPOINT * quanity)));
						} *//*else {
							Toast.makeText(
									getApplicationContext(),
									"Deal limit exceeds. Your maximum deal quantity is "
											+ deallimit + ".",
									Toast.LENGTH_SHORT).show();
							qty.setText("");
						}*//*
						*//*
						 * if(quanity>Integer.parseInt(avail)&&(!avail.equals("0"
						 * ))) { Toast.makeText(getApplicationContext(),
						 * "Deal limit exceeds. Your available deal quantity is "
						 * +avail+".", Toast.LENGTH_SHORT).show();
						 * qty.setText(""); }
						 *//*
						else if(quanity <= Integer.parseInt(avail)) {
							
							totalCreditsTxt.setText(String
									.valueOf((CPOINT * quanity)));
						} else if (avail.equals("0")) {
							totalCreditsTxt.setText(String
									.valueOf((CPOINT * quanity)));
						} else {
							Toast.makeText(
									getApplicationContext(),
									R.string.deallimiterror+"" 
											+ avail + ".", Toast.LENGTH_SHORT)
									.show();
						}

					} catch (Exception e) {
					}
				} else {
					totalCreditsTxt.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});*/

		printBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String quantity = qty.getText().toString().trim();

				if (quantity.length() > 0) {
					try {
						int quty = Integer.parseInt(quantity);
						if (quty <= 0) {
							//StringUtil.setError(qty,getString(R.string.coupongreatererror));
							Toast.makeText(RedeemNowActivity.this,""+getString(R.string.coupongreatererror),Toast.LENGTH_LONG).show();
						} else {
							final String DEALKEY = getIntent().getExtras()
									.getString("DEALKEY");
							final String DEALID = getIntent().getExtras()
									.getString("DEALID");
							new Print(DEALKEY, DEALID, quantity);
						}
					} catch (Exception e) {
					}

				} else {
					//StringUtil.setError(qty, ""+R.string.couponval);
					Toast.makeText(RedeemNowActivity.this,""+R.string.couponval,Toast.LENGTH_LONG).show();
				}
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.plus){
			String count = qty.getText().toString().trim();
			if (count.length() > 0) {
				int count2=Integer.parseInt(count);
				count2++;
				qty.setText(""+count2);
			}
		}else {
			String count = qty.getText().toString().trim();
			if (count.length() > 0) {
				int count2=Integer.parseInt(count);
				if (count2>0){
					count2--;
					qty.setText(""+count2);
				}else{
					qty.setText("0");
				}
			}
		}
	}

	private final class Print implements ApiResponse {

		public Print(String dealkey, String dealid, String qty) {
			// TODO Auto-generated constructor stub
			Session session = new Session(RedeemNowActivity.this);
			final String USERID = session.getUSerId();

			final String URL = "payment.html?user_id=" + USERID + "&dealkey="
					+ dealkey + "&id=" + dealid + "&quantity=" + qty;
			new ApiService(RedeemNowActivity.this, this).execute(URL);
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if (isSuccess) {
				try {
					
					System.out.println("Result"+result);
					JSONObject jobj = new JSONObject(result)
							.getJSONObject("response");
					String msg = jobj.getString("Message");
					String http_code = jobj.getString("httpCode");
					Logger.showCenterToast(msg, RedeemNowActivity.this);
					if (http_code.equals("202")) {
						// new MyProfile();
						final String DEALID = jobj.getString("transaction_id");
						Log.v("iiiiiiiD", DEALID);
						Intent intent = new Intent(RedeemNowActivity.this,
								MyCouponsDetailsActivity.class);
						intent.putExtra("TRANSID", DEALID);
						startActivity(intent);
						finish();
					}

				} catch (JSONException e) {
				}
			} else {
				Logger.showCenterToast(result, RedeemNowActivity.this);
			}
		}
	}

	private class MyProfile implements ApiResponse {

		public MyProfile() {
			// TODO Auto-generated constructor stub
			Session session = new Session(RedeemNowActivity.this);
			final String USERID = session.getUSerId();
			final String URL = "user_profile.html/?id=" + USERID;
			new ApiService(RedeemNowActivity.this, this).execute(URL);
		}

		@Override
		public void getResult(boolean isSuccess, String result) {
			// TODO Auto-generated method stub
			if (isSuccess) {
				try {
					JSONObject jobj = new JSONObject(result).getJSONObject(
							"response").getJSONObject("user_detail");
					// final String USER_CREDITS =
					// jobj.getString("credit_balance");
					final String RWRD_POINTS = jobj.getString("reward_balance");
					// final String FIRST_NAME = jobj.getString("firstname");
					TabActivity.reward_points = RWRD_POINTS;
					TextView rewardTxt = (TextView) findViewById(R.id.reward_txt);
					rewardTxt.setText(getString(R.string.rewardpoints) + " : "
							+ TabActivity.reward_points);
					// TabActivity.user_credits = USER_CREDITS;
					// TabActivity.user_name = FIRST_NAME;

				} catch (JSONException e) {
				}
			}
			// startActivity(new Intent(RedeemNowActivity.this,
			// MycouponListActivity.class));
			String DEALID = getIntent().getExtras().getString("DEALID");
			Intent intent = new Intent(RedeemNowActivity.this,
					MyCouponsDetailsActivity.class);
			intent.putExtra("DEALID", DEALID);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 //new MyProfile();
	}
}
