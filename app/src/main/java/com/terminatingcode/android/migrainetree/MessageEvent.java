package com.terminatingcode.android.migrainetree;

/**
 * Defines a Message Event for EventBus Messaging
 * Created by Sarah on 6/21/2016.
 */
public class MessageEvent {
    public final String[] cities;

    public MessageEvent(String[] cities){
        this.cities = cities;
    }
}
