package com.terminatingcode.android.migrainetree.SQL;

import android.provider.BaseColumns;

/**
 * Defines the contract for the Menstrual Records Table
 * Created by Sarah on 7/23/2016.
 */
public class MenstrualRecord implements BaseColumns {
    public static final String TABLE_NAME = "menstrualRecords";
    public static final String DATE = "menstrualRecordDate";
    public static final String DAY_IN_CYCLE = "dayInCycle";
}
