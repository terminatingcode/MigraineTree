package com.terminatingcode.android.migrainetree;

import android.content.SharedPreferences;

/**
 * Checks SharedPreferences to see if location has
 * been saved in SharedPreferences
 * Created by Sarah on 6/20/2016.
 */
public class SharedPrefsUtils {

    private SharedPreferences mSharedPreferences;


    public SharedPrefsUtils(SharedPreferences mSharedPreferences){
        this.mSharedPreferences = mSharedPreferences;
    }

    /**
     * checks if SharedPreferences is storing a city
     * If not, returns true
     * @return boolean if not saved
     */
    public boolean needLocationSpecified(){
        final String CITY_SAVED = mSharedPreferences.getString(Constants.LOCATIONUID, Constants.LOCATION_UNDEFINED);
        return CITY_SAVED.equals(Constants.LOCATION_UNDEFINED);
    }

    /**
     * saves selected city to SharedPreferences
     * as Weather Undergrounds unique id for that city
     * @param citySelected the city the user selected from the AutoCompleteTextView
     */
    public void saveSelectedCity(String citySelected, String cityUID) {
        if(cityUID != null && citySelected != null && mSharedPreferences != null){
            mSharedPreferences.edit().putString(Constants.LOCATIONUID, cityUID).apply();
            mSharedPreferences.edit().putString(Constants.LOCATION_NAME, citySelected);
        }
    }

}
