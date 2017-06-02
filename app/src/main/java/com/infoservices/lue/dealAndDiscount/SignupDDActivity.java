package com.infoservices.lue.dealAndDiscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignupDDActivity extends AppCompatActivity implements ApiResponse {
    private ArrayList<String> ctryname = new ArrayList<String>();
    private ArrayList<String> ctryid = new ArrayList<String>();
    ArrayAdapter<String> ctrynameadap;
    ArrayAdapter<String> ctynameadap;
    Spinner spncun;
    String countryid, cityid, uname, upass, uphone, mail, emailsub, iterms;
    private ArrayList<String> ctyname = new ArrayList<String>();
    private ArrayList<String> ctyid = new ArrayList<String>();

    Spinner spncty;
    private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_dd);
        spncun = (Spinner) findViewById(R.id.spncountry);
        spncty = (Spinner) findViewById(R.id.spncity);
        new ApiService(this, this).execute("country.html");

        spncun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int posi, long arg3) {
                // TODO Auto-generated method stub
                if (posi != 0) {
                    countryid = ctryid.get(posi);
                    Log.v("if call", "going citylist");
                    new citylist();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        spncty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (arg2 != 0) {
                    cityid = ctyid.get(arg2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
    public void sign_up_click(View v) {
        // Toast.makeText(getApplicationContext(), "Signup click",
        // Toast.LENGTH_SHORT).show();
        EditText txtemail = (EditText) findViewById(R.id.edtemail);
        EditText txtname = (EditText) findViewById(R.id.edtname);
        EditText txtpass = (EditText) findViewById(R.id.edtpass);
        EditText txtphone = (EditText) findViewById(R.id.edtphone);
        CheckBox chkbox = (CheckBox) findViewById(R.id.chkterms);
        CheckBox chksub = (CheckBox) findViewById(R.id.chkupdate);
        mail = txtemail.getText().toString().trim();
        uname = txtname.getText().toString().trim();
        upass = txtpass.getText().toString().trim();
        uphone = txtphone.getText().toString().trim();
        if (chkbox.isChecked()) {
            iterms = "1";
        } else {
            iterms = "0";
        }
        if (chksub.isChecked()) {
            emailsub = "1";
        } else {
            emailsub = "0";
        }


       if (uname.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.nameval,
                    Toast.LENGTH_SHORT).show();
        } else if (upass.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.passval,
                    Toast.LENGTH_SHORT).show();
        } else if (mail.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.emailval,   // Done By Ritesh
                    Toast.LENGTH_SHORT).show();
        }
        else if (!chkbox.isChecked()) {
            Toast.makeText(getApplicationContext(),
                    R.string.accptval,
                    Toast.LENGTH_SHORT).show();
        } else if (countryid == null) {
            Toast.makeText(getApplicationContext(),
                    R.string.countryval, Toast.LENGTH_SHORT).show();
        } else if (cityid == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.cityval),Toast.LENGTH_SHORT).show();
        }
        if (mail.length() > 0 & uname.length() > 0 & upass.length() > 0
                & chkbox.isChecked() & countryid != null & cityid != null) {
            // Toast.makeText(getApplicationContext(), "Sign up success",
            // Toast.LENGTH_SHORT).show();
            new signupdata();
        }
        // else
        // {
        // Toast.makeText(getApplicationContext(),
        // "enter the required values and agree the terms and conditions",
        // Toast.LENGTH_SHORT).show();
        // }
    }
    public void cancel_click(View v) {
        finish();
    }

    public void privacy_click(View v) {
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra("Share", getString(R.string.privacyurl));
        startActivity(intent);
    }

    public void terms_click(View v) {
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra("Share", getString(R.string.tcurl));
        startActivity(intent);
    }

    @Override
    public void getResult(boolean isSuccess, String result) {
        // TODO Auto-generated method stub
        if (isSuccess) {
            try {
                JSONObject jobj = new JSONObject(result)
                        .getJSONObject("response");
                String str = jobj.getString("httpCode");
                if (str.equals("202")) {
                    JSONArray array = jobj.getJSONArray("country_list");
                    int count = array.length();
                    ctryname.add("Select Country");
                    ctryid.add("-1");
                    for (int i = 0; i < count; i++) {
                        String counname = array.getJSONObject(i).getString(
                                "country_name");
                        String counid = array.getJSONObject(i).getString(
                                "country_id");
                        ctryname.add(counname);
                        ctryid.add(counid);
                    }
                    ctrynameadap = new ArrayAdapter<String>(
                            getApplicationContext(),
                            R.layout.simplespinneritem, ctryname);
                    ctrynameadap
                            .setDropDownViewResource( R.layout.simplespinneritem);
                    spncun.setAdapter(ctrynameadap);

                }
            } catch (Exception e) {

            }
        }
    }

    class signupdata implements ApiResponse {
        public signupdata() {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", mail));
            nameValuePairs.add(new BasicNameValuePair("password", upass));
            nameValuePairs.add(new BasicNameValuePair("name", uname));
            nameValuePairs.add(new BasicNameValuePair("mobile", uphone));
            nameValuePairs.add(new BasicNameValuePair("country", countryid));
            nameValuePairs.add(new BasicNameValuePair("city", cityid));
            nameValuePairs.add(new BasicNameValuePair("iagree", iterms));
            nameValuePairs.add(new BasicNameValuePair("email_subscriper",
                    emailsub));
            Log.d("parameters","parameters"+nameValuePairs);
            new ApiService(SignupDDActivity.this, this, true, nameValuePairs)
                    .execute("signup.html");
            // new ApiService(SignupActivity.this, this).execute("signup.html");
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            // TODO Auto-generated method stub
            Log.v("Sign up Status", result);

            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);

                    JSONObject obj = json.getJSONObject("response");
                    System.out.println("-------object----" + obj);

                    if (obj.getInt("httpCode") == 202) {

                        message = obj.getString("Message");
                        Toast.makeText(getApplicationContext(), message,
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupDDActivity.this,
                                Logindd.class);
                        startActivity(intent);

                    } else if (obj.getInt("httpCode") == 401) {
                        message = obj.getString("Message");
                        new AlertDialog.Builder(SignupDDActivity.this)
                                .setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.cancel();

                                            }
                                        }).show();

                    }
                } catch (Exception e) {

                }
            }

        }

    }

    class citylist implements ApiResponse {
        public citylist() {
            // TODO Auto-generated constructor stub
            new ApiService(SignupDDActivity.this, this)
                    .execute("city.html?country_id=" + countryid);
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    JSONObject jobj = new JSONObject(result)
                            .getJSONObject("response");
                    String str = jobj.getString("httpCode");
                    if (str.equals("202")) {
                        JSONArray array = jobj.getJSONArray("city_list");
                        int count = array.length();
                        ctyname.add(""+getString(R.string.citydrop1));
                        ctyid.add("-1");
                        for (int i = 0; i < count; i++) {
                            String counname = array.getJSONObject(i).getString(
                                    "city_name");
                            String counid = array.getJSONObject(i).getString(
                                    "city_id");
                            ctyname.add(counname);
                            ctyid.add(counid);
                        }
                        ctynameadap = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.simplespinneritem, ctyname);
                        ctynameadap
                                .setDropDownViewResource(R.layout.simplespinneritem);
                        spncty.setAdapter(ctynameadap);
                    }
                } catch (Exception e) {

                }
            }
        }

    }
}


