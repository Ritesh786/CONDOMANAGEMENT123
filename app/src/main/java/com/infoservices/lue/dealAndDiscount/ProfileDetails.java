package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.SharedPrefs;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileDetails extends Activity implements ApiResponse {

    TextView firstName,LastName,EmailID,Address1,Address2,Phone,country,city,zipcode;
    String reposone ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefs.setLanguage(getBaseContext());
        setContentView(R.layout.activity_profile_details);
        firstName = (TextView) findViewById(R.id.firstnamevalue);
        LastName = (TextView) findViewById(R.id.lastnamevalue);
        EmailID = (TextView) findViewById(R.id.email_edt);
        Address1 = (TextView) findViewById(R.id.address1);
        Address2 = (TextView) findViewById(R.id.address2);
        Phone = (TextView) findViewById(R.id.mobile_no);
        country = (TextView) findViewById(R.id.country);
        city = (TextView) findViewById(R.id.city);
        zipcode = (TextView) findViewById(R.id.zipCode);
        ImageView imageViewleft = (ImageView) findViewById(R.id.imageViewleft);
        imageViewleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayout edit = (LinearLayout) findViewById(R.id.linearlayout_save);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetails.this,EditProfile.class);
                intent.putExtra("reposone",reposone);
                startActivity(intent);
            }
        });
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);

        final ApiResponse apiResponse = this;

        Session session = new Session(ProfileDetails.this);
        final String USERID = session.getUSerId();
        String URl = "user_profile.html/?id=" + USERID;
        new ApiService( ProfileDetails.this, apiResponse, true, nameValuePairs).execute(URl);
    }


    @Override
    public void getResult(boolean isSuccess, String result) {
     System.out.print(result);
        try {
            reposone = result;
            JSONObject jsonObject= new JSONObject(result);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject data =response.getJSONObject("user_detail");
            firstName.setText(data.getString("firstname"));
            LastName.setText(data.getString("lastname"));
            EmailID.setText(data.getString("email"));
            Phone.setText(data.getString("phone_number"));
            Address1.setText(data.getString("address1"));
            Address2.setText(data.getString("address2"));
            country.setText(data.getString("country_name"));
            city.setText(data.getString("city_name"));
            zipcode.setText(data.getString("zipcode"));
        }
        catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }
}
