package com.infoservices.lue.condomanagement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.infoservices.lue.entity.ManagementEntity;
import com.infoservices.lue.fragments.BullatineandNotices;
import com.infoservices.lue.utills.LoadApi;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONObject;

import java.util.ArrayList;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    AsyncLoadData2 asyncLoad2;
    AlertDialog alertDialog;
    ArrayList<String> arrayList=new ArrayList<String>();
    ArrayList<ManagementEntity> managementEntities=null;
    String signup="";
    EditText full_name2,user_name2,password2,user_email2,user_mobile2,user_address,edit_unit_no;
    Button button_signup2;
    MaterialSpinner spinner;
    String name="",mgnt_id="",user_name="",password="",email="",address="",mobile="",reg_id="",unit_no="";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern =  "^\\+[0-9]{9,13}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        View header=findViewById(R.id.signup_header);
        ImageView back= (ImageView) header.findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        loadData();
        spinner = (MaterialSpinner) findViewById(R.id.spinner_signup);
        edit_unit_no=(EditText)findViewById(R.id.edit_unit_no);
        full_name2=(EditText)findViewById(R.id.full_name2);
        user_name2=(EditText)findViewById(R.id.user_name2);
        password2=(EditText)findViewById(R.id.password2);
        user_email2=(EditText)findViewById(R.id.user_email2);
        user_mobile2=(EditText)findViewById(R.id.user_mobile2);
        user_address=(EditText)findViewById(R.id.user_address);
        button_signup2=(Button) findViewById(R.id.button_signup2);
        button_signup2.setOnClickListener(this);

       /* spiner_signup.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>(){
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });
*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_signup2) {
            signup = "signup";
            SharedPreferences sharedPreferences = getSharedPreferences("loginpreference", Context.MODE_PRIVATE);
            String sp_text = spinner.getText().toString();
            for (int i=0;i<managementEntities.size();i++){
                if (sp_text.equals(managementEntities.get(i).getName())){
                    mgnt_id=managementEntities.get(i).getId();
                    break;
                }
            }
            unit_no=edit_unit_no.getText().toString().toUpperCase();
            name = full_name2.getText().toString();
            user_name = user_name2.getText().toString();
            password = password2.getText().toString();
            email = user_email2.getText().toString();
            address = user_address.getText().toString();
            mobile = user_mobile2.getText().toString();
            reg_id = sharedPreferences.getString("fcm", "");
            if (unit_no.equals("")){
                Toast.makeText(getApplicationContext(),"Empty Unit Number !!",Toast.LENGTH_LONG).show();
            }
            else if (name.equals("")||name.length()<5){
                Toast.makeText(getApplicationContext(),"Empty or Name must have atleast 5 letters !!",Toast.LENGTH_LONG).show();
            }
            else if(user_name.equals("")||user_name.length()<5){
                Toast.makeText(getApplicationContext(),"Empty or Username must have atleast 5 letters!!",Toast.LENGTH_LONG).show();
            }
            else if(password.equals("")||password.length()<5){
                Toast.makeText(getApplicationContext(),"Empty or Password must have atleast 5 letters !!",Toast.LENGTH_LONG).show();
            }
            else if(email.equals("")|| !email.matches(emailPattern)){
                Toast.makeText(getApplicationContext(),"Empty or wrong user name !!",Toast.LENGTH_LONG).show();
            }
            else if(mobile.equals("")||mobile.length()<8){
                Toast.makeText(getApplicationContext(),"Empty or Invalid Mobile !!",Toast.LENGTH_LONG).show();
            }
            else if(address.equals("")||address.length()<10){
                Toast.makeText(getApplicationContext(),"Empty or Address must have atleast 10 letters !!",Toast.LENGTH_LONG).show();
            }
            else{
                loadData();
            }
        }
    }

    // asynchronous task
    class AsyncLoadData extends AsyncTask<String, Void, ArrayList<ManagementEntity>> {
        boolean flag = false;

        @Override
        protected ArrayList<ManagementEntity> doInBackground(String... params) {

            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "Logging...";
            LoadApi api = new LoadApi();
                try {
                    managementEntities = api.getMAnagementList();
                }
                    catch (Exception e) {
                        e.printStackTrace();

                    }
                return managementEntities;

            }


        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(ArrayList<ManagementEntity> result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            arrayList.clear();
            for (int i=0;i<managementEntities.size();i++){
                arrayList.add(managementEntities.get(i).getName());
            }
            spinner = (MaterialSpinner) findViewById(R.id.spinner_signup);
            spinner.setItems(arrayList);

        }

    }
    class AsyncLoadData2 extends AsyncTask<String, Void, String> {
        boolean flag = false;

        @Override
        protected String doInBackground(String... params) {

            Log.d("inside ", " do in back above");
            String res="";
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "Logging...";
            LoadApi api = new LoadApi();
            try {
                res = api.signup(name,mgnt_id,user_name,password,email,address,"+60"+mobile,reg_id,unit_no);

            }
            catch (Exception e) {
                e.printStackTrace();

            }
            return res;

        }


        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            try{
                JSONObject jsonObject=new JSONObject(result);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
                alertDialogBuilder.setMessage(""+jsonObject.getString("msg"));
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // loadData();
                        arg0.dismiss();
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                  @Override
                                                  public void onShow(DialogInterface arg0) {
                                                      alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                                      alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                                  }
                                              }
                );
                alertDialog.show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(SignUpActivity.this
                        ,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(SignUpActivity.this);
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
            if (signup.equals("signup")){
                asyncLoad2 = new AsyncLoadData2();
                Log.d("inside ", " load data inside async2");
                asyncLoad2.execute();
            }else{
            asyncLoad = new AsyncLoadData();
                Log.d("inside ", " load data inside async");
                asyncLoad.execute();
            }

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}


