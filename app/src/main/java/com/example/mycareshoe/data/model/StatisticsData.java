package com.example.mycareshoe.data.model;

import android.content.Context;

import com.example.mycareshoe.helpers.SharedPrefManager;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StatisticsData {

    private String date;
    private int statsId;
    private long cadence;
    private long balance;
    private long steps;
    private double pace;
    private long leftStanceTime;
    private long rightStanceTime;
    private ArrayList<SensorsReading> readings;

    public StatisticsData(String date, int statsId, ArrayList<SensorsReading> readings, Context context) {

        this.date = date;
        this.statsId = statsId;
        setLiveStats(readings, context);
        this.readings = readings;
    }

    public Map<String, Long> getStatInfo() {
        Map<String, Long> statInfo = new HashMap<String, Long>() {{
            put("cadence", cadence);
            put("balance", balance);
            put("steps", steps);
        }};

        return statInfo;
    }

    public long getLeftStanceTime() {
        return leftStanceTime;
    }

    public void setLeftStanceTime(long leftStanceTime) {
        this.leftStanceTime = leftStanceTime;
    }

    public long getRightStanceTime() {
        return rightStanceTime;
    }

    public void setRightStanceTime(long rightStanceTime) {
        this.rightStanceTime = rightStanceTime;
    }

    public HashMap<String, String> checkStanceTime(HashMap<String, String> stanceDataMap) throws ParseException {

        if (stanceDataMap.containsKey("heelStrike") && stanceDataMap.containsKey("toeOff")) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            java.util.Date heelStrike = dateFormat.parse(stanceDataMap.get("heelStrike"));

            java.util.Date toeOff = dateFormat.parse(stanceDataMap.get("toeOff"));

            Long stanceTime = TimeUnit.MILLISECONDS.toSeconds(toeOff.getTime() - heelStrike.getTime());

            if (stanceTime < 15) {
                stanceDataMap.put("stanceTime", String.valueOf(stanceTime));
            }
        }

        return stanceDataMap;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatsId() {
        return statsId;
    }

    public void setStatsId(int statsId) {
        this.statsId = statsId;
    }

    public long getCadence() {
        return cadence;
    }

    public void setCadence(long cadence) {
        this.cadence = cadence;
    }

    public long getBalance() {
        return balance;
    }

    public void setLiveStats(ArrayList<SensorsReading> readings, Context context) {
        long sensorsSum = 0;
        long sensorsLeftSum = 0;
        int stepsCounter = 0;

        for (int i = 0; i < readings.size(); i++) {
            sensorsSum = sensorsSum + readings.get(i).getLeftFootSensorsSum() + readings.get(i).getRightFootSensorsSum();
            sensorsLeftSum = readings.get(i).getLeftFootSensorsSum() + sensorsLeftSum;


            if (readings.get(i).getRightFootSensorsSum() == 0 && readings.get(i).getLeftFootSensorsSum() > 0)
                stepsCounter++;

        }
        if (sensorsSum == 0) {
            setBalance(0);
        } else {
            setBalance((sensorsLeftSum * 100) / sensorsSum);
        }
        setCadence(stepsCounter * 60);
        setSteps(getSteps() + stepsCounter);


        if (stepsCounter > 0)
            setPace(round((100000 / (stepsCounter * SharedPrefManager.getInstance(context).getStrideLength())) / 60));
        else
            setPace(0);
    }

    public long getSteps() {
        return steps;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public ArrayList<SensorsReading> getReadings() {
        return readings;
    }

    public void setReadings(ArrayList<SensorsReading> readings) {
        this.readings = readings;
    }

    public static double round(double value) {
        DecimalFormat df = new DecimalFormat("#,##");
        value = Double.valueOf(df.format(value));

        return value;
    }
}
