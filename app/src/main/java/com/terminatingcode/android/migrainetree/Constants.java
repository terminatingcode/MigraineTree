package com.terminatingcode.android.migrainetree;

/**
 * Interface to hold constant values
 * Created by Sarah on 6/27/2016.
 */
public interface Constants {
    String PREFERENCES_FILE_KEY = "com.terminatingcode.migrainetree.PREFERENCE_FILE_KEY";
    String LOCATIONUID = "location";
    String LOCATION_NAME = "locationName";
    String LOCATION_UNDEFINED = "locationUndefined";
    String CITY_NOT_SET = "city not set";
    String DATE_KEY = "dateKey";
    String SAVE_MENSTRUAL_DATA = "menstrualData";
    boolean DEFAULT_SAVE_MENSTRUAL_DATA = true;
    int DEFAULT_NO_DATA = 0;
    String INSERTED_URI = "insertedUri";
    String START_HOUR = "startHour";
    int NOTIFICATION_ID = 0;

    String NETWORK_PREFIX = "network.";

    String NETWORK_WIFI_ONLY = NETWORK_PREFIX + "wifiOnly";
}
