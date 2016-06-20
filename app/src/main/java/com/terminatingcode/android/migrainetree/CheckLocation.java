package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Checks SharedPreferences to see if location has
 * been saved in SharedPreferences
 * Created by Sarah on 6/20/2016.
 */
public class CheckLocation {
    private final String APP_NAME = "com.terminatingcode.migrainetree";

    /**
     * checks if SharedPreferences is storing a city
     * If not, returns true
     * @param context the Activity Context
     * @return boolean if not saved
     */
    public boolean needLocationSpecified(Context context){
        final SharedPreferences mSharedPreferences = context
                .getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        final String LOCATION = "location";
        final String CITY_NOT_SAVED = "city not saved";
        final String CITY_SAVED = mSharedPreferences.getString(LOCATION, CITY_NOT_SAVED);

        return CITY_SAVED.equals(CITY_NOT_SAVED);
    }

}
