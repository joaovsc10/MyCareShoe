package com.example.mycareshoe.data.model;

import java.io.Serializable;

public class Patient extends User implements Serializable {

    private String gender;
    private String birth;
    private double height;
    private double weight;
    private int feetSize;
    private String diabetesStatus;
    private String feetType;
    private String name;
    private double strideLength;
    private int pressureThreshold;
    private int occurencesNumber;
    private int timeInterval;

    public Patient(int userId, String username, int profile_id, String email, String password, int patient_number) {

        super(userId, username, profile_id, email, password, patient_number);
    }

    public Patient(int userId, String username, int profile_id, String email, String password, int patient_number, String gender, String birth, double height, double weight, int feetSize, String diabetesStatus, String feetType, String name, int pressureThreshold, int occurencesNumber, int timeInterval) {

        super(userId, username, profile_id, email, password, patient_number);
        this.gender = gender;
        this.birth = birth;
        setHeight(height);
        this.weight = weight;
        this.feetSize = feetSize;
        this.diabetesStatus = diabetesStatus;
        this.feetType = feetType;
        this.name = name;
        this.pressureThreshold = pressureThreshold;
        this.occurencesNumber = occurencesNumber;
        this.timeInterval = timeInterval;
    }

    public Patient(String gender, String birth, double height, double weight, int feetSize, String diabetesStatus, String feetType, String name, int pressureThreshold, int occurencesNumber, int timeInterval) {

        this.gender = gender;
        this.birth = birth;
        setHeight(height);
        this.weight = weight;
        this.feetSize = feetSize;
        this.diabetesStatus = diabetesStatus;
        this.feetType = feetType;
        this.name = name;
        this.pressureThreshold = pressureThreshold;
        this.occurencesNumber = occurencesNumber;
        this.timeInterval = timeInterval;
    }

    public double getStrideLength() {
        if (getHeight() > 0 && getGender() != null) {
            if (getGender().equals("Male")) {
                setStrideLength(getHeight() * 100 * 0.415);
            } else {
                setStrideLength(getHeight() * 100 * 0.413);
            }
        } else {
            setStrideLength(165 * 0.414);
        }
        return strideLength;
    }

    public int getOccurencesNumber() {
        return occurencesNumber;
    }

    public void setOccurencesNumber(int occurencesNumber) {
        this.occurencesNumber = occurencesNumber;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void setPressureThreshold(int pressureThreshold) {
        this.pressureThreshold = pressureThreshold;
    }

    public int getPressureThreshold() {
        return pressureThreshold;
    }

    public void setStrideLength(double strideLength) {
        this.strideLength = strideLength;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        if (getHeight() > 0 && getGender() != null) {
            if (getGender().equals("Male")) {
                setStrideLength(getHeight() * 100 * 0.415);
            } else {
                setStrideLength(getHeight() * 100 * 0.413);
            }
        }
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getFeetSize() {
        return feetSize;
    }

    public void setFeetSize(int feetSize) {
        this.feetSize = feetSize;
    }

    public String getDiabetesStatus() {
        return diabetesStatus;
    }

    public void setDiabetesStatus(String diabetesStatus) {
        this.diabetesStatus = diabetesStatus;
    }

    public String getFeetType() {
        return feetType;
    }

    public void setFeetType(String feetType) {
        this.feetType = feetType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
