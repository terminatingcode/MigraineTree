package com.terminatingcode.android.migrainetree;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper to hold a map containing Unique IDs for
 * Weather Underground queries
 * Created by Sarah on 6/21/2016.
 */
public class CitiesMapWrapper {
    private Map<String, String> citiesUID;

    public CitiesMapWrapper(){
        citiesUID = new HashMap<>();
    }

    public Map getCitiesUID(){
        return citiesUID;
    }

    public void put(String city, String uid){
        if(!citiesUID.containsKey(city)) citiesUID.put(city, uid);
    }

    public String getUID(String city){
        return citiesUID.get(city);
    }
}
