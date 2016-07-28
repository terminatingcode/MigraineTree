package com.terminatingcode.android.migrainetree;

import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;
import com.terminatingcode.android.migrainetree.Weather.WeatherHour;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Tests Weather24Hour
 * Created by Sarah on 6/24/2016.
 */
public class Weather24HourTest {

    private Weather24Hour mWeather24Hour;
    private WeatherHour mWeatherHour;

    @Before
    public void setUp(){
        mWeather24Hour = new Weather24Hour();
        mWeatherHour = new WeatherHour();
    }

    @Test
    public void testAddHour() throws Exception {
        mWeather24Hour.addHour(mWeatherHour);
        int expected = 1;
        int result = mWeather24Hour.getHours().size();
        assertEquals(expected, result);
    }

    @Test
    public void testAddHourWontExceed24() throws Exception {
        for(int i = 0; i < 30; i++){
            mWeather24Hour.addHour(mWeatherHour);
        }
        int expected = 24;
        int result = mWeather24Hour.getHours().size();
        assertEquals(expected, result);
    }

    @Test (expected = RuntimeException.class)
    public void testCalculateChangesThrowsExceptionIfNotEnoughHoursParsed() throws Exception {
        mWeather24Hour.calculateChanges();
    }

    @Test
    public void testCalculateChanges(){
        WeatherHour mockHourMinus24Hours =  initialiseMockWeatherHour(10.0, 1468972800000L);
        WeatherHour mockHourMinus12Hours = initialiseMockWeatherHour(-5.0, 1468976400000L);
        WeatherHour mockHourOfMigraine = initialiseMockWeatherHour(20.0, 1468980000000L);
        for(int i = 0; i < 8; i++){
            mWeather24Hour.addHour(mockHourMinus24Hours);
        }
        for(int i = 0; i < 8; i++){
            mWeather24Hour.addHour(mockHourMinus12Hours);
        }
        for(int i = 0; i < 8; i++){
            mWeather24Hour.addHour(mockHourOfMigraine);
        }

        double expected12Hrs = 25.0;
        double expected24Hrs = 10.0;
        mWeather24Hour.calculateChanges();
        assertEquals(expected12Hrs, mWeather24Hour.getApChange12Hrs());
        assertEquals(expected24Hrs, mWeather24Hour.getApChange24Hrs());
        assertEquals(expected12Hrs, mWeather24Hour.getHumChange12Hrs());
        assertEquals(expected24Hrs, mWeather24Hour.getHumChange24Hrs());
        assertEquals(expected12Hrs, mWeather24Hour.getTempChange12Hrs());
        assertEquals(expected24Hrs, mWeather24Hour.getTempChange24Hrs());
        assertEquals(expected12Hrs, mWeather24Hour.getWspdChange12Hrs());
        assertEquals(expected24Hrs, mWeather24Hour.getWspdChange24Hrs());

    }

    private WeatherHour initialiseMockWeatherHour(double x, long milliseconds){
        WeatherHour mockHour = new WeatherHour();
        Date date = new Date(milliseconds);
        mockHour.setHourStart(date);
        mockHour.setTemp(x);
        mockHour.setHum(x);
        mockHour.setWspd(x);
        mockHour.setPressure(x);
        return mockHour;
    }
}