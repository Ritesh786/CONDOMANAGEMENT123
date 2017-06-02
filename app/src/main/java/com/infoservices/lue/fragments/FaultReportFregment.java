package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.infoservices.lue.adapters.FacilitiesAdapter;
import com.infoservices.lue.condomanagement.MainActivity;
import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.entity.FacilitiesEntity;
import com.infoservices.lue.utills.LoadApi;
import com.infoservices.lue.utills.Utils;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaultReportFregment extends Fragment {
   ImageView snap;
    Button upload_photo;
    GridView gridView;
    private static final int CAMERA_REQUEST = 1888;
    SnapedPhotoAdapter snapedPhotoAdapter;
    ArrayList<Bitmap> photoes=new ArrayList<Bitmap>();

    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    EditText edit_des_fault_report;
    public FaultReportFregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fault_report_fregment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        edit_des_fault_report=(EditText)getActivity().findViewById( R.id.edit_des_fault_report ) ;
        snap=(ImageView)getActivity().findViewById(R.id.snap);
        gridView=(GridView)getActivity().findViewById(R.id.gridview);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        upload_photo=(Button)getActivity().findViewById(R.id.upload_photo);
        upload_photo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils
                        .isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(),
                            "Please check your internet connection", Toast.LENGTH_SHORT)
                            .show();
                } else if(photoes.size()<1){
                    Toast.makeText(getActivity(),
                            "Please snap some images", Toast.LENGTH_SHORT)
                            .show();
                }else if(edit_des_fault_report.getText().toString().equals( "" )){
                    Toast.makeText(getActivity(),
                            "Please enter report !", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    Log.d("inside ", " flag value 0 above");
                    loadData();

                }
            }
        } );
    }
    private void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //snap.setImageBitmap(photo);
            photoes.add(photo);
            snapedPhotoAdapter = new SnapedPhotoAdapter(getActivity(),
                    photoes);
            gridView.setAdapter(snapedPhotoAdapter);
            snapedPhotoAdapter.notifyDataSetChanged();
        }
    }
    public class SnapedPhotoAdapter extends BaseAdapter {
       private Activity mContext;
        ArrayList<Bitmap> photoes2;
        private  LayoutInflater inflater=null;
        // Constructor
        public SnapedPhotoAdapter(Activity c, ArrayList<Bitmap> photoes) {
            mContext = c;
            photoes2=photoes;
            inflater = ( LayoutInflater )mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return photoes2.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }
        public class Holder
        {
            //TextView tv;
            ImageView img;
        }
        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.photo_item, null);
            // holder.tv=(TextView) rowView.findViewById(R.id.textView1);
            holder.img=(ImageView) rowView.findViewById(R.id.inflate_photo);
            //holder.tv.setText(result[position]);
            holder.img.setImageBitmap(photoes2.get(position));
            return rowView;
        }

    }



    private String getBase64( Bitmap bitmap){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

    }
       private String getImages(){
           String surl="";
           StringBuilder sb=new StringBuilder(surl);
           for (int i=0;i<photoes.size();i++){
               if(i==(photoes.size()-1)){
              sb.append( getBase64( photoes.get( i )) ); }
               else{
                   sb.append( getBase64( photoes.get( i ))+"," );
               }
           }
           return sb.toString();
       }
    @SuppressLint("NewApi")
    Handler mHandler = new Handler( new Handler.Callback() {

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
            String s=null;
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";

            // if(flagValue==0){
            try {

                s=postData(params);
            } catch (Exception e) {
                e.printStackTrace();

            }
            // }
            return s;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            try {
                JSONObject jobject=new JSONObject(result);
                //status
                if(jobject.getString("status").equals("0")){
                    Toast.makeText(getActivity(),"Report not Sent",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getActivity(),"Report Successfully sent !",Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    @SuppressLint("InlinedApi")
    private void showProgDialog () {
        try {
            progress_dialog = null;
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(getActivity()
                        ,R.style.AppCompatAlertDialogStyle);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
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
            asyncLoad.execute(sharedPreferences.getString( "id","" ),getImages(),edit_des_fault_report.getText().toString(),sharedPreferences.getString( "mgnt_id","" ));
        }
    }
    public String postData(String[] valuse) throws IOException {
        String s=null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.condoassist2u.com/fault_report.php");
            JSONObject jsonObject= new JSONObject(  );
            jsonObject.accumulate( "member_id",valuse[0] );
            jsonObject.accumulate( "image",valuse[1]);
            jsonObject.accumulate( "fault_report",valuse[2]);
            jsonObject.accumulate( "mgnt_id",valuse[3]);
            String json= jsonObject.toString();
            StringEntity stringEntity= new StringEntity( json );
            httpPost.setEntity((stringEntity));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
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
}
