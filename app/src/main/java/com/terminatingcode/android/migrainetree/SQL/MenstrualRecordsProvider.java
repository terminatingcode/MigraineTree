package com.terminatingcode.android.migrainetree.SQL;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
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
public final class MenstrualRecordsProvider extends ContentProvider{
    public static final String AUTHORITY = "com.terminatingcode.android.migrainetree.Calendar.provider";
    private static final String LOG_NAME = "MenstrualRecordsProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + MenstrualRecord.TABLE_NAME);
    public static final int ALL_MENSTRUAL_RECORDS = 10;
    public static final int DATES = 20;
    public static final String[] ALL_COLUMNS = new String[]{
                    MenstrualRecord._ID,
                    MenstrualRecord.COLUMN_NAME_MENSTRUAL_RECORD_DATE,
                    MenstrualRecord.COLUMN_NAME_DAY_IN_CYCLE};
    public static final String CREATE_MENSTRUAL_TABLE = "CREATE TABLE "
            + MenstrualRecord.TABLE_NAME + " ("
            + MenstrualRecord._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MenstrualRecord.COLUMN_NAME_MENSTRUAL_RECORD_DATE + " INTEGER NOT NULL, "
            + MenstrualRecord.COLUMN_NAME_DAY_IN_CYCLE + " INTEGER NOT NULL);";
    public static final String DELETE_MENSTRUAL_TABLE =
            "DROP TABLE IF EXISTS " + MenstrualRecord.TABLE_NAME;
    public MenstrualRecordsDbHelper mDbHelper;

    static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        mUriMatcher.addURI(AUTHORITY, MenstrualRecord.TABLE_NAME, ALL_MENSTRUAL_RECORDS);
        mUriMatcher.addURI(AUTHORITY, MenstrualRecord.TABLE_NAME + "/#", DATES);
    }

    public MenstrualRecordsProvider() {}

    @Override
    public boolean onCreate() {
        Context context = getContext();
        Log.d(LOG_NAME, "context is" + context);
        mDbHelper = new MenstrualRecordsDbHelper(context);
        return (mDbHelper == null);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        //default project is to return all columns
        if(projection == null) projection = ALL_COLUMNS;
        //default sort order is to sort by date
        if(sortOrder == null) sortOrder = MenstrualRecord.COLUMN_NAME_MENSTRUAL_RECORD_DATE;
        Log.d(LOG_NAME, "helper is null:" + (mDbHelper == null));
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch(mUriMatcher.match(uri)){
            case ALL_MENSTRUAL_RECORDS:
                return db.query(MenstrualRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case DATES:
                selection = selection == null? MenstrualRecord._ID + " = ?" : MenstrualRecord._ID + " ? AND (" + selection + ")";
                selectionArgs = fixSelectionArgs(selectionArgs, uri.getLastPathSegment());
                return db.query(MenstrualRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);

        }
    }

    /**
     * Prepends ID to selectionArgs
     * @param selectionArgs the selected arguments for query
     * @param recordId the uri path to ID
     * @return selectionArgs with ID prepended
     */
    public static String[] fixSelectionArgs(String[] selectionArgs, String recordId){
        if(selectionArgs == null){
            return new String[]{recordId};
        }else{
            String[] newSelectionArgs = new String[selectionArgs.length + 1];
            newSelectionArgs[0] = recordId;
            System.arraycopy(selectionArgs, 0, newSelectionArgs, 1, selectionArgs.length);
            return newSelectionArgs;
        }
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(MenstrualRecord.TABLE_NAME, "", values);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(MenstrualRecord.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    public static abstract class MenstrualRecord implements BaseColumns {
        public static final String TABLE_NAME = "menstrualRecordsProvider";
        public static final String COLUMN_NAME_MENSTRUAL_RECORD_DATE = "menstrualRecordDate";
        public static final String COLUMN_NAME_DAY_IN_CYCLE = "dayInCycle";
    }

    private class MenstrualRecordsDbHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "MenstrualRecords.db";

        private MenstrualRecordsDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_NAME, "Table created: " + CREATE_MENSTRUAL_TABLE);
            db.execSQL(CREATE_MENSTRUAL_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DELETE_MENSTRUAL_TABLE);
            onCreate(db);
        }
        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
            onUpgrade(db, oldVersion, newVersion);
        }
    }


}
