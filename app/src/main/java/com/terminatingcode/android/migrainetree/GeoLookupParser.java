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

    private CitiesMapSingleton mCitiesMapSingleton = CitiesMapSingleton.newInstance();

    public String[] parse(JSONObject json) throws JSONException {
        if (json == null) return new String[]{"null JSONObject"};
        JSONObject response = json.getJSONObject("response");
        if(json.has("location")){
            String[] location = new String[1];
            location[0] = parseCity(json.getJSONObject("location"));
            return location;
        }else if(response.has("results")) {
            return extractResults(response.getJSONArray("results"));
        }
        else return new String[]{"..."};
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
        StringBuilder sb = new StringBuilder("");
        try {
            String city = json.getString("city");
            String state = json.getString("state");
            String country = json.getString("country_name");
            sb.append(city).append(" ")
                    .append(state).append(" ")
                    .append(country);
            String uid = getUID(json);
            putUID(sb.toString(), uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public String getUID(JSONObject json) throws JSONException {
        return json.getString("l");
    }

    public void putUID(String city, String uid){
        mCitiesMapSingleton.put(city, uid);
    }
}
