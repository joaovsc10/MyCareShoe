package com.example.mycareshoe.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.PersonalDataHelper;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button cancelButton;
    private Button saveButton;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText patient_number;
    private PersonalDataHelper personalDataHelper = new PersonalDataHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        cancelButton = (Button) findViewById(R.id.cancelIcon);
        saveButton = (Button) findViewById(R.id.checkIcon);
        saveButton.setEnabled(false);
        username= (EditText) findViewById(R.id.usernameRegister);
        password= (EditText) findViewById(R.id.passwordRegister);
        email= (EditText) findViewById(R.id.email);
        patient_number= (EditText) findViewById(R.id.patientNumberRegister);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount(personalDataHelper.getInputArguments());
            }
        });

        personalDataHelper.validateFields(username, email, password, patient_number, saveButton);

    }





    private void createAccount(Map<String, String> registerForm) {
        class createAccount extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject obj) {
                try {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (obj.getString("success").equals("1")) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(obj);

            }

            @Override
            protected JSONObject doInBackground(Void... voids) {
                //creating request handler object
                JSONParser jsonParser = new JSONParser();
                //creating request parameters
                ArrayList params = new ArrayList();
               for (Map.Entry<String, String> editedField : registerForm.entrySet()) {
                    params.add(new BasicNameValuePair(editedField.getKey(), editedField.getValue()));
                }

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_CREATE_USER,"POST", params);
            }
        }
        createAccount createAcc = new createAccount();
        createAcc.execute();
        //   getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack("Monitoring").commit();
    }

    public void onBackPressed() {
        //do nothing
    }
}
