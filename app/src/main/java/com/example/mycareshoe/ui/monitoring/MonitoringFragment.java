package com.example.mycareshoe.ui.monitoring;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.mycareshoe.R;
import com.example.mycareshoe.ui.MainActivity;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

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

    // images array
    int[] images = {R.drawable.foot_icon, R.drawable.foot_icon_inverted};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;

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
                WarningsFragment dialogFragment = new WarningsFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
                return true;

            case R.id.infoButton:
                TemperatureHumidityFragment dialogFragmentTemp = new TemperatureHumidityFragment();
                dialogFragmentTemp.show(getActivity().getSupportFragmentManager(),"simple dialog");
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

        final TextView textViewToChange = (TextView) getView().findViewById(R.id.steps);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);


        textViewToChange.setText("100");


        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!runningStatus){
                    chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                    chronometer.start();
                    runningStatus=true;
                    t.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            Random r = new Random();
                            progressBar.setProgress(r.nextInt(100));
                        }

                    }, 0,500);//put here time 1000 milliseconds=1 second
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

    }
