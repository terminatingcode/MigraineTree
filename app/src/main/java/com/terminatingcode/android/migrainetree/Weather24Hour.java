package com.terminatingcode.android.migrainetree;

/**
 * Stores data for 24 hour period leading up to a migraine occurrence
 * Created by Sarah on 6/24/2016.
 */
public class Weather24Hour {
    private WeatherHour[] hours24;
    private double apChange24hrs;
    private double apChange12hrs;
    private double humChange24hrs;
    private double humChange12hrs;
    private double tempChange24hrs;
    private double tempChange12hrs;
    private double wspdChange24hrs;
    private double wspdChange12hrs;

    public WeatherHour[] getHours24() {
        return hours24;
    }

    public void setHours24(WeatherHour[] hours24) {
        this.hours24 = hours24;
    }

    public double getApChange24hrs() {
        return apChange24hrs;
    }

    public void setApChange24hrs(double apChange24hrs) {
        this.apChange24hrs = apChange24hrs;
    }

    public double getApChange12hrs() {
        return apChange12hrs;
    }

    public void setApChange12hrs(double apChange12hrs) {
        this.apChange12hrs = apChange12hrs;
    }

    public double getHumChange24hrs() {
        return humChange24hrs;
    }

    public void setHumChange24hrs(double humChange24hrs) {
        this.humChange24hrs = humChange24hrs;
    }

    public double getHumChange12hrs() {
        return humChange12hrs;
    }

    public void setHumChange12hrs(double humChange12hrs) {
        this.humChange12hrs = humChange12hrs;
    }

    public double getTempChange24hrs() {
        return tempChange24hrs;
    }

    public void setTempChange24hrs(double tempChange24hrs) {
        this.tempChange24hrs = tempChange24hrs;
    }

    public double getTempChange12hrs() {
        return tempChange12hrs;
    }

    public void setTempChange12hrs(double tempChange12hrs) {
        this.tempChange12hrs = tempChange12hrs;
    }

    public double getWspdChange24hrs() {
        return wspdChange24hrs;
    }

    public void setWspdChange24hrs(double wspdChange24hrs) {
        this.wspdChange24hrs = wspdChange24hrs;
    }

    public double getWspdChange12hrs() {
        return wspdChange12hrs;
    }

    public void setWspdChange12hrs(double wspdChange12hrs) {
        this.wspdChange12hrs = wspdChange12hrs;
    }

}
