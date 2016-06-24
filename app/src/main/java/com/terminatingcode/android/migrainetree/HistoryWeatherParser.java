package com.terminatingcode.android.migrainetree;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Parses Historical Weather data from Weather Underground
 * Created by Sarah on 6/23/2016.
 */
public class HistoryWeatherParser {

    public Weather24Hour parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        return null;
    }

    public WeatherHour parseHour(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        return null;
    }

    public Date parseDate(JSONObject jsonObject){
        return null;
    }
}
