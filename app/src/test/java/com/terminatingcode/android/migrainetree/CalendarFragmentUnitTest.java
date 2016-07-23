package com.terminatingcode.android.migrainetree;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;

/**
 * Unit Tests for helper methods in CalendarFragment
 * Created by Sarah on 7/21/2016.
 */
public class CalendarFragmentUnitTest {

    @Test
    public void testCalculateDayInCycle() throws Exception {
        Calendar calendar = new GregorianCalendar(2016, 7, 21);
        HashSet<Calendar> events = new HashSet<>();
        events.add(calendar);
        Date date = calendar.getTime();
        int expected = 1;
        int result = CalendarFragment.calculateDayInCycle(date, events);
        assertEquals(expected, result);

        Calendar yesterday =  new GregorianCalendar(2016, 7, 20);
        events.add(yesterday);
        expected = 2;
        result = CalendarFragment.calculateDayInCycle(date, events);
        assertEquals(expected, result);
    }
}