package com.terminatingcode.android.migrainetree.Weather;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.terminatingcode.android.migrainetree.Constants;
import com.terminatingcode.android.migrainetree.MyRequestQueue;
import com.terminatingcode.android.migrainetree.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Service to initiate WeatherUnderground History call
 * Creates and tracks a Weather24Hour object
 * Created by Sarah on 7/13/2016.
 */
public class WeatherHistoryService extends IntentService {
    private static final String NAME = "WeatherHistoryService";
    private static Weather24Hour sWeather24Hour;

    public WeatherHistoryService() {
        super(NAME);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(NAME, NAME + "started");
            final String locationUID = intent.getStringExtra(Constants.LOCATIONUID);
            final String date = intent.getStringExtra(Constants.DATE_KEY);
            getWeatherHistory(date, locationUID);
        }else{
            Log.e(NAME, "intent null for WeatherHistoryService!");
        }
        setIntentRedelivery(true);
    }

    public void getWeatherHistory(String dateString, String locationID){
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("dd/MM/yyyyhh:mm", Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String newDateString;
        Date date;
        Calendar calendar = Calendar.getInstance();
        try {
            date = oldDateFormat.parse(dateString);
            calendar.setTimeInMillis(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        newDateString = newDateFormat.format(date);
        sWeather24Hour = new Weather24Hour();
        sWeather24Hour.setMigraineStart(calendar);
        Log.d(NAME, "starting request with " + newDateString);
        makeHTTPRequest(locationID, newDateString);
        newDateString = minusDay(calendar, newDateFormat);
        makeHTTPRequest(locationID, newDateString);
    }

    public static String minusDay(Calendar calendar, DateFormat dateFormat){
        Calendar copy = Calendar.getInstance();
        copy.setTimeInMillis(calendar.getTimeInMillis());
        copy.add(Calendar.DAY_OF_MONTH, -1);
        Date date = calendar.getTime();
        String newDateString = dateFormat.format(date);
        return newDateString;
    }

    private void makeHTTPRequest(String locationID, String date) {
        MyRequestQueue queue = MyRequestQueue.getInstance(this);
        final HistoryWeatherParser hwp = new HistoryWeatherParser();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.WUurl))
                .append(getString(R.string.WUAPIKey))
                .append(getString(R.string.history))
                .append(date)
                .append(getString(R.string.q))
                .append(locationID)
                .append(getString(R.string.json));
        final String url = sb.toString();
        Log.d(NAME, "sending url  " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(NAME, "received response " + response);
                            Weather24Hour updated24WeatherHour = hwp.parse(response, sWeather24Hour);
                            addHours(updated24WeatherHour);
                        } catch (JSONException e) {
                            Log.d(NAME, "JSONException " + e);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(NAME, "Error in JsonObjectRequest");
                    }
                });
        queue.addToRequestQueue(jsonObjectRequest);
    }

    private void addHours(Weather24Hour updatedWeather24Hour){
        sWeather24Hour = updatedWeather24Hour;
    }
}
