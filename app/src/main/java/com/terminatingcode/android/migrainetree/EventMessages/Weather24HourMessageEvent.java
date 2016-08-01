package com.terminatingcode.android.migrainetree.EventMessages;

import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;

/**
 * EventBus POJO Message Event for Weather24Hour
 * Created by Sarah on 7/29/2016.
 */
public class Weather24HourMessageEvent {
    public final Weather24Hour mWeather24Hour;

    public Weather24HourMessageEvent(Weather24Hour weather24Hour){
        this.mWeather24Hour = weather24Hour;
    }
}
