package com.infoservices.lue.condomanagement;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.widget.ProfilePictureView.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button msearchcate,msearchstate,msearcharea;
    Spinner msearccattxtspinner,msearchstatetxtspinner,msearchareatxtspinner;

    private ArrayList<String> category;
    private ArrayList<StateId> state;
    private ArrayList<String> area;
    int stateid2 = 0;

    private ArrayList<String> stateid;

    public static final String DATA_URLCAT = "http://api.condoassist2u.com/get_categories.php";
    public static final String DATA_URLSTATE = "http://api.condoassist2u.com/get_state.php";
    public static final String DATA_URLAREA = "http://api.condoassist2u.com/get_area.php";


    private JSONArray result;
    private JSONArray resultstate;
    private JSONArray resultarea = null;



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
        area.add("Select By State");


        stateid = new ArrayList<String>();

        msearcharea = (Button) view.findViewById(R.id.Searcharea_btn);

        msearccattxtspinner = (Spinner) view.findViewById(R.id.searchcategory);
        msearchstatetxtspinner = (Spinner) view.findViewById(R.id.SearchState);
        msearchareatxtspinner = (Spinner) view.findViewById(R.id.SearchArea);


        msearccattxtspinner.setOnItemSelectedListener(this);
        msearchareatxtspinner.setOnItemSelectedListener(this);
        msearchstatetxtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                stateid2 = state.get(position).getId();
                Log.d("err1245", String.valueOf(state.get(position).getId()));
                new sendstatearea().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        getData();
        getdatastate();


        return  view;
    }

    private void getData(){
        String str = "catv";
        Log.d("catvolley",str);
        StringRequest stringRequest = new StringRequest(DATA_URLCAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jobject = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jobject = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = jobject.getJSONArray("info");

                            //Calling method getStudents to get the students from the JSON Array
                            getcategory(result);
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

    private void getdatastate(){
        String str = "statev";
        Log.d("stevolley",str);
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
                Log.d("cat123",json.getString("cat_name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        msearccattxtspinner.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, category));

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
      //  brString.add("Select By State");
        for (StateId br : state) {
            brString.add(String.valueOf(br.getName()));
            Log.d("state123",String.valueOf(br.getName()));
        }
        msearchstatetxtspinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, brString));

    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getContext(),"working",Toast.LENGTH_SHORT).show();

//        if(view.getId() == R.id.SearchState){
//
//         stateid2 = state.get(position).getId();
//            Log.d("err123", String.valueOf(stateid2));
//
//
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





    private class sendstatearea extends AsyncTask<String, Void, String> {


        protected String  doInBackground(String... args) {

            String s="";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(DATA_URLAREA);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("state_id",stateid2);
                 Log.d("er123", String.valueOf(stateid2));

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(JSONObject json) {

          //  JSONObject jobject = null;
           // jobject = new JSONObject(json);

            //Storing the Array of JSON String to our JSON Array



            try {
                resultarea = json.getJSONArray("info");
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
                Log.d("json123",json.getString("area_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        msearchareatxtspinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, area));

    }

    private String readResponse(HttpResponse httpResponse) {

        InputStream is=null;
        String return_text="";
        try {
            is=httpResponse.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
            Log.d("return_text",""+return_text);
        } catch (Exception e)
        {

        }
        return return_text;
    }


}



