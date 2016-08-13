package com.terminatingcode.android.migrainetree.amazonaws.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.terminatingcode.android.migrainetree.amazonaws.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.Set;

public class DemoNoSQLMigraineRecordResult implements com.terminatingcode.android.migrainetree.amazonaws.nosql.DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final MigraineRecordDO result;

    DemoNoSQLMigraineRecordResult(final MigraineRecordDO result) {
        this.result = result;
    }
    @Override
    public void updateItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final double originalValue = result.getAP12Hours();
        result.setAP12Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setAP12Hours(originalValue);
            throw ex;
        }
    }

    @Override
    public void deleteItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        mapper.delete(result);
    }

    private void setKeyTextViewStyle(final TextView textView) {
        textView.setTextColor(KEY_TEXT_COLOR);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(5), dp(2), dp(5), 0);
        textView.setLayoutParams(layoutParams);
    }

    /**
     * @param dp number of design pixels.
     * @return number of pixels corresponding to the desired design pixels.
     */
    private int dp(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void setValueTextViewStyle(final TextView textView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(15), 0, dp(15), dp(2));
        textView.setLayoutParams(layoutParams);
    }

    private void setKeyAndValueTextViewStyles(final TextView keyTextView, final TextView valueTextView) {
        setKeyTextViewStyle(keyTextView);
        setValueTextViewStyle(valueTextView);
    }

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02X", bytes[0]));
        for(int index = 1; index < bytes.length; index++) {
            builder.append(String.format(" %02X", bytes[index]));
        }
        return builder.toString();
    }

    private static String byteSetsToHexStrings(Set<byte[]> bytesSet) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        for (byte[] bytes : bytesSet) {
            builder.append(String.format("%d: ", ++index));
            builder.append(bytesToHexString(bytes));
            if (index < bytesSet.size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public View getView(final Context context, final View convertView, int position) {
        final LinearLayout layout;
        final TextView resultNumberTextView;
        final TextView userIdKeyTextView;
        final TextView userIdValueTextView;
        final TextView recordIdKeyTextView;
        final TextView recordIdValueTextView;
        final TextView aP12HoursKeyTextView;
        final TextView aP12HoursValueTextView;
        final TextView aP24HoursKeyTextView;
        final TextView aP24HoursValueTextView;
        final TextView aP3HoursKeyTextView;
        final TextView aP3HoursValueTextView;
        final TextView auraKeyTextView;
        final TextView auraValueTextView;
        final TextView cityKeyTextView;
        final TextView cityValueTextView;
        final TextView confusionKeyTextView;
        final TextView confusionValueTextView;
        final TextView congestionKeyTextView;
        final TextView congestionValueTextView;
        final TextView currentAPKeyTextView;
        final TextView currentAPValueTextView;
        final TextView currentHumKeyTextView;
        final TextView currentHumValueTextView;
        final TextView currentTempKeyTextView;
        final TextView currentTempValueTextView;
        final TextView earsKeyTextView;
        final TextView earsValueTextView;
        final TextView eatenKeyTextView;
        final TextView eatenValueTextView;
        final TextView endHourKeyTextView;
        final TextView endHourValueTextView;
        final TextView eyeStrainKeyTextView;
        final TextView eyeStrainValueTextView;
        final TextView hum12HoursKeyTextView;
        final TextView hum12HoursValueTextView;
        final TextView hum24HoursKeyTextView;
        final TextView hum24HoursValueTextView;
        final TextView hum3HoursKeyTextView;
        final TextView hum3HoursValueTextView;
        final TextView medicationKeyTextView;
        final TextView medicationValueTextView;
        final TextView menstrualDayKeyTextView;
        final TextView menstrualDayValueTextView;
        final TextView nauseaKeyTextView;
        final TextView nauseaValueTextView;
        final TextView painAtOnsetKeyTextView;
        final TextView painAtOnsetValueTextView;
        final TextView painAtPeakKeyTextView;
        final TextView painAtPeakValueTextView;
        final TextView painSourceKeyTextView;
        final TextView painSourceValueTextView;
        final TextView painTypeKeyTextView;
        final TextView painTypeValueTextView;
        final TextView sensitivityToLightKeyTextView;
        final TextView sensitivityToLightValueTextView;
        final TextView sensitivityToNoiseKeyTextView;
        final TextView sensitivityToNoiseValueTextView;
        final TextView sensitivityToSmellKeyTextView;
        final TextView sensitivityToSmellValueTextView;
        final TextView sleepKeyTextView;
        final TextView sleepValueTextView;
        final TextView startHourKeyTextView;
        final TextView startHourValueTextView;
        final TextView stressKeyTextView;
        final TextView stressValueTextView;
        final TextView temp12HoursKeyTextView;
        final TextView temp12HoursValueTextView;
        final TextView temp24HoursKeyTextView;
        final TextView temp24HoursValueTextView;
        final TextView temp3HoursKeyTextView;
        final TextView temp3HoursValueTextView;
        final TextView waterKeyTextView;
        final TextView waterValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            userIdKeyTextView = new TextView(context);
            userIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userIdKeyTextView, userIdValueTextView);
            layout.addView(userIdKeyTextView);
            layout.addView(userIdValueTextView);

            recordIdKeyTextView = new TextView(context);
            recordIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(recordIdKeyTextView, recordIdValueTextView);
            layout.addView(recordIdKeyTextView);
            layout.addView(recordIdValueTextView);

            aP12HoursKeyTextView = new TextView(context);
            aP12HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(aP12HoursKeyTextView, aP12HoursValueTextView);
            layout.addView(aP12HoursKeyTextView);
            layout.addView(aP12HoursValueTextView);

            aP24HoursKeyTextView = new TextView(context);
            aP24HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(aP24HoursKeyTextView, aP24HoursValueTextView);
            layout.addView(aP24HoursKeyTextView);
            layout.addView(aP24HoursValueTextView);

            aP3HoursKeyTextView = new TextView(context);
            aP3HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(aP3HoursKeyTextView, aP3HoursValueTextView);
            layout.addView(aP3HoursKeyTextView);
            layout.addView(aP3HoursValueTextView);

            auraKeyTextView = new TextView(context);
            auraValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(auraKeyTextView, auraValueTextView);
            layout.addView(auraKeyTextView);
            layout.addView(auraValueTextView);

            cityKeyTextView = new TextView(context);
            cityValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(cityKeyTextView, cityValueTextView);
            layout.addView(cityKeyTextView);
            layout.addView(cityValueTextView);

            confusionKeyTextView = new TextView(context);
            confusionValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(confusionKeyTextView, confusionValueTextView);
            layout.addView(confusionKeyTextView);
            layout.addView(confusionValueTextView);

            congestionKeyTextView = new TextView(context);
            congestionValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(congestionKeyTextView, congestionValueTextView);
            layout.addView(congestionKeyTextView);
            layout.addView(congestionValueTextView);

            currentAPKeyTextView = new TextView(context);
            currentAPValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(currentAPKeyTextView, currentAPValueTextView);
            layout.addView(currentAPKeyTextView);
            layout.addView(currentAPValueTextView);

            currentHumKeyTextView = new TextView(context);
            currentHumValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(currentHumKeyTextView, currentHumValueTextView);
            layout.addView(currentHumKeyTextView);
            layout.addView(currentHumValueTextView);

            currentTempKeyTextView = new TextView(context);
            currentTempValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(currentTempKeyTextView, currentTempValueTextView);
            layout.addView(currentTempKeyTextView);
            layout.addView(currentTempValueTextView);

            earsKeyTextView = new TextView(context);
            earsValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(earsKeyTextView, earsValueTextView);
            layout.addView(earsKeyTextView);
            layout.addView(earsValueTextView);

            eatenKeyTextView = new TextView(context);
            eatenValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(eatenKeyTextView, eatenValueTextView);
            layout.addView(eatenKeyTextView);
            layout.addView(eatenValueTextView);

            endHourKeyTextView = new TextView(context);
            endHourValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(endHourKeyTextView, endHourValueTextView);
            layout.addView(endHourKeyTextView);
            layout.addView(endHourValueTextView);

            eyeStrainKeyTextView = new TextView(context);
            eyeStrainValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(eyeStrainKeyTextView, eyeStrainValueTextView);
            layout.addView(eyeStrainKeyTextView);
            layout.addView(eyeStrainValueTextView);

            hum12HoursKeyTextView = new TextView(context);
            hum12HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(hum12HoursKeyTextView, hum12HoursValueTextView);
            layout.addView(hum12HoursKeyTextView);
            layout.addView(hum12HoursValueTextView);

            hum24HoursKeyTextView = new TextView(context);
            hum24HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(hum24HoursKeyTextView, hum24HoursValueTextView);
            layout.addView(hum24HoursKeyTextView);
            layout.addView(hum24HoursValueTextView);

            hum3HoursKeyTextView = new TextView(context);
            hum3HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(hum3HoursKeyTextView, hum3HoursValueTextView);
            layout.addView(hum3HoursKeyTextView);
            layout.addView(hum3HoursValueTextView);

            medicationKeyTextView = new TextView(context);
            medicationValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(medicationKeyTextView, medicationValueTextView);
            layout.addView(medicationKeyTextView);
            layout.addView(medicationValueTextView);

            menstrualDayKeyTextView = new TextView(context);
            menstrualDayValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(menstrualDayKeyTextView, menstrualDayValueTextView);
            layout.addView(menstrualDayKeyTextView);
            layout.addView(menstrualDayValueTextView);

            nauseaKeyTextView = new TextView(context);
            nauseaValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(nauseaKeyTextView, nauseaValueTextView);
            layout.addView(nauseaKeyTextView);
            layout.addView(nauseaValueTextView);

            painAtOnsetKeyTextView = new TextView(context);
            painAtOnsetValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(painAtOnsetKeyTextView, painAtOnsetValueTextView);
            layout.addView(painAtOnsetKeyTextView);
            layout.addView(painAtOnsetValueTextView);

            painAtPeakKeyTextView = new TextView(context);
            painAtPeakValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(painAtPeakKeyTextView, painAtPeakValueTextView);
            layout.addView(painAtPeakKeyTextView);
            layout.addView(painAtPeakValueTextView);

            painSourceKeyTextView = new TextView(context);
            painSourceValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(painSourceKeyTextView, painSourceValueTextView);
            layout.addView(painSourceKeyTextView);
            layout.addView(painSourceValueTextView);

            painTypeKeyTextView = new TextView(context);
            painTypeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(painTypeKeyTextView, painTypeValueTextView);
            layout.addView(painTypeKeyTextView);
            layout.addView(painTypeValueTextView);

            sensitivityToLightKeyTextView = new TextView(context);
            sensitivityToLightValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(sensitivityToLightKeyTextView, sensitivityToLightValueTextView);
            layout.addView(sensitivityToLightKeyTextView);
            layout.addView(sensitivityToLightValueTextView);

            sensitivityToNoiseKeyTextView = new TextView(context);
            sensitivityToNoiseValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(sensitivityToNoiseKeyTextView, sensitivityToNoiseValueTextView);
            layout.addView(sensitivityToNoiseKeyTextView);
            layout.addView(sensitivityToNoiseValueTextView);

            sensitivityToSmellKeyTextView = new TextView(context);
            sensitivityToSmellValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(sensitivityToSmellKeyTextView, sensitivityToSmellValueTextView);
            layout.addView(sensitivityToSmellKeyTextView);
            layout.addView(sensitivityToSmellValueTextView);

            sleepKeyTextView = new TextView(context);
            sleepValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(sleepKeyTextView, sleepValueTextView);
            layout.addView(sleepKeyTextView);
            layout.addView(sleepValueTextView);

            startHourKeyTextView = new TextView(context);
            startHourValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(startHourKeyTextView, startHourValueTextView);
            layout.addView(startHourKeyTextView);
            layout.addView(startHourValueTextView);

            stressKeyTextView = new TextView(context);
            stressValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(stressKeyTextView, stressValueTextView);
            layout.addView(stressKeyTextView);
            layout.addView(stressValueTextView);

            temp12HoursKeyTextView = new TextView(context);
            temp12HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(temp12HoursKeyTextView, temp12HoursValueTextView);
            layout.addView(temp12HoursKeyTextView);
            layout.addView(temp12HoursValueTextView);

            temp24HoursKeyTextView = new TextView(context);
            temp24HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(temp24HoursKeyTextView, temp24HoursValueTextView);
            layout.addView(temp24HoursKeyTextView);
            layout.addView(temp24HoursValueTextView);

            temp3HoursKeyTextView = new TextView(context);
            temp3HoursValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(temp3HoursKeyTextView, temp3HoursValueTextView);
            layout.addView(temp3HoursKeyTextView);
            layout.addView(temp3HoursValueTextView);

            waterKeyTextView = new TextView(context);
            waterValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(waterKeyTextView, waterValueTextView);
            layout.addView(waterKeyTextView);
            layout.addView(waterValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            recordIdKeyTextView = (TextView) layout.getChildAt(3);
            recordIdValueTextView = (TextView) layout.getChildAt(4);

            aP12HoursKeyTextView = (TextView) layout.getChildAt(5);
            aP12HoursValueTextView = (TextView) layout.getChildAt(6);

            aP24HoursKeyTextView = (TextView) layout.getChildAt(7);
            aP24HoursValueTextView = (TextView) layout.getChildAt(8);

            aP3HoursKeyTextView = (TextView) layout.getChildAt(9);
            aP3HoursValueTextView = (TextView) layout.getChildAt(10);

            auraKeyTextView = (TextView) layout.getChildAt(11);
            auraValueTextView = (TextView) layout.getChildAt(12);

            cityKeyTextView = (TextView) layout.getChildAt(13);
            cityValueTextView = (TextView) layout.getChildAt(14);

            confusionKeyTextView = (TextView) layout.getChildAt(15);
            confusionValueTextView = (TextView) layout.getChildAt(16);

            congestionKeyTextView = (TextView) layout.getChildAt(17);
            congestionValueTextView = (TextView) layout.getChildAt(18);

            currentAPKeyTextView = (TextView) layout.getChildAt(19);
            currentAPValueTextView = (TextView) layout.getChildAt(20);

            currentHumKeyTextView = (TextView) layout.getChildAt(21);
            currentHumValueTextView = (TextView) layout.getChildAt(22);

            currentTempKeyTextView = (TextView) layout.getChildAt(23);
            currentTempValueTextView = (TextView) layout.getChildAt(24);

            earsKeyTextView = (TextView) layout.getChildAt(25);
            earsValueTextView = (TextView) layout.getChildAt(26);

            eatenKeyTextView = (TextView) layout.getChildAt(27);
            eatenValueTextView = (TextView) layout.getChildAt(28);

            endHourKeyTextView = (TextView) layout.getChildAt(29);
            endHourValueTextView = (TextView) layout.getChildAt(30);

            eyeStrainKeyTextView = (TextView) layout.getChildAt(31);
            eyeStrainValueTextView = (TextView) layout.getChildAt(32);

            hum12HoursKeyTextView = (TextView) layout.getChildAt(33);
            hum12HoursValueTextView = (TextView) layout.getChildAt(34);

            hum24HoursKeyTextView = (TextView) layout.getChildAt(35);
            hum24HoursValueTextView = (TextView) layout.getChildAt(36);

            hum3HoursKeyTextView = (TextView) layout.getChildAt(37);
            hum3HoursValueTextView = (TextView) layout.getChildAt(38);

            medicationKeyTextView = (TextView) layout.getChildAt(39);
            medicationValueTextView = (TextView) layout.getChildAt(40);

            menstrualDayKeyTextView = (TextView) layout.getChildAt(41);
            menstrualDayValueTextView = (TextView) layout.getChildAt(42);

            nauseaKeyTextView = (TextView) layout.getChildAt(43);
            nauseaValueTextView = (TextView) layout.getChildAt(44);

            painAtOnsetKeyTextView = (TextView) layout.getChildAt(45);
            painAtOnsetValueTextView = (TextView) layout.getChildAt(46);

            painAtPeakKeyTextView = (TextView) layout.getChildAt(47);
            painAtPeakValueTextView = (TextView) layout.getChildAt(48);

            painSourceKeyTextView = (TextView) layout.getChildAt(49);
            painSourceValueTextView = (TextView) layout.getChildAt(50);

            painTypeKeyTextView = (TextView) layout.getChildAt(51);
            painTypeValueTextView = (TextView) layout.getChildAt(52);

            sensitivityToLightKeyTextView = (TextView) layout.getChildAt(53);
            sensitivityToLightValueTextView = (TextView) layout.getChildAt(54);

            sensitivityToNoiseKeyTextView = (TextView) layout.getChildAt(55);
            sensitivityToNoiseValueTextView = (TextView) layout.getChildAt(56);

            sensitivityToSmellKeyTextView = (TextView) layout.getChildAt(57);
            sensitivityToSmellValueTextView = (TextView) layout.getChildAt(58);

            sleepKeyTextView = (TextView) layout.getChildAt(59);
            sleepValueTextView = (TextView) layout.getChildAt(60);

            startHourKeyTextView = (TextView) layout.getChildAt(61);
            startHourValueTextView = (TextView) layout.getChildAt(62);

            stressKeyTextView = (TextView) layout.getChildAt(63);
            stressValueTextView = (TextView) layout.getChildAt(64);

            temp12HoursKeyTextView = (TextView) layout.getChildAt(65);
            temp12HoursValueTextView = (TextView) layout.getChildAt(66);

            temp24HoursKeyTextView = (TextView) layout.getChildAt(67);
            temp24HoursValueTextView = (TextView) layout.getChildAt(68);

            temp3HoursKeyTextView = (TextView) layout.getChildAt(69);
            temp3HoursValueTextView = (TextView) layout.getChildAt(70);

            waterKeyTextView = (TextView) layout.getChildAt(71);
            waterValueTextView = (TextView) layout.getChildAt(72);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        recordIdKeyTextView.setText("RecordId");
        recordIdValueTextView.setText("" + result.getRecordId().longValue());
        aP12HoursKeyTextView.setText("AP12Hours");
        aP12HoursValueTextView.setText("" + result.getAP12Hours().longValue());
        aP24HoursKeyTextView.setText("AP24Hours");
        aP24HoursValueTextView.setText("" + result.getAP24Hours().longValue());
        aP3HoursKeyTextView.setText("AP3Hours");
        aP3HoursValueTextView.setText("" + result.getAP3Hours().longValue());
        auraKeyTextView.setText("Aura");
        auraValueTextView.setText("" + result.getAura());
        cityKeyTextView.setText("City");
        cityValueTextView.setText(result.getCity());
        confusionKeyTextView.setText("Confusion");
        confusionValueTextView.setText("" + result.getConfusion());
        congestionKeyTextView.setText("Congestion");
        congestionValueTextView.setText("" + result.getCongestion());
        currentAPKeyTextView.setText("CurrentAP");
        currentAPValueTextView.setText("" + result.getCurrentAP());
        currentHumKeyTextView.setText("CurrentHum");
        currentHumValueTextView.setText("" + result.getCurrentHum());
        currentTempKeyTextView.setText("CurrentTemp");
        currentTempValueTextView.setText("" + result.getCurrentTemp());
        earsKeyTextView.setText("Ears");
        earsValueTextView.setText("" + result.getEars());
        eatenKeyTextView.setText("Eaten");
        eatenValueTextView.setText("" + result.getEaten());
        endHourKeyTextView.setText("EndHour");
        endHourValueTextView.setText("" + result.getEndHour().longValue());
        eyeStrainKeyTextView.setText("EyeStrain");
        eyeStrainValueTextView.setText("" + result.getEyeStrain().longValue());
        hum12HoursKeyTextView.setText("Hum12Hours");
        hum12HoursValueTextView.setText("" + result.getHum12Hours().longValue());
        hum24HoursKeyTextView.setText("Hum24Hours");
        hum24HoursValueTextView.setText("" + result.getHum24Hours().longValue());
        hum3HoursKeyTextView.setText("Hum3Hours");
        hum3HoursValueTextView.setText("" + result.getHum3Hours().longValue());
        medicationKeyTextView.setText("Medication");
        medicationValueTextView.setText(result.getMedication());
        menstrualDayKeyTextView.setText("MenstrualDay");
        menstrualDayValueTextView.setText("" + result.getMenstrualDay().longValue());
        nauseaKeyTextView.setText("Nausea");
        nauseaValueTextView.setText("" + result.getNausea());
        painAtOnsetKeyTextView.setText("PainAtOnset");
        painAtOnsetValueTextView.setText("" + result.getPainAtOnset().longValue());
        painAtPeakKeyTextView.setText("PainAtPeak");
        painAtPeakValueTextView.setText("" + result.getPainAtPeak().longValue());
        painSourceKeyTextView.setText("PainSource");
        painSourceValueTextView.setText(result.getPainSource());
        painTypeKeyTextView.setText("PainType");
        painTypeValueTextView.setText(result.getPainType());
        sensitivityToLightKeyTextView.setText("SensitivityToLight");
        sensitivityToLightValueTextView.setText("" + result.getSensitivityToLight());
        sensitivityToNoiseKeyTextView.setText("SensitivityToNoise");
        sensitivityToNoiseValueTextView.setText("" + result.getSensitivityToNoise());
        sensitivityToSmellKeyTextView.setText("SensitivityToSmell");
        sensitivityToSmellValueTextView.setText("" + result.getSensitivityToSmell());
        sleepKeyTextView.setText("Sleep");
        sleepValueTextView.setText("" + result.getSleep().longValue());
        startHourKeyTextView.setText("StartHour");
        startHourValueTextView.setText("" + result.getStartHour().longValue());
        stressKeyTextView.setText("Stress");
        stressValueTextView.setText("" + result.getStress().longValue());
        temp12HoursKeyTextView.setText("Temp12Hours");
        temp12HoursValueTextView.setText("" + result.getTemp12Hours().longValue());
        temp24HoursKeyTextView.setText("Temp24Hours");
        temp24HoursValueTextView.setText("" + result.getTemp24Hours().longValue());
        temp3HoursKeyTextView.setText("Temp3Hours");
        temp3HoursValueTextView.setText("" + result.getTemp3Hours().longValue());
        waterKeyTextView.setText("Water");
        waterValueTextView.setText("" + result.getWater());
        return layout;
    }
}
