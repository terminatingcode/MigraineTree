package com.terminatingcode.android.migrainetree;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MenstrualRecord;
import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;

import org.junit.Before;

import java.util.Arrays;

/**
 * Unit Tests for LocalContentProvider
 * based on example code in Android Programming: Pushing the Limits
 * by Erik Hellman
 * Created by Sarah on 7/20/2016.
 */
public class LocalContentProviderTest extends ProviderTestCase2<LocalContentProvider> {

    private MockContentResolver mResolver;
    private Uri MenstrualUri = LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS;
    private Uri MigraineUri = LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS;

    public LocalContentProviderTest() {
        super(LocalContentProvider.class, LocalContentProvider.AUTHORITY);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        mResolver = getMockContentResolver();
    }

    public void testsMenstrualDatabaseIsCreated(){
        Cursor cursor = null;
        try{
            cursor= mResolver.query(MenstrualUri, null, null, null, null);
            assertNotNull(cursor);
            assertFalse(cursor.moveToNext());
        }finally{
            if(cursor != null) cursor.close();
        }
    }

    public void testsMigraineDatabaseIsCreated(){
        Cursor cursor = null;
        try{
            cursor= mResolver.query(MigraineUri, null, null, null, null);
            assertNotNull(cursor);
            assertFalse(cursor.moveToNext());
        }finally{
            if(cursor != null) cursor.close();
        }
    }

    public void testsAllMenstrualColumnsReturned(){
        final String[] ALL_MENSTRUAL_COLUMNS = new String[]{
                MenstrualRecord._ID,
                MenstrualRecord.DATE};
        Cursor cursor = null;
        try{
            int numOfColumns = ALL_MENSTRUAL_COLUMNS.length;
            cursor= mResolver.query(MenstrualUri, null, null, null, null);
            String[] columns = new String[numOfColumns];
            System.arraycopy(ALL_MENSTRUAL_COLUMNS, 0, columns, 0, numOfColumns);
            Arrays.sort(columns);
            String[] columnNames = cursor.getColumnNames();
            Arrays.sort(columnNames);
            assertTrue(Arrays.equals(columns, columnNames));
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    public void testsAllMigraineColumnsReturned(){
        final String[] ALL_MIGRAINE_COLUMNS = new String[]{
                MigraineRecord._ID, MigraineRecord.START_HOUR, MigraineRecord.END_HOUR,
                MigraineRecord.PAIN_AT_ONSET, MigraineRecord.PAIN_AT_PEAK,
                MigraineRecord.CITY, MigraineRecord.AURA, MigraineRecord.EATEN,
                MigraineRecord.WATER, MigraineRecord.SLEEP, MigraineRecord.STRESS,
                MigraineRecord.EYE_STRAIN, MigraineRecord.PAIN_TYPE, MigraineRecord.PAIN_SOURCE,
                MigraineRecord.MEDICATION, MigraineRecord.NAUSEA, MigraineRecord.SENSITIVITY_TO_LIGHT,
                MigraineRecord.SENSITIVITY_TO_NOISE, MigraineRecord.SENSITIVITY_TO_SMELL,
                MigraineRecord.CONGESTION, MigraineRecord.EARS, MigraineRecord.CONFUSION,
                MigraineRecord.MENSTRUAL_DAY, MigraineRecord.CURRENT_TEMP, MigraineRecord.CURRENT_HUM,
                MigraineRecord.CURRENT_AP, MigraineRecord.TEMP3HOURS, MigraineRecord.HUM3HOURS,
                MigraineRecord.AP3HOURS, MigraineRecord.TEMP12HOURS, MigraineRecord.HUM12HOURS, MigraineRecord.AP12HOURS,
                MigraineRecord.TEMP24HOURS, MigraineRecord.HUM24HOURS, MigraineRecord.AP24HOURS};
        Cursor cursor = null;
        try{
            int numOfColumns = ALL_MIGRAINE_COLUMNS.length;
            cursor= mResolver.query(MigraineUri, null, null, null, null);
            String[] columns = new String[numOfColumns];
            System.arraycopy(ALL_MIGRAINE_COLUMNS, 0, columns, 0, numOfColumns);
            Arrays.sort(columns);
            String[] columnNames = cursor.getColumnNames();
            Arrays.sort(columnNames);
            assertTrue(Arrays.equals(columns, columnNames));
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    public void testsMenstrualDatabaseInsertDelete(){
        Long expectedDate = 1466668560000L;
        ContentValues values = new ContentValues();
        values.put(MenstrualRecord.DATE, expectedDate);
        Uri insertedUri = mResolver.insert(MenstrualUri, values);
        assertNotNull(insertedUri);

        Cursor cursor = null;
        //test insert
        try{
            cursor = mResolver.query(insertedUri, null, null, null, null);
            assertNotNull(cursor);
            assertTrue(cursor.moveToNext());

            int dateColumnId = cursor.getColumnIndex(
                    MenstrualRecord.DATE);
            Long resultDate = cursor.getLong(dateColumnId);
            assertEquals(expectedDate, resultDate);
        }finally{
            if(cursor != null) cursor.close();
        }

        //test delete
        try{
            int expected = 1;
            int deleted = mResolver.delete(insertedUri, null, null);
            assertEquals(expected, deleted);

            cursor = mResolver.query(insertedUri, null, null, null, null);
            assertNotNull(cursor);
            assertFalse(cursor.moveToNext());
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    public void testsMigraineDatabaseInsertDeleteUpdate(){
        Long startHour = 1466668560000L;
        Long endHour = 1466669580000L;
        String city = "city";
        int painAtOnset = 1;
        int paintAtPeak = 10;
        Boolean aura = true;
        Boolean eaten = false;
        Boolean water = true;
        int sleep = 8;
        int stress = 7;
        int eyeStrain = 5;
        String painType = "type";
        String painSource = "source";
        String medication = "medication";
        Boolean nausea = true;
        Boolean light = true;
        Boolean noise = false;
        Boolean smell = true;
        Boolean congestion = false;
        Boolean ears = false;
        Boolean confusion = true;
        int cycleDay = 10;

        ContentValues values = new ContentValues();
        values.put(MigraineRecord.START_HOUR, startHour);
        values.put(MigraineRecord.END_HOUR, endHour);
        values.put(MigraineRecord.CITY, city);
        values.put(MigraineRecord.PAIN_AT_ONSET, painAtOnset);
        values.put(MigraineRecord.PAIN_AT_PEAK, paintAtPeak);
        values.put(MigraineRecord.AURA, aura);
        values.put(MigraineRecord.EATEN, eaten);
        values.put(MigraineRecord.WATER, water);
        values.put(MigraineRecord.SLEEP, sleep);
        values.put(MigraineRecord.STRESS, stress);
        values.put(MigraineRecord.EYE_STRAIN, eyeStrain);
        values.put(MigraineRecord.PAIN_TYPE, painType);
        values.put(MigraineRecord.PAIN_SOURCE, painSource);
        values.put(MigraineRecord.MEDICATION, medication);
        values.put(MigraineRecord.NAUSEA, nausea);
        values.put(MigraineRecord.SENSITIVITY_TO_LIGHT, light);
        values.put(MigraineRecord.SENSITIVITY_TO_NOISE, noise);
        values.put(MigraineRecord.SENSITIVITY_TO_SMELL, smell);
        values.put(MigraineRecord.CONGESTION, congestion);
        values.put(MigraineRecord.EARS, ears);
        values.put(MigraineRecord.CONFUSION, confusion);
        values.put(MigraineRecord.MENSTRUAL_DAY, cycleDay);
        Uri insertedUri = mResolver.insert(MigraineUri, values);
        assertNotNull(insertedUri);

        Cursor cursor = null;
        //test insert
        try{
            cursor = mResolver.query(insertedUri, null, null, null, null);
            assertNotNull(cursor);
            assertTrue(cursor.moveToNext());

            int dateColumnId = cursor.getColumnIndex(MigraineRecord.START_HOUR);
            Long resultDate = cursor.getLong(dateColumnId);
            assertEquals(startHour, resultDate);

            int cityColumnId = cursor.getColumnIndex(MigraineRecord.CITY);
            String resultCity = cursor.getString(cityColumnId);
            assertEquals(city, resultCity);


            //test update
            int endHourIndex = cursor.getColumnIndex(MigraineRecord.END_HOUR);
            Long initialEndHour = cursor.getLong(endHourIndex);
            assertEquals(endHour, initialEndHour);

            int painAtPeakIndex = cursor.getColumnIndex(MigraineRecord.PAIN_AT_PEAK);
            int initialPainAtPeak = cursor.getInt(painAtPeakIndex);
            assertEquals(paintAtPeak, initialPainAtPeak);

            Long updatedEndHour = 1466669990000L;
            int updatedPaintAtPeak = 7;
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(MigraineRecord.END_HOUR, updatedEndHour);
            updatedValues.put(MigraineRecord.PAIN_AT_PEAK, updatedPaintAtPeak);

            int updated = mResolver.update(insertedUri,updatedValues, null, null);
            int expected = 1;
            assertEquals(expected, updated);

            cursor = mResolver.query(insertedUri, null, null, null, null);
            assertNotNull(cursor);
            assertTrue(cursor.moveToNext());

            int updatedEndHourIndex = cursor.getColumnIndex(MigraineRecord.END_HOUR);
            Long resultUpdatedEndHour = cursor.getLong(updatedEndHourIndex);
            assertEquals(updatedEndHour, resultUpdatedEndHour);

            int updatedPainIndex = cursor.getColumnIndex(MigraineRecord.PAIN_AT_PEAK);
            int resultUpdatedPain = cursor.getInt(updatedPainIndex);
            assertEquals(updatedPaintAtPeak, resultUpdatedPain);
        }finally{
            if(cursor != null) cursor.close();
        }

        //test delete
        try{
            int expected = 1;
            int deleted = mResolver.delete(insertedUri, null, null);
            assertEquals(expected, deleted);

            cursor = mResolver.query(insertedUri, null, null, null, null);
            assertNotNull(cursor);
            assertFalse(cursor.moveToNext());
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    public void testsInvalidUri(){
        try{
            Uri invalidUri = Uri.parse("content://" + LocalContentProvider.AUTHORITY + "/wrongPath");
            Cursor cursor = mResolver.query(invalidUri, null, null, null, null);
            if(cursor != null) cursor.close();
            fail("Expected IllegalArgumentException");
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}