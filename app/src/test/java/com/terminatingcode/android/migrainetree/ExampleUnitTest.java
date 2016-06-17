package com.terminatingcode.android.migrainetree;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    GeoLookupParser glp;

    @Before
    public void initialise(){
        glp = new GeoLookupParser();
    }

    @Test
    public void nullJSONObjectReturnsNullMessage() {
        JSONObject j = null;
        String[] result = glp.parse(j);
        String[] expected = {"null JSONObject"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void lookupReturnsCityStateCountryOneLocation() throws JSONException {
        String s = "{\n" +
                "\t\"location\": {\n" +
                "\t\t\"type\":\"CITY\",\n" +
                "\t\t\"country\":\"US\",\n" +
                "\t\t\"country_iso3166\":\"US\",\n" +
                "\t\t\"country_name\":\"USA\",\n" +
                "\t\t\"state\":\"NY\",\n" +
                "\t\t\"city\":\"New York\",\n" +
                "\t}\n" +
                "}";
        JSONObject j = new JSONObject(s);
        String[] result = glp.parse(j);
        String[] expected = {"New York, NY, USA"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void lookupReturnsCityStateCountryMultipleLocations() throws JSONException {
        String s = "{\n" +
                "  \"response\": {\n" +
                "  \"version\":\"0.1\",\n" +
                "  \"termsofService\":\"http://www.wunderground.com/weather/api/d/terms.html\",\n" +
                "  \"features\": {\n" +
                "  \"geolookup\": 1\n" +
                "  }\n" +
                "\t\t, \"results\": [\n" +
                "\t\t{\n" +
                "\t\t\"name\": \"Paris\",\n" +
                "\t\t\"city\": \"Paris\",\n" +
                "\t\t\"state\": \"AR\",\n" +
                "\t\t\"country\": \"US\",\n" +
                "\t\t\"country_iso3166\":\"US\",\n" +
                "\t\t\"country_name\":\"USA\",\n" +
                "\t\t\"zmw\": \"72855.1.99999\",\n" +
                "\t\t\"l\": \"/q/zmw:72855.1.99999\"\n" +
                "\t\t}\n" +
                "\t\t,\n" +
                "\t\t{\n" +
                "\t\t\"name\": \"Paris\",\n" +
                "\t\t\"city\": \"Paris\",\n" +
                "\t\t\"state\": \"\",\n" +
                "\t\t\"country\": \"FR\",\n" +
                "\t\t\"country_iso3166\":\"FR\",\n" +
                "\t\t\"country_name\":\"France\",\n" +
                "\t\t\"zmw\": \"00000.37.07156\",\n" +
                "\t\t\"l\": \"/q/zmw:00000.37.07156\"\n" +
                "\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";
        JSONObject j = new JSONObject(s);
        String[] result = glp.parse(j);
        String[] expected = {"Paris, AR, USA", "Paris, FR, France"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void lookupReturnsQueryNotFound() throws JSONException {
        String s = "\n" +
                "{\n" +
                "  \"response\": {\n" +
                "  \"version\":\"0.1\",\n" +
                "  \"termsofService\":\"http://www.wunderground.com/weather/api/d/terms.html\",\n" +
                "  \"features\": {\n" +
                "  \"geolookup\": 1\n" +
                "  }\n" +
                "\t\t,\n" +
                "\t\"error\": {\n" +
                "\t\t\"type\": \"querynotfound\"\n" +
                "\t\t,\"description\": \"No cities match your search query\"\n" +
                "\t}\n" +
                "\t}\n" +
                "}";
        JSONObject j = new JSONObject(s);
        String[] expected = {"city not found"};
        String[] result = glp.parse(j);
        assertArrayEquals(expected, result);
    }

}