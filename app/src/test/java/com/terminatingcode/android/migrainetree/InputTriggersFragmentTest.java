package com.terminatingcode.android.migrainetree;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.terminatingcode.android.migrainetree.controller.InputTriggersFragment;
import com.terminatingcode.android.migrainetree.model.SQLite.LocalContentProvider;
import com.terminatingcode.android.migrainetree.model.SQLite.MenstrualRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;

/**
 * Tests InputTriggersFragment
 * Created by Sarah on 7/6/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class InputTriggersFragmentTest {

    private InputTriggersFragment mInputTriggersFragment;
    private ContentResolver mResolver;
    private long january11990;
    private long december201989;
    private long december211989;
    private long november211989;

    @Before
    public void setup(){
        mInputTriggersFragment = new InputTriggersFragment();
        SupportFragmentTestUtil.startFragment(mInputTriggersFragment);
        mResolver = RuntimeEnvironment.application.getContentResolver();
        january11990 = 631152000000L;
        december201989 = 630115200000L;
        december211989 = 630201600000L;
        november211989 = 627609600000L;
    }

    @Test
    public void testgetCycleDayDoesntAccountForPreviousCycle(){
        ContentValues values = new ContentValues();
        values.put(MenstrualRecord.DATE, december201989);
        mResolver.insert(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, values);
        values = new ContentValues();
        values.put(MenstrualRecord.DATE, december211989);
        mResolver.insert(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, values);
        values = new ContentValues();
        values.put(MenstrualRecord.DATE, november211989);
        mResolver.insert(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, values);
        long result = mInputTriggersFragment.getCycleDay(january11990);
        long expected = 13;
        assertEquals(expected, result);
    }

    @Test
    public void testGetCycleReturnsZeroIfBeforeDate(){
        long result = mInputTriggersFragment.getCycleDay(january11990);
        long expected = 0;
        assertEquals(expected, result);
    }

    @After
    public void tearDown(){
        String selection = MenstrualRecord.DATE + " IN (" + december201989 + ","
                + december211989 + "," + november211989 + ")";
        mResolver.delete(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, selection,  null);
    }

}