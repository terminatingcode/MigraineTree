package com.terminatingcode.android.migrainetree;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper to hold a Map containing Unique IDs for
 * Weather Underground queries
 * Created by Sarah on 6/21/2016.
 */
public class CitiesMapSingleton
{
    private static CitiesMapSingleton instance;
    private static  Map<String, String> citiesUID;

    private CitiesMapSingleton(){
        citiesUID = new HashMap<>();
    }

    public static synchronized CitiesMapSingleton newInstance(){
        if(instance == null){
            instance = new CitiesMapSingleton();
        }
        return instance;
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

    public void clearMap(){
        citiesUID.clear();
    }
}
