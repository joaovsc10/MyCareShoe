package com.example.mycareshoe.data.model;

import java.io.Serializable;

public class Patient extends User implements Serializable {

    private String gender;
    private String birth;
    private int height;
    private int weight;
    private int feetSize;
    private String diabetesStatus;
    private String feetType;
    private String name;


    public Patient ( int userId, String username, int profile_id, String email, String password, int patient_number){

        super(userId, username, profile_id, email, password, patient_number);
    }

    public Patient(int userId, String username, int profile_id, String email, String password, int patient_number, String gender, String birth, int height, int weight, int feetSize, String diabetesStatus, String feetType, String name) {

        super(userId, username, profile_id, email, password, patient_number);
        this.gender = gender;
        this.birth = birth;
        this.height = height;
        this.weight = weight;
        this.feetSize = feetSize;
        this.diabetesStatus = diabetesStatus;
        this.feetType = feetType;
        this.name = name;
    }

    public Patient(String gender, String birth, int height, int weight, int feetSize, String diabetesStatus, String feetType, String name) {

        this.gender = gender;
        this.birth = birth;
        this.height = height;
        this.weight = weight;
        this.feetSize = feetSize;
        this.diabetesStatus = diabetesStatus;
        this.feetType = feetType;
        this.name = name;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
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
