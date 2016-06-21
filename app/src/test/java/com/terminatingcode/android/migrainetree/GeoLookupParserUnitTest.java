package com.terminatingcode.android.migrainetree;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class GeoLookupParserUnitTest {

    GeoLookupParser glp;

    @Before
    public void initialise(){
        glp = new GeoLookupParser();
    }

    @Test
    public void nullJSONObjectReturnsNullMessage() throws JSONException {
        String[] result = glp.parse(null);
        String[] expected = {"null JSONObject"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void parseReturnsCityStateCountryOneLocation() throws JSONException {
        String s = "{\"response\":{\"version\":\"0.1\",\"termsofService\":\"http:\\/\\/www.wunderground.com\\/weather\\/api\\/d\\/terms.html\",\"features\":{\"geolookup\":1}},\"location\":{\"type\":\"INTLCITY\",\"country\":\"UK\",\"country_iso3166\":\"GB\",\"country_name\":\"United Kingdom\",\"state\":\"\",\"city\":\"Par\",\"tz_short\":\"BST\",\"tz_long\":\"Europe\\/London\",\"lat\":\"50.34999847\",\"lon\":\"-4.71666718\",\"zip\":\"00000\",\"magic\":\"8\",\"wmo\":\"03823\",\"l\":\"\\/q\\/zmw:00000.8.03823\"}}";
        JSONObject j = new JSONObject(s);
        String[] result = glp.parse(j);
        String[] expected = {"Par  United Kingdom"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void parseReturnsCityStateCountryMultipleLocations() throws JSONException {
        String s = "{\"response\":{\"version\":\"0.1\",\"termsofService\":\"http:\\/\\/www.wunderground.com\\/weather\\/api\\/d\\/terms.html\",\"features\":{\"geolookup\":1},\"results\":[{\"name\":\"Paris\",\"city\":\"Paris\",\"state\":\"AR\",\"country\":\"US\",\"country_iso3166\":\"US\",\"country_name\":\"USA\",\"zmw\":\"72855.1.99999\",\"l\":\"\\/q\\/zmw:72855.1.99999\"},{\"name\":\"Paris\",\"city\":\"Paris\",\"state\":\"\",\"country\":\"FR\",\"country_iso3166\":\"FR\",\"country_name\":\"France\",\"zmw\":\"00000.37.07156\",\"l\":\"\\/q\\/zmw:00000.37.07156\"}]}}";
        JSONObject j = new JSONObject(s);
        String[] result = glp.parse(j);
        String[] expected = {"Paris AR USA", "Paris  France"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void parseReturnsDots() throws JSONException {
        String s = "{\"response\":{\"version\":\"0.1\",\"termsofService\":\"http:\\/\\/www.wunderground.com\\/weather\\/api\\/d\\/terms.html\",\"features\":{\"geolookup\":1},\"error\":{\"type\":\"querynotfound\",\"description\":\"No cities match your search query\"}}}";
        JSONObject j = new JSONObject(s);
        String[] expected = {"..."};
        String[] result = glp.parse(j);
        assertArrayEquals(expected, result);
    }

    @Test
    public void getUIDReturnsUID() throws JSONException {
        String s = "{\"response\":{\"version\":\"0.1\",\"termsofService\":\"http:\\/\\/www.wunderground.com\\/weather\\/api\\/d\\/terms.html\",\"features\":{\"geolookup\":1}},\"location\":{\"type\":\"INTLCITY\",\"country\":\"UK\",\"country_iso3166\":\"GB\",\"country_name\":\"United Kingdom\",\"state\":\"\",\"city\":\"Par\",\"tz_short\":\"BST\",\"tz_long\":\"Europe\\/London\",\"lat\":\"50.34999847\",\"lon\":\"-4.71666718\",\"zip\":\"00000\",\"magic\":\"8\",\"wmo\":\"03823\",\"l\":\"\\/q\\/zmw:00000.8.03823\"}}";
        JSONObject j = new JSONObject(s);
        String expected = "/q\\/zmw:00000.8.03823";
        String result = glp.getUID(j);
        assertEquals(expected, result);
    }

}