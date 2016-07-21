package com.terminatingcode.android.migrainetree;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.terminatingcode.android.migrainetree.SQL.MenstrualRecordsProvider;

import org.junit.Before;

import java.util.Arrays;

/**
 * Unit Tests for MenstrualRecordsProvider
 * based on example code in Android Programming: Pushing the Limits
 * by Erik Hellman
 * Created by Sarah on 7/20/2016.
 */
public class MenstrualRecordsProviderTest extends ProviderTestCase2<MenstrualRecordsProvider> {

    private MockContentResolver mResolver;
    private Uri TEST_URI = Uri.parse("content://com.apt1.code.provider/menstrualRecords");

    public MenstrualRecordsProviderTest() {
        super(MenstrualRecordsProvider.class, MenstrualRecordsProvider.AUTHORITY);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        mResolver = getMockContentResolver();
    }

    public void testsDatabaseIsCreated(){
        Cursor cursor = null;
        try{
            cursor= mResolver.query(TEST_URI, null, null, null, null);
            assertNotNull(cursor);
            assertFalse(cursor.moveToNext());
        }finally{
            if(cursor != null) cursor.close();
        }
    }

    public void testsAllColumnsReturned(){
        Cursor cursor = null;
        try{
            int numOfColumns = MenstrualRecordsProvider.ALL_COLUMNS.length;
            cursor= mResolver.query(TEST_URI, null, null, null, null);
            String[] columns = new String[numOfColumns];
            System.arraycopy(MenstrualRecordsProvider.ALL_COLUMNS, 0, columns, 0, numOfColumns);
            Arrays.sort(columns);
            String[] columnNames = cursor.getColumnNames();
            Arrays.sort(columnNames);
            assertTrue(Arrays.equals(columns, columnNames));
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    public void testsDatabaseInsert(){
        Long expectedDate = 1466668560000L;
        int expectedDay = 1;
        ContentValues values = new ContentValues();
        values.put(MenstrualRecordsProvider.MenstrualRecord.COLUMN_NAME_MENSTRUAL_RECORD_DATE, expectedDate);
        values.put(MenstrualRecordsProvider.MenstrualRecord.COLUMN_NAME_DAY_IN_CYCLE, expectedDay);
        Uri insertedUri = mResolver.insert(TEST_URI, values);
        assertNotNull(insertedUri);

        Cursor cursor = null;
        //test insert
        try{
            cursor = mResolver.query(insertedUri, null, null, null, null);
            assertNotNull(cursor);
            assertTrue(cursor.moveToNext());

            int dateColumnId = cursor.getColumnIndex(
                    MenstrualRecordsProvider.MenstrualRecord.COLUMN_NAME_MENSTRUAL_RECORD_DATE);
            Long resultDate = cursor.getLong(dateColumnId);
            assertEquals(expectedDate, resultDate);

            int dayColumnId = cursor.getColumnIndex(
                    MenstrualRecordsProvider.MenstrualRecord.COLUMN_NAME_DAY_IN_CYCLE);
            int resultDay = cursor.getInt(dayColumnId);
            assertEquals(expectedDay, resultDay);
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
            Uri invalidUri = Uri.parse("content://" + MenstrualRecordsProvider.AUTHORITY + "/wrongPath");
            Cursor cursor = mResolver.query(invalidUri, null, null, null, null);
            if(cursor != null) cursor.close();
            fail("Expected IllegalArgumentException");
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    public void testsHelperMethodFixSelectionArgs(){
        String testId = "id";
        String[] expected = new String[]{testId};
        String[] result = MenstrualRecordsProvider.fixSelectionArgs(null, testId);
        assertTrue(Arrays.equals(expected, result));

        String[] testArgs = new String[]{"1", "2"};
        expected = new String[]{testId, "1", "2"};
        result = MenstrualRecordsProvider.fixSelectionArgs(testArgs, testId);
        assertTrue(Arrays.equals(expected, result));
    }
}