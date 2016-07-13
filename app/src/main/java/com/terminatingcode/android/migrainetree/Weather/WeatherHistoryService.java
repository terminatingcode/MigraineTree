package com.terminatingcode.android.migrainetree.Weather;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.terminatingcode.android.migrainetree.MessageEvent;
import com.terminatingcode.android.migrainetree.MyRequestQueue;
import com.terminatingcode.android.migrainetree.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Service to initiate WeatherUnderground History call
 * Creates and tracks a Weather24Hour object
 * Created by Sarah on 7/13/2016.
 */
public class WeatherHistoryService extends IntentService {
    private static final String NAME = "WeatherHistoryService";

    public WeatherHistoryService() {
        super(NAME);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(NAME, NAME + "started");
            final String date = intent.getAction();
            final String locationUID = intent.getAction();
            getWeatherHistory(date, locationUID);
        }
        setIntentRedelivery(false);
    }

    public void getWeatherHistory(String dateString, String locationID){
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("DD/MM/YYYY", Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat("YYYYMMDD", Locale.getDefault());
        String newDateString;
        Date date = null;
        try {
            date = oldDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        newDateString = newDateFormat.format(date);


        MyRequestQueue queue = MyRequestQueue.getInstance(this);
        final GeoLookupParser glp = new GeoLookupParser();
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.WUurl))
                .append(getString(R.string.WUAPIKey))
                .append(getString(R.string.history))
                .append(newDateString)
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
                            String[] cities = glp.parse(response);
                            EventBus.getDefault().post(new MessageEvent(cities));
                        } catch (JSONException e) {
                            Log.d(NAME, "JSONException " + e);
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
}
