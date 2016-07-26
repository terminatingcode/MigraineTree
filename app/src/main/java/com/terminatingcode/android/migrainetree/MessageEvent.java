package com.terminatingcode.android.migrainetree;

import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;

/**
 * Defines Message Events for EventBus Messaging
 * Created by Sarah on 6/21/2016.
 */
public class MessageEvent {
    public String[] cities;
    public Weather24Hour mWeather24Hour;

    public MessageEvent(String[] cities){
        this.cities = cities;
    }
    public MessageEvent(Weather24Hour weather24Hour){
        this.mWeather24Hour = weather24Hour;
    }
}
