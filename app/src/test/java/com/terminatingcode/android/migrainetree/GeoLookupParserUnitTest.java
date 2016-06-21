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
    public void lookupReturnsCityStateCountryOneLocation() throws JSONException {
        String s = "{\"location\": {\"type\":\"CITY\",\"country\":\"US\",\"country_iso3166\":\"US\",\"country_name\":\"USA\",\"state\":\"NY\",\"city\":\"New York\",}}";
        JSONObject j = new JSONObject(s);
        String[] result = glp.parse(j);
        String[] expected = {"New York NY USA"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void lookupReturnsCityStateCountryMultipleLocations() throws JSONException {
        String s = "{\"features\":{\"geolookup\":1},\"results\":"
                + "[{\"name\": \"Paris\",\"city\": \"Paris\",\"state\": \"AR\",\"country\": \"US\",\"country_iso3166\":\"US\",\"country_name\":\"USA\"," +
                "\"zmw\": \"72855.1.99999\"," +
                "\"l\": \"/q/zmw:72855.1.99999\"}," +
                "{\"name\": \"Paris\"," +
                "\"city\": \"Paris\"," +
                "\"state\": \"\"," +
                "\"country\": \"FR\"," +
                "\"country_iso3166\":\"FR\"," +
                "\"country_name\":\"France\"," +
                "\"zmw\": \"00000.37.07156\"," +
                "\"l\": \"/q/zmw:00000.37.07156\"}" +
                "]" +
                "}";
        JSONObject j = new JSONObject(s);
        String[] result = glp.parse(j);
        String[] expected = {"Paris AR USA", "Paris  France"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void lookupReturnsQueryNotFound() throws JSONException {
        String s = "{\"error\": {\"type\": \"querynotfound\",\"description\": \"No cities match your search query\"}}}";
        JSONObject j = new JSONObject(s);
        String[] expected = {"city not found"};
        String[] result = glp.parse(j);
        assertArrayEquals(expected, result);
    }

}