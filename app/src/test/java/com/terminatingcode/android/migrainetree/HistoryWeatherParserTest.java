package com.terminatingcode.android.migrainetree;

import com.terminatingcode.android.migrainetree.Weather.HistoryWeatherParser;
import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;
import com.terminatingcode.android.migrainetree.Weather.WeatherHour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Unit Tests for HistoryWeatherParser
 * Created by Sarah on 6/23/2016.
 */
public class HistoryWeatherParserTest {

    private HistoryWeatherParser mHistoryWeatherParser;
    private  JSONObject mJSONObject;
    private JSONObject mJSONObjectHistory;
    private WeatherHour mMockHour;
    private Date mStartHour;
    private JSONArray mJsonHours;
    private JSONObject mJsonHour;
    private Weather24Hour mWeather24Hour;
    private Calendar mCalendar;
    private String testQuery = "{\"response\": {" +
            "\"version\":\"0.1\"," +
            "\"termsofService\":\"http://www.wunderground.com/weather/api/d/terms.html\"," +
            "\"features\": {" +
            "\"history\": 1}}," +
            "\"history\": {" +
            "\"date\": {" +
            "\"pretty\": \"June 23, 2016\"," +
            "\"year\": \"2016\"," +
            "\"mon\": \"06\"," +
            "\"mday\": \"23\"," +
            "\"hour\": \"00\"," +
            "\"min\": \"56\"," +
            "\"tzname\": \"America/Los_Angeles\"}," +
            "\"utcdate\": {" +
            "\"pretty\": \"June 23, 2016\"," +
            "\"year\": \"2016\"," +
            "\"mon\": \"06\"," +
            "\"mday\": \"23\"," +
            "\"hour\": \"07\"," +
            "\"min\": \"56\"," +
            "\"tzname\": \"UTC\"}," +
            "\"observations\": [{" +
            "\"date\": {" +
            "\"pretty\": \"12:56 AM PDT on June 23, 2016\"," +
            "\"year\": \"2016\"," +
            "\"mon\": \"06\"," +
            "\"mday\": \"23\"," +
            "\"hour\": \"00\"," +
            "\"min\": \"56\"," +
            "\"tzname\": \"America/Los_Angeles\"}," +
            "\"utcdate\": {" +
            "\"pretty\": \"7:56 AM GMT on June 23, 2016\"," +
            "\"year\": \"2016\"," +
            "\"mon\": \"06\"," +
            "\"mday\": \"23\"," +
            "\"hour\": \"07\"," +
            "\"min\": \"56\"," +
            "\"tzname\": \"UTC\"}," +
            "\"tempm\":\"12.8\", \"tempi\":\"55.0\",\"dewptm\":\"10.0\", \"dewpti\":\"50.0\",\"hum\":\"83\",\"wspdm\":\"20.4\", \"wspdi\":\"12.7\",\"wgustm\":\"-9999.0\", \"wgusti\":\"-9999.0\",\"wdird\":\"280\",\"wdire\":\"West\",\"vism\":\"16.1\", \"visi\":\"10.0\",\"pressurem\":\"1016.9\", \"pressurei\":\"30.03\",\"windchillm\":\"-999\", \"windchilli\":\"-999\",\"heatindexm\":\"-9999\", \"heatindexi\":\"-9999\",\"precipm\":\"-9999.00\", \"precipi\":\"-9999.00\",\"conds\":\"Partly Cloudy\",\"icon\":\"partlycloudy\",\"fog\":\"0\",\"rain\":\"0\",\"snow\":\"0\",\"hail\":\"0\",\"thunder\":\"0\",\"tornado\":\"0\",\"metar\":\"METAR KSFO 230756Z 28011KT 10SM FEW012 13/10 A3003 RMK AO2 SLP169 T01280100 401830128\"}," +
            "{\"date\": {" +
            "\"pretty\": \"1:56 AM PDT on June 23, 2016\"," +
            "\"year\": \"2016\"," +
            "\"mon\": \"06\"," +
            "\"mday\": \"23\"," +
            "\"hour\": \"01\"," +
            "\"min\": \"56\"," +
            "\"tzname\": \"America/Los_Angeles\"}," +
            "\"utcdate\": {" +
            "\"pretty\": \"8:56 AM GMT on June 23, 2016\"," +
            "\"year\": \"2016\"," +
            "\"mon\": \"06\"," +
            "\"mday\": \"23\"," +
            "\"hour\": \"08\"," +
            "\"min\": \"56\"," +
            "\"tzname\": \"UTC\"}," +
            "\"tempm\":\"13.3\", \"tempi\":\"55.9\",\"dewptm\":\"10.0\", \"dewpti\":\"50.0\",\"hum\":\"80\",\"wspdm\":\"20.4\", \"wspdi\":\"12.7\",\"wgustm\":\"-9999.0\", \"wgusti\":\"-9999.0\",\"wdird\":\"270\",\"wdire\":\"West\",\"vism\":\"16.1\", \"visi\":\"10.0\",\"pressurem\":\"1017.2\", \"pressurei\":\"30.04\",\"windchillm\":\"-999\", \"windchilli\":\"-999\",\"heatindexm\":\"-9999\", \"heatindexi\":\"-9999\",\"precipm\":\"-9999.00\", \"precipi\":\"-9999.00\",\"conds\":\"Partly Cloudy\",\"icon\":\"partlycloudy\",\"fog\":\"0\",\"rain\":\"0\",\"snow\":\"0\",\"hail\":\"0\",\"thunder\":\"0\",\"tornado\":\"0\",\"metar\":\"METAR KSFO 230856Z 27011KT 10SM FEW011 13/10 A3004 RMK AO2 SLP172 T01330100 53004\"}," +
            "]," +
            "\"dailysummary\": [" +
            "{ \"date\": {" +
            "\"pretty\": \"12:56 AM PDT on June 23, 2016\"," +
            "\"year\": \"2016\"," +
            "\"mon\": \"06\"," +
            "\"mday\": \"23\"," +
            "\"hour\": \"00\"," +
            "\"min\": \"56\"," +
            "\"tzname\": \"America/Los_Angeles\"}," +
            "\"fog\":\"0\",\"rain\":\"0\",\"snow\":\"0\",\"snowfallm\":\"0.00\", \"snowfalli\":\"0.00\",\"monthtodatesnowfallm\":\"0.00\", \"monthtodatesnowfalli\":\"0.00\",\"since1julsnowfallm\":\"0.00\", \"since1julsnowfalli\":\"0.00\",\"snowdepthm\":\"0.00\", \"snowdepthi\":\"0.00\",\"hail\":\"0\",\"thunder\":\"0\",\"tornado\":\"0\",\"meantempm\":\"18\", \"meantempi\":\"64\",\"meandewptm\":\"9\", \"meandewpti\":\"49\",\"meanpressurem\":\"1018.12\", \"meanpressurei\":\"30.07\",\"meanwindspdm\":\"25\", \"meanwindspdi\":\"15\",\"meanwdire\":\"WNW\",\"meanwdird\":\"285\",\"meanvism\":\"16.1\", \"meanvisi\":\"10.0\",\"humidity\":\"64\",\"maxtempm\":\"23\", \"maxtempi\":\"73\",\"mintempm\":\"12\", \"mintempi\":\"54\",\"maxhumidity\":\"89\",\"minhumidity\":\"40\",\"maxdewptm\":\"11\", \"maxdewpti\":\"52\",\"mindewptm\":\"8\", \"mindewpti\":\"46\",\"maxpressurem\":\"1019\", \"maxpressurei\":\"30.09\",\"minpressurem\":\"1017\", \"minpressurei\":\"30.03\",\"maxwspdm\":\"61\", \"maxwspdi\":\"38\",\"minwspdm\":\"15\", \"minwspdi\":\"9\",\"maxvism\":\"16.1\", \"maxvisi\":\"10.0\",\"minvism\":\"16.1\", \"minvisi\":\"10.0\",\"gdegreedays\":\"14\",\"heatingdegreedays\":\"2\",\"coolingdegreedays\":\"0\",\"precipm\":\"0.0\", \"precipi\":\"0.00\",\"precipsource\":\"3Or6HourObs\",\"heatingdegreedaysnormal\":\"3\",\"monthtodateheatingdegreedays\":\"40\",\"monthtodateheatingdegreedaysnormal\":\"84\",\"since1sepheatingdegreedays\":\"\",\"since1sepheatingdegreedaysnormal\":\"\",\"since1julheatingdegreedays\":\"1933\",\"since1julheatingdegreedaysnormal\":\"2632\",\"coolingdegreedaysnormal\":\"1\",\"monthtodatecoolingdegreedays\":\"18\",\"monthtodatecoolingdegreedaysnormal\":\"20\",\"since1sepcoolingdegreedays\":\"\",\"since1sepcoolingdegreedaysnormal\":\"\",\"since1jancoolingdegreedays\":\"49\",\"since1jancoolingdegreedaysnormal\":\"39\" }]}}";

    @Before
    public void setUp() throws Exception {
        mHistoryWeatherParser = new HistoryWeatherParser();
        mJSONObject = new JSONObject(testQuery);
        mJSONObjectHistory = mJSONObject.getJSONObject("history");
        Long milliseconds = 1466668560000L;
        Long migraineStart = 1466726390000L;
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(migraineStart);
        mStartHour = new Date(milliseconds);
        mJsonHours = mJSONObjectHistory.getJSONArray("observations");
        mJsonHour = mJsonHours.getJSONObject(0);
        mMockHour = initialiseMockWeatherHour();
        mWeather24Hour = new Weather24Hour();
    }


    @Test (expected = JSONException.class)
    public void nullJSONObjectParseThrowsJSONException() throws JSONException, ParseException {
        mHistoryWeatherParser.parse(null, new Weather24Hour(), mCalendar);
    }

    @Test (expected = IllegalArgumentException.class)
    public void nullJSONObjectParseThrowsIllegalArgumentException() throws JSONException, ParseException {
        mHistoryWeatherParser.parse(mJSONObjectHistory, null, mCalendar);
    }
    
    @Test
    public void parsePopulatesWeather24Hour() throws JSONException, ParseException {
        mHistoryWeatherParser.parse(mJSONObject, mWeather24Hour, mCalendar);
        int expected = 2;
        int result = mWeather24Hour.getHours().size();
        assertEquals(expected, result);
    }

    @Test (expected = JSONException.class)
    public void nullJSONObjectParseHourThrowsException() throws JSONException, ParseException {
        mHistoryWeatherParser.parseHour(null, mCalendar);
    }

    @Test
    public void parseHourReturnsWeatherHour() throws JSONException, ParseException {
        WeatherHour mWeatherHour = mHistoryWeatherParser.parseHour(mJsonHour, mCalendar);
        assertEquals(mMockHour, mWeatherHour);
    }

    @Test
    public void parseDateReturnsDate() throws JSONException, ParseException {
        JSONObject date = mJsonHour.getJSONObject("utcdate");
        Date result = mHistoryWeatherParser.parseDate(date);
        assertEquals(mStartHour, result);
    }

    private WeatherHour initialiseMockWeatherHour(){
        WeatherHour mockHour = new WeatherHour();
        mockHour.setHourStart(mStartHour);
        mockHour.setTemp(12.8);
        mockHour.setDewpt(10.0);
        mockHour.setHum(83);
        mockHour.setWspd(20.4);
        mockHour.setWdir(280);
        mockHour.setVis(16.1);
        mockHour.setPressure(1016.9);
        mockHour.setWindchill(-999);
        mockHour.setHeatindex(-9999);
        mockHour.setPreci(-9999.00);
        mockHour.setFog(false);
        mockHour.setRain(false);
        mockHour.setSnow(false);
        mockHour.setHail(false);
        mockHour.setThunder(false);
        mockHour.setTornado(false);
        return mockHour;

    }
}