package com.infoservices.lue.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.infoservices.lue.condomanagement.R;
import com.infoservices.lue.utills.LoadApi;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFregment extends Fragment implements View.OnClickListener{
EditText edit_name,edit_email,edit_mobile,edit_home_contact,edit_emergency_contact,edit_relation,
        edit_address,edit_unit_no2,edit_car2,edit_maitainance2;
Button update_pro_button;
    ImageView uodate_image_up;
    private static final int SELECT_PICTURE = 0;
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Id = "id";
    public static final String UserName = "username";
    public static final String Name = "name";
    public static final String Salt = "salt";
    public static final String Phone = "mobile";
    public static final String Email = "email";
    public static final String Unitno = "unit_no";
    public static final String HouseContact = "house_phone";
    public static final String EmergencyContact = "emergency_contact";
    public static final String Relationship = "relationship";
    public static final String Address = "address";
    public final static String IMAGE ="image";
    Bitmap bitmap;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "";
    private static final int LOAD_QUESTION_SUCCESS = 2;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    AsyncLoadData asyncLoad;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern =  "^\\+[0-9]{9,13}$";
    public EditProfileFregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_edit_profile_fregment, container, false );

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        uodate_image_up=(ImageView)getActivity().findViewById( R.id. uodate_image_up) ;
        uodate_image_up.setOnClickListener( this );
        sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(IMAGE,"").equals("null"))
        Picasso.with(getActivity())
                .load("http://api.condoassist2u.com/"+sharedPreferences.getString( IMAGE,"" ))
                .placeholder(R.drawable.userprofileicon2)   // optional
                .error(R.drawable.error)     // optional
                .into(uodate_image_up);
        edit_name=(EditText)getActivity().findViewById( R.id.edit_name );
        if (!sharedPreferences.getString(Name,"").equals("null"))edit_name.setText( sharedPreferences.getString( Name,"" ) );
        edit_unit_no2=(EditText)getActivity().findViewById( R.id.edit_unit_no2 );
        if (!sharedPreferences.getString(Unitno,"").equals("null"))edit_unit_no2.setText( sharedPreferences.getString( Unitno,"" ) );
        edit_email=(EditText)getActivity().findViewById( R.id.edit_email );
        if (!sharedPreferences.getString(Email,"").equals("null"))edit_email.setText( sharedPreferences.getString( Email,"" ) );
        edit_mobile=(EditText)getActivity().findViewById( R.id.edit_mobile );
        if (!sharedPreferences.getString(Phone,"").equals("null"))edit_mobile.setText(sharedPreferences.getString( Phone,"" ).replace("+60",""));
        edit_home_contact=(EditText)getActivity().findViewById( R.id.edit_home_contact );
        if (!sharedPreferences.getString(HouseContact,"").equals("null"))edit_home_contact.setText( sharedPreferences.getString( HouseContact,"" ).replace("+60","") );
        edit_emergency_contact=(EditText)getActivity().findViewById( R.id.edit_emergency_contact );
        if (!sharedPreferences.getString(EmergencyContact,"").equals("null"))edit_emergency_contact.setText( sharedPreferences.getString( EmergencyContact,"" ).replace("+60","") );
        edit_relation=(EditText)getActivity().findViewById( R.id.edit_relation );
        if (!sharedPreferences.getString(Relationship,"").equals("null"))edit_relation.setText( sharedPreferences.getString( Relationship,"" ) );
        edit_address=(EditText)getActivity().findViewById( R.id.edit_address );
        if (!sharedPreferences.getString(Address,"").equals("null"))edit_address.setText( sharedPreferences.getString( Address,"" ) );
        edit_car2=(EditText)getActivity().findViewById( R.id.edit_car2 );
        if (!sharedPreferences.getString("car_no_plate","").equals("null"))edit_car2.setText( sharedPreferences.getString( "car_no_plate","" ) );
        edit_maitainance2=(EditText)getActivity().findViewById( R.id.edit_maitainance2 );
        if (!sharedPreferences.getString("maintenance_fee","").equals("null"))edit_maitainance2.setText( sharedPreferences.getString( "maintenance_fee","" ) );
        update_pro_button=(Button)getActivity().findViewById( R.id.update_pro_button );
        update_pro_button.setOnClickListener(this);
    }
    public void pickPhoto() {
        //TODO: launch the photo picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity. RESULT_OK) {
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
                uodate_image_up.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

   /* private Bitmap getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        // Convert file path into bitmap image using below line.
        Bitmap bitmap2 = BitmapFactory.decodeFile(filePath);
        return bitmap2;
    }*/

    @Override
    public void onClick(View v) {
         if(v.getId()==uodate_image_up.getId()){
            pickPhoto();
        }else if(v.getId()==update_pro_button.getId()){
             if (edit_name.getText().toString().equals( "" )){
                   Toast.makeText( getActivity(),"Enter your name" ,Toast.LENGTH_LONG).show();
             }
             else if (edit_unit_no2.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter new unit no." ,Toast.LENGTH_LONG).show();
             }
            else if (edit_email.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter your EMAIL" ,Toast.LENGTH_LONG).show();
             }
            else if (!edit_email.getText().toString().matches( emailPattern )){
                 Toast.makeText( getActivity(),"Invalid Email !!!" ,Toast.LENGTH_LONG).show();
             }
            else if (edit_mobile.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter your mobile no..." ,Toast.LENGTH_LONG).show();
             }
             else if (edit_mobile.getText().toString().length()<7){
                 Toast.makeText( getActivity(),"Invalid Mobile No. !!!" ,Toast.LENGTH_LONG).show();
             }
             else if (edit_home_contact.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter your home contact." ,Toast.LENGTH_LONG).show();
             }
            else if (edit_home_contact.getText().toString().length()<7){
                 Toast.makeText( getActivity(),"Invalid home Contact !!!" ,Toast.LENGTH_LONG).show();
             }
            else if (edit_emergency_contact.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter your emergency contact." ,Toast.LENGTH_LONG).show();
             }
             else if (edit_emergency_contact.getText().toString().length()<7){
                 Toast.makeText( getActivity(),"Invalid Emergency Contact !!!" ,Toast.LENGTH_LONG).show();
             }
            else if (edit_relation.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter your relationship" ,Toast.LENGTH_LONG).show();
             }
             else if (edit_address.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter valid address" ,Toast.LENGTH_LONG).show();
             } else if (edit_car2.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter Car number" ,Toast.LENGTH_LONG).show();
             } else if (edit_maitainance2.getText().toString().equals( "" )){
                 Toast.makeText( getActivity(),"Enter maintainance fee" ,Toast.LENGTH_LONG).show();
             }else if (getBase64( bitmap )==null){
                 Toast.makeText( getActivity(),"Plese Select an Image!!" ,Toast.LENGTH_LONG).show();
             }else{
                 loadData();
             }
         }
    }
    private String getBase64( Bitmap bitmap3){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        if (bitmap3!=null){
        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);}
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

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
            String sret="";
            Log.d("inside ", " do in back above");

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            LoadApi loadApi=new LoadApi();
            // if(flagValue==0){
            try {

                sret=loadApi.updateProfile(params);
            } catch (Exception e) {
                e.printStackTrace();

            }
            // }
            return sret;
        }

        @TargetApi(Build.VERSION_CODES.DONUT)
        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
            Log.d("profile update==",""+result);
            try {
                JSONObject jobject1=new JSONObject(result);
                //status
                if(jobject1.getString("status").equals("1")){
                    Toast.makeText(getActivity(),"Profile Updated Successfully !!!",Toast.LENGTH_LONG).show();
                    JSONArray jsonArray=new JSONArray(  jobject1.getString( "info" ) );
                    JSONObject jsonObject=jsonArray.getJSONObject( 0 );
                    Log.d( "After Login",""+jsonObject.toString());
                    sharedPreferences = getActivity().getSharedPreferences(MyLoginPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Id, jsonObject.getString( "id" ));
                    editor.putString(UserName, jsonObject.getString( "username" ));
                    editor.putString(Name, jsonObject.getString( "name" ));
                    editor.putString(Salt, jsonObject.getString( "salt" ));
                    editor.putString(Phone, jsonObject.getString( "mobile" ));
                    editor.putString(Email, jsonObject.getString( "email" ));
                    editor.putString(Unitno, jsonObject.getString( "unit_no" ));
                    editor.putString(HouseContact, jsonObject.getString( "house_phone" ));
                    editor.putString(EmergencyContact, jsonObject.getString( "emergency_contact"));
                    editor.putString(Relationship, jsonObject.getString( "relationship" ));
                    editor.putString(IMAGE, jsonObject.getString( "image" ));
                    editor.putString(Address, jsonObject.getString( "address" ));
                    editor.putString("maintenance_fee", jsonObject.getString( "maintenance_fee" ));
                    editor.putString("car_no_plate", jsonObject.getString( "car_no_plate" ));
                    editor.commit();
                    ProfileFregment fragment2 = new ProfileFregment();
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_command_sugg, fragment2).commit();
                } else if(jobject1.getString("status").equals("0")){
                    Toast.makeText(getActivity(),"Profile not updated ! Try again !!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),"Profile not updated ! Try again !!",Toast.LENGTH_LONG).show();
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
            asyncLoad.execute(sharedPreferences.getString( Id,"" )
                    ,edit_name.getText().toString()
                    ,edit_email.getText().toString()
                    ,edit_address.getText().toString()
                    ,"+60"+edit_home_contact.getText().toString()
                    ,"+60"+edit_mobile.getText().toString()
                    ,"+60"+edit_emergency_contact.getText().toString()
                    ,edit_relation.getText().toString()
                    ,edit_unit_no2.getText().toString().toUpperCase()
                    ,getBase64( bitmap )
                    ,edit_car2.getText().toString()
                    ,edit_maitainance2.getText().toString());
        }
    }
}
