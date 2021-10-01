package com.example.mycareshoe.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.mycareshoe.R;

import java.util.HashMap;
import java.util.Map;

public class PersonalDataHelper {

    private Map<String,String> invalidInputsList = new HashMap<>(Map.of(
            "username", "false",
            "password", "false",
            "email", "false",
            "patient_number", "false"
    ));
    private boolean invalidInput=false;

    private Map<String,String> inputArguments = new HashMap<>();

    public Map<String, String> getInputArguments() {
        return inputArguments;
    }

    public boolean isInvalidInput() {
        return invalidInput;
    }

    public void setInvalidInput(boolean invalidInput) {
        this.invalidInput = invalidInput;
    }


    public void validateChangedText(EditText text, String fieldName, Button saveButton){


            TextWatcher tw = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    setInvalidInput(false);

                    if (s.toString().isEmpty()) {
                        invalidInputsList.put(fieldName, "false");

                    }
                    if (!s.toString().isEmpty() && !isInvalidInput()) {

                        switch (fieldName) {

                            case "patient_number":
                                if (s.length() != 9) {
                                    setInvalidInput(true);
                                    invalidInputsList.put(fieldName, "false");
                                } else {
                                    invalidInputsList.put(fieldName, "true");
                                }
                                break;

                            case "email":
                                if (!isEmailValid(s)) {
                                    setInvalidInput(true);
                                    invalidInputsList.put(fieldName, "false");
                                } else {
                                    invalidInputsList.put(fieldName, "true");
                                }
                                break;
                            default:
                                invalidInputsList.put(fieldName, "true");
                                setInvalidInput(false);
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

                    if (invalidInputsList.get(fieldName) == "true")
                        inputArguments.put(fieldName, s.toString().trim());
                    else
                        inputArguments.remove(fieldName);

                    if (!invalidInputsList.containsValue("false")) {
                        saveButton.setEnabled(true);
                    } else {
                        saveButton.setEnabled(false);
                    }

                }
            };

            text.addTextChangedListener(tw);


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void validateFields(EditText username, EditText email, EditText password, EditText patient_number, Button saveButton){

        validateChangedText(username, "username", saveButton);

         validateChangedText(email, "email", saveButton);

        validateChangedText(password, "password", saveButton);

        if(patient_number==null){
            invalidInputsList.remove("patient_number");
        }else {
            validateChangedText(patient_number, "patient_number", saveButton);
        }
    }
}
