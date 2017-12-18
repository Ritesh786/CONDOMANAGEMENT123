package com.infoservices.lue.dealAndDiscount;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.infoservices.lue.api.ApiService;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.interfaces.ApiResponse;
import com.infoservices.lue.session.Session;
import com.infoservices.lue.util.SharedPrefs;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ChangePassword extends Activity implements ApiResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefs.setLanguage(getBaseContext());
        setContentView(R.layout.activity_change_password);
        Button cancel = (Button) findViewById(R.id.textView_cancel);
        ImageView back_change_pass = (ImageView) findViewById(R.id.back_change_pass);
        Button send = (Button) findViewById(R.id.textView_send);
        final EditText edittext_oldpassword = (EditText) findViewById(R.id.edittext_oldpassword);
        final EditText edittext_newpassword = (EditText) findViewById(R.id.edittext_newpassword);
        final EditText edittext_repetpassword = (EditText) findViewById(R.id.edittext_repetpassword);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ApiResponse apiResponse = this;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkforgotPassValidation(edittext_oldpassword.getText().toString().trim(),
                        edittext_newpassword.getText().toString().trim(),edittext_repetpassword.getText().toString().trim())){
                    Session session = new Session(ChangePassword.this);
                    final String USERID = session.getUSerId();
                    final String oldpass = edittext_oldpassword.getText().toString().trim();
                    final String passwo = edittext_newpassword.getText().toString().trim();
                    List<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                    nameValuePairs.add(new BasicNameValuePair("oldpassword", oldpass));
//                    nameValuePairs.add(new BasicNameValuePair("password", passwo));
                    String URl = "change-password/?id=" + USERID+"&oldpassword="+edittext_oldpassword.getText()
                            +"&password="+edittext_newpassword.getText();
                    new ApiService( ChangePassword.this, apiResponse, true, nameValuePairs).execute(URl);

                    Log.d("param00", String.valueOf(nameValuePairs));
                }

            }
        });
    }

    private boolean checkforgotPassValidation(String oldpass, String newpass, String repass){
        boolean flag = true;
        if(!isEmpty(oldpass)) {
            flag = false;

            Toast.makeText(ChangePassword.this,getString(R.string.oldpassworderror),Toast.LENGTH_LONG).show();
        }
        else if(! isEmpty(newpass)){
            flag = false;

            Toast.makeText(ChangePassword.this,getString(R.string.newpassworderror),Toast.LENGTH_LONG).show();
        } else if(! isEmpty(repass)){
            flag = false;

            Toast.makeText(ChangePassword.this,getString(R.string.confirmpasswordError),Toast.LENGTH_LONG).show();
        }
        else if(!isSame(newpass, repass)){
            flag = false;
            Toast.makeText(ChangePassword.this,getString(R.string.passwordNotMatch),Toast.LENGTH_LONG).show();
        }
        return flag;
    }

    public  boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public  boolean isEmpty(String target) {
        if (target.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public   boolean isSame(String target, String newtar) {
        if (target.equals(newtar)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getResult(boolean isSuccess, String result) {
        System.out.print(result);
        if (isSuccess)
            Toast.makeText(ChangePassword.this,getString(R.string.passwordSuccess),Toast.LENGTH_LONG).show();
    }
}
