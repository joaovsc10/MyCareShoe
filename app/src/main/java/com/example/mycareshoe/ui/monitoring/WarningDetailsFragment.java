package com.example.mycareshoe.ui.monitoring;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.SensorsReading;
import com.example.mycareshoe.data.model.User;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.login.JSONParser;
import com.google.android.material.tabs.TabLayout;

import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WarningDetailsFragment extends Fragment {

    // images array
    int[] images = {R.drawable.foot_icon, R.drawable.foot_icon_inverted};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;
    TextView hiperpressionSensorsName;
    TextView datevalue;


    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.warnings_details, container, false);


    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing the ViewPager Object
        mViewPager = (ViewPager) getView().findViewById(R.id.viewPagerDetails);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(this.getActivity(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);

        Bundle bundle = this.getArguments();
        loadReading(bundle.getString("reading_id"));

        hiperpressionSensorsName = (TextView) getView().findViewById(R.id.sensorsName);
        datevalue = (TextView) getView().findViewById(R.id.dateValue);
    }

    private void loadReading(String reading_id) {
        class loadReading extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject obj) {

                try {
                    SensorsReading reading = new SensorsReading(
                            obj.getInt("reading_id"),
                            obj.optInt(SensorsReading.Sensors.S1.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S2.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S3.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S4.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S5.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S6.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S7.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S8.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S9.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S10.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S11.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S12.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S13.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S14.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S15.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S16.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S17.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S18.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S19.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S20.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S21.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S22.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S23.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S24.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S25.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.S26.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.T1.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.T2.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.H1.toString(), 0),
                            obj.optInt(SensorsReading.Sensors.H2.toString(), 0),
                            obj.getString("date"),
                            obj.getInt("patient_number")
                    );
                    validateSensorValues(reading);
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
                params.add(new BasicNameValuePair("reading_id", reading_id));

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_GET_SENSOR_READING,"GET", params);
            }
        }
        loadReading reading = new loadReading();
        reading.execute();
    }


    private void validateSensorValues(SensorsReading reading){

        String presentedNames = new String();
        if(reading.getHiperpressionSensors()!=null) {
            for (int i = 0; i < reading.getHiperpressionSensors().size(); i++) {
                presentedNames = presentedNames + reading.getHiperpressionSensors().get(i) + ", ";
                changeSensorsColor(reading.sensorDistribution.get(reading.getHiperpressionSensors().get(i)));
            }

            hiperpressionSensorsName.setText(presentedNames.substring(0, presentedNames.length() - 2));
            datevalue.setText(reading.getDate());
        }
    }

    private void changeSensorsColor(int sensorImageId){

        ImageView imageViewIcon = (ImageView) getView().findViewById(sensorImageId);
        imageViewIcon.setColorFilter(getContext().getResources().getColor(R.color.red));
    }

}
