package com.infoservices.lue.socialMedia;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Talent track on 04-02-2016.
 */
public class FacebookProcess extends AsyncTask<String, Void, String> {

    Dialog pDialog;
    Context context;
    SocialMedia socialMedia;
    Callback<SocialMedia> callback;
    public Session currentSession;
    public Boolean isFaceBookWorking = true;
    private static final String APP_ID = "1589864331276091";
    public void proceedLogin(){
        try {
            socialMedia = new SocialMedia();
            currentSession = Session.getActiveSession();
            if (currentSession == null || currentSession.getState().isClosed()) {
                try {
                    Session session = new Session.Builder(context).setApplicationId(APP_ID).build();
                    //Session session = new Session.Builder(context).build();
                    Session.setActiveSession(session);
                    List<String> hh = session.getPermissions();
                    Log.e("allper", hh.toString());
                    currentSession = session;
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            if (currentSession.isOpened()) {
                // Do whatever u want. User has logged in
                Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {

                        if (user != null) {
                            socialMedia.person_name = user.getName();

                            Log.e("ok", "" + user);
                            if (Session.getActiveSession() != null)

                                currentSession = Session.getActiveSession();
                            try {
                                String tok = currentSession.getAccessToken();
                                if (null == tok) {

                                } else if (tok.equalsIgnoreCase("")) {

                                } else {
                                    if (isFaceBookWorking) {
                                        isFaceBookWorking = false;
                                        String sociel_personPhotoUrl = "https://graph.facebook.com/me?fields=id,name,email,gender,birthday&access_token=" + tok;
                                        execute(sociel_personPhotoUrl);
                                    }
                                }
                                Log.e("token", "" + tok);
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                            if (currentSession == null || currentSession.getState().isClosed()) {
                                Session session = new Session.Builder(context).build();
                                List<String> hh = session.getPermissions();
                                hh.toString();
                                Log.e("allper", hh.toString());
                                Session.setActiveSession(session);
                                currentSession = session;
                            }

                        }
                    }
                });

                Request.executeBatchAsync(request);

            } else if (!currentSession.isOpened()) {
                Activity activity = (Activity)context;
                Session.OpenRequest openRequest = new Session.OpenRequest(activity);
                openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);

                openRequest.setPermissions(Arrays.asList("email"));
                openRequest.setCallback(new Session.StatusCallback() {
                    // callback when session changes state
                    @Override
                    public void call(Session session, SessionState state,
                                     Exception exception) {
                        if (session.isOpened()) {
                            // make request to the /me API
                            Request request = Request.newMeRequest(session,
                                    new Request.GraphUserCallback() {

                                        // callback after Graph API response with user
                                        // object
                                        @Override
                                        public void onCompleted(GraphUser user,
                                                                Response response) {
                                            if (user != null) {
                                                Log.e("ok", "" + user);
                                                socialMedia.person_name = user.getName();
                                                if (Session.getActiveSession() != null)


                                                    currentSession = Session.getActiveSession();
                                                try {
                                                    String tok = currentSession.getAccessToken();
                                                    if (null == tok) {

                                                    } else if (tok.equalsIgnoreCase("")) {

                                                    } else {
                                                        if (isFaceBookWorking) {
                                                            isFaceBookWorking = false;
                                                            String sociel_personPhotoUrl = "https://graph.facebook.com/me?fields=id,name,email,gender,birthday&access_token=" + tok;
                                                            execute(sociel_personPhotoUrl);
                                                        }
                                                    }
                                                    Log.e("token", "" + tok);
                                                } catch (Exception e) {
                                                    // TODO: handle exception
                                                    e.printStackTrace();
                                                }
                                                if (currentSession == null || currentSession.getState().isClosed()) {
                                                    Session session = new Session.Builder(context).build();
                                                    List<String> hh = session.getPermissions();
                                                    hh.toString();
                                                    Log.e("allper", hh.toString());
                                                    Session.setActiveSession(session);
                                                    currentSession = session;
                                                }
                                            }
                                        }

                                    });
                            request.executeAsync();

                        }
                    }
                });
                try {
                    Session session = new Session(context);//.getActiveSession().setActiveSession(c);
                    Session.setActiveSession(session);
                    session.openForRead(openRequest);
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }


                ////
            }
            try {


                //Session.getActiveSession().closeAndClearTokenInformation();
            } catch (Exception e) {
                e.printStackTrace();

            }

            // Session.getActiveSession().setActiveSession(null);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public FacebookProcess(Context context, Callback<SocialMedia> callback){
        this.context = context;
        this.callback = callback;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        socialMedia = new SocialMedia();
       // pDialog = Utils.ProgressDialog(context);
       //WebServiceHandler.java showprogress();
    }

    @Override
    protected String doInBackground(String... params) {
        String Content = "";
        try {
            WebServiceHandler.getInstance().isExternalCall = true;
            Content = (String) WebServiceHandler.getInstance().CallHTTP(params[0], "GET");
            WebServiceHandler.getInstance().isExternalCall = false;
            return Content;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        //stopprogress();
        try {
            JSONObject json = new JSONObject(result);
            if (result != null) {
                socialMedia.person_name = json.getString("name");
                socialMedia.id = json.getString("id");
                socialMedia.email_id = json.has("email")?json.getString("email") : "";
                socialMedia.gender = json.has("gender")?json.getString("gender") : "";
                socialMedia.date_of_birth = json.has("birthday")?json.getString("birthday") : "";
                socialMedia.photoUrl = "https://graph.facebook.com/" + socialMedia.id + "/picture?type=large&width=600&height=600";
                new FacebookImageDownloader().execute(socialMedia.photoUrl);
            }
            else
                callback.onSuccess(socialMedia);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }




//    public void showprogress() {
//        pDialog = Utils.ProgressDialog(context);
//        pDialog.setCancelable(true);
//        if (!pDialog.isShowing()){
//            pDialog.show();
//        }
//    }

    private class FacebookImageDownloader extends AsyncTask<String, Void, byte[]> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
           // showprogress();
        }

        @Override
        protected byte[] doInBackground(String... params) {//

            try {
                String urls = params[0];
                URL url = new URL(urls);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            } catch (Exception e) {

            }
            return null;


        }

        @Override
        protected void onPostExecute(byte[] result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
           // stopprogress();
            try {
                socialMedia.imageInByte = result == null ? "": Base64.encodeToString(result, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                Session.getActiveSession().closeAndClearTokenInformation();
                callback.onSuccess(socialMedia);
            }
        }


    }

}
