package com.example.mycareshoe.ui.statistics;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.SensorsReading;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class PressurePlotStatisticsFragment extends StatisticsHelperFragment {


    private GraphView linegraphPressureLeft;
    private GraphView linegraphPressureRight;
    private EditText startDate;
    private EditText endDate;
    private String startDateString;
    private String endDateString;
    private DatePickerDialog picker;
    private ImageButton searchBtn;
    private ProgressBar progressBar;
    private ArrayList<Integer> xLeftValues;
    private ArrayList<Integer> xRightValues;
    private ArrayList<Integer> yLeftValues;
    private ArrayList<Integer> yRightValues;
    private TreeMap<String, Integer> meanSensorValuesLeft= new TreeMap<String, Integer>();
    private TreeMap<String, Integer> meanSensorValuesRight= new TreeMap<String, Integer>();
    private DataPoint[] dataPoints;
    private DataPoint[] dataPointsLeft;
    private DataPoint[] dataPointsRight;

    public DataPoint[] getDataPointsLeft() {
        return dataPointsLeft;
    }

    public void setDataPointsLeft(DataPoint[] dataPointsLeft) {
        this.dataPointsLeft = dataPointsLeft;
    }

    public DataPoint[] getDataPointsRight() {
        return dataPointsRight;
    }

    public void setDataPointsRight(DataPoint[] dataPointsRight) {
        this.dataPointsRight = dataPointsRight;
    }

    public ArrayList<Integer> getxLeftValues() {
        return xLeftValues;
    }

    public void setxLeftValues(ArrayList<Integer> xLeftValues) {
        this.xLeftValues = xLeftValues;
    }

    public ArrayList<Integer> getxRightValues() {
        return xRightValues;
    }

    public void setxRightValues(ArrayList<Integer> xRightValues) {
        this.xRightValues = xRightValues;
    }

    public ArrayList<Integer> getyLeftValues() {
        return yLeftValues;
    }

    public void setyLeftValues(ArrayList<Integer> yLeftValues) {
        this.yLeftValues = yLeftValues;
    }

    public ArrayList<Integer> getyRightValues() {
        return yRightValues;
    }

    public void setyRightValues(ArrayList<Integer> yRightValues) {
        this.yRightValues = yRightValues;
    }

    public TreeMap<String, Integer> getMeanSensorValuesLeft() {
        return meanSensorValuesLeft;
    }

    public void setMeanSensorValuesLeft(TreeMap<String, Integer> meanSensorValuesLeft) {
        this.meanSensorValuesLeft = meanSensorValuesLeft;
    }

    public TreeMap<String, Integer> getMeanSensorValuesRight() {
        return meanSensorValuesRight;
    }

    public void setMeanSensorValuesRight(TreeMap<String, Integer> meanSensorValuesRight) {
        this.meanSensorValuesRight = meanSensorValuesRight;
    }

    public void setDataPoints(DataPoint[] dataPoints) {
        this.dataPoints = dataPoints;
    }

    public DataPoint[] getDataPoints() {
        return dataPoints;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pressure_plots, parent, false);


    }



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.plot_statistics_name_en));


        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        linegraphPressureLeft = (GraphView) view.findViewById(R.id.line_graph);
        linegraphPressureRight = (GraphView) view.findViewById(R.id.line_graph2);

        startDate = view.findViewById(R.id.start_date_text);
        endDate = view.findViewById(R.id.end_date_text);
        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        searchBtn = view.findViewById(R.id.search_btn);
        searchBtn.setEnabled(false);

        setDateTextClickListener(startDate, "start", searchBtn);
        setDateTextClickListener(endDate,"end", searchBtn);


        if (savedInstanceState != null) {

            setxLeftValues(savedInstanceState.getIntegerArrayList("xLeftValues"));
            setyLeftValues(savedInstanceState.getIntegerArrayList("yLeftValues"));
            setxRightValues(savedInstanceState.getIntegerArrayList("xRightValues"));
            setyRightValues(savedInstanceState.getIntegerArrayList("yRightValues"));
            setStartDateString(savedInstanceState.getString("startDate"));
            setEndDateString(savedInstanceState.getString("endDate"));

        }
        if(getyLeftValues()!=null && getyRightValues()!=null) {
            try {
                setPressureGraph(linegraphPressureLeft, meanSensorValuesLeft, "L", getStartDateString().equals(getEndDateString()));
                setPressureGraph(linegraphPressureRight, meanSensorValuesRight, "R", getStartDateString().equals(getEndDateString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        searchBtn.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {

                removeGraphData(false);
                readSensorsEntry(getStartDateString(), getEndDateString());

            }
        });

    }



    private void readSensorsEntry(String startDateString, String endDateString) {
        class readSensorsEntry extends AsyncTask<Void, Void, JSONObject> {


            @Override
            protected void onPreExecute() {

                meanSensorValuesRight.clear();
                meanSensorValuesLeft.clear();
                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject objs) {

                try {
                    JSONArray array= objs.getJSONArray("records");
                    int numWarnings=0;
                    for(int i=0; i<array.length();i++){

                        JsonElement mJson =  JsonParser.parseString(String.valueOf(array.getJSONObject(i)));
                        Gson gson = new Gson();
                        SensorsReading object = gson.fromJson(mJson, SensorsReading.class);

                        if(object!=null) {
                            meanSensorValuesLeft.put(object.getDate(), object.getLeftFootSensorsMean());
                            meanSensorValuesRight.put(object.getDate(), object.getRightFootSensorsMean());
                            if (object.getHiperpressionSensors()!=null) {
                                numWarnings++;
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    if(meanSensorValuesLeft.size()==0 && meanSensorValuesRight.size()==0 && getyLeftValues()==null && getyRightValues()==null){

                            removeGraphData(true);
                    }else{
                        try {
                            setPressureGraph(linegraphPressureLeft,meanSensorValuesLeft, "L", startDateString.equals(endDateString));
                            setPressureGraph(linegraphPressureRight,meanSensorValuesRight, "R", startDateString.equals(endDateString));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }




                progressBar.setVisibility(View.GONE);

                super.onPostExecute(objs);

            }


            @Override
            protected JSONObject doInBackground(Void... voids) {

                return getReadings("sensorsReading", startDateString, endDateString);
            }
        }
        readSensorsEntry warnings = new readSensorsEntry();
        warnings.execute();
    }

    private void removeGraphData(boolean showNoReadingsMessage){

        if(linegraphPressureLeft!=null)
            linegraphPressureLeft.removeAllSeries();

        if(linegraphPressureRight!=null)
            linegraphPressureRight.removeAllSeries();
        if(showNoReadingsMessage) {
            linegraphPressureRight.setTitle(getResources().getString(R.string.empty_graph_en));
            linegraphPressureLeft.setTitle(getResources().getString(R.string.empty_graph_en));
            Toast.makeText(getActivity(), getResources().getString(R.string.no_records_message_en), Toast.LENGTH_SHORT).show();
        }
    }
    private void setPressureGraph(GraphView linegraph, TreeMap<String, Integer> meanSensorValues, String foot, boolean oneDay) throws ParseException {

        Date date;
        int hour, dataPointPos = 0;

        ArrayList<Integer> valueList;
        ArrayList<String> keyList = null;

        if (getyLeftValues() != null && getyRightValues() != null){

            if (foot.equals("L"))
                valueList = getyLeftValues();
            else
                valueList = getyRightValues();
    }else {
            valueList = new ArrayList<Integer>(meanSensorValues.values());
            keyList = new ArrayList<String>(meanSensorValues.keySet());
        }
        TreeMap<Integer,Integer> sensorValueByHour= new TreeMap<>();
        GridLabelRenderer gridLabel = linegraph.getGridLabelRenderer();

        if(!oneDay) {
            dataPoints= new DataPoint[valueList.size()];
            for (int i = 0; i < valueList.size(); i++) {

                dataPoints[i] = new DataPoint(i, valueList.get(i));

            }
            gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.sensor_reading_xtitle_en));
        }else{

            dataPoints= new DataPoint[23];
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance


            for(int j=0; j<23;j++){
                if(keyList==null){
                    sensorValueByHour.put(j, valueList.get(j));
                }else {
                    sensorValueByHour.put(j, 0);
                }
            }

            if(keyList!=null) {
                for (int i = 0; i < valueList.size(); i++) {


                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(keyList.get(i));
                    calendar.setTime(date);

                    hour = calendar.get(Calendar.HOUR_OF_DAY);

                    sensorValueByHour.replace(hour, (sensorValueByHour.get(hour) + valueList.get(i)) / 2);

                    //     dataPoints[i]= new DataPoint(i, meanSensorValues.get(meanSensorValues.keySet().toArray()[i]));

                }
            }

            for (TreeMap.Entry<Integer, Integer> entry : sensorValueByHour.entrySet()) {

                dataPoints[dataPointPos]= new DataPoint(entry.getKey(), entry.getValue());
                dataPointPos++;
            }

            gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.hours_of_day_xtitle_en));
        }

        //            linegraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));

        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<DataPoint>(dataPoints);
        linegraph.getViewport().setXAxisBoundsManual(true);
        linegraph.getGridLabelRenderer().setHorizontalLabelsAngle(170);
        linegraph.getViewport().setScrollable(true);
        linegraph.getViewport().setMaxX(dataPoints.length);

        //          gridLabel.setVerticalAxisTitle(getResources().getString(R.string.pressure_ytitle_en));


        if(foot.equals("L")){
            setDataPointsLeft(dataPoints);
            linegraph.setTitle(getResources().getString(R.string.mean_pressure_left_en));}
        else{
            setDataPointsRight(dataPoints);
            linegraph.setTitle(getResources().getString(R.string.mean_pressure_right_en));}
        //     linegraph.getViewport().setMinX(0);
        //  linegraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        linegraph.addSeries(lineSeries);



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if(getDataPoints()!=null) {
            xLeftValues = new ArrayList<Integer>();
            yLeftValues = new ArrayList<Integer>();
            xRightValues = new ArrayList<Integer>();
            yRightValues = new ArrayList<Integer>();

            for (int i = 0; i < getDataPoints().length; i++) {

                yLeftValues.add((int) getDataPointsLeft()[i].getY());
                xLeftValues.add((int) getDataPointsLeft()[i].getX());
                xRightValues.add((int) getDataPointsRight()[i].getX());
                yRightValues.add((int) getDataPointsRight()[i].getY());
            }


            outState.putIntegerArrayList("xLeftValues", xLeftValues);
            outState.putIntegerArrayList("yLeftValues", yLeftValues);
            outState.putIntegerArrayList("xRightValues", xRightValues);
            outState.putIntegerArrayList("yRightValues", yRightValues);
            outState.putString("startDate", getStartDateString());
            outState.putString("endDate", getEndDateString());
        }
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            setxLeftValues(savedInstanceState.getIntegerArrayList("xLeftValues"));
            setyLeftValues(savedInstanceState.getIntegerArrayList("yLeftValues"));
            setxRightValues(savedInstanceState.getIntegerArrayList("xRightValues"));
            setyRightValues(savedInstanceState.getIntegerArrayList("yRightValues"));
            setStartDateString(savedInstanceState.getString("startDate"));
            setEndDateString(savedInstanceState.getString("endDate"));
        }

    }



}