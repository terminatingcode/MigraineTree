package com.terminatingcode.android.migrainetree;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores data for 24 hour period leading up to a migraine occurrence
 * Created by Sarah on 6/24/2016.
 */
public class Weather24Hour {

    private List<WeatherHour> mHours;
    private double mApChange24Hrs;
    private double mApChange12Hrs;
    private double mHumChange24Hrs;
    private double mHumChange12Hrs;
    private double mTempChange24Hrs;
    private double mTempChange12Hrs;
    private double mWspdChange24Hrs;
    private double mWspdChange12Hrs;

    public Weather24Hour(){
        mHours = new ArrayList<>();
    }

    public void addHour(WeatherHour hour){
        if(mHours.size() != 24) mHours.add(hour);
    }

    public List<WeatherHour> getHours() {
        return mHours;
    }

    /**
     * Calculates the change in values
     * 24 hours before migraine minus the start hour of the migraine
     * @throws RuntimeException when method called and 24 hours have not been added
     */
    public void calculateChanges() throws RuntimeException{
        if(mHours.size() == 24){
            WeatherHour one = mHours.get(23);
            WeatherHour twelve = mHours.get(11);
            WeatherHour twentyFour = mHours.get(0);
            mApChange24Hrs = one.getPressure() - twentyFour.getPressure();
            mApChange12Hrs = one.getPressure() - twelve.getPressure();
            mHumChange24Hrs = one.getHum() - twentyFour.getHum();
            mHumChange12Hrs = one.getHum() - twelve.getHum();
            mTempChange24Hrs = one.getTemp() - twentyFour.getTemp();
            mTempChange12Hrs = one.getTemp() - twelve.getTemp();
            mWspdChange24Hrs = one.getWspd() - twentyFour.getWspd();
            mWspdChange12Hrs = one.getWspd() - twelve.getWspd();
        }else{
            throw new RuntimeException("not enough hours parsed and placed in List");
        }
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
