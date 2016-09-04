package com.terminatingcode.android.migrainetree.model.weather;

import android.util.Log;

import com.terminatingcode.android.migrainetree.utils.Constants;
import com.terminatingcode.android.migrainetree.model.eventBus.Weather24HourMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Stores data for the 24 hour period leading up to a migraine occurrence
 * Created by Sarah on 6/24/2016.
 */
public class Weather24Hour {

    private static final String NAME = "Weather24Hour";
    private Calendar migraineStart;
    private List<WeatherHour> mHours;
    private double mApChange24Hrs = Constants.DEFAULT_NO_DATA;
    private double mApChange12Hrs = Constants.DEFAULT_NO_DATA;
    private double mApChange3Hrs = Constants.DEFAULT_NO_DATA;
    private double mHumChange24Hrs = Constants.DEFAULT_NO_DATA;
    private double mHumChange12Hrs = Constants.DEFAULT_NO_DATA;
    private double mHumChange3Hrs = Constants.DEFAULT_NO_DATA;
    private double mTempChange24Hrs = Constants.DEFAULT_NO_DATA;
    private double mTempChange12Hrs = Constants.DEFAULT_NO_DATA;
    private double mTempChange3Hrs = Constants.DEFAULT_NO_DATA;
    private double mWspdChange24Hrs = Constants.DEFAULT_NO_DATA;
    private double mWspdChange12Hrs = Constants.DEFAULT_NO_DATA;
    private double mWspdChange3Hrs = Constants.DEFAULT_NO_DATA;
    private WeatherHour currentHour;

    public Weather24Hour(){
        mHours = new ArrayList<>();
    }

    public WeatherHour getCurrentHour() {
        return currentHour;
    }

    public void addHour(WeatherHour hour){
        if(mHours.size() == 23){
            mHours.add(hour);
            EventBus.getDefault().post(new Weather24HourMessageEvent(this));
            Log.d(NAME, "finished service: " + mHours.size());
        }else if(mHours.size() != 24){
            mHours.add(hour);
        }
    }

    /**
     * WeatherUnderground sometimes returns duplicates for hours
     * @param hour the hour just parsed
     * @return if the list of hours already contains that hour
     */
    public boolean contains(WeatherHour hour){
        for(WeatherHour weatherHour : mHours){
            if(weatherHour.getHourStart().compareTo(hour.getHourStart()) == 0)
                return true;
        }
        return false;
    }

    public List<WeatherHour> getHours() {
        return mHours;
    }
    public int getSize(){ return mHours.size();}

    /**
     * Calculates the change in values
     * 24 hours before migraine minus the start hour of the migraine
     * @throws RuntimeException when method called and 24 hours have not been added
     */
    public void calculateChanges() throws RuntimeException{
        //make sure hours are sorted by date
        if(mHours.size() == 24){
            Collections.sort(mHours, new Comparator<WeatherHour>() {
                @Override
                public int compare(WeatherHour lhs, WeatherHour rhs) {
                    return lhs.getHourStart().compareTo(rhs.getHourStart());
                }
            });
            for(WeatherHour hour : mHours){
//                Log.d(NAME, hour.getHourStart().get(Calendar.DAY_OF_MONTH) + ", " + hour.getHourStart().get(Calendar.HOUR_OF_DAY));
            }
            WeatherHour one = mHours.get(23);
            currentHour = one;
//            Log.d(NAME, "one: " + one.getHourStart().get(Calendar.DAY_OF_MONTH) + one.getHourStart().get(Calendar.HOUR_OF_DAY));
            WeatherHour twelve = mHours.get(11);
//            Log.d(NAME, "twelve: " + twelve.getHourStart().get(Calendar.DAY_OF_MONTH) + twelve.getHourStart().get(Calendar.HOUR_OF_DAY));
            WeatherHour three = mHours.get(2);
//            Log.d(NAME, "three: " + three.getHourStart().get(Calendar.DAY_OF_MONTH) + twelve.getHourStart().get(Calendar.HOUR_OF_DAY));
            WeatherHour twentyFour = mHours.get(0);
//            Log.d(NAME, "24: " + twentyFour.getHourStart().get(Calendar.DAY_OF_MONTH) + twentyFour.getHourStart().get(Calendar.HOUR_OF_DAY));
            mApChange24Hrs = one.getPressure() - twentyFour.getPressure();
            mApChange12Hrs = one.getPressure() - twelve.getPressure();
            mApChange3Hrs = one.getPressure() - three.getPressure();
            mHumChange24Hrs = one.getHum() - twentyFour.getHum();
            mHumChange12Hrs = one.getHum() - twelve.getHum();
            mHumChange3Hrs = one.getHum() - three.getHum();
            mTempChange24Hrs = one.getTemp() - twentyFour.getTemp();
            mTempChange12Hrs = one.getTemp() - twelve.getTemp();
            mTempChange3Hrs = one.getHum() - three.getHum();
            mWspdChange24Hrs = one.getWspd() - twentyFour.getWspd();
            mWspdChange12Hrs = one.getWspd() - twelve.getWspd();
            mWspdChange3Hrs = one.getWspd() - three.getWspd();
        }else{
            throw new RuntimeException("not enough hours parsed and placed in List");
        }
    }

    public Calendar getMigraineStart() {
        return migraineStart;
    }

    public void setMigraineStart(Calendar migraineStart) {
        this.migraineStart = migraineStart;
    }

    public double getApChange3Hrs() {
        return mApChange3Hrs;
    }

    public double getHumChange3Hrs() {
        return mHumChange3Hrs;
    }

    public double getTempChange3Hrs() {
        return mTempChange3Hrs;
    }

    public double getWspdChange3Hrs() {
        return mWspdChange3Hrs;
    }
    public double getApChange24Hrs() {
        return mApChange24Hrs;
    }

    public void setApChange24Hrs(double apChange24Hrs) {
        this.mApChange24Hrs = apChange24Hrs;
    }

    public double getApChange12Hrs() {
        return mApChange12Hrs;
    }

    public void setApChange12Hrs(double apChange12Hrs) {
        this.mApChange12Hrs = apChange12Hrs;
    }

    public double getHumChange24Hrs() {
        return mHumChange24Hrs;
    }

    public void setHumChange24Hrs(double humChange24Hrs) {
        this.mHumChange24Hrs = humChange24Hrs;
    }

    public double getHumChange12Hrs() {
        return mHumChange12Hrs;
    }

    public void setHumChange12Hrs(double humChange12Hrs) {
        this.mHumChange12Hrs = humChange12Hrs;
    }

    public double getTempChange24Hrs() {
        return mTempChange24Hrs;
    }

    public void setTempChange24Hrs(double tempChange24Hrs) {
        this.mTempChange24Hrs = tempChange24Hrs;
    }

    public double getTempChange12Hrs() {
        return mTempChange12Hrs;
    }

    public void setTempChange12Hrs(double tempChange12Hrs) {
        this.mTempChange12Hrs = tempChange12Hrs;
    }

    public double getWspdChange24Hrs() {
        return mWspdChange24Hrs;
    }

    public void setWspdChange24Hrs(double wspdChange24Hrs) {
        this.mWspdChange24Hrs = wspdChange24Hrs;
    }

    public double getWspdChange12Hrs() {
        return mWspdChange12Hrs;
    }

    public void setWspdChange12Hrs(double wspdChange12Hrs) {
        this.mWspdChange12Hrs = wspdChange12Hrs;
    }
}
