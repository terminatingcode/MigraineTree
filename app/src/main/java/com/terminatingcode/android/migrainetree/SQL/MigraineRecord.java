package com.terminatingcode.android.migrainetree.SQL;

import android.provider.BaseColumns;

/**
 * Defines the contract for the Migraine Records Table
 * Created by Sarah on 7/23/2016.
 */
public class MigraineRecord implements BaseColumns {
    public static final String TABLE_NAME = "migraineRecords";
    public static final String START_HOUR = "startHour";
    public static final String END_HOUR = "endHour";
    public static final String PAIN_AT_ONSET = "painAtOnset";
    public static final String PAIN_AT_PEAK = "painAtPeak";
    public static final String CITY = "city";
    public static final String AURA = "aura";
    public static final String EATEN = "eaten";
    public static final String WATER = "water";
    public static final String SLEEP = "sleep";
    public static final String STRESS = "stress";
    public static final String EYE_STRAIN = "eyeStrain";
    public static final String PAIN_TYPE = "painType";
    public static final String PAIN_SOURCE = "painSource";
    public static final String MEDICATION = "medication";
    public static final String NAUSEA = "nausea";
    public static final String SENSITIVITY_TO_LIGHT = "light";
    public static final String SENSITIVITY_TO_NOISE = "noise";
    public static final String SENSITIVITY_TO_SMELL = "smell";
    public static final String CONGESTION = "congestion";
    public static final String EARS = "ears";
    public static final String CONFUSION = "confusion";
    public static final String MENSTRUAL_DAY = "menstrualDay";
}
