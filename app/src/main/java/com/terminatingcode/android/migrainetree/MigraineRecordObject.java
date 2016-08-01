package com.terminatingcode.android.migrainetree;

import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;

import java.util.Calendar;

/**
 * Holds Migraine Record data prior to being confirmed
 * Once confirmed, saves to SQL
 * Created by Sarah on 7/28/2016.
 */
public class MigraineRecordObject {
    private final String NAME = "MigraineRecordObject";

    private Calendar StartHour;
    private Calendar EndHour;
    private int PainAtOnset;
    private int PainAtPeak;
    private String City;
    private boolean Aura;
    private boolean Eaten;
    private boolean Water;
    private int Sleep;
    private int Stress;
    private int EyeStrain;
    private String PainType;
    private String PainSource;
    private String Medication;
    private boolean Nausea;
    private boolean SensitivityToLight;
    private boolean SensitivityToNoise;
    private boolean SensitivityToSmell;
    private boolean Congestion;
    private boolean Ears;
    private boolean Confusion;
    private int MenstrualDay;
    private Weather24Hour sWeather24Hour;

    public Calendar getStartHour() {
        return StartHour;
    }

    public void setStartHour(Calendar startHour) {
        StartHour = startHour;
    }

    public Calendar getEndHour() {
        return EndHour;
    }

    public void setEndHour(Calendar endHour) {
        EndHour = endHour;
    }

    public int getPainAtOnset() {
        return PainAtOnset;
    }

    public void setPainAtOnset(int painAtOnset) {
        PainAtOnset = painAtOnset;
    }

    public int getPainAtPeak() {
        return PainAtPeak;
    }

    public void setPainAtPeak(int painAtPeak) {
        PainAtPeak = painAtPeak;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public boolean isAura() {
        return Aura;
    }

    public void setAura(boolean aura) {
        Aura = aura;
    }

    public boolean isEaten() {
        return Eaten;
    }

    public void setEaten(boolean eaten) {
        Eaten = eaten;
    }

    public boolean isWater() {
        return Water;
    }

    public void setWater(boolean water) {
        Water = water;
    }

    public int getSleep() {
        return Sleep;
    }

    public void setSleep(int sleep) {
        Sleep = sleep;
    }

    public int getStress() {
        return Stress;
    }

    public void setStress(int stress) {
        Stress = stress;
    }

    public int getEyeStrain() {
        return EyeStrain;
    }

    public void setEyeStrain(int eyeStrain) {
        EyeStrain = eyeStrain;
    }

    public String getPainType() {
        return PainType;
    }

    public void setPainType(String painType) {
        PainType = painType;
    }

    public String getPainSource() {
        return PainSource;
    }

    public void setPainSource(String painSource) {
        PainSource = painSource;
    }

    public String getMedication() {
        return Medication;
    }

    public void setMedication(String medication) {
        Medication = medication;
    }

    public boolean isNausea() {
        return Nausea;
    }

    public void setNausea(boolean nausea) {
        Nausea = nausea;
    }

    public boolean isSensitivityToLight() {
        return SensitivityToLight;
    }

    public void setSensitivityToLight(boolean sensitivityToLight) {
        SensitivityToLight = sensitivityToLight;
    }

    public boolean isSensitivityToNoise() {
        return SensitivityToNoise;
    }

    public void setSensitivityToNoise(boolean sensitivityToNoise) {
        SensitivityToNoise = sensitivityToNoise;
    }

    public boolean isSensitivityToSmell() {
        return SensitivityToSmell;
    }

    public void setSensitivityToSmell(boolean sensitivityToSmell) {
        SensitivityToSmell = sensitivityToSmell;
    }

    public boolean isCongestion() {
        return Congestion;
    }

    public void setCongestion(boolean congestion) {
        Congestion = congestion;
    }

    public boolean isEars() {
        return Ears;
    }

    public void setEars(boolean ears) {
        Ears = ears;
    }

    public boolean isConfusion() {
        return Confusion;
    }

    public void setConfusion(boolean confusion) {
        Confusion = confusion;
    }

    public int getMenstrualDay() {
        return MenstrualDay;
    }

    public void setMenstrualDay(int menstrualDay) {
        MenstrualDay = menstrualDay;
    }

    public Weather24Hour getWeather24Hour() {
        return sWeather24Hour;
    }

    public void setWeather24Hour(Weather24Hour weather24Hour) {
        sWeather24Hour = weather24Hour;
    }


}
