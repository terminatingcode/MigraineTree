package com.terminatingcode.android.migrainetree;

import android.content.Context;
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

import java.util.ArrayList;


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
        initialisePieChart();
        initialiseBarChart();
        return rootView;
    }

    private void initialiseBarChart() {
        triggersBarChart.setDrawValueAboveBar(true);
        triggersBarChart.setDescription("Frequency of triggers");
        triggersBarChart.setDescriptionColor(Color.WHITE);
        triggersBarChart.setDescriptionTextSize(18);
        triggersBarChart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        float start = 0.5f;
        int count = 6;
        int range = 50;
        final String[] triggers = new String[]{
                "",
                "aura",
                "food",
                "dehydration",
                "sleep",
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

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(i + 1f, val));
        }

        BarDataSet set1;

        if (triggersBarChart.getData() != null &&
                triggersBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) triggersBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            triggersBarChart.getData().notifyDataChanged();
            triggersBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Triggers");
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

    public void initialisePieChart(){
        avgPainPieChart.setDescription("Average Pain at Peak");
        avgPainPieChart.setDescriptionColor(Color.WHITE);
        avgPainPieChart.setDescriptionTextSize(18);
        avgPainPieChart.setCenterText("average");
        avgPainPieChart.setUsePercentValues(true);
        avgPainPieChart.setHoleRadius(42f);
        avgPainPieChart.setTransparentCircleRadius(46f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(3, "aura only"));
        entries.add(new PieEntry(3,"mild"));
        entries.add(new PieEntry(2,"moderate"));
        entries.add(new PieEntry(1,"severe"));
        entries.add(new PieEntry(2,"debilitating"));
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
