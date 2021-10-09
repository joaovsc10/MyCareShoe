package com.example.mycareshoe.service;

import java.util.UUID;

public class URLs {

    private static final String ROOT_URL = "http://192.168.0.104/mycareshoeapi/";

    public static final String URL_LOGIN = ROOT_URL + "user/login.php";

    public static final String URL_READ_PATIENT_INFO = ROOT_URL + "patient/patient_search.php";

    public static final String URL_UPDATE_PATIENT_INFO = ROOT_URL + "patient/update_patient_info.php";

    public static final String URL_UPDATE_USER_INFO = ROOT_URL + "user/update_user_info.php";

    public static final String URL_GET_WARNINGS = ROOT_URL + "warnings/warnings_search.php";

    public static final String URL_CREATE_READING = ROOT_URL + "readings/create_reading.php";

    public static final String URL_CREATE_WARNING = ROOT_URL + "warnings/create_warning.php";

    public static final String URL_CREATE_STATISTIC = ROOT_URL + "statistics/create_statistics.php";

    public static final String URL_CREATE_USER = ROOT_URL + "user/registration.php";

    public static final String URL_SEARCH_DATE = ROOT_URL + "search.php";

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static final UUID MY_UUID2 = UUID.fromString("0000111f-0000-1000-8000-00805f9b34fb");

}