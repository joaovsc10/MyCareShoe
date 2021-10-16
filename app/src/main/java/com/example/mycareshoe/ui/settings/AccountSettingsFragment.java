package com.example.mycareshoe.ui.settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.PersonalDataHelper;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.service.HTTPRequest;
import com.example.mycareshoe.service.URLs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;

public class AccountSettingsFragment extends Fragment {

    private Button cancelButton;
    private Button saveButton;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText patient_number;
    private PersonalDataHelper personalDataHelper = new PersonalDataHelper();

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account_settings, container, false);


    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.account_info));

        cancelButton = (Button) view.findViewById(R.id.cancelIcon);
        saveButton = (Button) view.findViewById(R.id.checkIcon);
        saveButton.setEnabled(false);
        username = (EditText) view.findViewById(R.id.usernameRegister);
        password = (EditText) view.findViewById(R.id.passwordRegister);
        email = (EditText) view.findViewById(R.id.email);
        patient_number = (EditText) view.findViewById(R.id.patientNumberRegister);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1)
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateAccount(personalDataHelper.getInputArguments());
            }
        });

        personalDataHelper.validateFields(username, email, password, patient_number, saveButton);
    }

    private void updateAccount(Map<String, String> registerForm) {
        class updateAccount extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject obj) {
                if (obj == null) {
                    Toast.makeText(getContext(), "Error updating your account information!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                super.onPostExecute(obj);

            }

            @Override
            protected JSONObject doInBackground(Void... voids) {

                //creating request handler object
                HTTPRequest httpRequest = new HTTPRequest();
                FormBody.Builder formBuilder = new FormBody.Builder();

                for (Map.Entry<String, String> editedField : registerForm.entrySet()) {
                    formBuilder.add(editedField.getKey(), editedField.getValue());
                }

                formBuilder.add("user_id", Integer.toString(SharedPrefManager.getInstance(getContext()).getUserId()));

                //returning the response
                return httpRequest.makeHttpRequest(URLs.URL_UPDATE_USER_INFO, "PUT", formBuilder, null);
            }
        }
        updateAccount updateAccount = new updateAccount();
        updateAccount.execute();
        //   getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack("Monitoring").commit();
    }
}
