package com.example.mycareshoe.data;


import android.os.AsyncTask;

import com.example.mycareshoe.data.model.LoggedInUser;
import com.example.mycareshoe.ui.login.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

public class LoginDataSource {



    String name=null;

    class Login extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser=new JSONParser();
        JSONObject json;
        LoggedInUser user=null;
        String URL= "http://192.168.0.113/mycareshoe/index.php";


        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            String email = args[2];
            String password = args[1];
            name= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if(email.length()>0)
                params.add(new BasicNameValuePair("email",email));

            json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {

            try {
                String successStatus=json.getString("success");
                if(successStatus.equals("1")) {
                    user = new LoggedInUser(java.util.UUID.randomUUID().toString(), name);
                }
            } catch (JSONException e) {
                new Result.Error(new IOException("Error logging in", e));
            }


        }

    }

    public Result<LoggedInUser> login(String username, String password)  {


        try {
            Login login = new Login();
            AsyncTask asyncTask =  login.execute(username, password, "");
                String asyncTaskOutput= asyncTask.get().toString();

            if(asyncTaskOutput.contains("\"success\":1")) {
                // TODO: handle loggedInUser authentication
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                UUID.randomUUID().toString(),
                                name);
                return new Result.Success<>(fakeUser);
            }
            else
                return new Result.Error("Error logging in: wrong data");

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}