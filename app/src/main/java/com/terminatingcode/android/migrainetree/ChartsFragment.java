package com.terminatingcode.android.migrainetree;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String NAME = "ChartsFragment";

    private PieChart avgPainPieChart;
    private BarChart triggersBarChart;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChartsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartsFragment newInstance(String param1, String param2) {
        ChartsFragment fragment = new ChartsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_charts, container, false);
        avgPainPieChart = (PieChart) rootView.findViewById(R.id.averagePainchart);
        triggersBarChart = (BarChart) rootView.findViewById(R.id.triggersBarChart);
        querySQLite();
        return rootView;
    }

    public void querySQLite(){
        List<Integer> painPeak = new ArrayList<>();
        ContentResolver mResolver = getActivity().getContentResolver();
        Cursor cursor = mResolver.query(LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, null, null, null, null);
        try{
            if(cursor != null  && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int food = 0;
                    int dehydration = 0;
                    int sleepTooLittle = 0;
                    int sleepTooMuch = 0;
                    int stress = 0;
                    int eyeStrain = 0;
                    int painIndex = cursor.getColumnIndex(MigraineRecord.PAIN_AT_PEAK);
                    int foodIndex = cursor.getColumnIndex(MigraineRecord.EATEN);
                    int dehydrationIndex = cursor.getColumnIndex(MigraineRecord.WATER);
                    int sleepIndex = cursor.getColumnIndex(MigraineRecord.SLEEP);
                    int stressIndex = cursor.getColumnIndex(MigraineRecord.STRESS);
                    int eyeStrainIndex = cursor.getColumnIndex(MigraineRecord.EYE_STRAIN);
                    painPeak.add(cursor.getInt(painIndex));
                    if(cursor.getInt(foodIndex) == 0) food++;
                    if(cursor.getInt(dehydrationIndex) == 0) dehydration++;
                    int sleepAmount = cursor.getInt(sleepIndex);
                    if(sleepAmount < 6) sleepTooLittle++;
                    if(sleepAmount > 9) sleepTooMuch++;
                    if(cursor.getInt(stressIndex) > 5) stress++;
                    if(cursor.getInt(eyeStrainIndex) > 5)eyeStrain++;
                    initialisePieChart(painPeak);
                    initialiseBarChart(food, dehydration, sleepTooLittle, sleepTooMuch, stress, eyeStrain);
                }while (cursor.moveToNext());
            }
        }finally {
            if(cursor != null) cursor.close();
        }
    }



    public void initialisePieChart(List<Integer> painPeak){
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

    public void initialiseBarChart(int food, int dehydration, int sleepTooLittle, int sleepTooMuch,
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
