package com.example.mycareshoe.data.model;

import java.io.Serializable;


public class User implements Serializable {

    private int userId;
    private String username;
    private int profile_id;
    private String email;
    private String password;
    private int patient_number;


    public User(int userId, String username, int profile_id, String email, String password, int patient_number) {
        this.userId = userId;
        this.username = username;
        this.profile_id = profile_id;
        this.email = email;
        this.password = password;
        this.patient_number = patient_number;
    }

    public User() {
        this.userId = -1;
        this.username = null;
        this.profile_id = -1;
        this.email = null;
        this.password = null;
        this.patient_number = -1;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPatient_number() {
        return patient_number;
    }

    public void setPatient_number(int patient_number) {
        this.patient_number = patient_number;
    }
}