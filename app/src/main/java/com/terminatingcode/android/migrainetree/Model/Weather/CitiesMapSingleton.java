package com.terminatingcode.android.migrainetree.model.weather;

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
        if(city != null && uid != null) citiesUID.put(city, uid);
    }

    public String getUID(String city){
        String missing = "city not mapped";
        String uid =  citiesUID.get(city);
        return (uid == null? missing: uid);
    }

    public void clearMap(){
        citiesUID.clear();
    }
}
