package com.example.mycareshoe.data;

import java.util.Date;

public class Warnings {

    private Date warningTime;
    private String[] sensors;
    private String duration;
    private static int counter=0;


    public Warnings() {

    }

    public Warnings(Date warningTime, String[] sensors, String duration) {
        this.warningTime = warningTime;
        this.sensors = sensors;
        this.duration = duration;
        counter++;
    }

    public Date getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(Date warningTime) {
        this.warningTime = warningTime;
    }

    public String[] getSensors() {
        return sensors;
    }

    public void setSensors(String[] sensors) {
        this.sensors = sensors;
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
