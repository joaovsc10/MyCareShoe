package com.example.mycareshoe.helpers;

import android.content.Context;

import com.example.mycareshoe.model.Patient;
import com.example.mycareshoe.service.HTTPRequest;
import com.example.mycareshoe.service.URLs;

import org.json.JSONObject;

import okhttp3.HttpUrl;

public class PatientHelper {

    public JSONObject getPersonalInfo(Context context, int patientNumber) {
        //creating request handler object
        HTTPRequest httpRequest = new HTTPRequest();
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(URLs.URL_READ_PATIENT_INFO).newBuilder();

        urlBuilder.addQueryParameter("p", Integer.toString(SharedPrefManager.getInstance(context).getPatient(true).getPatient_number()));


        //returning the response
        JSONObject obj = httpRequest.makeHttpRequest(URLs.URL_READ_PATIENT_INFO, "GET", null, urlBuilder);

        Patient patient = null;
        patient = new Patient(
                obj.optString("gender", ""),
                obj.optString("birth", ""),
                obj.optDouble("height", 0),
                obj.optDouble("weight", 0),
                obj.optInt("feet_size", 0),
                obj.optString("diabetes", ""),
                obj.optString("type_feet", ""),
                obj.optString("name", ""),
                obj.optInt("pressure_threshold", 0),
                obj.optInt("occurences_number", 0),
                obj.optInt("time_interval", 0)

        );


        //storing the user in shared preferences
        SharedPrefManager.getInstance(context).updatePersonalInfo(patient);

        return obj;
    }
}
