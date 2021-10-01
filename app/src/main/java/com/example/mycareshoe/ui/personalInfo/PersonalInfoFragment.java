package com.example.mycareshoe.ui.personalInfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.Patient;
import com.example.mycareshoe.helpers.PatientHelper;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.login.JSONParser;
import com.example.mycareshoe.ui.login.LoginActivity;
import com.example.mycareshoe.ui.monitoring.MonitoringFragment;
import com.example.mycareshoe.ui.settings.SettingsFragment;

import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalInfoFragment extends Fragment  {

    TextView textViewPatientNumber;
    TextView textViewName;
    TextView textViewWeight;
    TextView textViewHeight;
    TextView textViewFeetSize;
    TextView textViewBirthday;
    Button saveBtn;
    Button cancelBtn;
    ArrayAdapter genderAdapter;
    private ArrayAdapter diabetesAdapter;
    private ArrayAdapter feetTypeAdapter;
    private Spinner genderSpinner;
    private Spinner diabetesSpinner;
    private Spinner feetTypeSpinner;
    private boolean invalidInput=false;
    private PatientHelper patientHelper = new PatientHelper();

    public boolean isInvalidInput() {
        return invalidInput;
    }

    public void setInvalidInput(boolean invalidInput) {
        this.invalidInput = invalidInput;
    }

    String[] infos= new String[]{
            "name",
            "gender",
            "height",
            "weight",
            "feet_size",
            "diabetes",
            "type_feet"
    };

    List<String> genders = Arrays.asList("","Male", "Female", "Other");
    List<String> diabetesStatus = Arrays.asList("","Present", "Absent");
    List<String> feetTypes = Arrays.asList("","Normal foot", "Flat foot", "Hollow foot");
    Map<String,String> updateArguments = new HashMap<>();
    Map<String,String> invalidInputsList = new HashMap<>(Map.of(
            "height", "false",
            "weight", "false",
            "feet_size", "false"
    ));


    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personalinfo, container, false);


    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.personalInfo_en));


//https://code.tutsplus.com/tutorials/android-essentials-creating-simple-user-forms--mobile-1758

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        saveBtn= (Button) view.findViewById(R.id.checkIcon);
        cancelBtn=(Button) view.findViewById(R.id.cancelIcon);

        saveBtn.setEnabled(false);
        textViewPatientNumber = (TextView) view.findViewById(R.id.patientNumber);
        textViewName = (TextView) view.findViewById(R.id.name);
        textViewWeight= (TextView) view.findViewById(R.id.weight);
        textViewHeight= (TextView) view.findViewById(R.id.height);
        textViewFeetSize= (TextView) view.findViewById(R.id.feetSize);
        textViewBirthday= (TextView) view.findViewById(R.id.birthdate);

        genderSpinner=setSpinner(genderSpinner, R.id.spinnerGender, genders, genderAdapter, view);
        genderAdapter= (ArrayAdapter) genderSpinner.getAdapter();
        diabetesSpinner=setSpinner(diabetesSpinner, R.id.spinnerDiabetes, diabetesStatus, diabetesAdapter, view);
        diabetesAdapter= (ArrayAdapter) diabetesSpinner.getAdapter();
        feetTypeSpinner=setSpinner(feetTypeSpinner, R.id.spinnerFeetType, feetTypes, feetTypeAdapter, view);
        feetTypeAdapter= (ArrayAdapter) feetTypeSpinner.getAdapter();


        getPersonalInfo();



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePersonalInfo(updateArguments);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });



    }


    private Spinner setSpinner(Spinner spinner, int viewId, List<String> spinnerOptions, ArrayAdapter adapter, View view){

        spinner = view.findViewById(viewId);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        spinner.setAdapter(adapter);

        return spinner;
    }

    private void validateChangedSpinner(Spinner spinner, List<String> listOptions, String initialValue, String fieldName){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {


                if(listOptions.get(position).toString().equals(initialValue)==false || (initialValue=="null" && position!=0)){

                    updateArguments.put(fieldName,listOptions.get(position).toString());
                    if(!invalidInputsList.containsValue("true"))
                        saveBtn.setEnabled(true);
                }
                else{
                    updateArguments.remove(fieldName);
                    if(updateArguments.isEmpty())
                        saveBtn.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }
    private void validateChangedText(TextView text, String initialValue, String fieldName){

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setInvalidInput(false);

                if(s.toString().endsWith(".") || s.toString().contains(",") || s.toString().startsWith(".")){
                    setInvalidInput(true);
                }

                if(s.toString().isEmpty() ) {
                    invalidInputsList.put(fieldName, "false");

                }
                if(!s.toString().isEmpty() && !isInvalidInput()) {
                    double value = Double.parseDouble(s.toString());
                    switch (fieldName) {
                        case "weight":
                            if (value < 20 || value > 700) {
                                setInvalidInput(true);
                                invalidInputsList.put(fieldName, "true");
                            }else{
                                invalidInputsList.put(fieldName, "false");
                            }
                            break;

                        case "height":
                            if (value < 0.5 || value > 3) {
                                setInvalidInput(true);
                                invalidInputsList.put(fieldName, "true");
                            }else{
                                invalidInputsList.put(fieldName, "false");
                            }
                            break;

                        case "feet_size":
                            if (value < 15 || value > 75) {
                                setInvalidInput(true);
                                invalidInputsList.put(fieldName, "true");
                            }else{
                                invalidInputsList.put(fieldName, "false");
                            }
                            break;
                    }
                }
                if (isInvalidInput()) {
                    text.setError("Invalid " + fieldName + " value");
                    text.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (initialValue.equals(s.toString().trim()) || ((initialValue.equals("-1") || initialValue.equals("0")) && s.toString().equals("")) || invalidInputsList.get(fieldName) == "true") {
                    updateArguments.remove(fieldName);
                    saveBtn.setEnabled(false);
                }
                else {

                        updateArguments.put(fieldName, s.toString().trim());
                        saveBtn.setEnabled(true);
                }
                if(s.toString().equals("") &&(!updateArguments.isEmpty() && !invalidInputsList.containsValue("true"))) {
                    saveBtn.setEnabled(true);
                    updateArguments.remove(fieldName);

                }

            }
        };

        text.addTextChangedListener(tw);

    }

    private void getPersonalInfo() {
        class PersonalInfo extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(JSONObject obj) {
                super.onPostExecute(obj);
                progressBar.setVisibility(View.GONE);
                textViewName.setText(obj.optString("name",""));

                textViewName.setEnabled(false);
                textViewWeight.setText(obj.optString("weight",""));
                textViewHeight.setText(obj.optString("height",""));
                textViewFeetSize.setText(obj.optString("feet_size",""));
                textViewBirthday.setText(obj.optString("birth",""));

                genderSpinner.setSelection(genderAdapter.getPosition(obj.optString("gender")));
                diabetesSpinner.setSelection(diabetesAdapter.getPosition(obj.optString("diabetes")));
                feetTypeSpinner.setSelection(feetTypeAdapter.getPosition(obj.optString("type_feet")));
                textViewBirthday.setEnabled(false);

                //converting response to json object
                for (String info: infos) {
                        if (obj.optString(info).equals("null") || obj.optString(info).equals("0")) {
                            switch (info) {
                                case "name":
                                    textViewName.setText("");
                                    break;
                                case "weight":
                                    textViewWeight.setText("");
                                    break;
                                case "height":
                                    textViewHeight.setText("");
                                    break;
                                case "feet_size":
                                    textViewFeetSize.setText("");
                                    break;

                            }
                        }

                }

                //getting the current user
                Patient patient = SharedPrefManager.getInstance(getActivity()).getPatient(true);

                //setting the values to the textviews
                textViewPatientNumber.setText(Integer.toString(patient.getPatient_number()));
                textViewPatientNumber.setEnabled(false);

                validateChangedSpinner(genderSpinner, genders, patient.getGender(), "gender");
                validateChangedSpinner(diabetesSpinner, diabetesStatus, patient.getDiabetesStatus(), "diabetes");
                validateChangedSpinner(feetTypeSpinner, feetTypes, patient.getFeetType(), "type_feet");
                validateChangedText(textViewWeight,Double.toString(patient.getWeight()), "weight");
                validateChangedText(textViewHeight,Double.toString(patient.getHeight()), "height");
                validateChangedText(textViewFeetSize,Integer.toString(patient.getFeetSize()), "feet_size");

            }

            @Override
            protected JSONObject doInBackground(Void... voids) {

                return patientHelper.getPersonalInfo(getContext(),0);
            }
        }
        PersonalInfo pinfo = new PersonalInfo();
        pinfo.execute();
    }

    private void updatePersonalInfo(Map<String, String> updatedata) {
        class updatePersonalInfo extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject obj) {
                try {
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.add(new BasicNameValuePair("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number())));
                for (Map.Entry<String, String> editedField : updatedata.entrySet()) {
                    params.add(new BasicNameValuePair(editedField.getKey(), editedField.getValue()));
                }

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_UPDATE_PATIENT_INFO,"PUT", params);
            }
        }
        updatePersonalInfo pinfo = new updatePersonalInfo();
        pinfo.execute();
    //   getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack("Monitoring").commit();
    }


}
