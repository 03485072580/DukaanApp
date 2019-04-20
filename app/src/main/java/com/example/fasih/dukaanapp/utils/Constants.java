package com.example.fasih.dukaanapp.utils;

/**
 * Created by Fasih on 01/11/19.
 */

public class Constants {
    public static final float GEOFENCE_RADIUS_IN_METERS = 200 ;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 24000 * 60 * 60;

    public static final int SUCCESS_RESULT = 1;
    public static final int FAILURE_RESULT = 0;
    public static final String RESULT_CODE = "200";

    public static final String PACKAGE_NAME = "com.example.fasih.dukaanapp";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    public static String scope = "user";
}
