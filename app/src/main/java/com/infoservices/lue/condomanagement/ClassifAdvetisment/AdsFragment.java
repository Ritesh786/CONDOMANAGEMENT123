package com.infoservices.lue.condomanagement.ClassifAdvetisment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infoservices.lue.condomanagement.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Button msearchadsbtn;
    Spinner msearccattxtspinner, msearchstatetxtspinner, msearchareatxtspinner;

    private ArrayList<String> category;
    private ArrayList<StateId> state;
    private ArrayList<String> area;
    int stateid2 = 0;

    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference";
    public static final String MGNT_ID = "mgnt_id";

    private ArrayList<String> stateid;
    JSONArray getimage = null;

    public static final String DATA_URLCAT = "http://api.condoassist2u.com/get_categories.php";
    public static final String DATA_URLSTATE = "http://api.condoassist2u.com/get_state.php";
    public static final String DATA_URLAREA = "http://api.condoassist2u.com/get_area.php";
    public static final String DATA_GETIMAGE = "http://api.condoassist2u.com/search_classified_ad.php";


    private JSONArray result;
    private JSONArray resultstate;
    private JSONArray resultarea = null;

    private int mLastSpinnerPosition = 0;

    private List<pojo> movieList = new ArrayList<pojo>();

    String spr;
    pojo movie;

    private static final String TAG = AdsFragment.class.getSimpleName();


    // public static final String TAG_STATEID = "state_id";


    public AdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ads, container, false);

        category = new ArrayList<String>();
        category.add("Select By Category");

        state = new ArrayList<StateId>();
        //   state.add("Select By State");

        area = new ArrayList<String>();
        //  area.add("Select By Area");


        stateid = new ArrayList<String>();

        msearchadsbtn = (Button) view.findViewById(R.id.Searchads_btn);
        msearchadsbtn.setOnClickListener(this);

        msearccattxtspinner = (Spinner) view.findViewById(R.id.searchcategory);
        msearchstatetxtspinner = (Spinner) view.findViewById(R.id.SearchState);
        msearchareatxtspinner = (Spinner) view.findViewById(R.id.SearchArea);

        msearccattxtspinner.setOnItemSelectedListener(this);
        msearchareatxtspinner.setOnItemSelectedListener(this);

//        msearchareatxtspinner.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

        msearchstatetxtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mLastSpinnerPosition == position) {
                    return;
                }

                mLastSpinnerPosition = position;


                stateid2 = state.get(position).getId();
                Log.d("err1245", String.valueOf(state.get(position).getId()));
                new sendstatearea().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                msearchstatetxtspinner.setAdapter(null);

            }
        });


        //  getData();
        new getcategory().execute();
        getdatastate();



        return view;
    }


//    private void getData(){
//        String str = "catv";
//        Log.d("catvolley",str);
//        StringRequest stringRequest = new StringRequest(DATA_URLCAT,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject jobject = null;
//                        try {
//                            //Parsing the fetched Json String to JSON Object
//                            jobject = new JSONObject(response);
//
//                            //Storing the Array of JSON String to our JSON Array
//                            result = jobject.getJSONArray("info");
//
//                            //Calling method getStudents to get the students from the JSON Array
//                            getcategory(result);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//        //Creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//
//
//    }

    private class getcategory extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }


        protected String doInBackground(String... args) {
            String parsedString = "";

            try {

                URL url = new URL(DATA_URLCAT);
                URLConnection conn = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                InputStream is = httpConn.getInputStream();
                parsedString = convertinputStreamToString(is);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return parsedString;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            Log.d("OnPOst", " " + json);
            pDialog.dismiss();


            Toast.makeText(getActivity(), "onpostwork.....", Toast.LENGTH_SHORT).show();

            try {
                JSONObject obj = new JSONObject(json);

                result = obj.getJSONArray("info");
                getcategory(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public static String convertinputStreamToString(InputStream ists)
            throws IOException {
        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(
                        ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }


    private void getdatastate() {
        String str = "statev";
        Log.d("stevolley", str);
        StringRequest stringRequest = new StringRequest(DATA_URLSTATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jobject = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jobject = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultstate = jobject.getJSONArray("info");

                            //Calling method getStudents to get the students from the JSON Array
                            getstate(resultstate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }

    private void getcategory(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                category.add(json.getString("cat_name"));
                Log.d("cat123", json.getString("cat_name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        msearccattxtspinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, category));

    }

    private void getstate(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
//                JSONObject json = j.getJSONObject(i);
                state.add(new StateId((JSONObject) j.get(i)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        List<String> brString = new ArrayList<>();
        // brString.add("Select By State");
        for (StateId br : state) {
            brString.add(String.valueOf(br.getName()));
            Log.d("state123", String.valueOf(br.getName()));
        }
        msearchstatetxtspinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brString));

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getContext(), "working", Toast.LENGTH_SHORT).show();

        if (parent.getId() == R.id.SearchState) {

            String str12 = "workhere";

            msearchareatxtspinner.setSelection(position);
            Log.d("err123", str12);


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class sendstatearea extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            String s = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(DATA_URLAREA);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("state_id", stateid2);
                Log.d("er123", String.valueOf(stateid2));

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        protected void onProgressUpdate(Integer... progress) {

        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            Log.d("OnPOst", " " + json);
            pDialog.dismiss();


            Toast.makeText(getActivity(), "onpostwork.....", Toast.LENGTH_SHORT).show();

            try {
                JSONObject obj = new JSONObject(json);

                resultarea = obj.getJSONArray("info");
                getarea(resultarea);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void getarea(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                area.add(json.getString("area_name"));
                Log.d("json123", json.getString("area_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, area);
        msearchareatxtspinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //  msearchareatxtspinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, area));

        // new ArrayAdapter<String>().clear();
    }

    private String readResponse(HttpResponse httpResponse) {

        InputStream is = null;
        String return_text = "";
        try {
            is = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
            Log.d("return_text", "" + return_text);
        } catch (Exception e) {

        }
        return return_text;
    }



     class getadsdetail extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        String getcat = msearccattxtspinner.getSelectedItem().toString().trim();
        String getstate = msearchstatetxtspinner.getSelectedItem().toString().trim();
        String getarea = msearchareatxtspinner.getSelectedItem().toString().trim();



         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             pDialog = new ProgressDialog(getContext());
             pDialog.setMessage("Please Wait ...");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();

         }

        @Override
        protected String doInBackground(String... args) {
            String s = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(DATA_GETIMAGE);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mgnt_id", spr);
                jsonObject.accumulate("cat_name", getcat);
                jsonObject.accumulate("area_name", getarea);
                Log.d("er12113",spr+getcat+getarea);

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readadsResponse(httpResponse);
                Log.d("tag1", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();

                Log.d("espone",exception.toString());

            }
            return s;

        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            pDialog.dismiss();
            try {
                JSONObject objone = new JSONObject(json);
                getimage = objone.getJSONArray("msg");


                Intent showadintnt = new Intent(getContext(), AdsShow.class);
                showadintnt.putExtra("mylist", getimage.toString());
                Log.d("json000", String.valueOf(getimage));
                startActivity(showadintnt);


                Log.d("json007", String.valueOf(getimage));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

        private String readadsResponse(HttpResponse httpResponse) {

            InputStream is = null;
            String return_text = "";
            try {
                is = httpResponse.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                return_text = sb.toString();
                Log.d("return1230", "" + return_text);
            } catch (Exception e) {

            }
            return return_text;
        }




    @Override
    public void onClick(View v) {


        sharedPreferences = getContext().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        spr = sharedPreferences.getString(MGNT_ID, "");
        Log.d("val01000", String.valueOf(movieList));
        Log.d("catvolley123", spr);
        new getadsdetail().execute();


    }


}



