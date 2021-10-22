package com.example.mycareshoe.ui.monitoring;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.PatientHelper;
import com.example.mycareshoe.model.SensorsReading;
import com.example.mycareshoe.model.StatisticsData;
import com.example.mycareshoe.model.Warnings;
import com.example.mycareshoe.helpers.ChronometerHelper;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.service.HTTPRequest;
import com.example.mycareshoe.service.URLs;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;

import okhttp3.FormBody;

public class MonitoringFragment extends Fragment {


    private boolean runningStatus;
    private Timer t = new Timer();
    // creating object of ViewPager
    ViewPager mViewPager;
    private long steps = 0, pauseOffset = 0, cadence = 0, leftStance = 0, rightStance = 0, timeWhenStopped = 0, lastTimerValueOnFragment = 0, recentTimerValueOnFragment = 0, lastChronoValue = 0;
    private double pace = 0;
    private WarningsFragment warningsFragment = new WarningsFragment();
    private static ArrayList<SensorsReading> sensorsReadingArrayList = new ArrayList<>();
    private ArrayList<StatisticsData> statisticsDataArrayList = new ArrayList<>();
    private static SensorsReading sr;
    private ArrayList<Warnings> warningsArrayList = new ArrayList<>();
    public ChronometerHelper chronometer = new ChronometerHelper();
    private Runnable updateStats, updateTimeDependentStats;
    private PatientHelper patientHelper = new PatientHelper();
    private HashMap<String, String> leftStanceTime = new HashMap<>();
    private HashMap<String, String> rightStanceTime = new HashMap<>();
    private static Menu optionsMenu;
    private static boolean warningsLeftToRead;
    private boolean isWarningsButtonRed;


    public boolean isWarningsButtonRed() {
        return isWarningsButtonRed;
    }

    public void setWarningsButtonRed(boolean warningsButtonRed) {
        isWarningsButtonRed = warningsButtonRed;
    }

    public void setWarningsLeftToRead(boolean warningsLeftToRead) {
        this.warningsLeftToRead = warningsLeftToRead;
    }

    public Menu getOptionsMenu() {
        return optionsMenu;
    }

    public void setOptionsMenu(Menu optionsMenu) {
        this.optionsMenu = optionsMenu;
    }

    public HashMap<String, String> getLeftStanceTime() {
        return leftStanceTime;
    }

    public void setLeftStanceTime(HashMap<String, String> leftStanceTime) {
        this.leftStanceTime = leftStanceTime;
    }

    public HashMap<String, String> getRightStanceTime() {
        return rightStanceTime;
    }

    public void setRightStanceTime(HashMap<String, String> rightStanceTime) {
        this.rightStanceTime = rightStanceTime;
    }

    public boolean getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(boolean runningStatus) {
        this.runningStatus = runningStatus;
    }

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

    private Handler handler = new Handler((Looper.getMainLooper()));
    private Handler handler2 = new Handler((Looper.getMainLooper()));
    private Handler handler3 = new Handler((Looper.getMainLooper()));

    public long getLeftStance() {
        return leftStance;
    }

    public void setLeftStance(long leftStance, TextView leftStanceText) {
        this.leftStance = leftStance;
        leftStanceText.setText(new StringBuilder().append(Long.toString(leftStance)).append(" s").toString());
    }

    public long getRightStance() {
        return rightStance;
    }

    public void setRightStance(long rightStance, TextView rightStanceText) {
        this.rightStance = rightStance;
        rightStanceText.setText(new StringBuilder().append(Long.toString(rightStance)).append(" s").toString());
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps, TextView stepsText) {
        this.steps = steps;
        stepsText.setText(Long.toString(steps));
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getCadence() {
        return cadence;
    }

    public void setCadence(long cadence, TextView cadenceText) {
        this.cadence = cadence;
        cadenceText.setText(Long.toString(cadence));
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace, TextView paceText) {
        this.pace = pace;
        paceText.setText(Double.toString(pace));
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public void setCadence(long cadence) {
        this.cadence = cadence;
    }

    public void setLeftStance(long leftStance) {
        this.leftStance = leftStance;
    }

    public void setRightStance(long rightStance) {
        this.rightStance = rightStance;
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

        setOptionsMenu(menu);

        super.onCreateOptionsMenu(menu, inflater);

        setWarningButtonColor();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.warnings:

                setWarningsLeftToRead(false);
                setWarningButtonColor();

                mViewPager = (ViewPager) getView().findViewById(R.id.viewPagerMain);
                WarningsFragment dialogFragment = new WarningsFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "simple dialog");
                return true;

            case R.id.temperatureHumidity:
                if (sr == null) {
                    Toast.makeText(getContext(), "No data available! Connect the bluetooth devices first.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("reading", getSr());
                    TemperatureHumidityFragment dialogFragmentTemp = new TemperatureHumidityFragment();
                    dialogFragmentTemp.setArguments(bundle);
                    dialogFragmentTemp.show(getActivity().getSupportFragmentManager(), "simple dialog");
                }
                return true;

            case R.id.information:
                InformationsFragment dialogInfoFragment = new InformationsFragment();
                dialogInfoFragment.show(getActivity().getSupportFragmentManager(), "simple dialog");
                return true;
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (chronometer == null) {
            chronometer = new ChronometerHelper();
        }

        chronometer.setChrono(getView().findViewById(R.id.chronometer));
        chronometer.setTimeWhenStopped(timeWhenStopped);


        // Initializing the ViewPager Object
        mViewPager = (ViewPager) getView().findViewById(R.id.viewPagerMain);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(this.getActivity(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);


        getActivity().setTitle(getResources().getString(R.string.monitorization_en));

        ImageView play = (ImageView) view.findViewById(R.id.play);
        ImageView pause = (ImageView) view.findViewById(R.id.pause);
        ImageView stop = (ImageView) view.findViewById(R.id.stop);
        TextView stepsText = (TextView) view.findViewById(R.id.steps);
        TextView cadenceText = (TextView) view.findViewById(R.id.cadence);
        TextView leftStanceText = (TextView) view.findViewById(R.id.stanceTimeLeft);
        TextView rightStanceText = (TextView) view.findViewById(R.id.stanceTimeRight);
        TextView paceText = (TextView) view.findViewById(R.id.pace);

        if (savedInstanceState != null) {
            stepsText.setText(Long.toString(savedInstanceState.getLong("steps")));
            cadenceText.setText(Long.toString(savedInstanceState.getLong("cadence")));
            leftStanceText.setText(Long.toString(savedInstanceState.getLong("leftStance")));
            rightStanceText.setText(Long.toString(savedInstanceState.getLong("rightStance")));
            paceText.setText(Double.toString(savedInstanceState.getDouble("pace")));
        } else {

            stepsText.setText(Long.toString(getSteps()));
            cadenceText.setText(Long.toString(getCadence()));
            leftStanceText.setText(Long.toString(getLeftStance()) + " s");
            rightStanceText.setText(Long.toString(getRightStance()) + " s");
            paceText.setText(Double.toString(getPace()));

        }

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    validateSensorValues(getSr(), mViewPager, progressBar);

                    if (warningsLeftToRead && !isWarningsButtonRed()) {
                        setWarningButtonColor();
                        setWarningsLeftToRead(true);
                    }

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 0);
            }
        };

        handler.postDelayed(runnable, 0);


        updateStats = new Runnable() {
            @Override
            public void run() {

                if (chronometer.isRunning()) {

                    updateStatisticsThread(view, leftStanceText, rightStanceText);
                    stepsText.setText(Long.toString(getSteps()));

                }
                handler2.postDelayed(updateStats, 2000);


            }
        };


        updateTimeDependentStats = new Runnable() {

            @Override
            public void run() {

                if (chronometer.isRunning()) {

                    updateLiveStatisticsThread(view, paceText, cadenceText);

                }
                handler3.postDelayed(this, 3000);
            }
        };


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!getRunningStatus()) {

                    handler2.postDelayed(updateStats, 2000);
                    handler3.postDelayed(updateTimeDependentStats, 3000);
                    chronometer.start();
                    setRunningStatus(true);
                    SharedPrefManager.getInstance(getContext()).setChronoRunningStatus(true);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chronometer.isRunning()) {
                    setRunningStatus(false);
                    SharedPrefManager.getInstance(getContext()).setChronoRunningStatus(false);
                    timeWhenStopped = chronometer.stop();
                    handler2.removeCallbacks(updateStats);
                    handler3.removeCallbacks(updateTimeDependentStats);
                    t.cancel();
                    t = new Timer();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRunningStatus(false);
                SharedPrefManager.getInstance(getContext()).setChronoRunningStatus(false);
                timeWhenStopped = chronometer.reset();
                setSteps(0, stepsText);
                setCadence(0, cadenceText);
                setPace(0, paceText);
                setLeftStance(0, leftStanceText);
                setRightStance(0, rightStanceText);
                handler2.removeCallbacks(updateStats);
                handler3.removeCallbacks(updateTimeDependentStats);
                t.cancel();
                t = new Timer();
                sensorsReadingArrayList.clear();
                statisticsDataArrayList.clear();
                sr = null;
                leftStanceTime.clear();
                rightStanceTime.clear();
            }
        });

    }


    private void checkStancePhase(ArrayList<SensorsReading> sensorsReadingArrayList, HashMap<String, String> leftStanceMap, HashMap<String, String> rightStanceMap) {

        SensorsReading sensorsReading = new SensorsReading(SharedPrefManager.getInstance(getContext()).getOverPressureValue());
        for (int i = 0; i < sensorsReadingArrayList.size(); i++) {

            sensorsReading = sensorsReadingArrayList.get(i);

            if (sensorsReading.isLeftHeelStrike())
                leftStanceMap.put("heelStrike", sensorsReading.getDate());
            if (sensorsReading.isLeftFootToeOff())
                leftStanceMap.put("toeOff", sensorsReading.getDate());

            if (sensorsReading.isLeftFootMidSwing()) {
                if (leftStanceMap.containsKey("heelStrike") && leftStanceMap.containsKey("toeOff"))
                    leftStanceMap.put("midSwing", sensorsReading.getDate());
            }

            if (sensorsReading.isRightHeelStrike())
                rightStanceMap.put("heelStrike", sensorsReading.getDate());
            if (sensorsReading.isRightFootToeOff())
                rightStanceMap.put("toeOff", sensorsReading.getDate());

            if (sensorsReading.isRightFootMidSwing()) {
                if (rightStanceMap.containsKey("heelStrike") && rightStanceMap.containsKey("toeOff"))
                    rightStanceMap.put("midSwing", sensorsReading.getDate());
            }

        }

        setLeftStanceTime(leftStanceMap);
        setRightStanceTime(rightStanceMap);

    }

    public void updateLiveStatisticsThread(View view, TextView paceText, TextView cadenceText) {

        class updateLiveStatisticsThread extends AsyncTask<Void, Void, Double[]> {

            @Override
            protected void onPostExecute(Double[] stats) {

                if (chronometer.isRunning() && stats != null) {
                    setPace(stats[0], paceText);
                    setCadence(Math.round(stats[1]), cadenceText);
                    statisticsDataArrayList.clear();
                } else {
                    setPace(0, (TextView) view.findViewById(R.id.pace));
                    setCadence(0, (TextView) view.findViewById(R.id.cadence));
                }


            }


            @Override
            protected Double[] doInBackground(Void... voids) {
                //creating request handler object
                String paceUIString;
                double paceUI = 0;
                long cadenceUI = 0;
                int steps = 0;
                if (chronometer.isRunning()) {
                    if (statisticsDataArrayList.size() != 0) {

                        for (int i = 0; i < statisticsDataArrayList.size(); i++) {

                            steps += statisticsDataArrayList.get(i).getSteps();

                      /*      if (i == 0) {
                                paceUI = statisticsDataArrayList.get(i).getPace();
                                cadenceUI = statisticsDataArrayList.get(i).getCadence();
                            } else {
                                paceUI = (paceUI + statisticsDataArrayList.get(i).getPace()) / 2;
                                cadenceUI = (cadenceUI + statisticsDataArrayList.get(i).getCadence()) / 2;
                            }
                            */

                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long time = 0;
                        try {
                            time = (dateFormat.parse(statisticsDataArrayList.get(statisticsDataArrayList.size() - 1).getDate()).getTime() - dateFormat.parse(statisticsDataArrayList.get(0).getDate()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (time == 0)
                            time = 1500;

                        if (steps != 0) {

                            paceUI = (time / 60) / (steps * SharedPrefManager.getInstance(getContext()).getStrideLength());
                            cadenceUI = (steps * 60) / (time / 1000);
                        } else {
                            paceUI = getPace();
                            cadenceUI = getCadence();
                        }

                        if (getPace() != 0)
                            paceUI = (paceUI + getPace()) / 2;
                    }

                    return new Double[]{new BigDecimal(Double.toString(paceUI)).setScale(2, RoundingMode.HALF_UP).doubleValue(), Double.valueOf(cadenceUI)};


                }

                return null;
            }

        }
        updateLiveStatisticsThread updateLiveStatisticsThread = new updateLiveStatisticsThread();
        updateLiveStatisticsThread.execute();


    }

    public void updateStatisticsThread(View view, TextView leftStanceText, TextView rightStanceText) {


        class updateStatisticsThread extends AsyncTask<Void, Void, StatisticsData> {

            @Override
            protected void onPostExecute(StatisticsData stats) {

                int priorsensorListSize = 0;
                if (stats != null) {
                    try {

                        priorsensorListSize = sensorsReadingArrayList.size();
                        setLeftStanceTime(stats.checkStanceTime(getLeftStanceTime()));
                        setRightStanceTime(stats.checkStanceTime(getRightStanceTime()));

                        if (getLeftStanceTime().containsKey("stanceTime")) {
                            setLeftStance(Long.parseLong(getLeftStanceTime().get("stanceTime")), leftStanceText);
                            stats.setLeftStanceTime(Long.parseLong(getLeftStanceTime().get("stanceTime")));
                            getLeftStanceTime().clear();
                        }

                        if (getRightStanceTime().containsKey("stanceTime")) {
                            setRightStance(Long.parseLong(getRightStanceTime().get("stanceTime")), rightStanceText);
                            stats.setRightStanceTime(Long.parseLong(getRightStanceTime().get("stanceTime")));
                            getRightStanceTime().clear();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    sensorsReadingArrayList.subList(0, priorsensorListSize).clear();


                }
            }

            @Override
            protected StatisticsData doInBackground(Void... voids) {
                //creating request handler object

                if (chronometer.isRunning()) {
                    if (sensorsReadingArrayList.size() != 0) {
                        StatisticsData stats = new StatisticsData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()), 1, sensorsReadingArrayList, getContext());

                        setSteps(getSteps() + stats.getSteps());

                        statisticsDataArrayList.add(stats);
                        checkStancePhase(sensorsReadingArrayList, getLeftStanceTime(), getRightStanceTime());
                        createStatistics(stats);

                        return stats;

                    }

                }

                return null;
            }
        }

        updateStatisticsThread updateStatisticsThread = new updateStatisticsThread();
        updateStatisticsThread.execute();

    }

    public void createReading(SensorsReading reading) {

        class createReading extends AsyncTask<Void, Void, JSONObject> {

            private Context mContext;

            public createReading(Context context) {
                mContext = context;
            }

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }


            @Override
            protected void onPostExecute(JSONObject obj) {

                if (obj == null)
                    Toast.makeText(getContext(), "Error saving your sensors reading record!",
                            Toast.LENGTH_SHORT).show();
                super.onPostExecute(obj);

            }

            @Override
            protected JSONObject doInBackground(Void... voids) {

                if (reading != null) {
                    setSr(reading);
                    sensorsReadingArrayList.add(getSr());
                }
                //creating request handler object
                HTTPRequest httpRequest = new HTTPRequest();
                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder = reading.getSensorsQueryParams();
                formBuilder.add("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number()));
                formBuilder.add("date", reading.getDate());
                formBuilder.add("T1", Float.toString(reading.getT1()));
                formBuilder.add("T2", Float.toString(reading.getT2()));
                formBuilder.add("H1", Float.toString(reading.getH1()));
                formBuilder.add("H2", Float.toString(reading.getH2()));


                JSONObject readingJson = httpRequest.makeHttpRequest(URLs.URL_CREATE_READING, "POST", formBuilder, null);


                try {
                    if (readingJson.getString("success").equals("1")) {
                        if (reading.getHiperpressionSensors() != null) {
                            try {

                                checkWarningCreation(reading.getHiperpressionSensors(), reading, mContext);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //returning the response
                return readingJson;

            }
        }

        createReading readingCreation = new createReading(getContext());

        if (SharedPrefManager.getInstance(getContext()).isChronoRunning())
            readingCreation.execute();


    }

    private void validateSensorValues(SensorsReading reading, View view, ProgressBar progressBar) throws NoSuchMethodException {

        if (reading != null) {

            if (getContext() != null) {
                for (String key : reading.sensorDistribution.keySet()) {

                    reading.changeSensorsColor(reading.sensorDistribution.get(key), view, (int) reading.getSensors().get(key), getContext());
                }

                progressBar.setProgress((int) reading.calculateBalance(getSr()));


                reading.setSensorColorPrinted(true);


            }
        }
    }

    public void createStatistics(StatisticsData statisticsData) {

        class createStatistics extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject obj) {

                if (obj == null)
                    Toast.makeText(getContext(), "Error saving your statistics record!",
                            Toast.LENGTH_SHORT).show();
                super.onPostExecute(obj);

            }

            @Override
            protected JSONObject doInBackground(Void... voids) {

                //creating request handler object
                HTTPRequest httpRequest = new HTTPRequest();
                FormBody.Builder formBuilder = new FormBody.Builder();

                formBuilder.add("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number()));
                formBuilder.add("date", statisticsData.getDate());
                formBuilder.add("cadence", Float.toString(statisticsData.getCadence()));
                formBuilder.add("balance", Float.toString(statisticsData.getBalance()));
                formBuilder.add("steps", Float.toString(statisticsData.getSteps()));
                formBuilder.add("left_foot_stance", Float.toString(statisticsData.getLeftStanceTime()));
                formBuilder.add("right_foot_stance", Float.toString(statisticsData.getRightStanceTime()));


                //returning the response
                return httpRequest.makeHttpRequest(URLs.URL_CREATE_STATISTIC, "POST", formBuilder, null);
            }
        }
        createStatistics createStatistics = new createStatistics();
        createStatistics.execute();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if (chronometer != null)
            chronometer.saveInstanceState(outState);

        outState.putBoolean("warningsLeftToRead", warningsLeftToRead);
        if (getSteps() != 0) {
            outState.putLong("steps", getSteps());
            outState.putLong("cadence", getCadence());
            outState.putDouble("pace", getPace());
        }

        if (getLeftStance() != 0)
            outState.putLong("leftStance", getLeftStance());

        if (getRightStance() != 0)
            outState.putLong("rightStance", getRightStance());

    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            chronometer.restoreInstanceState(savedInstanceState);

            setPace(savedInstanceState.getDouble("pace"));
            setSteps(savedInstanceState.getLong("steps"));
            setCadence(savedInstanceState.getLong("cadence"));
            setLeftStance(savedInstanceState.getLong("leftStance"));
            setRightStance(savedInstanceState.getLong("rightStance"));
            setWarningsLeftToRead(savedInstanceState.getBoolean("warningsLeftToRead"));
            handler2.removeCallbacksAndMessages(updateStats);
            handler3.removeCallbacksAndMessages(updateTimeDependentStats);
            handler2.postDelayed(updateStats, 2000);
            handler3.postDelayed(updateTimeDependentStats, 3000);
        }

    }

    @Override
    public void onPause() {
        lastChronoValue = SystemClock.elapsedRealtime() - chronometer.getChrono().getBase();
        super.onPause();
        lastTimerValueOnFragment = System.currentTimeMillis();
    }

    @Override
    public void onResume() {

        recentTimerValueOnFragment = System.currentTimeMillis();
        super.onResume();
        if (chronometer.isRunning()) {

            if (lastChronoValue != 0)
                chronometer.setCurrentTime(recentTimerValueOnFragment - lastTimerValueOnFragment + lastChronoValue);
            chronometer.getChrono().start();
        }
    }

    private void checkWarningCreation(ArrayList<String> hyperpressureSensors, SensorsReading reading, Context context) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar warningDate = Calendar.getInstance();
        Calendar readingDate = Calendar.getInstance();
        boolean sensorWarningFound = false;
        String sensorsWarning = null;


        setWarningsLeftToRead(true);

        for (int i = 0; i < hyperpressureSensors.size(); i++) {

            sensorWarningFound = false;

            readingDate.setTime(formatter.parse(reading.getDate()));

            if (warningsArrayList.size() == 0) {

                warningsArrayList.add(new Warnings(reading.getDate(), hyperpressureSensors.get(i), 1));
            } else {
                for (int j = 0; j < warningsArrayList.size(); j++) {

                    warningDate.setTime(formatter.parse(warningsArrayList.get(j).getWarningTime()));

                    warningDate.add(Calendar.SECOND, SharedPrefManager.getInstance(getContext()).getTimeInterval());

                    if (warningsArrayList.get(j).getSensor().equals(hyperpressureSensors.get(i))) {

                        sensorWarningFound = true;
                        if (warningDate.compareTo(readingDate) > 0) {

                            warningsArrayList.get(j).setCounter(warningsArrayList.get(j).getCounter() + 1);


                            if (warningsArrayList.get(j).getCounter() == SharedPrefManager.getInstance(getContext()).getOccurrencesNumber()) {


                                if (sensorsWarning != null)
                                    sensorsWarning = sensorsWarning + warningsArrayList.get(j).getSensor() + ",";
                                else
                                    sensorsWarning = warningsArrayList.get(j).getSensor() + ",";

                                warningsArrayList.remove(j);
                            }

                        } else {
                            warningsArrayList.remove(j);
                            warningsArrayList.add(new Warnings(reading.getDate(), hyperpressureSensors.get(i), 1));
                        }
                    }
                }
                if (!sensorWarningFound)
                    warningsArrayList.add(new Warnings(reading.getDate(), hyperpressureSensors.get(i), 1));
            }
        }

        if (sensorsWarning != null) {
            sensorsWarning = sensorsWarning.substring(0, sensorsWarning.length() - 1);
            warningsFragment.createWarning(sensorsWarning, reading.getDate());
        }
    }

    private void setWarningButtonColor() {

        if (getOptionsMenu().size() != 0 && getContext() != null) {
            Drawable drawable = getOptionsMenu().getItem(2).getIcon();
            if (warningsLeftToRead) {

                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getContext().getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }
            } else {
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getContext().getResources().getColor(R.color.grey_toolbar), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }
}
