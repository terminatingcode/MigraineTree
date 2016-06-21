package com.terminatingcode.android.migrainetree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the json returned when looking up a location
 * results may be multiple cities, just one city, or none
 * Created by Sarah on 6/16/2016.
 */
public class GeoLookupParser {

    public String[] parse(JSONObject json) throws JSONException {
        if(json == null) return new String[]{"null JSONObject"};
        else if(json.has("results")) {
            return extractResults(json.getJSONArray("results"));
        }else if(json.has("location")){
            String[] location = new String[1];
            location[0] = parseCity(json.getJSONObject("location"));
            return location;
        }
        else return new String[]{"error"};
    }

    private String[] extractResults(JSONArray array){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++){
            try {
                JSONObject json = array.getJSONObject(i);
                String city = parseCity(json);
                list.add(city);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String[] cities = new String[list.size()];
        return list.toArray(cities);
    }

    private String parseCity(JSONObject json){
        String city = "";
        String state = "";
        String country = "";
        try {
            city = json.getString("city");
            state = json.getString("state");
            country = json.getString("country_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city + " " + state + " " + country;

    }
}
