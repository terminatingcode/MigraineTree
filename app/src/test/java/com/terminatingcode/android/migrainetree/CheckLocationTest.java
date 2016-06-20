package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests for CheckLocation
 * CheckLocation's needsLocationSpecified returns
 * True if no city saved in SharedPreferences
 * Created by Sarah on 6/20/2016.
 */
public class CheckLocationTest {
    private CheckLocation mCheckLocation;
    private SharedPreferences mPrefs;
    private Context context;

    @Before
    public void setUp(){
        mCheckLocation = new CheckLocation();
        mPrefs = Mockito.mock(SharedPreferences.class);
        context = Mockito.mock(Context.class);
        Mockito
                .when(context.getSharedPreferences(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(mPrefs);
    }

    @Test
    public void testNeedsLocationSavedReturnsTrue() {
        final String cityNotSaved = "city not saved";
        Mockito.when(mPrefs.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(cityNotSaved);
        assertTrue(mCheckLocation.needLocationSpecified(context));
    }

    @Test
    public void testNeedsLocationSavedReturnsFalse(){
        final String city = "city";
        Mockito.when(mPrefs.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(city);
        assertFalse(mCheckLocation.needLocationSpecified(context));
    }
}
