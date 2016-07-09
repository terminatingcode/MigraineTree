package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowPreferenceManager;

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

    @Before
    public void setUp(){
        Context context = RuntimeEnvironment.application.getApplicationContext();
        mPrefs = ShadowPreferenceManager.getDefaultSharedPreferences(context);
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
    public void getSavedCity(){
        mPrefs.edit().putString(Constants.LOCATION_NAME, location).apply();
        String result = mSharedPrefsUtils.getSavedCity();
        assertEquals(location, result);
    }

    @After
    public void cleanUp(){
        mPrefs.edit().clear().commit();
    }
}
