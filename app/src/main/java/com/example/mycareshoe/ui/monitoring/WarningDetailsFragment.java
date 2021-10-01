package com.example.mycareshoe.ui.monitoring;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
    ProgressBar progressBar;


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

        progressBar = getView().findViewById(R.id.progressBar);
        hiperpressionSensorsName = (TextView) getView().findViewById(R.id.sensorsName);
        datevalue = (TextView) getView().findViewById(R.id.dateValue);

        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = this.getArguments();

        Handler h= new Handler();

        Runnable run= new Runnable() {
            @Override
            public void run() {
                if(!mViewPagerAdapter.isInflated())
                    h.postDelayed(this, 0);
                else{
                    validateSensorValues(bundle.getString("warning_date"), bundle.getString("sensors"));
                    h.removeCallbacks(this);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        h.postDelayed(run,0);
    }

    private void validateSensorValues(String warning_date, String sensors){

        SensorsReading sr = new SensorsReading();

        String[] individualSensors = sensors.split(",");
        for (String sensor: individualSensors
             ) {
            changeSensorsColor(sr.sensorDistribution.get(sensor));
        }

            hiperpressionSensorsName.setText(sensors);
            datevalue.setText(warning_date);

    }

    private void changeSensorsColor(int sensorImageId){


        ImageView imageViewIcon = (ImageView) getView().findViewById(sensorImageId);
        imageViewIcon.setColorFilter(getContext().getResources().getColor(R.color.red));

    }

}
