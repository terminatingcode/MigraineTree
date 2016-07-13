package com.terminatingcode.android.migrainetree.Weather;

import android.app.IntentService;
import android.content.Intent;

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
            final String action = intent.getAction();
            getWeatherHistory();
        }
        setIntentRedelivery(false);
    }

    public void getWeatherHistory(){

    }
}
