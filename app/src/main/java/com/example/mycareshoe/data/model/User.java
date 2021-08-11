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
    private String gender;
    private String birth;
    private int height;
    private int weight;
    private int feetSize;
    private String diabetesStatus;
    private String feetType;
    private String name;

    public User(int userId, String username, int profile_id, String email, String password, int patient_number) {
        this.userId = userId;
        this.username = username;
        this.profile_id = profile_id;
        this.email = email;
        this.password = password;
        this.patient_number = patient_number;
    }

    public User(String gender, String birth, int height, int weight, int feetSize, String diabetesStatus, String feetType, String name) {
        this.gender = gender;
        this.birth = birth;
        this.height = height;
        this.weight = weight;
        this.feetSize = feetSize;
        this.diabetesStatus = diabetesStatus;
        this.feetType = feetType;
        this.name = name;
    }

    public User(int userId, String username, int profile_id, String email, String password, int patient_number, String gender, String birth, int height, int weight, int feetSize, String diabetesStatus, String feetType, String name) {
        this.userId = userId;
        this.username = username;
        this.profile_id = profile_id;
        this.email = email;
        this.password = password;
        this.patient_number = patient_number;
        this.gender = gender;
        this.birth = birth;
        this.height = height;
        this.weight = weight;
        this.feetSize = feetSize;
        this.diabetesStatus = diabetesStatus;
        this.feetType = feetType;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getFeetSize() {
        return feetSize;
    }

    public String getDiabetesStatus() {
        return diabetesStatus;
    }

    public String getFeetType() {
        return feetType;
    }

    public String getName() {
        return name;
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