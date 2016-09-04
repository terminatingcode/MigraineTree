package com.terminatingcode.android.migrainetree.model.eventBus;

/**
 * Defines Message Events for EventBus Messaging
 * POJO for Cities returned by GeoLookupService
 * Created by Sarah on 6/21/2016.
 */
public class CitiesMessageEvent {
    public final String[] cities;

    public CitiesMessageEvent(String[] cities){
        this.cities = cities;
    }

}
