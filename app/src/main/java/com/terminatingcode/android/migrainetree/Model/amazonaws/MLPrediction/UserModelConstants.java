package com.terminatingcode.android.migrainetree.model.amazonaws.MLPrediction;

import com.terminatingcode.android.migrainetree.model.MigraineRecordObject;
import java.util.HashMap;

/**
 * Class to hold the model id and attribute names used
 * in the FeatureSelection model on AWS Machine Learning
 */
public class UserModelConstants {
    public static final String MODEL_ID = "ml-NqKGx0pooNR";

    public static final String CURRENT_TEMP = "CurrentTemp (N)";
    public static final String CURRENT_HUM = "CurrentHum (N)";
    public static final String CURRENT_AP = "CurrentAP (N)";
    public static final String TEMP_3_HOURS = "Temp3Hours (N)";
    public static final String TEMP_12_HOURS = "Temp12Hours (N)";
    public static final String TEMP_24_HOURS = "Temp24Hours (N)";
    public static final String HUM_3_HOURS = "Hum3Hours (N)";
    public static final String HUM_12_HOURS = "Hum12Hours (N)";
    public static final String HUM_24_HOURS = "Hum24Hours (N)";
    public static final String AP_3_HOURS = "AP3Hours (N)";
    public static final String AP_12_HOURS = "AP12Hours (N)";
    public static final String AP_24_HOURS = "AP24Hours (N)";
    public static final String AURA = "Aura (N)";
    public static final String CONFUSION = "Confusion (N)";
    public static final String CONGESTION = "Congestion (N)";
    public static final String EARS = "Ears (N)";
    public static final String EATEN = "Eaten (N)";
    public static final String EYES = "EyeStrain (N)";
    public static final String MEDS = "Medication (S)";
    public static final String NAUSEA = "Nausea (N)";
    public static final String SOURCE = "PainSource (S)";
    public static final String TYPE = "PainType (S)";
    public static final String LIGHT = "SensitivityToLight (N)";
    public static final String NOISE = "SensitivityToNoise (N)";
    public static final String SMELL = "SensitivityToSmell (N)";
    public static final String STRESS = "Stress (N)";
    public static final String WATER = "Water (N)";


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
        record.put(CONFUSION, String.valueOf(migraineRecordObject.isConfusion()));
        record.put(CONGESTION, String.valueOf(migraineRecordObject.isCongestion()));
        record.put(EARS, String.valueOf(migraineRecordObject.isEars()));
        record.put(EATEN, String.valueOf(migraineRecordObject.isEaten()));
        record.put(EYES, String.valueOf(migraineRecordObject.getEyeStrain()));
        record.put(MEDS, String.valueOf(migraineRecordObject.getMedication()));
        record.put(NAUSEA, String.valueOf(migraineRecordObject.isNausea()));
        record.put(SOURCE, String.valueOf(migraineRecordObject.getPainSource()));
        record.put(TYPE, String.valueOf(migraineRecordObject.getPainType()));
        record.put(LIGHT, String.valueOf(migraineRecordObject.isSensitivityToLight()));
        record.put(NOISE, String.valueOf(migraineRecordObject.isSensitivityToNoise()));
        record.put(SMELL, String.valueOf(migraineRecordObject.isSensitivityToSmell()));
        record.put(STRESS, String.valueOf(migraineRecordObject.getStress()));
        record.put(WATER, String.valueOf(migraineRecordObject.isWater()));

        return record;
    }
}
