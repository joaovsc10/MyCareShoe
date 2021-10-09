package com.example.mycareshoe.model;


public class Warnings {

    private String warningTime;
    private String sensor;
    private int counter;


    public Warnings() {

    }

    public Warnings(String warningTime, String sensor, int counter) {
        this.warningTime = warningTime;
        this.sensor = sensor;
        this.counter = counter;
    }

    public String getSensor() {
        return sensor;
    }

    public String getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(String warningTime) {
        this.warningTime = warningTime;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
