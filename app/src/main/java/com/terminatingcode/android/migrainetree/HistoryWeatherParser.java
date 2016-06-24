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
    private String utcdate = "utcdate";
    private String year = "year";
    private String month = "mon";
    private String day = "mday";
    private String hour = "hour";
    private String minutes = "min";

    public Weather24Hour parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        return null;
    }

    public WeatherHour parseHour(JSONObject jsonObject) throws JSONException, ParseException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        WeatherHour hour = new WeatherHour();
        hour.setHourStart(parseDate(jsonObject.getJSONObject(utcdate)));
        return null;
    }

    /**
     * parses JSONObject holding UTC date into Android friendly date
     * @param jsonObject UTC date info
     * @return Date date
     * @throws JSONException caught higher
     * @throws ParseException caught higher
     */
    public Date parseDate(JSONObject jsonObject) throws JSONException, ParseException {
        StringBuilder sb = new StringBuilder();
        sb.append(jsonObject.getString(year)).append(" ");
        sb.append(jsonObject.getString(month)).append(" ");
        sb.append(jsonObject.getString(day)).append(" ");
        sb.append(jsonObject.getString(hour)).append(":");
        sb.append(jsonObject.getString(minutes));

        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.parse(sb.toString());
    }
}
