package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infoservices.lue.adapters.PaymentTransactionAdapter;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.PaymentEntity;
import com.infoservices.lue.utills.LoadApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistoryFragment extends Fragment {
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    TextView text_transaction;
    ArrayList<PaymentEntity>paymentEntities=new ArrayList<PaymentEntity>();
    ListView list_payment;
    public TransactionHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text_transaction=(TextView)getActivity().findViewById(R.id.text_transaction);
        list_payment=(ListView) getActivity().findViewById(R.id.list_payment);
        text_transaction.setVisibility(View.GONE);
        sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        loadData();
    }

    @SuppressLint("NewApi")
    Handler mHandler = new Handler(new Handler.Callback() {

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
    class AsyncLoadData extends AsyncTask<String, Void, String> {
        boolean flag = false;

        @Override
        protected String doInBackground(String... params) {
            String sret="";
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi loadApi=new LoadApi();
            // if(flagValue==0){
            try {

                sret=loadApi.loadTransactions(params);
            } catch (Exception e) {
                e.printStackTrace();

            }
            // }
            return sret;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            Log.d ( "transaction list",""+result );
            try {
                JSONObject jobject1=new JSONObject(result);
                //status
                if(jobject1.getString("status").equals("1")){
                    JSONArray jsonArray=jobject1.getJSONArray("info");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jobject=jsonArray.getJSONObject(i);
                        PaymentEntity paymentEntity=new PaymentEntity();
                        paymentEntity.setId(jobject.getString("id"));
                        paymentEntity.setMgnt_id(jobject.getString("mgnt_id"));
                        paymentEntity.setMember_id(jobject.getString("member_id"));
                        paymentEntity.setQuantity(jobject.getString("quantity"));
                        paymentEntity.setUnit_price(jobject.getString("unit_price"));
                        paymentEntity.setDiscount(jobject.getString("discount"));
                        paymentEntity.setDelivery_charge(jobject.getString("delivery_charge"));
                        paymentEntity.setGst(jobject.getString("gst"));
                        paymentEntity.setGrand_total(jobject.getString("grand_total"));
                        paymentEntity.setVia(jobject.getString("via"));
                        paymentEntity.setStatus(jobject.getString("status"));
                        paymentEntity.setDate(jobject.getString("date"));
                        paymentEntities.add(paymentEntity);
                    }
                    list_payment.setAdapter(new PaymentTransactionAdapter(getActivity(),paymentEntities));
                } else if(jobject1.getString("status").equals("0")){
                    text_transaction.setText(jobject1.getString("msg"));
                }else{
                    Toast.makeText(getActivity(),"Something went worng ! Try again !!",Toast.LENGTH_LONG).show();
                    text_transaction.setVisibility(View.VISIBLE);
                    text_transaction.setText("Something went worng ! Try again !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    @SuppressLint("InlinedApi")
    private void showProgDialog () {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(getActivity()
                        ,R.style.AppCompatAlertDialogStyle);
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
            asyncLoad.execute(sharedPreferences.getString( Id,"" )
                    );
        }
    }
}
