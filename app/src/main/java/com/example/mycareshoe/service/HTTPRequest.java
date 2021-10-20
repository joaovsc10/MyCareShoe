package com.example.mycareshoe.service;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPRequest {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static String error = "";

    public JSONObject makeHttpRequest(String urlString, String method,
                                      FormBody.Builder postParams, HttpUrl.Builder getParams) {

        jObj = null;
        json = null;
        error = null;

        HttpURLConnection urlConnection = null;

        // GET
        try {
            OkHttpClient client = new OkHttpClient();
            Request request;

            RequestBody formBody;
            if (method.equals("GET")) {

                request = new Request.Builder()
                        .url(getParams.build().toString())
                        .build();
            } else {

                if (method.equals("POST") || method.equals("PUT")) {
                    formBody = postParams.build();
                    request = new Request.Builder()
                            .url(urlString)
                            .post(formBody)
                            .build();
                } else {
                    formBody = postParams.build();
                    request = new Request.Builder()
                            .url(urlString)
                            .put(formBody)
                            .build();
                }
            }

            Response response = client.newCall(request).execute();

            json = response.body().string();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (NoRouteToHostException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            JSONObject json = new JSONObject();
            try {
                json.put("success", "0");
                json.put("message", "No internet connection");
            } catch (JSONException exception) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return json;
        } catch (IOException e) {
            JSONObject json = new JSONObject();
            try {
                json.put("success", "0");
                json.put("message", "Error connecting to the server");
            } catch (JSONException exception) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return json;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        // try to parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            jObj.put("error_code", error);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }


}
