package com.example.mycareshoe.ui.personalInfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.User;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.login.JSONParser;
import com.example.mycareshoe.ui.login.LoginActivity;

import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class PersonalInfoFragment extends Fragment  {

    TextView textViewPatientNumber;
    TextView textViewName;
    TextView textViewWeight;
    TextView textViewHeight;
    TextView textViewFeetSize;
    TextView textViewBirthday;

    String[] infos= new String[]{
            "name",
            "gender",
            "height",
            "weight",
            "feet_size",
            "diabetes",
            "type_feet"
    };

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

        textViewPatientNumber = (TextView) view.findViewById(R.id.patientNumber);
        textViewName = (TextView) view.findViewById(R.id.name);
        textViewWeight= (TextView) view.findViewById(R.id.weight);
        textViewHeight= (TextView) view.findViewById(R.id.height);
        textViewFeetSize= (TextView) view.findViewById(R.id.feetSize);
        textViewBirthday= (TextView) view.findViewById(R.id.birthdate);

        //getting the current user
        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        //setting the values to the textviews
        textViewPatientNumber.setText(Integer.toString(user.getPatient_number()));
        textViewPatientNumber.setEnabled(false);


        getPersonalInfo();


    }

    private void getPersonalInfo() {
        class PersonalInfo extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject obj) {
                super.onPostExecute(obj);

                try {

                    textViewName.setText(obj.getString("name"));
                    textViewName.setEnabled(false);
                    textViewWeight.setText(obj.getString("weight"));
                    textViewWeight.setText(obj.getString("height"));
                    textViewFeetSize.setText(obj.getString("feet_size"));
                    textViewBirthday.setText(obj.getString("birth"));
                    textViewBirthday.setEnabled(false);

                    //converting response to json object
                    for (String info: infos) {
                        if(obj.getString(info).equals("null")){
                            switch (info){
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
                    //storing the user in shared preferences
                    SharedPrefManager.getInstance(getContext()).getUser();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(Void... voids) {
                //creating request handler object
                JSONParser jsonParser = new JSONParser();

                //creating request parameters
                ArrayList params = new ArrayList();
                params.add(new BasicNameValuePair("p", Integer.toString(SharedPrefManager.getInstance(getContext()).getUser().getPatient_number())));

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_READ_PATIENT_INFO,"GET", params);
            }
        }
        PersonalInfo pinfo = new PersonalInfo();
        pinfo.execute();
    }
}
