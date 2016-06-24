package com.terminatingcode.android.migrainetree;

import java.util.Date;

/**
 * Object holds summary of hourly weather information
 * parsed from Weather Underground history query
 * Created by Sarah on 6/24/2016.
 */
public class WeatherHour {
    private Date hourStart;
    private double temp;
    private double dewpt;
    private double hum;
    private double wspd;
    private double wgust;
    private double wdir;
    private double vis;
    private double pressure;
    private double windchill;
    private double heatindex;
    private double preci;
    private boolean fog;
    private boolean rain;
    private boolean snow;
    private boolean hail;
    private boolean thunder;
    private boolean tornado;

    public Date getHourStart() {
        return hourStart;
    }

    public void setHourStart(Date hourStart) {
        this.hourStart = hourStart;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getDewpt() {
        return dewpt;
    }

    public void setDewpt(double dewpt) {
        this.dewpt = dewpt;
    }

    public double getHum() {
        return hum;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }

    public double getWspd() {
        return wspd;
    }

    public void setWspd(double wspd) {
        this.wspd = wspd;
    }

    public double getWgust() {
        return wgust;
    }

    public void setWgust(double wgust) {
        this.wgust = wgust;
    }

    public double getWdir() {
        return wdir;
    }

    public void setWdir(double wdir) {
        this.wdir = wdir;
    }

    public double getVis() {
        return vis;
    }

    public void setVis(double vis) {
        this.vis = vis;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getWindchill() {
        return windchill;
    }

    public void setWindchill(double windchill) {
        this.windchill = windchill;
    }

    public double getHeatindex() {
        return heatindex;
    }

    public void setHeatindex(double heatindex) {
        this.heatindex = heatindex;
    }

    public double getPreci() {
        return preci;
    }

    public void setPreci(double preci) {
        this.preci = preci;
    }

    public boolean isFog() {
        return fog;
    }

    public void setFog(boolean fog) {
        this.fog = fog;
    }

    public boolean isRain() {
        return rain;
    }

    public void setRain(boolean rain) {
        this.rain = rain;
    }

    public boolean isSnow() {
        return snow;
    }

    public void setSnow(boolean snow) {
        this.snow = snow;
    }

    public boolean isHail() {
        return hail;
    }

    public void setHail(boolean hail) {
        this.hail = hail;
    }

    public boolean isThunder() {
        return thunder;
    }

    public void setThunder(boolean thunder) {
        this.thunder = thunder;
    }

    public boolean isTornado() {
        return tornado;
    }

    public void setTornado(boolean tornado) {
        this.tornado = tornado;
    }

}
