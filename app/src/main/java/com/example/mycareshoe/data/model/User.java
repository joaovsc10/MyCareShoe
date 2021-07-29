package com.example.mycareshoe.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {

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

    public int getProfile_id() {
        return profile_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPatient_number() {
        return patient_number;
    }

    public int getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return username;
    }
}