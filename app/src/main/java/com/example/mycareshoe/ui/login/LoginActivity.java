package com.example.mycareshoe.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.Patient;
import com.example.mycareshoe.data.model.User;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.MainActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);


        Button login= (Button) findViewById(R.id.login);
        //if user presses on login
        //calling the method login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });



    }

    public void onBackPressed() {
        //do nothing
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void userLogin() {
        //first getting the values
        final String usernameEmail = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(usernameEmail)) {
            editTextUsername.setError("Please enter your username/e-mail");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }


        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(JSONObject obj) {
                super.onPostExecute(obj);
                progressBar.setVisibility(View.GONE);

                try {

                    //if no error in response
                    if (obj.getString("success").equals("1")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject json = obj.getJSONObject("user");
                        Patient patient = new Patient(
                                json.getInt("user_id"),
                                json.getString("username"),
                                json.getInt("profile_id"),
                                json.getString("email"),
                                json.getString("password"),
                                json.getInt("patient_number")
                                );


                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).patientLogin(patient);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                        Drawable dr = getResources().getDrawable(R.drawable.ic_baseline_error_24);
                        //add an error icon to yur drawable files
                        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                        editTextUsername.setCompoundDrawables(null,null,dr,null);
                        editTextPassword.setCompoundDrawables(null,null,dr,null);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(Void... voids) {
                //creating request handler object
                JSONParser jsonParser = new JSONParser();

                ArrayList params = new ArrayList();

                if(isEmailValid(usernameEmail))
                    params.add(new BasicNameValuePair("email", usernameEmail));
                else
                    params.add(new BasicNameValuePair("username", usernameEmail));

                params.add(new BasicNameValuePair("password", password));

                //returing the response
                return jsonParser.makeHttpRequest(URLs.URL_LOGIN,"POST", params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }


}