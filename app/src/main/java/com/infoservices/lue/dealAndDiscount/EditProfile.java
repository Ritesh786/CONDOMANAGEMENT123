package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.SharedPrefs;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends Activity implements ApiResponse {

    EditText firstName,LastName,EmailID,Address1,Address2,Phone,zipcode;
    ImageView back_edit_profile;
    Spinner country,city;
    String countryid, cityid;
    private ArrayList<String> ctyname = new ArrayList<String>();
    private ArrayList<String> ctyid = new ArrayList<String>();
    private ArrayList<String> ctryname = new ArrayList<String>();
    private ArrayList<String> ctryid = new ArrayList<String>();
    ArrayAdapter<String> ctrynameadap;
    ArrayAdapter<String> ctynameadap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);
        SharedPrefs.setLanguage(getBaseContext());
        firstName = (EditText) findViewById(R.id.firstName);
        LastName = (EditText) findViewById(R.id.lastname);
        EmailID = (EditText) findViewById(R.id.emailID);
        Address1 = (EditText) findViewById(R.id.address1);
        Address2 = (EditText) findViewById(R.id.address2);
        Phone = (EditText) findViewById(R.id.edtphone);
        country = (Spinner) findViewById(R.id.spncountry);
        city = (Spinner) findViewById(R.id.spncity);
        zipcode = (EditText) findViewById(R.id.zipcode);
        back_edit_profile = (ImageView) findViewById(R.id.back_edit_profile);
        back_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        Button save = (Button) findViewById(R.id.btnsignup);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new SaveData();
            }
        });
        initData();
        new ApiService(this, this).execute("country.html");
    }

    private void initData(){
        String value = getIntent().getExtras().getString("reposone");
        try {
            JSONObject jsonObject= new JSONObject(value);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject data =response.getJSONObject("user_detail");
            firstName.setText(data.getString("firstname"));
            LastName.setText(data.getString("lastname"));
            EmailID.setText(data.getString("email"));
            Phone.setText(data.getString("phone_number"));
            Address1.setText(data.getString("address1"));
            Address2.setText(data.getString("address2"));
           // country.setText(data.getString("country_name"));
            //city.setText(data.getString("city_name"));
            zipcode.setText(data.getString("zipcode"));
        }
        catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }
    @Override
    public void getResult(boolean isSuccess, String result) {
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
                    country.setAdapter(ctrynameadap);

                }
            } catch (Exception e) {

            }
        }
    }

    class citylist implements ApiResponse {
        public citylist() {
            // TODO Auto-generated constructor stub
            new ApiService(EditProfile.this, this)
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
                        city.setAdapter(ctynameadap);
                    }
                } catch (Exception e) {

                }
            }
        }

    }

    class SaveData implements ApiResponse {
        public SaveData() {
            // TODO Auto-generated constructor stub

            Session session = new Session(EditProfile.this);
            final String USERID = session.getUSerId();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", USERID));
            nameValuePairs.add(new BasicNameValuePair("firstname", firstName.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("lastname", LastName.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("email", EmailID.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("mobile", Phone.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("address1", Address1.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("address2", Address2.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("country",country.getSelectedItem().toString()));
            nameValuePairs.add(new BasicNameValuePair("city",city.getSelectedItem().toString()));
            nameValuePairs.add(new BasicNameValuePair("zipcode",zipcode.getText().toString()));
            Log.d("parameters","parameters"+nameValuePairs);
            new ApiService(EditProfile.this, this, true, nameValuePairs)
                    .execute("edit-profile.html");

        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                      }
        }

    }

}
