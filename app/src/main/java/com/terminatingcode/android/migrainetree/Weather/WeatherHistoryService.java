package com.terminatingcode.android.migrainetree.Weather;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
            getWeatherHistory(date);
        }
        setIntentRedelivery(false);
    }

    public void getWeatherHistory(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
