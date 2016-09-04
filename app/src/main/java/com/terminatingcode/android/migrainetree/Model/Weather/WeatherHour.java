package com.terminatingcode.android.migrainetree.model.weather;

import java.util.Calendar;

/**
 * Object holds summary of hourly weather information
 * parsed from Weather Underground history query
 * Created by Sarah on 6/24/2016.
 */
public class WeatherHour {
    private Calendar hourStart;
    private double temp;
    private double dewpt;
    private double hum;
    private double wspd;
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

    public Calendar getHourStart() {
        return hourStart;
    }

    public void setHourStart(Calendar hourStart) {
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

    @Override
    public String toString() {
        return "WeatherHour{" +
                "hourStart=" + hourStart +
                ", temp=" + temp +
                ", dewpt=" + dewpt +
                ", hum=" + hum +
                ", wspd=" + wspd +
                ", wdir=" + wdir +
                ", vis=" + vis +
                ", pressure=" + pressure +
                ", windchill=" + windchill +
                ", heatindex=" + heatindex +
                ", preci=" + preci +
                ", fog=" + fog +
                ", rain=" + rain +
                ", snow=" + snow +
                ", hail=" + hail +
                ", thunder=" + thunder +
                ", tornado=" + tornado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherHour that = (WeatherHour) o;

        if (Double.compare(that.temp, temp) != 0) return false;
        if (Double.compare(that.dewpt, dewpt) != 0) return false;
        if (Double.compare(that.hum, hum) != 0) return false;
        if (Double.compare(that.wspd, wspd) != 0) return false;
        if (Double.compare(that.wdir, wdir) != 0) return false;
        if (Double.compare(that.vis, vis) != 0) return false;
        if (Double.compare(that.pressure, pressure) != 0) return false;
        if (Double.compare(that.windchill, windchill) != 0) return false;
        if (Double.compare(that.heatindex, heatindex) != 0) return false;
        if (Double.compare(that.preci, preci) != 0) return false;
        if (fog != that.fog) return false;
        if (rain != that.rain) return false;
        if (snow != that.snow) return false;
        if (hail != that.hail) return false;
        if (thunder != that.thunder) return false;
        if (tornado != that.tornado) return false;
        return hourStart.equals(that.hourStart);

    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        result = hourStart.hashCode();
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(dewpt);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(hum);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(wspd);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(wdir);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(vis);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(pressure);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(windchill);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(heatindex);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(preci);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        result = 31 * result + (fog ? 1 : 0);
        result = 31 * result + (rain ? 1 : 0);
        result = 31 * result + (snow ? 1 : 0);
        result = 31 * result + (hail ? 1 : 0);
        result = 31 * result + (thunder ? 1 : 0);
        result = 31 * result + (tornado ? 1 : 0);
        return result;
    }
}
