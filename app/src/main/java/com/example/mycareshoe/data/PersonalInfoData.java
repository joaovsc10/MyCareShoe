package com.example.mycareshoe.data;

import android.os.AsyncTask;

import com.example.mycareshoe.data.model.User;
import com.example.mycareshoe.ui.login.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonalInfoData {

    String name = null;

    class PersonalInfo extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        JSONObject json;
        User user = null;
        String URL = "http://192.168.0.101/mycareshoe/patient_search.php";


        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            String email = args[2];
            String password = args[1];
            name = args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if (email.length() > 0)
                params.add(new BasicNameValuePair("email", email));

            json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {



        }

    }


}
