package com.example.mycareshoe.ui.statistics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.SensorsReading;
import com.example.mycareshoe.data.model.StatisticsData;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.ui.monitoring.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class PressureStatisticsFragment extends StatisticsHelperFragment {


    private EditText startDate;
    private EditText endDate;
    private ProgressBar progressBar;
    private ProgressBar balance;
    private Map<String, Integer> meanSensorValues;
    private Map<String, Float> meanTempHumidityValues;
    private Map<String, Long> meanStats;
    private SensorsReading sensorsReadingObject;
    private TextView cadenceMean;
    private TextView stepsCount;
    private TextView leftFootTemperature;
    private TextView rightFootTemperature;
    private TextView leftFootHumidity;
    private TextView rightFootHumidity;
    int[] images = {R.drawable.foot_icon, R.drawable.foot_icon_inverted};

    ViewPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;

    public Map<String, Integer> getMeanSensorValues() {
        return meanSensorValues;
    }

    public void setMeanSensorValues(Map<String, Integer> meanSensorValues) {
        this.meanSensorValues = meanSensorValues;
    }

    public Map<String, Float> getMeanTempHumidityValues() {
        return meanTempHumidityValues;
    }

    public void setMeanTempHumidityValues(Map<String, Float> meanTempHumidityValues) {
        this.meanTempHumidityValues = meanTempHumidityValues;
    }

    public Map<String, Long> getMeanStats() {
        return meanStats;
    }

    public void setMeanStats(Map<String, Long> meanStats) {
        this.meanStats = meanStats;
    }


    public SensorsReading getSensorsReadingObject() {
        return sensorsReadingObject;
    }

    public void setSensorsReadingObject(SensorsReading sensorsReadingObject) {
        this.sensorsReadingObject = sensorsReadingObject;
    }

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        getActivity().setTitle(getResources().getString(R.string.illustrated_statistics_name_en));

        View view = inflater.inflate(R.layout.fragment_pressure_stats, parent, false);

        // Initializing the ViewPager Object
        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerStats);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(this.getActivity(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);

        Handler h = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (!mViewPagerAdapter.isInflated())
                    h.postDelayed(this, 0);
                else {
                    setSensorsStatValues(new SensorsReading(SharedPrefManager.getInstance(getContext()).getOverPressureValue()), getView());
                    setOtherStatsInfo();
                    h.removeCallbacks(this);
                }
            }
        };


        cadenceMean = view.findViewById(R.id.cadenceStats);
        stepsCount = view.findViewById(R.id.stepsStats);
        startDate = view.findViewById(R.id.start_date_text);
        endDate = view.findViewById(R.id.end_date_text);
        balance = view.findViewById(R.id.balanceBar);
        leftFootTemperature = (TextView) view.findViewById(R.id.leftFootTemperature);
        rightFootTemperature = (TextView) view.findViewById(R.id.rightFootTemperature);
        leftFootHumidity = (TextView) view.findViewById(R.id.leftFootHumidity);
        rightFootHumidity = (TextView) view.findViewById(R.id.rightFootHumidity);

        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        if (savedInstanceState != null) {

            setMeanSensorValues((Map<String, Integer>) savedInstanceState.getSerializable("meanSensors"));
            setMeanTempHumidityValues((Map<String, Float>) savedInstanceState.getSerializable("meanTempHumidity"));
            setMeanStats((Map<String, Long>) savedInstanceState.getSerializable("meanStats"));
            setSensorsReadingObject((SensorsReading) savedInstanceState.getSerializable("sensorsReadingObject"));
        }


        if (getMeanSensorValues() != null)
            h.postDelayed(run, 0);


        //Now specific components here (you can initialize Buttons etc)

        ImageButton searchBtn = view.findViewById(R.id.search_btn);
        searchBtn.setEnabled(false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        super.setDateTextClickListener(startDate, "start", searchBtn);
        super.setDateTextClickListener(endDate, "end", searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                meanSensorValues = null;
                meanTempHumidityValues = null;
                readSensorsEntry(getStartDateString(), getEndDateString());
                readStatsEntry(getStartDateString(), getEndDateString());

            }
        });


        return view;
    }

    private void readSensorsEntry(String startDateString, String endDateString) {
        class readSensorsEntry extends AsyncTask<Void, Void, JSONObject> {


            @Override
            protected void onPreExecute() {

                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject objs) {
                SensorsReading object = null;
                try {
                    Map<String, Integer> sensorsValueRecord;
                    Map<String, Float> tempHumidityRecord;
                    JSONArray array = objs.getJSONArray("records");


                    for (int i = 0; i < array.length(); i++) {

                        JsonElement mJson = JsonParser.parseString(String.valueOf(array.getJSONObject(i)));
                        Gson gson = new Gson();
                        object = gson.fromJson(mJson, SensorsReading.class);
                        setSensorsReadingObject(object);
                        sensorsValueRecord = object.getSensors();
                        tempHumidityRecord = object.getSensorsTempHumidity();

                        if (meanSensorValues == null) {
                            meanSensorValues = sensorsValueRecord;
                            meanTempHumidityValues = tempHumidityRecord;
                        } else {
                            for (String key : meanSensorValues.keySet()) {
                                meanSensorValues.replace(key, (sensorsValueRecord.get(key) + meanSensorValues.get(key)) / 2);
                            }

                            for (String key2 : meanTempHumidityValues.keySet()) {
                                meanTempHumidityValues.replace(key2, (tempHumidityRecord.get(key2) + meanTempHumidityValues.get(key2)) / 2);
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setSensorsStatValues(object, getView());
                progressBar.setVisibility(View.GONE);

                super.onPostExecute(objs);

            }


            @Override
            protected JSONObject doInBackground(Void... voids) {

                return getReadings("sensorsReading", startDateString, endDateString);
            }
        }
        readSensorsEntry meanSensors = new readSensorsEntry();
        meanSensors.execute();
    }


    private void setSensorsStatValues(SensorsReading object, View view) {
        if (meanSensorValues != null) {

            object.setPressureThreshold(SharedPrefManager.getInstance(getContext()).getOverPressureValue());

            for (String key : object.sensorDistribution.keySet()) {

                object.changeSensorsColor(object.sensorDistribution.get(key), view, (int) meanSensorValues.get(key), getContext());

            }
            if (meanTempHumidityValues != null) {
                leftFootTemperature.setText(new StringBuilder().append(String.format("%.2f", meanTempHumidityValues.get("T1"))).append("ºC").toString());
                rightFootTemperature.setText(new StringBuilder().append(String.format("%.2f", meanTempHumidityValues.get("T2"))).append("ºC").toString());
                leftFootHumidity.setText(new StringBuilder().append(String.format("%.2f", meanTempHumidityValues.get("H1"))).append("%").toString());
                rightFootHumidity.setText(new StringBuilder().append(String.format("%.2f", meanTempHumidityValues.get("H2"))).append("%").toString());

            }
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.no_records_message_en),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void readStatsEntry(String startDateString, String endDateString) {
        class readStatsEntry extends AsyncTask<Void, Void, JSONObject> {


            @Override
            protected void onPreExecute() {

                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject objs) {
                StatisticsData object = null;
                try {
                    Map<String, Long> statsRecord;
                    JSONArray array = objs.getJSONArray("records");


                    for (int i = 0; i < array.length(); i++) {

                        JsonElement mJson = JsonParser.parseString(String.valueOf(array.getJSONObject(i)));
                        Gson gson = new Gson();
                        object = gson.fromJson(mJson, StatisticsData.class);
                        statsRecord = object.getStatInfo();

                        if (meanStats == null) {
                            meanStats = statsRecord;
                        } else {
                            for (String key : meanStats.keySet()) {
                                if (key.equals("steps"))
                                    meanStats.replace(key, statsRecord.get(key) + meanStats.get(key));
                                else
                                    meanStats.replace(key, (statsRecord.get(key) + meanStats.get(key)) / 2);
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setOtherStatsInfo();
                progressBar.setVisibility(View.GONE);

                super.onPostExecute(objs);

            }


            @Override
            protected JSONObject doInBackground(Void... voids) {

                return getReadings("statistics", startDateString, endDateString);
            }
        }
        readStatsEntry meanStats = new readStatsEntry();
        meanStats.execute();
    }

    private void setOtherStatsInfo() {
        if (meanStats != null) {

            cadenceMean.setText(new StringBuilder().append(getResources().getString(R.string.cadence_mean_en)).append(" : ").append(meanStats.get("cadence")).toString());
            stepsCount.setText(new StringBuilder().append(getResources().getString(R.string.steps_count_stats_en)).append(" : ").append(meanStats.get("steps")).toString());
            balance.setProgress(Math.toIntExact(meanStats.get("balance")));

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putSerializable("meanSensors", (Serializable) getMeanSensorValues());
        outState.putSerializable("meanTempHumidity", (Serializable) getMeanTempHumidityValues());
        outState.putSerializable("meanStats", (Serializable) getMeanStats());
        outState.putSerializable("meanStats", (Serializable) getMeanStats());
        outState.putSerializable("sensorsReadingObject", (Serializable) getSensorsReadingObject());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            setMeanSensorValues((Map<String, Integer>) savedInstanceState.getSerializable("meanSensors"));
            setMeanTempHumidityValues((Map<String, Float>) savedInstanceState.getSerializable("meanTempHumidity"));
            setMeanStats((Map<String, Long>) savedInstanceState.getSerializable("meanStats"));
            //          setSensorsReadingObject((SensorsReading) savedInstanceState.getSerializable("sensorsReadingObject"));
        }


    }


}
