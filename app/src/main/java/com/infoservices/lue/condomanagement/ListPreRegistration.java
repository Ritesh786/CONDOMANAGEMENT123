package com.infoservices.lue.condomanagement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infoservices.lue.adapters.PreResitrationAdapter;
import com.infoservices.lue.entity.PreResitrationEntity;
import com.infoservices.lue.fragments.PreRegistationFragment;
import com.infoservices.lue.utills.LoadApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListPreRegistration extends AppCompatActivity implements View.OnClickListener{
View header_list_reg;
    TextView header_text;
    ImageView go_back;
    ListView list_pre_reg;

    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    public static final String Name = "name";
    public static final String MGNT_ID = "mgnt_id";
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    ArrayList<PreResitrationEntity> preResitrationEntities=new ArrayList<PreResitrationEntity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pre_registration);
        header_list_reg=findViewById(R.id.header_list_reg);
        header_text=(TextView)header_list_reg.findViewById(R.id.header_text);
        go_back=(ImageView) header_list_reg.findViewById(R.id.go_back);
        list_pre_reg=(ListView)findViewById(R.id.list_pre_reg2);
        header_text.setText(" Pre-Registration List ");
        go_back.setOnClickListener(this);
        loadData();
    }

    @Override
    public void onClick(View v) {
        finish();
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
            String res=null;
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi api = new LoadApi();
            try {
                res=api.listPreRegistation( params );
            } catch (Exception e) {

                e.printStackTrace();

            }
            // }
            return res;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {

            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);

            try {
                JSONObject jobject1 = new JSONObject(result);
                if (jobject1.getString( "status" ).equals( "1" )){
                    JSONArray jsonArray=jobject1.getJSONArray("info");
                    for (int i=0;i<jsonArray.length();i++){
                        PreResitrationEntity preResitrationEntity=new PreResitrationEntity();
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        preResitrationEntity.setMember_name(jsonObject.getString("visitor_name"));
                        preResitrationEntity.setMgnt_id(jsonObject.getString("mgnt_id"));
                        preResitrationEntity.setMember_id(jsonObject.getString("member_id"));
                        preResitrationEntity.setNric_number(jsonObject.getString("nric_number"));
                        preResitrationEntity.setCar_number(jsonObject.getString("car_number"));
                        preResitrationEntity.setPassport_no(jsonObject.getString("passport_no"));
                        preResitrationEntity.setFrom_date(jsonObject.getString("from_date"));
                        if (jsonObject.has("to_date"))preResitrationEntity.setTo_date(jsonObject.getString("to_date"));
                        preResitrationEntity.setReg_date(jsonObject.getString("reg_date"));
                        preResitrationEntity.setStatus(jsonObject.getString("status"));
                        preResitrationEntities.add(preResitrationEntity);
                    }
                }else{
                    Toast.makeText( ListPreRegistration.this,"Something went wrong!!",Toast.LENGTH_LONG ).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText( ListPreRegistration.this,"Something went wrong!!",Toast.LENGTH_LONG ).show();
            }
            if (preResitrationEntities.size()>0) {
                list_pre_reg.setAdapter(new PreResitrationAdapter(ListPreRegistration.this, preResitrationEntities));
            }
        }

    }


    @SuppressLint("InlinedApi")
    private void showProgDialog () {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(ListPreRegistration.this,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(ListPreRegistration.this);
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
            sharedPreferences = getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
            asyncLoad.execute(
                    sharedPreferences.getString( Id,"" )
            );

        }
    }

}
