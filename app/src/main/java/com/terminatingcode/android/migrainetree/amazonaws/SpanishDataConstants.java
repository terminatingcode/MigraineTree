package com.terminatingcode.android.migrainetree.amazonaws;

import com.terminatingcode.android.migrainetree.MigraineRecordObject;

import java.util.HashMap;

/**
 * Class to hold the model id and attribute names used
 * in the SpanishData model on AWS Machine Learning
 * Created by Sarah on 8/16/2016.
 */
public class SpanishDataConstants {
    public static final String MODEL_ID = "ml-WQB9hrMUj6r";

    public static final String CURRENT_TEMP = "current_temp";
    public static final String CURRENT_HUM = "current_hum";
    public static final String CURRENT_AP = "current_ap";
    public static final String TEMP_3_HOURS = "temp_3_hours";
    public static final String TEMP_12_HOURS = "temp_12_hours";
    public static final String TEMP_24_HOURS = "temp_24_hours";
    public static final String HUM_3_HOURS = "hum_3_hours";
    public static final String HUM_12_HOURS = "hum_12_hours";
    public static final String HUM_24_HOURS = "hum_24_hours";
    public static final String AP_3_HOURS = "ap_3_hours";
    public static final String AP_12_HOURS = "ap_12_hours";
    public static final String AP_24_HOURS = "ap_24_hours";
    public static final String AURA = "aura";

    public static HashMap<String, String> makeRecord(MigraineRecordObject migraineRecordObject){
        HashMap<String, String> record = new HashMap<>();
        record.put(CURRENT_TEMP, String.valueOf(migraineRecordObject.getCurrentTemp()));
        record.put(CURRENT_HUM, String.valueOf(migraineRecordObject.getCurrentHum()));
        record.put(CURRENT_AP, String.valueOf(migraineRecordObject.getCurrentAP()));
        record.put(TEMP_3_HOURS, String.valueOf(migraineRecordObject.getTemp3Hours()));
        record.put(TEMP_12_HOURS, String.valueOf(migraineRecordObject.getTemp12Hours()));
        record.put(TEMP_24_HOURS, String.valueOf(migraineRecordObject.getTemp24Hours()));
        record.put(HUM_3_HOURS, String.valueOf(migraineRecordObject.getHum3Hours()));
        record.put(HUM_12_HOURS, String.valueOf(migraineRecordObject.getHum12Hours()));
        record.put(HUM_24_HOURS, String.valueOf(migraineRecordObject.getHum24Hours()));
        record.put(AP_3_HOURS, String.valueOf(migraineRecordObject.getAP3Hours()));
        record.put(AP_12_HOURS, String.valueOf(migraineRecordObject.getAP12Hours()));
        record.put(AP_24_HOURS, String.valueOf(migraineRecordObject.getAP24Hours()));
        return record;
    }
}
