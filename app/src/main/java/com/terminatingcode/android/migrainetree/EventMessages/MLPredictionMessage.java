package com.terminatingcode.android.migrainetree.EventMessages;

/**
 * MessageEvent to send the predicted result to FeelBetterFragment
 * Created by Sarah on 8/16/2016.
 */
public class MLPredictionMessage {

    public final String result;

    public MLPredictionMessage(String result){
        this.result = result;
    }
}
