package com.terminatingcode.android.migrainetree;

import android.app.Activity;
import android.content.ContentValues;

import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MenstrualRecord;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
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

    private Activity mActivity;
    private InputTriggersFragment mInputTriggersFragment;

    @Before
    public void setup(){
        mActivity = Robolectric.setupActivity(MainActivity.class);
        mInputTriggersFragment = new InputTriggersFragment();
        SupportFragmentTestUtil.startFragment(mInputTriggersFragment);
    }

    @Test
    public void testGetCycleDay(){
        long january11990 = 631152000000L;
        long december201989 = 630115200000L;
        ContentValues values = new ContentValues();
        values.put(MenstrualRecord.DATE, december201989);
        RuntimeEnvironment.application.getContentResolver().insert(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, values);
        long result = mInputTriggersFragment.getCycleDay(january11990);
        long expected = 13;
        assertEquals(expected, result);
    }

    @Test
    public void testgetCycleDayDoesntAccountForPreviousCycle(){
        long january11990 = 631152000000L;
        long december201989 = 630115200000L;
        long december211989 = 630201600000L;
        long november211989 = 627609600000L;
        ContentValues values = new ContentValues();
        values.put(MenstrualRecord.DATE, december201989);
        values.put(MenstrualRecord.DATE, december211989);
        values.put(MenstrualRecord.DATE, november211989);
        RuntimeEnvironment.application.getContentResolver().insert(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, values);
        long result = mInputTriggersFragment.getCycleDay(january11990);
        long expected = 13;
        assertEquals(expected, result);
    }

}