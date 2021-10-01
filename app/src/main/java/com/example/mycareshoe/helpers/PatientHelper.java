package com.example.mycareshoe.helpers;

import android.content.Context;

import com.example.mycareshoe.data.model.Patient;
import com.example.mycareshoe.ui.login.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientHelper {

    public JSONObject getPersonalInfo(Context context, int patientNumber){
        //creating request handler object
        JSONParser jsonParser = new JSONParser();

        //creating request parameters
        ArrayList params = new ArrayList();

        params.add(new BasicNameValuePair("p", Integer.toString(SharedPrefManager.getInstance(context).getPatient(true).getPatient_number())));
        //returning the response
        JSONObject obj= jsonParser.makeHttpRequest(URLs.URL_READ_PATIENT_INFO,"GET", params);

        Patient patient = null;
        patient = new Patient(
                obj.optString("gender",""),
                obj.optString("birth",""),
                obj.optLong("height", 0),
                obj.optLong("weight", 0),
                obj.optInt("feet_size",0),
                obj.optString("diabetes",""),
                obj.optString("type_feet", ""),
                obj.optString("name",""),
                obj.optInt("pressure_threshold",0),
                obj.optInt("occurences_number",0),
                obj.optInt("time_interval",0)

        );


        //storing the user in shared preferences
        SharedPrefManager.getInstance(context).updatePersonalInfo(patient);

        return obj;
    }
}
