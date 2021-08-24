package com.example.mycareshoe.ui.monitoring;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.SensorsReading;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.login.JSONParser;
import com.google.android.material.tabs.TabLayout;

import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MonitoringFragment extends Fragment {

    private Chronometer chronometer;
    private boolean runningStatus;
    private long pauseOffset;
    private Timer t = new Timer();
    // creating object of ViewPager
    ViewPager mViewPager;
    private int steps=0;
    private WarningsFragment warningsFragment= new WarningsFragment();

    private static SensorsReading sr;



    public SensorsReading getSr() {
        return sr;
    }

    public void setSr(SensorsReading sr) {
        sr.setSensorColorPrinted(false);
        this.sr = sr;

    }


    // images array
    int[] images = {R.drawable.foot_icon, R.drawable.foot_icon_inverted};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;


    private Handler handler = new Handler();

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps, TextView stepsText) {
        this.steps = steps;
        stepsText.setText(Integer.toString(steps));
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_monitoring, container, false);



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.monitoring_headerbutons, menu);

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.warnings:
                mViewPager = (ViewPager) getView().findViewById(R.id.viewPagerMain);
                WarningsFragment dialogFragment = new WarningsFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
                return true;

            case R.id.infoButton:
                if(sr==null){
                    Toast.makeText(getContext(), "No data available! Connect the bluetooth devices first.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("reading", getSr());
                    TemperatureHumidityFragment dialogFragmentTemp = new TemperatureHumidityFragment();
                    dialogFragmentTemp.setArguments(bundle);
                    dialogFragmentTemp.show(getActivity().getSupportFragmentManager(), "simple dialog");
                }
                return true;
        }
        return false;
    }



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing the ViewPager Object
        mViewPager = (ViewPager) getView().findViewById(R.id.viewPagerMain);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(this.getActivity(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);


        getActivity().setTitle(getResources().getString(R.string.monitorization_en));
        chronometer = (Chronometer) getView().findViewById(R.id.chronometer);

        ImageView play = (ImageView) view.findViewById(R.id.play);
        ImageView pause = (ImageView) view.findViewById(R.id.pause);
        ImageView stop = (ImageView) view.findViewById(R.id.stop);
        TextView stepsText = (TextView) view.findViewById(R.id.steps);
        TextView cadenceText = (TextView) view.findViewById(R.id.cadence);

        final TextView textViewToChange = (TextView) getView().findViewById(R.id.steps);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    validateSensorValues(getSr(), mViewPager, stepsText, progressBar);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 0);
            }
        };

        handler.postDelayed(runnable,0);


        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!runningStatus){
                    chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                    chronometer.start();
                    runningStatus=true;

                    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        private long previousSteps = 0;
                        private long sensorBalance=0;
                        public void onChronometerTick(Chronometer chronometer) {

                            cadenceText.setText(Long.toString((steps-previousSteps)*60));

                            previousSteps=steps;
                        }
                    });
                    t.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            Random r = new Random();

                            //

                        }

                    }, 0,1000);//put here time 1000 milliseconds=1 second
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(runningStatus){
                    pauseOffset= SystemClock.elapsedRealtime()-chronometer.getBase();
                    chronometer.stop();
                    runningStatus=false;
                    t.cancel();
                    t = new Timer();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset=0;
                chronometer.stop();
                runningStatus=false;
                t.cancel();
                t = new Timer();
            }
        });



        }

    public void createReading(SensorsReading reading) {

        class createReading extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject obj) {

                try {
                    if(obj.getString("success").equals("1"))
                    {
                        warningsFragment.createWarning(obj.getString("reading_id"),obj.getString("date") );
                        setSr(reading);


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
                ArrayList params = reading.getsensorsQueryParams();
                params.add(new BasicNameValuePair("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number())));
                params.add(new BasicNameValuePair("date", reading.getDate()));
                params.add(new BasicNameValuePair("T1", Float.toString(reading.getT1())));
                params.add(new BasicNameValuePair("T2", Float.toString(reading.getT2())));
                params.add(new BasicNameValuePair("H1", Float.toString(reading.getH1())));
                params.add(new BasicNameValuePair("H2", Float.toString(reading.getH2())));

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_CREATE_READING,"PUT", params);
            }
        }
        createReading readingCreation = new createReading();
        readingCreation.execute();

    }

    private void validateSensorValues(SensorsReading reading, View view, TextView stepsText, ProgressBar progressBar) throws NoSuchMethodException {
        int index=0;

        if(reading!=null ) {

            for(String key: reading.sensorDistribution.keySet()){

                    changeSensorsColor(reading.sensorDistribution.get(key), view, (int) reading.getSensors().get(key));

                    index++;
                }

            progressBar.setProgress((int) calculateBalance());
            if(!reading.isSensorColorPrinted()) {
                if (reading.getRightFootSensorsSum() == 0 && reading.getLeftFootSensorsSum() > 0) {
                    setSteps(getSteps() + 1, stepsText);

                }
            }

            reading.setSensorColorPrinted(true);

        }
    }

    private void changeSensorsColor(int sensorImageId, View view, int sensorValue){

        float fraction=0;
        if(sensorValue<200) {
            fraction = ((float) sensorValue) / SharedPrefManager.getInstance(getContext()).getOverPressureValue();
        }
        else{
            fraction=1;
        }
        ImageView imageViewIcon = (ImageView) view.findViewById(sensorImageId);

        int color = ColorUtils.blendARGB(getResources().getColor(R.color.dark_green), getResources().getColor(R.color.red), (float) fraction);
        imageViewIcon.setColorFilter(color);



    }
    private long calculateBalance(){

        long sensorsSum=getSr().getLeftFootSensorsSum()+getSr().getRightFootSensorsSum();

        return (getSr().getLeftFootSensorsSum()*100)/sensorsSum;
    }





}
