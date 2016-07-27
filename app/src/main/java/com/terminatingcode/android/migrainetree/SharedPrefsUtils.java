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
            mSharedPreferences.edit().putString(Constants.LOCATION_NAME, citySelected).apply();
        }
    }

    /**
     * saves user preference to save menstrual data used in the calendar
     * @param preference true if saved, false if not
     */
    public void saveMenstrualDataPrefs(boolean preference){
        mSharedPreferences.edit().putBoolean(Constants.SAVE_MENSTRUAL_DATA, preference).apply();
    }

    /**
     * Returns city saved by user previously in SharedPreferences
     * @return city saved
     */
    public String getSavedCity(){
        return mSharedPreferences.getString(Constants.LOCATION_NAME, Constants.CITY_NOT_SET);
    }

    /**
     * Returns location uid used for Weather Underground http calls
     * @return UID
     */
    public String getSavedLocationUID(){
        return mSharedPreferences.getString(Constants.LOCATIONUID, Constants.CITY_NOT_SET);
    }

    /**
     * returns the user preference to save menstrual data
     * default is true
     * @return the user preference
     */
    public boolean getsavedMenstrualPref(){
        return mSharedPreferences.getBoolean(Constants.SAVE_MENSTRUAL_DATA,
                Constants.DEFAULT_SAVE_MENSTRUAL_DATA);
    }
}
