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
    private Weather24Hour mWeather24Hour;
    private static final String temperature = "tempm";
    private static final String dewPoint = "dewptm";
    private static final String humidity = "hum";
    private static final String windSpeed = "wspdm";
    private static final String windGust = "wgustm";
    private static final String windDirection = "wdird";
    private static final String visibility = "vism";
    private static final String airPressure = "pressurem";
    private static final String windChill = "windchillm";
    private static final String heatIndex = "heatindexm";
    private static final String precipitation = "precipm";
    private static final String fog = "fog";
    private static final String rain = "rain";
    private static final String snow = "snow";
    private static final String hail = "hail";
    private static final String thunder = "thunder";
    private static final String tornado = "tornado";
    private static final String utcdate = "utcdate";
    private static final String year = "year";
    private static final String month = "mon";
    private static final String day = "mday";
    private static final String hour = "hour";
    private static final String minutes = "min";

    public Weather24Hour parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        return null;
    }

    public WeatherHour parseHour(JSONObject jsonObject) throws JSONException, ParseException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        WeatherHour hour = new WeatherHour();
        hour.setHourStart(parseDate(jsonObject.getJSONObject(utcdate)));
        hour.setTemp(jsonObject.getDouble(temperature));
        hour.setDewpt(jsonObject.getDouble(dewPoint));
        hour.setHum(jsonObject.getDouble(humidity));
        hour.setWspd(jsonObject.getDouble(windSpeed));
        hour.setWgust(jsonObject.getDouble(windGust));
        hour.setWdir(jsonObject.getDouble(windDirection));
        hour.setVis(jsonObject.getDouble(visibility));
        hour.setPressure(jsonObject.getDouble(airPressure));
        hour.setWindchill(jsonObject.getDouble(windChill));
        hour.setHeatindex(jsonObject.getDouble(heatIndex));
        hour.setPreci(jsonObject.getDouble(precipitation));
        hour.setFog(jsonObject.getInt(fog) == 1);
        hour.setRain(jsonObject.getInt(rain) == 1);
        hour.setSnow(jsonObject.getInt(snow) == 1);
        hour.setHail(jsonObject.getInt(hail) == 1);
        hour.setThunder(jsonObject.getInt(thunder) == 1);
        hour.setTornado(jsonObject.getInt(tornado) == 1);
        return hour;
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
