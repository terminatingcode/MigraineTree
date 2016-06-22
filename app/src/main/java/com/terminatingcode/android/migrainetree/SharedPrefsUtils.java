package com.terminatingcode.android.migrainetree;

import android.content.SharedPreferences;

/**
 * Checks SharedPreferences to see if location has
 * been saved in SharedPreferences
 * Created by Sarah on 6/20/2016.
 */
public class SharedPrefsUtils {

    private SharedPreferences mSharedPreferences;
    private static final String LOCATION = "location";


    public SharedPrefsUtils(SharedPreferences mSharedPreferences){
        this.mSharedPreferences = mSharedPreferences;
    }

    /**
     * checks if SharedPreferences is storing a city
     * If not, returns true
     * @return boolean if not saved
     */
    public boolean needLocationSpecified(){
        final String CITY_NOT_SAVED = "city not saved";
        final String CITY_SAVED = mSharedPreferences.getString(LOCATION, CITY_NOT_SAVED);
        return CITY_SAVED.equals(CITY_NOT_SAVED);
    }

    /**
     * saves selected city to SharedPreferences
     * as Weather Undergrounds unique id for that city
     * @param citySelected the city the user selected from the AutoCompleteTextView
     */
    public void saveSelectedCity(String citySelected, String cityUID) {
        if(cityUID != null && citySelected != null && mSharedPreferences != null){
            mSharedPreferences.edit().putString(LOCATION, cityUID).apply();
        }
    }

}
