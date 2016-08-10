package com.terminatingcode.android.migrainetree.SQL;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Class to handle SQLite CRUD methods for MenstrualRecords
 * Date stored as INTEGER as Unix Time,
 * the number of seconds since 1970-01-01 00:00:00 UTC
 * as per http://www.sqlite.org/datatype3.html
 *
 * format of code based on Android training:
 * https://developer.android.com/training/basics/data-storage/databases.html#DbHelper
 *
 * Created by Sarah on 7/14/2016.
 */
public final class LocalContentProvider extends ContentProvider{
    public static final String AUTHORITY = "com.terminatingcode.android.migrainetree.provider";
    private static final String LOG_NAME = "LocalContentProvider";
    public static final Uri CONTENT_URI_MENSTRUAL_RECORDS =
            Uri.parse("content://" + AUTHORITY + "/" + MenstrualRecord.TABLE_NAME);
    public static final Uri CONTENT_URI_MIGRAINE_RECORDS =
            Uri.parse("content://" + AUTHORITY + "/" + MigraineRecord.TABLE_NAME);
    public static final int MENSTRUAL_RECORDS = 10;
    public static final int MENSTRUAL_RECORD_DATES = 20;
    public static final int MIGRAINE_RECORDS = 30;
    public static final int MIGRAINE_RECORDS_ID = 40;
    public DbHelper mDbHelper;

    //Menstrual Records Table
    public static final String CREATE_MENSTRUAL_TABLE = "CREATE TABLE "
            + MenstrualRecord.TABLE_NAME + " ("
            + MenstrualRecord._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MenstrualRecord.DATE + " INTEGER NOT NULL);";
    public static final String DELETE_MENSTRUAL_TABLE =
            "DROP TABLE IF EXISTS " + MenstrualRecord.TABLE_NAME;

    //Migraine Records Table
    public static final String CREATE_MIGRAINE_TABLE = "CREATE TABLE "
            + MigraineRecord.TABLE_NAME + " ("
            + MigraineRecord._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MigraineRecord.START_HOUR + " INTEGER CHECK("
            + MigraineRecord.START_HOUR + " > 0), "
            + MigraineRecord.END_HOUR + " INTEGER DEFAULT 9999999999999 CHECK("
            + MigraineRecord.END_HOUR + " > " + MigraineRecord.START_HOUR + "), "
            + MigraineRecord.CITY + " TEXT NOT NULL, "
            + MigraineRecord.PAIN_AT_ONSET + " INTEGER, "
            + MigraineRecord.PAIN_AT_PEAK + " INTEGER DEFAULT 0, "
            + MigraineRecord.AURA + " BOOLEAN NOT NULL, "
            + MigraineRecord.EATEN + " BOOLEAN NOT NULL, "
            + MigraineRecord.WATER + " BOOLEAN NOT NULL, "
            + MigraineRecord.SLEEP + " INTEGER CHECK("
            + MigraineRecord.SLEEP + " >= 0 AND "
            + MigraineRecord.SLEEP + " <= 24), "
            + MigraineRecord.STRESS + " INTEGER CHECK("
            + MigraineRecord.STRESS + " >= 0 AND "
            + MigraineRecord.STRESS + " <= 10), "
            + MigraineRecord.EYE_STRAIN + " INTEGER CHECK("
            + MigraineRecord.EYE_STRAIN + " >= 0 AND "
            + MigraineRecord.EYE_STRAIN + " <= 10), "
            + MigraineRecord.PAIN_TYPE + " TEXT NOT NULL, "
            + MigraineRecord.PAIN_SOURCE + " TEXT NOT NULL, "
            + MigraineRecord.MEDICATION + " TEXT NOT NULL, "
            + MigraineRecord.NAUSEA + " BOOLEAN NOT NULL, "
            + MigraineRecord.SENSITIVITY_TO_LIGHT + " BOOLEAN NOT NULL, "
            + MigraineRecord.SENSITIVITY_TO_NOISE + " BOOLEAN NOT NULL, "
            + MigraineRecord.SENSITIVITY_TO_SMELL + " BOOLEAN NOT NULL, "
            + MigraineRecord.CONGESTION + " BOOLEAN NOT NULL, "
            + MigraineRecord.EARS + " BOOLEAN NOT NULL, "
            + MigraineRecord.CONFUSION + " BOOLEAN NOT NULL, "
            + MigraineRecord.MENSTRUAL_DAY + " INTEGER, "
            + MigraineRecord.CURRENT_TEMP + " REAL, "
            + MigraineRecord.CURRENT_HUM + " REAL, "
            + MigraineRecord.CURRENT_AP + " REAL, "
            + MigraineRecord.TEMP3HOURS + " REAL, "
            + MigraineRecord.HUM3HOURS + " REAL, "
            + MigraineRecord.AP3HOURS + " REAL, "
            + MigraineRecord.TEMP12HOURS + " REAL, "
            + MigraineRecord.HUM12HOURS + " REAL, "
            + MigraineRecord.AP12HOURS + " REAL, "
            + MigraineRecord.TEMP24HOURS + " REAL, "
            + MigraineRecord.HUM24HOURS + " REAL, "
            + MigraineRecord.AP24HOURS + " REAL);";
    public static final String DELETE_MIGRAINE_TABLE = "DROP TABLE IF EXISTS " + MigraineRecord.TABLE_NAME;

    static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        mUriMatcher.addURI(AUTHORITY, MenstrualRecord.TABLE_NAME, MENSTRUAL_RECORDS);
        mUriMatcher.addURI(AUTHORITY, MenstrualRecord.TABLE_NAME + "/#", MENSTRUAL_RECORD_DATES);
        mUriMatcher.addURI(AUTHORITY, MigraineRecord.TABLE_NAME, MIGRAINE_RECORDS);
        mUriMatcher.addURI(AUTHORITY, MigraineRecord.TABLE_NAME + "/#", MIGRAINE_RECORDS_ID);
    }

    public LocalContentProvider() {}

    @Override
    public boolean onCreate() {
        Context context = getContext();
        Log.d(LOG_NAME, "context is" + context);
        mDbHelper = new DbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch(mUriMatcher.match(uri)){
            case MENSTRUAL_RECORDS:
                return db.query(MenstrualRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case MENSTRUAL_RECORD_DATES:
                return db.query(MenstrualRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case MIGRAINE_RECORDS:
                return db.query(MigraineRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case MIGRAINE_RECORDS_ID:
                String taskId = uri.getLastPathSegment();
                selectionArgs = fixSelectionArgs(selectionArgs, taskId);
                selection = fixSelectionString(selection);
                return db.query(MigraineRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);

        }
    }

    public static String[] fixSelectionArgs(String[] selectionArgs, String taskId){
        if(selectionArgs == null) {
            selectionArgs = new String[]{taskId};
            return selectionArgs;
        }
        else{
            String[] newSelectionArgs = new String[selectionArgs.length + 1];
            newSelectionArgs[0] = taskId;
            System.arraycopy(selectionArgs, 0, newSelectionArgs, 1, selectionArgs.length);
            return newSelectionArgs;
        }
    }

    public static String fixSelectionString(String selection){
        selection = selection == null? MigraineRecord._ID + " = ?" :
                MigraineRecord._ID + " = ? AND (" + selection + ")";
        return selection;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(mUriMatcher.match(uri)){
            case MENSTRUAL_RECORDS:
                return "com.terminatingcode.android.migrainetree.provider/menstrualRecords";
            case MENSTRUAL_RECORD_DATES:
                return "com.terminatingcode.android.migrainetree.provider/menstrualRecords";
            case MIGRAINE_RECORDS:
                return "com.terminatingcode.android.migrainetree.provider/migraineRecords";
            case MIGRAINE_RECORDS_ID:
                return "com.terminatingcode.android.migrainetree.provider/migraineRecords";
            default:
                throw new IllegalArgumentException("invalid URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db= mDbHelper.getWritableDatabase();
        long id;
        switch(mUriMatcher.match(uri)){
            case MENSTRUAL_RECORDS:
                id = db.insert(MenstrualRecord.TABLE_NAME, "", values);
                return Uri.withAppendedPath(uri, String.valueOf(id));
            case MENSTRUAL_RECORD_DATES:
                id = db.insert(MenstrualRecord.TABLE_NAME, "", values);
                return Uri.withAppendedPath(uri, String.valueOf(id));
            case MIGRAINE_RECORDS:
                id = db.insert(MigraineRecord.TABLE_NAME, "", values);
                return Uri.withAppendedPath(uri, String.valueOf(id));
            case MIGRAINE_RECORDS_ID:
                id = db.insert(MigraineRecord.TABLE_NAME, "", values);
                return Uri.withAppendedPath(uri, String.valueOf(id));
            default:
                throw new IllegalArgumentException("invalid URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch(mUriMatcher.match(uri)){
            case MENSTRUAL_RECORDS:
                return db.delete(MenstrualRecord.TABLE_NAME, selection, selectionArgs);
            case MENSTRUAL_RECORD_DATES:
                return db.delete(MenstrualRecord.TABLE_NAME, selection, selectionArgs);
            case MIGRAINE_RECORDS:
                return db.delete(MigraineRecord.TABLE_NAME, selection, selectionArgs);
            case MIGRAINE_RECORDS_ID:
                String taskId = uri.getLastPathSegment();
                selectionArgs = fixSelectionArgs(selectionArgs, taskId);
                selection = fixSelectionString(selection);
                return db.delete(MigraineRecord.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("invalid URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;
        switch(mUriMatcher.match(uri)){
            case MENSTRUAL_RECORDS:
                count = db.update(MenstrualRecord.TABLE_NAME, values, selection, selectionArgs);
                return count;
            case MIGRAINE_RECORDS:
                count = db.update(MigraineRecord.TABLE_NAME, values, selection, selectionArgs);
                return count;
            case MIGRAINE_RECORDS_ID:
                String taskId = uri.getLastPathSegment();
                selectionArgs = fixSelectionArgs(selectionArgs, taskId);
                selection = fixSelectionString(selection);
                count = db.update(MigraineRecord.TABLE_NAME, values, selection, selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("invalid URI: " + uri);
        }
    }

    private class DbHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION = 3;
        public static final String DATABASE_NAME = "Records.db";

        private DbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_NAME, "Table created: " + CREATE_MENSTRUAL_TABLE);
            Log.d(LOG_NAME, "Table created: " + CREATE_MIGRAINE_TABLE);
            db.execSQL(CREATE_MENSTRUAL_TABLE);
            db.execSQL(CREATE_MIGRAINE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DELETE_MENSTRUAL_TABLE);
            db.execSQL(DELETE_MIGRAINE_TABLE);
            onCreate(db);
        }
        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
