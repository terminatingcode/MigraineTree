package com.terminatingcode.android.migrainetree.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Parses Historical Weather data from Weather Underground
 * Created by Sarah on 6/23/2016.
 */
public class HistoryWeatherParser {
    private static final String temperature = "tempm";
    private static final String dewPoint = "dewptm";
    private static final String humidity = "hum";
    private static final String windSpeed = "wspdm";
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
    private static final double defaultDouble = 999.99;

    public Weather24Hour parse(JSONObject jsonObject, Weather24Hour weather24Hour, Calendar startTime)
            throws JSONException, ParseException, IllegalArgumentException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        if(weather24Hour == null) throw new IllegalArgumentException("weather24Hour not initialised");
        JSONObject historyResults = jsonObject.getJSONObject("history");
        JSONArray observations = historyResults.getJSONArray("observations");
        for (int i = observations.length() - 1; i >= 0; i--) {
            JSONObject hour = observations.getJSONObject(i);
            WeatherHour weatherHour = parseHour(hour, startTime);
            if(weatherHour != null) weather24Hour.addHour(weatherHour);
        }
        return weather24Hour;
    }

    public WeatherHour parseHour(JSONObject jsonObject, Calendar startTime) throws JSONException, ParseException {
        if(jsonObject == null) throw new JSONException("null JsonObject");
        Date date = parseDate(jsonObject.getJSONObject(utcdate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        if(startTime.after(calendar)) {
            WeatherHour hour = new WeatherHour();
            hour.setHourStart(date);
            try {
                hour.setTemp(jsonObject.getDouble(temperature));
            }catch (JSONException e) {
                hour.setTemp(defaultDouble);
            }
            try{
                hour.setDewpt(jsonObject.getDouble(dewPoint));
            }catch (JSONException e) {
                hour.setDewpt(defaultDouble);
            }
            try {
                hour.setHum(jsonObject.getDouble(humidity));
            }catch (JSONException e) {
                hour.setHum(defaultDouble);
            }
            try {
                hour.setWspd(jsonObject.getDouble(windSpeed));
            }catch(JSONException e){
                hour.setWspd(defaultDouble);
            }
            try{
                hour.setWdir(jsonObject.getDouble(windDirection));
            }catch(JSONException e){
                hour.setWdir(defaultDouble);
            }
            try{
                hour.setVis(jsonObject.getDouble(visibility));
            }catch(JSONException e){
                hour.setVis(defaultDouble);
            }
            try{
                hour.setPressure(jsonObject.getDouble(airPressure));
            }catch(JSONException e){
                hour.setPressure(defaultDouble);
            }
            try{
                hour.setWindchill(jsonObject.getDouble(windChill));
            }catch(JSONException e){
                hour.setWindchill(defaultDouble);
            }
            try{
                hour.setHeatindex(jsonObject.getDouble(heatIndex));
            }catch(JSONException e){
                hour.setHeatindex(defaultDouble);
            }
            try{
                hour.setPreci(jsonObject.getDouble(precipitation));
            }catch(JSONException e){
                hour.setPreci(defaultDouble);
            }
            try {
                hour.setFog(jsonObject.getInt(fog) == 1);
            }catch(JSONException e){
                hour.setFog(false);
            }
            try {
                hour.setRain(jsonObject.getInt(rain) == 1);
            }catch(JSONException e){
                hour.setRain(false);
            }
            try {
                hour.setSnow(jsonObject.getInt(snow) == 1);
            }catch (JSONException e){
                hour.setSnow(false);
            }
            try{
                hour.setHail(jsonObject.getInt(hail) == 1);
            }catch (JSONException e){
                hour.setHail(false);
            }
            try {
                hour.setThunder(jsonObject.getInt(thunder) == 1);
            }catch (JSONException e){
                hour.setThunder(false);
            }
            try {
                hour.setTornado(jsonObject.getInt(tornado) == 1);
            }catch (JSONException e){
                hour.setTornado(false);
            }
            return hour;
        }
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
