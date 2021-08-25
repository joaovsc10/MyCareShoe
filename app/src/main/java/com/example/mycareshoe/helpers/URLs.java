package com.example.mycareshoe.helpers;

import java.util.UUID;

public class URLs {

    private static final String ROOT_URL = "http://192.168.0.107/mycareshoe/";

    public static final String URL_LOGIN= ROOT_URL + "login.php";

    public static final String URL_READ_PATIENT_INFO= ROOT_URL + "patient_search.php";

    public static final String URL_UPDATE_PATIENT_INFO= ROOT_URL + "update_patient_info.php";

    public static final String URL_GET_WARNINGS= ROOT_URL + "warnings_search.php";

    public static final String URL_GET_SENSOR_READING= ROOT_URL + "read_one_reading.php";

    public static final String URL_CREATE_READING= ROOT_URL + "create_reading.php";

    public static final String URL_CREATE_WARNING= ROOT_URL + "create_warning.php";

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static final UUID MY_UUID2 = UUID.fromString("0000111f-0000-1000-8000-00805f9b34fb");

}