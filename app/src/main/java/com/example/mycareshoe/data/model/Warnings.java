package com.example.mycareshoe.data.model;

import java.util.Date;

public class Warnings {

    private Date warningTime;
    private SensorsReading sensorsReading;
    private String duration;
    private static int counter=0;


    public Warnings() {

    }

    public Warnings(Date warningTime, SensorsReading sensorsReading, String duration) {
        this.warningTime = warningTime;
        this.sensorsReading = sensorsReading;
        this.duration = duration;
        counter++;
    }

    public Date getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(Date warningTime) {
        this.warningTime = warningTime;
    }

    public SensorsReading getSensors() {
        return sensorsReading;
    }

    public void setSensors(SensorsReading sensors) {
        this.sensorsReading = sensorsReading;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public static int getNumOfWarnings() {
        return counter;
    }
}
