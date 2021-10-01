package com.example.mycareshoe.ui.monitoring;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.SensorsReading;
import com.example.mycareshoe.data.model.StatisticsData;
import com.example.mycareshoe.data.model.Warnings;
import com.example.mycareshoe.helpers.ChronometerHelper;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.login.JSONParser;
import com.google.android.material.tabs.TabLayout;

import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;

public class MonitoringFragment extends Fragment {


    private boolean runningStatus;
    private Timer t = new Timer();
    // creating object of ViewPager
    ViewPager mViewPager;
    private long  steps=0, pauseOffset=0, cadence=0, leftStance=0, rightStance=0, timeWhenStopped = 0, lastTimerValueOnFragment=0, recentTimerValueOnFragment=0, lastChronoValue=0;
    private double pace=0;
    private WarningsFragment warningsFragment= new WarningsFragment();
    private static ArrayList<SensorsReading> sensorsReadingArrayList= new ArrayList<>();
    private ArrayList<StatisticsData> statisticsDataArrayList=new ArrayList<>();
    private static SensorsReading sr;
    private ArrayList<Warnings> warningsArrayList=new ArrayList<>();
    public ChronometerHelper chronometer = new ChronometerHelper();
    private Runnable updateStats, updateLiveStats;

    private HashMap<String, String> leftStanceTime=new HashMap<>();
    private HashMap<String, String> rightStanceTime=new HashMap<>();
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

    private Handler handler = new Handler();

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

    public void setRightStance(long rightStance, TextView righttStanceText) {
        this.rightStance = rightStance;
        righttStanceText.setText(new StringBuilder().append(Long.toString(rightStance)).append(" s").toString());
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps, TextView stepsText) {
        this.steps = steps;
        System.out.println(Long.toString(steps));
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

        super.onCreateOptionsMenu(menu,inflater);

        setWarningButtonColor();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.warnings:

                setWarningsLeftToRead(false);
                setWarningButtonColor();

                mViewPager = (ViewPager) getView().findViewById(R.id.viewPagerMain);
                WarningsFragment dialogFragment = new WarningsFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
                return true;

            case R.id.temperatureHumidity:
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

            case R.id.information:
                InformationsFragment dialogInfoFragment = new InformationsFragment();
                dialogInfoFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
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

        if(chronometer==null){
            chronometer= new ChronometerHelper();
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

        if(savedInstanceState!=null) {
            stepsText.setText(Long.toString(savedInstanceState.getLong("steps")));
            cadenceText.setText(Long.toString(savedInstanceState.getLong("cadence")));
            leftStanceText.setText(Long.toString(savedInstanceState.getLong("leftStance")));
            rightStanceText.setText(Long.toString(savedInstanceState.getLong("rightStance")));
            paceText.setText(Double.toString(savedInstanceState.getDouble("pace")));
        }else{
            stepsText.setText(Long.toString(getSteps()));
            cadenceText.setText(Long.toString(getCadence()));
            leftStanceText.setText(Long.toString(getLeftStance()));
            rightStanceText.setText(Long.toString(getRightStance()));
            paceText.setText(Double.toString(getPace()));
        }

        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    validateSensorValues(getSr(), mViewPager, progressBar);

                    if(warningsLeftToRead && !isWarningsButtonRed()) {
                        setWarningButtonColor();
                        setWarningsLeftToRead(true);
                    }

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 0);
            }
        };

        handler.postDelayed(runnable,0);



        updateStats = new Runnable() {
            @Override
            public void run() {

            if(chronometer.isRunning()) {
                if (sensorsReadingArrayList.size() != 0) {
                    StatisticsData stats = new StatisticsData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()), 1, sensorsReadingArrayList, getContext());
                    statisticsDataArrayList.add(stats);


                    checkStancePhase(sensorsReadingArrayList,getLeftStanceTime(), getRightStanceTime());

                    try {
                        setLeftStanceTime(stats.checkStanceTime(getLeftStanceTime()));
                        setRightStanceTime(stats.checkStanceTime(getRightStanceTime()));

                        if(getLeftStanceTime().containsKey("stanceTime")){
                            setLeftStance(Long.parseLong(getLeftStanceTime().get("stanceTime")), leftStanceText);
                            stats.setLeftStanceTime(Long.parseLong(getLeftStanceTime().get("stanceTime")));
                            getLeftStanceTime().clear();
                        }

                        if(getRightStanceTime().containsKey("stanceTime")){
                            setRightStance(Long.parseLong(getRightStanceTime().get("stanceTime")), rightStanceText);
                            stats.setRightStanceTime(Long.parseLong(getRightStanceTime().get("stanceTime")));
                            getRightStanceTime().clear();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    createStatistics(stats);

                    setSteps(getSteps() + stats.getSteps(), stepsText);
                    sensorsReadingArrayList.clear();
                }

            }

                handler.postDelayed(this, 1000);
            }
        };

        updateLiveStats = new Runnable() {
            @Override
            public void run() {

                double paceUI=0;
                long cadenceUI=0;

                if(chronometer.isRunning()) {
                    if (statisticsDataArrayList.size() != 0) {

                        for(int i=0; i<statisticsDataArrayList.size();i++) {

                            if(i==0){
                                paceUI=statisticsDataArrayList.get(i).getPace();
                                cadenceUI=statisticsDataArrayList.get(i).getCadence();
                            }else {
                                paceUI = (paceUI + statisticsDataArrayList.get(i).getPace()) / 2;
                                cadenceUI = (cadenceUI + statisticsDataArrayList.get(i).getCadence()) / 2;
                            }
                        }



                        setPace(paceUI, paceText);
                        setCadence(cadenceUI, cadenceText);
                        statisticsDataArrayList.clear();
                    }else{
                        setPace(0, paceText);
                        setCadence(0, cadenceText);
                    }


                }

                handler.postDelayed(this, 2000);
            }
        };





        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!getRunningStatus()){

                    handler.postDelayed(updateStats,1000);
                    handler.postDelayed(updateLiveStats,2000);
                    chronometer.start();
                    setRunningStatus(true);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                if(chronometer.isRunning()){
                    setRunningStatus(false);
                    timeWhenStopped=chronometer.stop();
                    handler.removeCallbacks(updateStats);
                    handler.removeCallbacks(updateLiveStats);
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
                setRunningStatus(false);
                timeWhenStopped=chronometer.reset();
                setSteps(0, stepsText);
                setCadence(0, cadenceText);
                setPace(0, paceText);
                handler.removeCallbacks(updateStats);
                handler.removeCallbacks(updateLiveStats);
                t.cancel();
                t = new Timer();
            }
        });
        }


        private void checkStancePhase(ArrayList<SensorsReading> sensorsReadingArrayList, HashMap<String, String> leftStanceMap, HashMap<String, String> rightStanceMap){

            SensorsReading sensorsReading = new SensorsReading(SharedPrefManager.getInstance(getContext()).getOverPressureValue());
            for(int i=0; i<sensorsReadingArrayList.size();i++){

                sensorsReading = sensorsReadingArrayList.get(i);

                if(sensorsReading.isLeftHeelStrike())
                    leftStanceMap.put("heelStrike", sensorsReading.getDate());
                if(sensorsReading.isLeftFootToeOff()){
                    if(leftStanceMap.containsKey("heelStrike"))
                        leftStanceMap.put("toeOff", sensorsReading.getDate());}

                if(sensorsReading.isRightHeelStrike())
                    rightStanceMap.put("heelStrike", sensorsReading.getDate());
                if(sensorsReading.isRightFootToeOff()){
                    if(rightStanceMap.containsKey("heelStrike"))
                        rightStanceMap.put("toeOff", sensorsReading.getDate());}
            }

            setLeftStanceTime(leftStanceMap);
            setRightStanceTime(rightStanceMap);

        }
    public void createReading(SensorsReading reading) {

        class createReading extends AsyncTask<Void, Void, JSONObject> {

            private Context mContext;

            public createReading (Context context){
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


                    JSONObject readingJson= jsonParser.makeHttpRequest(URLs.URL_CREATE_READING, "PUT", params);

                try {
                        if(readingJson.getString("success").equals("1"))
                        {
                            if(reading.getHiperpressionSensors()!=null) {
                                try {

                                    checkWarningCreation(reading.getHiperpressionSensors(), reading, mContext);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }

                            setSr(reading);
                            sensorsReadingArrayList.add(getSr());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                //returning the response
                    return readingJson;

            }
        }

        createReading readingCreation = new createReading(getContext());
        readingCreation.execute();


    }

    private void validateSensorValues(SensorsReading reading, View view, ProgressBar progressBar) throws NoSuchMethodException {

        if(reading!=null ) {

            if(getContext()!=null) {
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

                super.onPostExecute(obj);

            }

            @Override
            protected JSONObject doInBackground(Void... voids) {
                //creating request handler object
                JSONParser jsonParser = new JSONParser();
                //creating request parameters
                ArrayList params = new ArrayList();
                params.add(new BasicNameValuePair("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number())));
                params.add(new BasicNameValuePair("date", statisticsData.getDate()));
                params.add(new BasicNameValuePair("cadence", Float.toString(statisticsData.getCadence())));
                params.add(new BasicNameValuePair("balance", Float.toString(statisticsData.getBalance())));
                params.add(new BasicNameValuePair("steps", Float.toString(statisticsData.getSteps())));
                params.add(new BasicNameValuePair("left_foot_stance", Float.toString(statisticsData.getLeftStanceTime())));
                params.add(new BasicNameValuePair("right_foot_stance", Float.toString(statisticsData.getRightStanceTime())));


                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_CREATE_STATISTIC,"PUT", params);
            }
        }
        createStatistics createStatistics = new createStatistics();
        createStatistics.execute();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if(chronometer!=null)
            chronometer.saveInstanceState(outState);

        outState.putBoolean("warningsLeftToRead", warningsLeftToRead);
        if(getSteps()!=0){
            outState.putLong("steps", getSteps());
            outState.putLong("cadence", getCadence());
            outState.putDouble("pace", getPace());
        }

        if(getLeftStance()!=0)
            outState.putLong("leftStance", getLeftStance());

        if(getRightStance()!=0)
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
            handler.postDelayed(updateStats,1000);
            handler.postDelayed(updateLiveStats,2000);
        }

    }

    @Override
    public void onPause() {
        lastChronoValue= SystemClock.elapsedRealtime() - chronometer.getChrono().getBase();
        super.onPause();
        lastTimerValueOnFragment=System.currentTimeMillis();
    }

    @Override
    public void onResume() {

        recentTimerValueOnFragment=System.currentTimeMillis();
        super.onResume();
        if(chronometer.isRunning()) {

            if(lastChronoValue!=0)
                chronometer.setCurrentTime(recentTimerValueOnFragment - lastTimerValueOnFragment + lastChronoValue);
            chronometer.getChrono().start();
        }
    }

    private void checkWarningCreation(ArrayList<String> hyperpressureSensors, SensorsReading reading, Context context) throws ParseException {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar warningDate = Calendar.getInstance();
        Calendar readingDate = Calendar.getInstance();
        boolean sensorWarningFound=false;
        String sensorsWarning = null;


        setWarningsLeftToRead(true);

        for(int i=0; i<hyperpressureSensors.size(); i++){

            sensorWarningFound=false;

            readingDate.setTime(formatter.parse(reading.getDate()));

            if(warningsArrayList.size()==0){

                warningsArrayList.add(new Warnings(reading.getDate(), hyperpressureSensors.get(i), 1));
            }
            else{
                for(int j=0; j<warningsArrayList.size(); j++){

                    warningDate.setTime(formatter.parse(warningsArrayList.get(j).getWarningTime()));

                    warningDate.add(Calendar.SECOND, SharedPrefManager.getInstance(getContext()).getTimeInterval());

                    if(warningsArrayList.get(j).getSensor().equals(hyperpressureSensors.get(i))){

                        sensorWarningFound=true;
                        if(warningDate.compareTo(readingDate)>0){

                            warningsArrayList.get(j).setCounter( warningsArrayList.get(j).getCounter()+1);


                            if(warningsArrayList.get(j).getCounter()== SharedPrefManager.getInstance(getContext()).getOccurrencesNumber()){


                                if(sensorsWarning!=null)
                                    sensorsWarning=sensorsWarning+warningsArrayList.get(j).getSensor()+",";
                                else
                                    sensorsWarning=warningsArrayList.get(j).getSensor()+",";

                                warningsArrayList.remove(j);
                            }

                        }
                        else{
                            warningsArrayList.remove(j);
                            warningsArrayList.add(new Warnings(reading.getDate(), hyperpressureSensors.get(i), 1));
                        }
                    }
                }
                if(!sensorWarningFound)
                    warningsArrayList.add(new Warnings(reading.getDate(), hyperpressureSensors.get(i), 1));
            }
        }

        if(sensorsWarning!=null){
            sensorsWarning=sensorsWarning.substring(0, sensorsWarning.length()-1);
            warningsFragment.createWarning(sensorsWarning,reading.getDate());
        }
    }

    private void setWarningButtonColor(){

        if(getOptionsMenu().size()!=0 && getContext()!=null){
            Drawable drawable = getOptionsMenu().getItem(2).getIcon();
            if(warningsLeftToRead){

                if(drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getContext().getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }
            }else{
                if(drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getContext().getResources().getColor(R.color.grey_toolbar), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }
}
