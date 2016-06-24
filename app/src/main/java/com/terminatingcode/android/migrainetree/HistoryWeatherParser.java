package com.terminatingcode.android.migrainetree;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public Date parseDate(JSONObject jsonObject) throws JSONException, ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append(jsonObject.getString("year")).append(" ");
        sb.append(jsonObject.getString("mon")).append(" ");
        sb.append(jsonObject.getString("mday")).append(" ");
        sb.append(jsonObject.getString("hour")).append(":");
        sb.append(jsonObject.getString("min"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = format.parse(sb.toString());
        return date;
    }
}
