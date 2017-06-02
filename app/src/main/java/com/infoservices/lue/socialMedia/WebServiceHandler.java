package com.infoservices.lue.socialMedia;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;


public class WebServiceHandler {

    private static WebServiceHandler theInstance;
    // private static int RetryCounter;
    public int httpResponsecode = -1;
    public  boolean isExternalCall;

    public static WebServiceHandler getInstance() {

        if (theInstance == null) {
            theInstance = new WebServiceHandler();
        }
        // RetryCounter = 1;
        return theInstance;
    }

    public static HttpURLConnection getHttpURLConnection(String serviceUrl, String RequestMethod) {

        HttpURLConnection urlConnection = null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
           // urlConnection.setConnectTimeout(Constant.WS_TIME_OUT);
           // urlConnection.setReadTimeout(Constant.WS_SOCKET_TIME_OUT);
            if (RequestMethod.equals("POST")) {
                urlConnection.setRequestMethod(RequestMethod);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //urlConnection.setUseCaches(false);
                // urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
            } else
                urlConnection.setRequestMethod(RequestMethod);
        } catch (MalformedURLException e) {
           // if (Constant.isDebug)
                Log.i("error", "MalformedURLException in running getHttpURLConnection method in WebServiceHandler");
        } catch (SocketTimeoutException e) {
           // if (Constant.isDebug)
                Log.i("error", "SocketTimeoutException in running getHttpURLConnection method in WebServiceHandler");
        } catch (IOException e) {
           // if (Constant.isDebug)
                Log.i("error", "IOException in running getHttpURLConnection method in WebServiceHandler");
        }
        return urlConnection;
    }

    public String getUrl(final String reqJson, String requestType) {
        System.out.println(reqJson);

        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String encodedString = "";
        String request = "";
        String uRL = "";
        if(isExternalCall){
            uRL = reqJson;
        }
        else {

           // uRL = Constant.BASE_URL;
            if (requestType.equals("GET")) {

                try {
                    request = reqJson;
                    encodedString = URLEncoder.encode(request, "UTF-8");

                } catch (final UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
               // uRL += "?d=" + encodedString;
            }//

        }
        return uRL;
    }

    public Bitmap DownloadImageFromCloud(String path) {
        InputStream in;
        Bitmap bmp = null;
        try {
            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            httpResponsecode = con.getResponseCode();
            if (httpResponsecode == HttpURLConnection.HTTP_OK) {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
            }

        } catch (Exception ex) {
           // if (Constant.isDebug)
                Log.e("error ", "Exception in download Image :" + ex.toString());
        }
        return bmp;
    }

    public final Object CallHTTP(final String reqJson, String HttpType) {
        Object result;
        result = callWS(reqJson, HttpType);

        return result;

    }


    public final Object callWS(final String reqJson, String requestType) {
       // if (Constant.isDebug)
            System.out.println(reqJson);
        HttpURLConnection connection = null;
        InputStream inStream = null;
        try {

            try {
                if (requestType.equalsIgnoreCase("GET"))
                    connection = getHttpURLConnection(
                            getUrl(reqJson, requestType), "GET");
                else {
                    connection = getHttpURLConnection(getUrl(reqJson, requestType), "POST");
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(reqJson);
                    wr.flush();
                    wr.close();
                }
                if (connection == null) return "";
                httpResponsecode = connection.getResponseCode();
                if (httpResponsecode == HttpURLConnection.HTTP_ACCEPTED || httpResponsecode == HttpURLConnection.HTTP_OK) {

                    try {
                        inStream = new BufferedInputStream(connection.getInputStream());
                    } catch (final IOException e) {
                      //  if (Constant.isDebug)
                            Log.i("error", "IOException in running callWS method in WebServiceHandler");
                    }
                }

            }  catch (SocketTimeoutException ste) {
                httpResponsecode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
                return "";
            } catch (final IOException | NumberFormatException e) {
               // if (Constant.isDebug)
                    Log.i("error", "IOException in running callWS method in WebServiceHandler");
                if (e.getMessage().contains("authentication challenge"))
                    httpResponsecode = HttpURLConnection.HTTP_UNAUTHORIZED;
                else if ("java.net.SocketTimeoutException: Read timed out".equals(e.getMessage()))
                    httpResponsecode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
                else
                    return "";
            }
            catch (final Exception e) {
                //if (Constant.isDebug)
                    Log.i("error", "ClientProtocolException in running callWS method in WebServiceHandler");
            }
            if (httpResponsecode == HttpURLConnection.HTTP_ACCEPTED || httpResponsecode == HttpURLConnection.HTTP_OK) {

                return inputStreamToString(inStream);
            } else if (httpResponsecode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED);
            } else if (httpResponsecode == HttpURLConnection.HTTP_BAD_REQUEST) {

                return String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST);
            }
        } catch (final IllegalStateException e) {
           // if (Constant.isDebug)
                Log.i("error", "IllegalStateException in running callWS method in WebServiceHandler");

        } finally {

        }

        return null;
    }

    private String inputStreamToString(final InputStream is) {
        String line = "";
        StringBuilder sb = new StringBuilder();
        String reply = "";
        final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
               // if (Constant.isDebug)
                    System.out.println(sb);
                reply = sb.toString();
            }
        } catch (final IOException e) {
            sb = new StringBuilder();
            sb.append(e.getMessage());
            return sb.toString();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                  //  if (Constant.isDebug)
                        Log.i("error", "IOException in running inputStreamToString method in WebServiceHandler");
                }
            }
        }
        return reply;
    }


}