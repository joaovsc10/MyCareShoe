package com.example.mycareshoe.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mycareshoe.data.model.User;
import com.example.mycareshoe.ui.login.LoginActivity;

import static android.accounts.AccountManager.KEY_PASSWORD;

/**
 * Created by Belal on 9/5/2017.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "sharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";
    private static final String KEY_PROFILE_ID = "keyprofileid";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_PATIENT_NUMBER = "keypatientnumber";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_BIRTH = "keybirth";
    private static final String KEY_HEIGHT = "keyheight";
    private static final String KEY_WEIGHT = "keyweight";
    private static final String KEY_FEET_SIZE = "keyfeetsize";
    private static final String KEY_DIABETES = "keydiabetes";
    private static final String KEY_FEET_TYPE = "keytype";
    private static final String KEY_NAME = "keyname";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getUserId());
        editor.putString(KEY_USERNAME, user.getDisplayName());
        editor.putInt(KEY_PROFILE_ID, user.getProfile_id());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putInt(KEY_PATIENT_NUMBER, user.getPatient_number());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.apply();
    }

    public void updatePersonalInfo(User user){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_BIRTH, user.getBirth());
        editor.putInt(KEY_HEIGHT, user.getHeight());
        editor.putInt(KEY_WEIGHT, user.getWeight());
        editor.putInt(KEY_FEET_SIZE, user.getFeetSize());
        editor.putString(KEY_DIABETES, user.getDiabetesStatus());
        editor.putString(KEY_FEET_TYPE, user.getFeetType());
        editor.putString(KEY_NAME, user.getName());
        editor.apply();

        System.out.println(sharedPreferences.getAll());
    }


    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser(boolean personalInfo) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if(personalInfo) {
            System.out.println(sharedPreferences.getAll());
            return new User(
                    sharedPreferences.getInt(KEY_ID, -1),
                    sharedPreferences.getString(KEY_USERNAME, null),
                    sharedPreferences.getInt(KEY_PROFILE_ID, -1),
                    sharedPreferences.getString(KEY_EMAIL, null),
                    sharedPreferences.getString(KEY_PASSWORD, null),
                    sharedPreferences.getInt(KEY_PATIENT_NUMBER, -1),
                    sharedPreferences.getString(KEY_GENDER, null),
                    sharedPreferences.getString(KEY_BIRTH, null),
                    sharedPreferences.getInt(KEY_HEIGHT, -1),
                    sharedPreferences.getInt(KEY_WEIGHT, -1),
                    sharedPreferences.getInt(KEY_FEET_SIZE, -1),
                    sharedPreferences.getString(KEY_DIABETES, null),
                    sharedPreferences.getString(KEY_FEET_TYPE, null),
                    sharedPreferences.getString(KEY_NAME, null)
            );
        }else{
            return new User(
                    sharedPreferences.getInt(KEY_ID, -1),
                    sharedPreferences.getString(KEY_USERNAME, null),
                    sharedPreferences.getInt(KEY_PROFILE_ID, -1),
                    sharedPreferences.getString(KEY_EMAIL, null),
                    sharedPreferences.getString(KEY_PASSWORD, null),
                    sharedPreferences.getInt(KEY_PATIENT_NUMBER, -1)
            );
        }
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
