package com.example.mycareshoe.ui.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycareshoe.R;
import com.example.mycareshoe.model.Patient;
import com.example.mycareshoe.helpers.PatientHelper;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.service.HTTPRequest;
import com.example.mycareshoe.service.URLs;
import com.example.mycareshoe.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    private PatientHelper patientHelper = new PatientHelper();
    private TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        createAccount = (TextView) findViewById(R.id.createAccount);
        Button login = (Button) findViewById(R.id.login);
        //if user presses on login
        //calling the method login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
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
                    if (obj != null) {
                        //if no error in response
                        if (obj.getString("success").equals("1")) {

                            if (obj.getJSONObject("user").getInt("profile_id") != 1) {
                                Toast.makeText(getApplicationContext(), "The APP is only meant to be used by a patient. Contact the administrator.", Toast.LENGTH_SHORT).show();
                            } else {

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
                            }
                        } else {

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            Drawable dr = getResources().getDrawable(R.drawable.ic_baseline_error_24);
                            //add an error icon to yur drawable files
                            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                            editTextUsername.setCompoundDrawables(null, null, dr, null);
                            editTextPassword.setCompoundDrawables(null, null, dr, null);


                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to connect with the host.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(Void... voids) {
                //creating request handler object
                HTTPRequest httpRequest = new HTTPRequest();
                JSONObject result = null;

                FormBody.Builder formBuilder = new FormBody.Builder()
                        .add("usernameEmail", usernameEmail)
                        .add("password", password);

                //returning the response
                try {
                    result = httpRequest.makeHttpRequest(URLs.URL_LOGIN, "POST", formBuilder, null);
                    if (result != null && result.getString("success").equals("1")) {
                        patientHelper.getPersonalInfo(getApplicationContext(), result.getJSONObject("user").getInt("patient_number"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;

            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }


}