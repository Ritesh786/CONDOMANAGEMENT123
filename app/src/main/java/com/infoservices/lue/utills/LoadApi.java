package com.infoservices.lue.utills;

import android.app.Activity;
import android.app.DownloadManager;
import android.util.Log;

import com.infoservices.lue.entity.ManagementEntity;
import com.infoservices.lue.entity.NotificationEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class LoadApi {
    int count=0;

    public String postData(String[] valuse) throws IOException {
        String s=null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/login.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "username", valuse[0] );
            jsonObject.accumulate("password", valuse[1]) ;
            jsonObject.accumulate( "regId", valuse[2]) ;
            jsonObject.accumulate( "type", valuse[3]) ;
            Log.d("login==",""+jsonObject.toString());
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.d("login res==",""+s);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
    }
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return return_text;

    }
    public String signup(String name,String mgnt_id,String username,String password,String email,String address,String mobile,String reg_id,String unit_no){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/registration.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate("name", ""+name) ;
            jsonObject.accumulate("mgnt_id", ""+mgnt_id) ;
            jsonObject.accumulate("user_name", ""+username) ;
            jsonObject.accumulate("password", ""+password) ;
            jsonObject.accumulate("email", ""+email) ;
            jsonObject.accumulate("address", ""+address) ;
            jsonObject.accumulate("mobile", ""+mobile) ;
            jsonObject.accumulate("reg_id", ""+reg_id) ;
            jsonObject.accumulate("unit_no", ""+unit_no) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pro_ret;
    }
    public String facilitiesLoader(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/facilities_booking.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate("mgnt_id", valuse[0]) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pro_ret;
    }
    public String rulesAndRegulations(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/rules_regulation.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate("mgnt_id", valuse[0]) ;
            Log.d("params====",valuse.toString());
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pro_ret;
    }
    public String getImportantContact(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/important_numbers.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate("mgnt_id", valuse[0]) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return pro_ret;
    }
    public void notificationLoader(Activity activity){
        ArrayList<NotificationEntity> notificationEntities=new ArrayList<NotificationEntity>();
        notificationEntities.clear();
        try {
            URL js = new URL("http://api.condoassist2u.com/bulletin_notices.php");
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jobject1 = new JSONObject(line);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jobject1.getString("info"));
            Log.d("facility list", " below");
            Log.d("jsonArray", "  " + jsonArray.toString());
            Log.d("sizeeeem", " kk no size");
            MyNotificationHelper myNotificationHelper=new MyNotificationHelper(activity );
            notificationEntities=myNotificationHelper.getAllNotifications();
            //myNotificationHelper.deleteAll(  );
            for (int i = 0; i < jsonArray.length(); i++) {
                // Log.d("111sizeeeem", "kk1111 "+jsonArray.length());
                Log.d("111sizeeeem", "kk1111 " + jsonArray.length());
                JSONObject jobject = jsonArray.getJSONObject(i);
                if (notificationEntities.size()==0) {
                    myNotificationHelper.insertnotification( jobject.getString( "id" ), jobject.getString( "mgnt_id" ),
                            jobject.getString( "title" ), jobject.getString( "image" ), jobject.getString( "description" ), jobject.getString( "status" ), jobject.getString( "created" ) );
                }else{
                    for (int i2=0;i2<notificationEntities.size();i2++){
                        if ((notificationEntities.get( i2 ).getNotification_id()).equals( jobject.getString( "id" ))){
                            Log.d( "uder if",""+count+""+(notificationEntities.get( i2 ).getNotification_id()).equals( jobject.getString( "id" )) );
                            count++;
                            break;
                        }
                    }
                    if(count==0){
                        Log.d("under insert","after notification inserted");
                        myNotificationHelper.insertnotification( jobject.getString( "id" ), jobject.getString( "mgnt_id" ),
                                jobject.getString( "title" ), jobject.getString( "image" ), jobject.getString( "description" )
                                , jobject.getString( "status" ), jobject.getString( "created" ) );
                        count=0;
                    }
                }
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }
    public ArrayList<ManagementEntity> getMAnagementList(){
        ArrayList<ManagementEntity> managementEntities=new ArrayList<ManagementEntity>();
        managementEntities.clear();
        try {
            URL js = new URL("http://api.condoassist2u.com/get_mgnt_lists.php");
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jobject1 = new JSONObject(line);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jobject1.getString("info"));
            Log.d("management list", " below");
            Log.d("jsonArray", "  " + jsonArray.toString());
            Log.d("sizeeeem", " kk no size");
            //myNotificationHelper.deleteAll(  );
            for (int i = 0; i < jsonArray.length(); i++) {
                  ManagementEntity managementEntity=new ManagementEntity();
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                managementEntity.setId(jsonObject.getString("id"));
                managementEntity.setName(jsonObject.getString("name"));
                managementEntities.add(managementEntity);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return managementEntities;
    }
    public  String addEvent(String[] valuse){
        String s=null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/get_event_from_tenants.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_id", valuse[0] );
            jsonObject.accumulate("title", valuse[1]) ;
            jsonObject.accumulate( "event_purpose", valuse[2]) ;
            jsonObject.accumulate( "event_date", valuse[3]) ;
            jsonObject.accumulate( "from_time", valuse[4]) ;
            jsonObject.accumulate( "to_time", valuse[5]) ;
            jsonObject.accumulate( "targeted_paxs", valuse[6]) ;
            jsonObject.accumulate( "description", valuse[7]) ;
            jsonObject.accumulate( "mgnt_id", valuse[8]) ;
            jsonObject.accumulate( "image", valuse[9]) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            s = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
    }
    public  String preRegistation(String[] valuse){
        String s=null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/post_pre_registration.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_name", valuse[0] );
            jsonObject.accumulate("mgnt_id", valuse[1]) ;
            jsonObject.accumulate( "member_id", valuse[2]) ;
            jsonObject.accumulate( "nric_number", valuse[3]) ;
            jsonObject.accumulate( "car_number", valuse[4]) ;
            jsonObject.accumulate( "from_date", valuse[5]) ;
            jsonObject.accumulate( "reg_date", valuse[6]) ;
            jsonObject.accumulate( "visitor_name", valuse[7]) ;
            jsonObject.accumulate( "passport_no", valuse[8]) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            Log.d("pre-registration===",""+jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            s = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
    }
    public  String listPreRegistation(String[] valuse){
        String s=null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/get_pre_registration.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_id", valuse[0]) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            s = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
    }
    public  String showEvent(String[] valuse){
        String s=null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/events.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "mgnt_id", valuse[0]) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            s = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
    }
    public String updateProfile(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/profile_update.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_id", valuse[0] );
            jsonObject.accumulate("name", valuse[1]) ;
            jsonObject.accumulate( "email", valuse[2]) ;
            jsonObject.accumulate( "address", valuse[3] );
            jsonObject.accumulate("house_phone", valuse[4]) ;
            jsonObject.accumulate( "mobile", valuse[5]) ;
            jsonObject.accumulate( "emergency_contact", valuse[6] );
            jsonObject.accumulate("relationship", valuse[7]) ;
            jsonObject.accumulate("unit_no", valuse[8]) ;
            jsonObject.accumulate( "image", valuse[9]) ;
            jsonObject.accumulate( "car_no_plate", valuse[10]) ;
            jsonObject.accumulate( "maintenance_fee", valuse[11]) ;
            Log.d("profile_update_json",""+jsonObject.toString());
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;
    }
    public String sentComment(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/comments_suggestions.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_id", valuse[0] );
            jsonObject.accumulate("mgnt_id", valuse[1]) ;
            jsonObject.accumulate( "name", valuse[2]) ;
            jsonObject.accumulate( "email", valuse[3] );
            jsonObject.accumulate("subject", valuse[4]) ;
            jsonObject.accumulate( "suggestions", valuse[5]) ;
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;

    }
    public String facilityBooking(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/post_booking_facilities.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_id", valuse[0] );
            jsonObject.accumulate("mgnt_id", valuse[1]) ;
            jsonObject.accumulate( "time", valuse[2]) ;
            jsonObject.accumulate( "date", valuse[3] );
            jsonObject.accumulate( "facility_id", valuse[4] );
            jsonObject.accumulate( "time2", valuse[5] );
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;

    }
    public String getApplicationForms(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/get_application_form.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "mgnt_id", valuse[0] );
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;

    }
    public String changeLoginStatus(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/logout.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "mgnt_id", valuse[0] );
            jsonObject.accumulate( "id", valuse[1] );
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;

    }
    public String generateAlarm(String[] valuse){
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/panic_notification.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "mgnt_id", valuse[0] );
            jsonObject.accumulate( "member_id", valuse[1] );
            jsonObject.accumulate( "latitude", valuse[2] );
            jsonObject.accumulate( "longitude", valuse[3] );
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;

    }
    public String loadTransactions(String[] params) {
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/transaction_list.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_id", params[0] );
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;
    }

    public String logoutFromOtherDevice(String[] params) {
        String pro_ret="";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/login_confirm.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "email", params[0] );
            Log.d("logoutFromOtherDevice ", " =="+jsonObject.toString());
            StringEntity stringEntity= new StringEntity(jsonObject.toString());
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            pro_ret = readResponse(httpResponse);
            Log.d("result", " =="+pro_ret);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pro_ret;
    }
}
