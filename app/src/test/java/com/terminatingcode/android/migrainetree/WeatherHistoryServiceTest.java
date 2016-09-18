package com.terminatingcode.android.migrainetree;

import com.terminatingcode.android.migrainetree.model.weather.WeatherHistoryService;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Sarah on 7/28/2016.
 */
public class WeatherHistoryServiceTest {

    @Test
    public void testMinusDay() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.add(Calendar.DAY_OF_MONTH, -1);
        Date expectedDate = expectedCalendar.getTime();
        String expected = df.format(expectedDate);
        String result = WeatherHistoryService.minusDay(calendar, df);
        assertEquals(expected, result);
    }
}