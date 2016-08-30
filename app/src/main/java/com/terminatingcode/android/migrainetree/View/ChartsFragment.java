package com.terminatingcode.android.migrainetree.view;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.terminatingcode.android.migrainetree.utils.DateUtils;
import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.model.SQLite.LocalContentProvider;
import com.terminatingcode.android.migrainetree.model.SQLite.MigraineRecord;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class ChartsFragment extends Fragment {
    private static final String NAME = "ChartsFragment";

    private PieChart avgPainPieChart;
    private PieChart avgTimeOfDayPiechart;
    private BarChart triggersBarChart;
    private BarChart symptomsBarChart;


    public ChartsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_charts, container, false);
        avgPainPieChart = (PieChart) rootView.findViewById(R.id.averagePainchart);
        triggersBarChart = (BarChart) rootView.findViewById(R.id.triggersBarChart);
        symptomsBarChart = (BarChart) rootView.findViewById(R.id.symptomsBarChart);
        avgTimeOfDayPiechart = (PieChart) rootView.findViewById(R.id.averageTimeOfDayChart);
        querySQLite();
        return rootView;
    }

    public void querySQLite(){
        List<Integer> painPeak = new ArrayList<>();
        List<Long> startHours = new ArrayList<>();
        List<Integer> hoursOfDay = new ArrayList<>();
        int[] symptoms = new int[8];
        int food = 0;
        int dehydration = 0;
        int sleepTooLittle = 0;
        int sleepTooMuch = 0;
        int stress = 0;
        int eyeStrain = 0;

        ContentResolver mResolver = getActivity().getContentResolver();
        Cursor cursor = mResolver.query(LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, null, null, null, null);
        try{
            if(cursor != null  && cursor.getCount() > 0) {
                int startTimeIndex = cursor.getColumnIndex(MigraineRecord.START_HOUR);
                int painIndex = cursor.getColumnIndex(MigraineRecord.PAIN_AT_PEAK);
                int foodIndex = cursor.getColumnIndex(MigraineRecord.EATEN);
                int dehydrationIndex = cursor.getColumnIndex(MigraineRecord.WATER);
                int sleepIndex = cursor.getColumnIndex(MigraineRecord.SLEEP);
                int stressIndex = cursor.getColumnIndex(MigraineRecord.STRESS);
                int eyeStrainIndex = cursor.getColumnIndex(MigraineRecord.EYE_STRAIN);
                int auraIndex = cursor.getColumnIndex(MigraineRecord.AURA);
                int nauseaIndex = cursor.getColumnIndex(MigraineRecord.NAUSEA);
                int lightIndex = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_LIGHT);
                int noiseIndex = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_NOISE);
                int smellIndex = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_SMELL);
                int confusionIndex = cursor.getColumnIndex(MigraineRecord.CONFUSION);
                int earsIndex = cursor.getColumnIndex(MigraineRecord.EARS);
                int congestionIndex = cursor.getColumnIndex(MigraineRecord.CONGESTION);
                cursor.moveToFirst();
                do {
                    painPeak.add(cursor.getInt(painIndex));
                    startHours.add(cursor.getLong(startTimeIndex));
                    if(cursor.getInt(foodIndex) == 0) food++;
                    if(cursor.getInt(dehydrationIndex) == 0) dehydration++;
                    int sleepAmount = cursor.getInt(sleepIndex);
                    if(sleepAmount < 6) sleepTooLittle++;
                    if(sleepAmount > 9) sleepTooMuch++;
                    if(cursor.getInt(stressIndex) > 5) stress++;
                    if(cursor.getInt(eyeStrainIndex) > 5)eyeStrain++;
                    if(cursor.getInt(auraIndex) == 0) symptoms[0] +=1;
                    if(cursor.getInt(nauseaIndex) == 0) symptoms[1] +=1;
                    if(cursor.getInt(lightIndex) == 0) symptoms[2] +=1;
                    if(cursor.getInt(noiseIndex) == 0) symptoms[3] +=1;
                    if(cursor.getInt(smellIndex) == 0) symptoms[4] +=1;
                    if(cursor.getInt(confusionIndex) == 0) symptoms[5] +=1;
                    if(cursor.getInt(earsIndex) == 0) symptoms[6] +=1;
                    if(cursor.getInt(congestionIndex) == 0) symptoms[7] +=1;
                }while (cursor.moveToNext());
            }
        }finally {
            if(cursor != null) cursor.close();
        }

        initialisePainPieChart(painPeak);
        initialiseTriggersBarChart(food, dehydration, sleepTooLittle, sleepTooMuch, stress, eyeStrain);
        initialiseSymptomsBarChart(symptoms);
        for(long date : startHours){
            hoursOfDay.add(DateUtils.convertLongToHourOfDay(date));
        }
        initialiseTimeOfDayPieChart(hoursOfDay);
    }



    public void initialisePainPieChart(List<Integer> painPeak){
        double average = 0.0;
        int auraOnly = 0;
        int mild = 0;
        int moderate = 0;
        int severe = 0;
        int debilitating = 0;
        for(Integer painLevel : painPeak){
            average += painLevel;
            if(painLevel == 0) auraOnly++;
            else if(painLevel < 3) mild++;
            else if(painLevel < 6) moderate++;
            else if(painLevel < 9) severe++;
            else debilitating++;
        }
        String avg = String.valueOf(average/painPeak.size());
        avgPainPieChart.setDescription("Average Pain at Peak");
        avgPainPieChart.setDescriptionColor(Color.WHITE);
        avgPainPieChart.setDescriptionTextSize(18);
        avgPainPieChart.setCenterText(avg);
        avgPainPieChart.setCenterTextSize(24);
        avgPainPieChart.setUsePercentValues(true);
        avgPainPieChart.setHoleRadius(32f);
        avgPainPieChart.setTransparentCircleRadius(36f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(auraOnly, "aura only"));
        entries.add(new PieEntry(mild,"mild"));
        entries.add(new PieEntry(moderate,"moderate"));
        entries.add(new PieEntry(severe,"severe"));
        entries.add(new PieEntry(debilitating,"debilitating"));
        PieDataSet dataSet = new PieDataSet(entries, "testing entries");
        dataSet.setColors(new int[] {
                R.color.aura_only,
                R.color.mild,
                R.color.moderate,
                R.color.severe,
                R.color.debilitating}, getActivity());
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        avgPainPieChart.setData(data);
    }

    public void initialiseTriggersBarChart(int food, int dehydration, int sleepTooLittle, int sleepTooMuch,
                                           int stress, int eyeStrain) {
        triggersBarChart.setDrawValueAboveBar(true);
        triggersBarChart.setDescription("Frequency of triggers");
        triggersBarChart.setDescriptionColor(Color.WHITE);
        triggersBarChart.setDescriptionTextSize(18);
        triggersBarChart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        float start = 0.5f;
        int count = 6;
        final String[] triggers = new String[]{
                "",
                "food",
                "dehydration",
                "<6h sleep",
                ">9h sleep",
                "stress",
                "eye strain",
                ""};

        XAxis xAxis = triggersBarChart.getXAxis();
        xAxis.setAxisMinValue(start);
        xAxis.setAxisMaxValue(count + 0.5f);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return triggers[(int) value % triggers.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        ArrayList<BarEntry> triggerVals = new ArrayList<>();

        triggerVals.add(new BarEntry(1, food));
        triggerVals.add(new BarEntry(2, dehydration));
        triggerVals.add(new BarEntry(3, sleepTooLittle));
        triggerVals.add(new BarEntry(4, sleepTooMuch));
        triggerVals.add(new BarEntry(5, stress));
        triggerVals.add(new BarEntry(6, eyeStrain));

        BarDataSet set1;

        if (triggersBarChart.getData() != null &&
                triggersBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) triggersBarChart.getData().getDataSetByIndex(0);
            set1.setValues(triggerVals);
            triggersBarChart.getData().notifyDataChanged();
            triggersBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(triggerVals, "Triggers");
            set1.setColors(new int[] {
                    R.color.aura_only,
                    R.color.mild,
                    R.color.moderate,
                    R.color.severe,
                    R.color.debilitating}, getActivity());

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            data.setValueTextColor(Color.LTGRAY);

            triggersBarChart.setData(data);
        }
    }

    public void initialiseSymptomsBarChart(int[] symptoms) {
        triggersBarChart.setDrawValueAboveBar(true);
        triggersBarChart.setDescription("Frequency of triggers");
        triggersBarChart.setDescriptionColor(Color.WHITE);
        triggersBarChart.setDescriptionTextSize(18);
        triggersBarChart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        float start = 0.5f;
        int count = 8;
        final String[] triggers = new String[]{
                "",
                "aura",
                "nausea",
                "light",
                "noise",
                "smell",
                "confusion",
                "ears",
                "congestion",
                ""};

        XAxis xAxis = symptomsBarChart.getXAxis();
        xAxis.setAxisMinValue(start);
        xAxis.setAxisMaxValue(count + 0.5f);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return triggers[(int) value % triggers.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        ArrayList<BarEntry> triggerVals = new ArrayList<>();

        triggerVals.add(new BarEntry(1, symptoms[0]));
        triggerVals.add(new BarEntry(2, symptoms[1]));
        triggerVals.add(new BarEntry(3, symptoms[2]));
        triggerVals.add(new BarEntry(4, symptoms[3]));
        triggerVals.add(new BarEntry(5, symptoms[4]));
        triggerVals.add(new BarEntry(6, symptoms[5]));
        triggerVals.add(new BarEntry(6, symptoms[6]));
        triggerVals.add(new BarEntry(6, symptoms[7]));

        BarDataSet set1;

        if (symptomsBarChart.getData() != null &&
                symptomsBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) symptomsBarChart.getData().getDataSetByIndex(0);
            set1.setValues(triggerVals);
            symptomsBarChart.getData().notifyDataChanged();
            symptomsBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(triggerVals, "Triggers");
            set1.setColors(new int[] {
                    R.color.aura_only,
                    R.color.mild,
                    R.color.moderate,
                    R.color.severe,
                    R.color.debilitating}, getActivity());

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            data.setValueTextColor(Color.LTGRAY);

            symptomsBarChart.setData(data);
        }
    }

    public void initialiseTimeOfDayPieChart(List<Integer> hoursOfDay){
        double average = 0.0;
        int morning = 0;
        int noon = 0;
        int evening = 0;
        int night = 0;
        for(Integer hour : hoursOfDay){
            Log.d(NAME, "hour " + hour);
            average += hour;
            if(hour >= 5 && hour <= 11) morning++;
            else if(hour > 11 && hour <= 15) noon++;
            else if(hour > 15 && hour < 20) evening++;
            else night++;
        }
        String avg = String.valueOf(average/hoursOfDay.size()) + "h";
        avgTimeOfDayPiechart.setDescription("Average Time of Day");
        avgTimeOfDayPiechart.setDescriptionColor(Color.WHITE);
        avgTimeOfDayPiechart.setDescriptionTextSize(18);
        avgTimeOfDayPiechart.setCenterText(avg);
        avgTimeOfDayPiechart.setCenterTextSize(24);
        avgTimeOfDayPiechart.setUsePercentValues(true);
        avgTimeOfDayPiechart.setHoleRadius(32f);
        avgTimeOfDayPiechart.setTransparentCircleRadius(36f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(morning, "morning"));
        entries.add(new PieEntry(noon,"noon"));
        entries.add(new PieEntry(evening,"evening"));
        entries.add(new PieEntry(night,"night"));
        PieDataSet dataSet = new PieDataSet(entries, "time of day entries");
        dataSet.setColors(new int[] {
                R.color.morning,
                R.color.noon,
                R.color.evening,
                R.color.night}, getActivity());
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        avgTimeOfDayPiechart.setData(data);
    }


}
