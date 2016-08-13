package com.terminatingcode.android.migrainetree.amazonaws.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "migrainetree-mobilehub-1937228315-MigraineRecord")

public class MigraineRecordDO {
    private String _userId;
    private Double _recordId;
    private Double _aP12Hours;
    private Double _aP24Hours;
    private Double _aP3Hours;
    private Boolean _aura;
    private String _city;
    private Boolean _confusion;
    private Boolean _congestion;
    private Double _currentAP;
    private Double _currentHum;
    private Double _currentTemp;
    private Boolean _ears;
    private Boolean _eaten;
    private Double _endHour;
    private Double _eyeStrain;
    private Double _hum12Hours;
    private Double _hum24Hours;
    private Double _hum3Hours;
    private String _medication;
    private Double _menstrualDay;
    private Boolean _nausea;
    private Double _painAtOnset;
    private Double _painAtPeak;
    private String _painSource;
    private String _painType;
    private Boolean _sensitivityToLight;
    private Boolean _sensitivityToNoise;
    private Boolean _sensitivityToSmell;
    private Double _sleep;
    private Double _startHour;
    private Double _stress;
    private Double _temp12Hours;
    private Double _temp24Hours;
    private Double _temp3Hours;
    private Boolean _water;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBIndexHashKey(attributeName = "userId", globalSecondaryIndexNames = {"userId-StartHour","userId-PainAtPeak",})
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "RecordId")
    @DynamoDBAttribute(attributeName = "RecordId")
    public Double getRecordId() {
        return _recordId;
    }

    public void setRecordId(final Double _recordId) {
        this._recordId = _recordId;
    }
    @DynamoDBAttribute(attributeName = "AP12Hours")
    public Double getAP12Hours() {
        return _aP12Hours;
    }

    public void setAP12Hours(final Double _aP12Hours) {
        this._aP12Hours = _aP12Hours;
    }
    @DynamoDBAttribute(attributeName = "AP24Hours")
    public Double getAP24Hours() {
        return _aP24Hours;
    }

    public void setAP24Hours(final Double _aP24Hours) {
        this._aP24Hours = _aP24Hours;
    }
    @DynamoDBAttribute(attributeName = "AP3Hours")
    public Double getAP3Hours() {
        return _aP3Hours;
    }

    public void setAP3Hours(final Double _aP3Hours) {
        this._aP3Hours = _aP3Hours;
    }
    @DynamoDBAttribute(attributeName = "Aura")
    public Boolean getAura() {
        return _aura;
    }

    public void setAura(final Boolean _aura) {
        this._aura = _aura;
    }
    @DynamoDBAttribute(attributeName = "City")
    public String getCity() {
        return _city;
    }

    public void setCity(final String _city) {
        this._city = _city;
    }
    @DynamoDBAttribute(attributeName = "Confusion")
    public Boolean getConfusion() {
        return _confusion;
    }

    public void setConfusion(final Boolean _confusion) {
        this._confusion = _confusion;
    }
    @DynamoDBAttribute(attributeName = "Congestion")
    public Boolean getCongestion() {
        return _congestion;
    }

    public void setCongestion(final Boolean _congestion) {
        this._congestion = _congestion;
    }
    @DynamoDBAttribute(attributeName = "CurrentAP")
    public Double getCurrentAP() {
        return _currentAP;
    }

    public void setCurrentAP(final Double _currentAP) {
        this._currentAP = _currentAP;
    }
    @DynamoDBAttribute(attributeName = "CurrentHum")
    public Double getCurrentHum() {
        return _currentHum;
    }

    public void setCurrentHum(final Double _currentHum) {
        this._currentHum = _currentHum;
    }
    @DynamoDBAttribute(attributeName = "CurrentTemp")
    public Double getCurrentTemp() {
        return _currentTemp;
    }

    public void setCurrentTemp(final Double _currentTemp) {
        this._currentTemp = _currentTemp;
    }
    @DynamoDBAttribute(attributeName = "Ears")
    public Boolean getEars() {
        return _ears;
    }

    public void setEars(final Boolean _ears) {
        this._ears = _ears;
    }
    @DynamoDBAttribute(attributeName = "Eaten")
    public Boolean getEaten() {
        return _eaten;
    }

    public void setEaten(final Boolean _eaten) {
        this._eaten = _eaten;
    }
    @DynamoDBAttribute(attributeName = "EndHour")
    public Double getEndHour() {
        return _endHour;
    }

    public void setEndHour(final Double _endHour) {
        this._endHour = _endHour;
    }
    @DynamoDBAttribute(attributeName = "EyeStrain")
    public Double getEyeStrain() {
        return _eyeStrain;
    }

    public void setEyeStrain(final Double _eyeStrain) {
        this._eyeStrain = _eyeStrain;
    }
    @DynamoDBAttribute(attributeName = "Hum12Hours")
    public Double getHum12Hours() {
        return _hum12Hours;
    }

    public void setHum12Hours(final Double _hum12Hours) {
        this._hum12Hours = _hum12Hours;
    }
    @DynamoDBAttribute(attributeName = "Hum24Hours")
    public Double getHum24Hours() {
        return _hum24Hours;
    }

    public void setHum24Hours(final Double _hum24Hours) {
        this._hum24Hours = _hum24Hours;
    }
    @DynamoDBAttribute(attributeName = "Hum3Hours")
    public Double getHum3Hours() {
        return _hum3Hours;
    }

    public void setHum3Hours(final Double _hum3Hours) {
        this._hum3Hours = _hum3Hours;
    }
    @DynamoDBAttribute(attributeName = "Medication")
    public String getMedication() {
        return _medication;
    }

    public void setMedication(final String _medication) {
        this._medication = _medication;
    }
    @DynamoDBAttribute(attributeName = "MenstrualDay")
    public Double getMenstrualDay() {
        return _menstrualDay;
    }

    public void setMenstrualDay(final Double _menstrualDay) {
        this._menstrualDay = _menstrualDay;
    }
    @DynamoDBAttribute(attributeName = "Nausea")
    public Boolean getNausea() {
        return _nausea;
    }

    public void setNausea(final Boolean _nausea) {
        this._nausea = _nausea;
    }
    @DynamoDBAttribute(attributeName = "PainAtOnset")
    public Double getPainAtOnset() {
        return _painAtOnset;
    }

    public void setPainAtOnset(final Double _painAtOnset) {
        this._painAtOnset = _painAtOnset;
    }
    @DynamoDBIndexRangeKey(attributeName = "PainAtPeak", globalSecondaryIndexName = "userId-PainAtPeak")
    public Double getPainAtPeak() {
        return _painAtPeak;
    }

    public void setPainAtPeak(final Double _painAtPeak) {
        this._painAtPeak = _painAtPeak;
    }
    @DynamoDBAttribute(attributeName = "PainSource")
    public String getPainSource() {
        return _painSource;
    }

    public void setPainSource(final String _painSource) {
        this._painSource = _painSource;
    }
    @DynamoDBAttribute(attributeName = "PainType")
    public String getPainType() {
        return _painType;
    }

    public void setPainType(final String _painType) {
        this._painType = _painType;
    }
    @DynamoDBAttribute(attributeName = "SensitivityToLight")
    public Boolean getSensitivityToLight() {
        return _sensitivityToLight;
    }

    public void setSensitivityToLight(final Boolean _sensitivityToLight) {
        this._sensitivityToLight = _sensitivityToLight;
    }
    @DynamoDBAttribute(attributeName = "SensitivityToNoise")
    public Boolean getSensitivityToNoise() {
        return _sensitivityToNoise;
    }

    public void setSensitivityToNoise(final Boolean _sensitivityToNoise) {
        this._sensitivityToNoise = _sensitivityToNoise;
    }
    @DynamoDBAttribute(attributeName = "SensitivityToSmell")
    public Boolean getSensitivityToSmell() {
        return _sensitivityToSmell;
    }

    public void setSensitivityToSmell(final Boolean _sensitivityToSmell) {
        this._sensitivityToSmell = _sensitivityToSmell;
    }
    @DynamoDBAttribute(attributeName = "Sleep")
    public Double getSleep() {
        return _sleep;
    }

    public void setSleep(final Double _sleep) {
        this._sleep = _sleep;
    }
    @DynamoDBIndexRangeKey(attributeName = "StartHour", globalSecondaryIndexName = "userId-StartHour")
    public Double getStartHour() {
        return _startHour;
    }

    public void setStartHour(final Double _startHour) {
        this._startHour = _startHour;
    }
    @DynamoDBAttribute(attributeName = "Stress")
    public Double getStress() {
        return _stress;
    }

    public void setStress(final Double _stress) {
        this._stress = _stress;
    }
    @DynamoDBAttribute(attributeName = "Temp12Hours")
    public Double getTemp12Hours() {
        return _temp12Hours;
    }

    public void setTemp12Hours(final Double _temp12Hours) {
        this._temp12Hours = _temp12Hours;
    }
    @DynamoDBAttribute(attributeName = "Temp24Hours")
    public Double getTemp24Hours() {
        return _temp24Hours;
    }

    public void setTemp24Hours(final Double _temp24Hours) {
        this._temp24Hours = _temp24Hours;
    }
    @DynamoDBAttribute(attributeName = "Temp3Hours")
    public Double getTemp3Hours() {
        return _temp3Hours;
    }

    public void setTemp3Hours(final Double _temp3Hours) {
        this._temp3Hours = _temp3Hours;
    }
    @DynamoDBAttribute(attributeName = "Water")
    public Boolean getWater() {
        return _water;
    }

    public void setWater(final Boolean _water) {
        this._water = _water;
    }

}
