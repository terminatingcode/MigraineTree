package com.terminatingcode.android.migrainetree.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Holds Migraine Record data prior to being confirmed
 * Created by Sarah on 7/28/2016.
 */
public class MigraineRecordObject implements Parcelable{
    private final String NAME = "MigraineRecordObject";

    private long StartHour;
    private long EndHour;
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
    private double currentTemp;
    private double Temp3Hours;
    private double Temp12Hours;
    private double Temp24Hours;
    private double currentHum;
    private double Hum3Hours;
    private double Hum12Hours;
    private double Hum24Hours;
    private double currentAP;
    private double AP3Hours;
    private double AP12Hours;
    private double AP24Hours;

    public MigraineRecordObject(){}

    public long getStartHour() {
        return StartHour;
    }

    public void setStartHour(long startHour) {
        StartHour = startHour;
    }

    public long getEndHour() {
        return EndHour;
    }

    public void setEndHour(long endHour) {
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

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getTemp3Hours() {
        return Temp3Hours;
    }

    public void setTemp3Hours(double temp3Hours) {
        Temp3Hours = temp3Hours;
    }

    public double getTemp12Hours() {
        return Temp12Hours;
    }

    public void setTemp12Hours(double temp12Hours) {
        Temp12Hours = temp12Hours;
    }

    public double getTemp24Hours() {
        return Temp24Hours;
    }

    public void setTemp24Hours(double temp24Hours) {
        Temp24Hours = temp24Hours;
    }

    public double getCurrentHum() {
        return currentHum;
    }

    public void setCurrentHum(double currentHum) {
        this.currentHum = currentHum;
    }

    public double getHum3Hours() {
        return Hum3Hours;
    }

    public void setHum3Hours(double hum3Hours) {
        Hum3Hours = hum3Hours;
    }

    public double getHum12Hours() {
        return Hum12Hours;
    }

    public void setHum12Hours(double hum12Hours) {
        Hum12Hours = hum12Hours;
    }

    public double getHum24Hours() {
        return Hum24Hours;
    }

    public void setHum24Hours(double hum24Hours) {
        Hum24Hours = hum24Hours;
    }

    public double getCurrentAP() {
        return currentAP;
    }

    public void setCurrentAP(double currentAP) {
        this.currentAP = currentAP;
    }

    public double getAP3Hours() {
        return AP3Hours;
    }

    public void setAP3Hours(double AP3Hours) {
        this.AP3Hours = AP3Hours;
    }

    public double getAP12Hours() {
        return AP12Hours;
    }

    public void setAP12Hours(double AP12Hours) {
        this.AP12Hours = AP12Hours;
    }

    public double getAP24Hours() {
        return AP24Hours;
    }

    public void setAP24Hours(double AP24Hours) {
        this.AP24Hours = AP24Hours;
    }

    public void setMenstrualDay(int menstrualDay) {
        MenstrualDay = menstrualDay;
    }

    protected MigraineRecordObject(Parcel in) {
        StartHour = in.readLong();
        EndHour = in.readLong();
        PainAtOnset = in.readInt();
        PainAtPeak = in.readInt();
        City = in.readString();
        Aura = in.readByte() != 0x00;
        Eaten = in.readByte() != 0x00;
        Water = in.readByte() != 0x00;
        Sleep = in.readInt();
        Stress = in.readInt();
        EyeStrain = in.readInt();
        PainType = in.readString();
        PainSource = in.readString();
        Medication = in.readString();
        Nausea = in.readByte() != 0x00;
        SensitivityToLight = in.readByte() != 0x00;
        SensitivityToNoise = in.readByte() != 0x00;
        SensitivityToSmell = in.readByte() != 0x00;
        Congestion = in.readByte() != 0x00;
        Ears = in.readByte() != 0x00;
        Confusion = in.readByte() != 0x00;
        MenstrualDay = in.readInt();
        currentTemp = in.readDouble();
        Temp3Hours = in.readDouble();
        Temp12Hours = in.readDouble();
        Temp24Hours = in.readDouble();
        currentHum = in.readDouble();
        Hum3Hours = in.readDouble();
        Hum12Hours = in.readDouble();
        Hum24Hours = in.readDouble();
        currentAP = in.readDouble();
        AP3Hours = in.readDouble();
        AP12Hours = in.readDouble();
        AP24Hours = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(StartHour);
        dest.writeLong(EndHour);
        dest.writeInt(PainAtOnset);
        dest.writeInt(PainAtPeak);
        dest.writeString(City);
        dest.writeByte((byte) (Aura ? 0x01 : 0x00));
        dest.writeByte((byte) (Eaten ? 0x01 : 0x00));
        dest.writeByte((byte) (Water ? 0x01 : 0x00));
        dest.writeInt(Sleep);
        dest.writeInt(Stress);
        dest.writeInt(EyeStrain);
        dest.writeString(PainType);
        dest.writeString(PainSource);
        dest.writeString(Medication);
        dest.writeByte((byte) (Nausea ? 0x01 : 0x00));
        dest.writeByte((byte) (SensitivityToLight ? 0x01 : 0x00));
        dest.writeByte((byte) (SensitivityToNoise ? 0x01 : 0x00));
        dest.writeByte((byte) (SensitivityToSmell ? 0x01 : 0x00));
        dest.writeByte((byte) (Congestion ? 0x01 : 0x00));
        dest.writeByte((byte) (Ears ? 0x01 : 0x00));
        dest.writeByte((byte) (Confusion ? 0x01 : 0x00));
        dest.writeInt(MenstrualDay);
        dest.writeDouble(currentTemp);
        dest.writeDouble(Temp3Hours);
        dest.writeDouble(Temp12Hours);
        dest.writeDouble(Temp24Hours);
        dest.writeDouble(currentHum);
        dest.writeDouble(Hum3Hours);
        dest.writeDouble(Hum12Hours);
        dest.writeDouble(Hum24Hours);
        dest.writeDouble(currentAP);
        dest.writeDouble(AP3Hours);
        dest.writeDouble(AP12Hours);
        dest.writeDouble(AP24Hours);
    }

    public static final Parcelable.Creator<MigraineRecordObject> CREATOR = new Parcelable.Creator<MigraineRecordObject>() {
        @Override
        public MigraineRecordObject createFromParcel(Parcel in) {
            return new MigraineRecordObject(in);
        }

        @Override
        public MigraineRecordObject[] newArray(int size) {
            return new MigraineRecordObject[size];
        }
    };

}
