package com.terminatingcode.android.migrainetree.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Methods to convert dates to Strings and String to longs
 * Created by Sarah on 8/10/2016.
 */
public class DateUtils {

    /**
     * formats the date in UTC milliseconds into the String format
     * @param milliseconds the UTC date
     * @return String formatted date
     */
    public static String convertLongToString(long milliseconds){
        if(milliseconds != Constants.DEFAULT_NO_DATA) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy\nhh:mm", Locale.getDefault());
            Date date = new Date(milliseconds);
            return df.format(date);
        }
        return null;
    }

    /**
     * converts formatted String back into UTC milliseconds
     * @param dateTime the formatted date String
     * @return the UTC milliseconds
     * @throws ParseException to be handled by the Fragment to notify the user no date was set
     */
    public static long convertStringToLong(String dateTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyyhh:mm", Locale.getDefault());
        Date date = df.parse(dateTime);
        return date.getTime();
    }

    public static long convertListStringToLong(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM, yyyy", Locale.getDefault());
        Date date = dateFormat.parse(dateString);
        return date.getTime();
    }

    public static String convertLongToListString(long startHour){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM, yyyy", Locale.getDefault());
        Date date = new Date(startHour);
        return dateFormat.format(date);
    }

    public static int convertLongToHourOfDay(long milliseconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}
