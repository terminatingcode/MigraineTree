package com.terminatingcode.android.migrainetree;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Sarah on 8/10/2016.
 */
public class DateUtilsTest {

    @Test
    public void testConvertLongToString() throws Exception {

        long jan_1_2016_3_58 = 1451620680000L;
        String expected = "01/01/2016\n03:58";
        String result = DateUtils.convertLongToString(jan_1_2016_3_58);
        assertEquals(expected, result);
    }

    @Test
    public void testConvertStringToLong() throws Exception {
        String source = "01/01/201603:58";
        long result = DateUtils.convertStringToLong(source);
        long expected = 1451620680000L;
        assertEquals(expected, result);
    }
}