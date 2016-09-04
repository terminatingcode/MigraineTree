package com.terminatingcode.android.migrainetree;

import com.terminatingcode.android.migrainetree.model.weather.CitiesMapSingleton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests CitiesMapSingleton
 * Created by Sarah on 6/22/2016.
 */
public class CitiesMapSingletonTest {

    private CitiesMapSingleton mCitiesMapSingleton;

    @Before
    public void setup(){
        mCitiesMapSingleton = CitiesMapSingleton.newInstance();
    }

    @Test
    public void testNewInstance() throws Exception {
        assertNotNull(mCitiesMapSingleton);
        assertNotNull(mCitiesMapSingleton.getCitiesUID());
    }

    @Test
    public void testGetCitiesUID() throws Exception {
        Class expected = HashMap.class;
        assertEquals(expected, mCitiesMapSingleton.getCitiesUID().getClass());
    }

    @Test
    public void testPut() throws Exception {
        Map map = mCitiesMapSingleton.getCitiesUID();
        mCitiesMapSingleton.clearMap();
        mCitiesMapSingleton.put("test", "test");
        int expected = 1;
        assertEquals(expected, map.size());
    }

    @Test
    public void testGetUID() throws Exception {
        mCitiesMapSingleton.put("test", "test");
        String expected = "test";
        assertEquals(expected, mCitiesMapSingleton.getUID("test"));
    }

    @After
    public void cleanUp(){
        mCitiesMapSingleton.clearMap();
    }
}