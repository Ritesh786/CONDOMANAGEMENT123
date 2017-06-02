package com.infoservices.lue.condomanagement;

import android.*;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.infoservices.lue.entity.ApplicationFormEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadPdfActivity extends AppCompatActivity {
ImageView download;
    ApplicationFormEntity applicationFormEntity;
    TextView tv_loading,pdf_title,pdf_created,pdf_description;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    String download_file_url = "http://api.condoassist2u.com/";
    float per = 0;
    View header;
    TextView header_text;
    ImageView back,logout;

    private int STORAGE_PERMISSION_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_download_pdf );
        Intent intent=getIntent();
        applicationFormEntity= (ApplicationFormEntity) intent.getSerializableExtra( "object" );
       // dest_file_path=applicationFormEntity.getImage().toString();
        header=findViewById( R.id.header_download_pdf );
        header_text=(TextView)header.findViewById( R.id.header_text);
        back=(ImageView)header.findViewById( R.id.go_back);
        logout=(ImageView)header.findViewById( R.id.logout);
        logout.setVisibility( View.GONE );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        header_text.setText( "Download Pdf");
        tv_loading=(TextView)findViewById( R.id.tv_loading );
        pdf_title=(TextView)findViewById( R.id.pdf_title );
        pdf_created=(TextView)findViewById( R.id.pdf_created );
        pdf_description=(TextView)findViewById( R.id.pdf_description );

        pdf_title.setText( ""+ applicationFormEntity.getTital());
        pdf_created.setText( "Uploaded: "+ applicationFormEntity.getCreated());
        pdf_description.setText( Html.fromHtml(applicationFormEntity.getDescription()));
        download=(ImageView)findViewById( R.id.download );
        download.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if ((int) Build.VERSION.SdkInt < 23)
                {*/
                downloadAndOpenPDF();
                // }
            }
        } );
        if(isReadStorageAllowed()!=true){
            //If the app has not the permission then asking for the permission
            requestStoragePermission();
        }



   }
    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {
                Uri path = Uri.fromFile(downloadFile(download_file_url+applicationFormEntity.getImage()));
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException e) {

                    tv_loading
                            .setError("PDF Reader application is not installed in your device");
                }
            }
        }).start();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");
            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final IOException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final Exception e) {
            setTextError(
                    "Failed to download image. Please check your internet connection.",
                    Color.RED);
        }
        return file;
    }

    void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(color);
                tv_loading.setText(message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)&&ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read and write the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
