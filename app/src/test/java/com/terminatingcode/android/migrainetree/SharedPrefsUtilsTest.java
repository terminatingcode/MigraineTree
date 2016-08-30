package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.terminatingcode.android.migrainetree.utils.Constants;
import com.terminatingcode.android.migrainetree.utils.SharedPrefsUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests for SharedPrefsUtils
 * SharedPrefsUtils's needsLocationSpecified returns
 * True if no city saved in SharedPreferences
 * Created by Sarah on 6/20/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest=Config.NONE)
public class SharedPrefsUtilsTest {
    private SharedPrefsUtils mSharedPrefsUtils;
    private SharedPreferences mPrefs;
    private final String location = "location";
    private final String uid = "testUID";
    private final boolean menstrualPref = false;

    @Before
    public void setUp(){
        Context context = RuntimeEnvironment.application.getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mSharedPrefsUtils = new SharedPrefsUtils(mPrefs);
    }

    @Test
    public void testNeedsLocationSavedReturnsTrue() {
        assertTrue(mSharedPrefsUtils.needLocationSpecified());
    }

    @Test
    public void testNeedsLocationSavedReturnsFalse(){
        mPrefs.edit().putString(location, uid).commit();
        assertFalse(mSharedPrefsUtils.needLocationSpecified());
    }

    @Test
    public void testSaveSelectedCity(){
        mSharedPrefsUtils.saveSelectedCity(location, uid);
        String resultLocation = mPrefs.getString(Constants.LOCATION_NAME, null);
        String resultuid = mPrefs.getString(Constants.LOCATIONUID, null);
        assertEquals(location, resultLocation);
        assertEquals(uid, resultuid);
    }

    @Test
    public void testGetSavedCity(){
        mPrefs.edit().putString(Constants.LOCATION_NAME, location).apply();
        String result = mSharedPrefsUtils.getSavedCity();
        assertEquals(location, result);
    }

    @Test
    public void testGetLocationUID(){
        mPrefs.edit().putString(Constants.LOCATIONUID, uid).apply();
        String result = mSharedPrefsUtils.getSavedLocationUID();
        assertEquals(uid, result);
    }

    @Test
    public void testSaveMenstrualDataPrefs(){
        mSharedPrefsUtils.saveMenstrualDataPrefs(menstrualPref);
        boolean resultpref = mPrefs.getBoolean(Constants.SAVE_MENSTRUAL_DATA, true);
        assertEquals(menstrualPref, resultpref);
    }

    @Test
    public void testGetMenstrualPref(){
        mPrefs.edit().putBoolean(Constants.SAVE_MENSTRUAL_DATA, menstrualPref).commit();
        boolean result = mSharedPrefsUtils.getsavedMenstrualPref();
        assertEquals(menstrualPref, result);
    }

    @After
    public void cleanUp(){
        mPrefs.edit().clear().commit();
    }
}
