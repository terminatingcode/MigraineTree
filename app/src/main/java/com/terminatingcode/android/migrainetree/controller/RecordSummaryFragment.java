package com.terminatingcode.android.migrainetree.controller;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.model.SQLite.LocalContentProvider;
import com.terminatingcode.android.migrainetree.model.SQLite.MigraineRecord;
import com.terminatingcode.android.migrainetree.utils.DateUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RecordSummaryFragment extends Fragment {
    private static final String ARG_RECORD_ID = "param1";
    private int recordId;

    private TextView dateTextView;
    private TextView lengthTextView;
    private TextView painTextView;
    private ImageView auraImageView;
    private ImageView eatenImageView;
    private ImageView waterImageView;
    private TextView sleepTextView;
    private TextView stressTextView;
    private TextView eyesTextView;
    private TextView painTypeTextView;
    private TextView painSourceTextView;
    private TextView medicationTextView;
    private TextView symptomsTextView;
    private TextView cycleDayTextView;
    private TextView tempTextView;
    private TextView humTextView;
    private TextView apTextView;
    private TextView temp12ChangeTextView;
    private TextView temp24ChangeTextView;
    private TextView hum12ChangeTextView;
    private TextView hum24ChangeTextView;
    private TextView ap12ChangeTextView;
    private TextView ap24ChangeTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recordId the id for the LocalContentProvider
     * @return A new instance of fragment RecordSummaryFragment.
     */
    public static RecordSummaryFragment newInstance(int recordId) {
        RecordSummaryFragment fragment = new RecordSummaryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECORD_ID, recordId);
        fragment.setArguments(args);
        return fragment;
    }
    public RecordSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recordId = getArguments().getInt(ARG_RECORD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_record_summary, container, false);
        dateTextView = (TextView) rootView.findViewById(R.id.summary_started);
        lengthTextView = (TextView) rootView.findViewById(R.id.summary_length);
        painTextView = (TextView) rootView.findViewById(R.id.summary_pain);
        auraImageView = (ImageView) rootView.findViewById(R.id.summary_aura);
        eatenImageView = (ImageView) rootView.findViewById(R.id.summary_eaten);
        waterImageView = (ImageView) rootView.findViewById(R.id.summary_water);
        sleepTextView = (TextView) rootView.findViewById(R.id.summary_sleep);
        stressTextView = (TextView) rootView.findViewById(R.id.summary_stress);
        eyesTextView = (TextView) rootView.findViewById(R.id.summary_eyes);
        painTypeTextView = (TextView) rootView.findViewById(R.id.summary_type);
        painSourceTextView = (TextView) rootView.findViewById(R.id.summary_source);
        medicationTextView = (TextView) rootView.findViewById(R.id.summary_medication);
        symptomsTextView = (TextView) rootView.findViewById(R.id.summary_symptoms);
        cycleDayTextView = (TextView) rootView.findViewById(R.id.summary_cycle);
        tempTextView = (TextView) rootView.findViewById(R.id.summary_temp);
        humTextView = (TextView) rootView.findViewById(R.id.summary_hum);
        apTextView = (TextView) rootView.findViewById(R.id.summary_ap);
        temp12ChangeTextView = (TextView) rootView.findViewById(R.id.summary_temp12);
        temp24ChangeTextView = (TextView) rootView.findViewById(R.id.summary_temp24);
        hum12ChangeTextView = (TextView) rootView.findViewById(R.id.summary_hum12);
        hum24ChangeTextView = (TextView) rootView.findViewById(R.id.summary_hum24);
        ap12ChangeTextView = (TextView) rootView.findViewById(R.id.summary_ap12);
        ap24ChangeTextView = (TextView) rootView.findViewById(R.id.summary_ap24);
        loadRecord();
        return rootView;
    }



    public void loadRecord(){
        ContentResolver mResolver = getActivity().getContentResolver();
        String whereClause = MigraineRecord._ID + " = " + recordId;
        Cursor cursor = mResolver.query(LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, null, whereClause, null, null);
        try{
            if(cursor != null  && cursor.getCount() > 0) {
                cursor.moveToNext();
                int startIndex = cursor.getColumnIndex(MigraineRecord.START_HOUR);
                int endIndex = cursor.getColumnIndex(MigraineRecord.END_HOUR);
                int painIndex = cursor.getColumnIndex(MigraineRecord.PAIN_AT_PEAK);
                int auraIndex = cursor.getColumnIndex(MigraineRecord.AURA);
                int eatenIndex = cursor.getColumnIndex(MigraineRecord.EATEN);
                int waterIndex = cursor.getColumnIndex(MigraineRecord.WATER);
                int sleepIndex = cursor.getColumnIndex(MigraineRecord.SLEEP);
                int stressIndex = cursor.getColumnIndex(MigraineRecord.STRESS);
                int eyeIndex = cursor.getColumnIndex(MigraineRecord.EYE_STRAIN);
                int typeIndex = cursor.getColumnIndex(MigraineRecord.PAIN_TYPE);
                int sourceIndex = cursor.getColumnIndex(MigraineRecord.PAIN_SOURCE);
                int medicationIndex = cursor.getColumnIndex(MigraineRecord.MEDICATION);
                int nauseaIndex = cursor.getColumnIndex(MigraineRecord.NAUSEA);
                int lightIndex = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_LIGHT);
                int noiseIndex = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_NOISE);
                int smellIndex = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_SMELL);
                int confusionIndex = cursor.getColumnIndex(MigraineRecord.CONFUSION);
                int earsIndex = cursor.getColumnIndex(MigraineRecord.EARS);
                int congestionIndex = cursor.getColumnIndex(MigraineRecord.CONGESTION);
                int cycleIndex = cursor.getColumnIndex(MigraineRecord.MENSTRUAL_DAY);
                int tempIndex = cursor.getColumnIndex(MigraineRecord.CURRENT_TEMP);
                int humIndex = cursor.getColumnIndex(MigraineRecord.CURRENT_HUM);
                int apIndex = cursor.getColumnIndex(MigraineRecord.CURRENT_AP);
                int temp12Index = cursor.getColumnIndex(MigraineRecord.TEMP12HOURS);
                int temp24Index = cursor.getColumnIndex(MigraineRecord.TEMP24HOURS);
                int hum12Index = cursor.getColumnIndex(MigraineRecord.HUM12HOURS);
                int hum24Index = cursor.getColumnIndex(MigraineRecord.HUM24HOURS);
                int ap12Index = cursor.getColumnIndex(MigraineRecord.AP12HOURS);
                int ap24Index = cursor.getColumnIndex(MigraineRecord.AP24HOURS);

                long startHour = cursor.getLong(startIndex);
                Log.d("tag", startHour + "hour");
                long endHour = cursor.getLong(endIndex);
                int cycleDay = cursor.getInt(cycleIndex);

                dateTextView.setText(String.valueOf(DateUtils.convertLongToListString(startHour)));
                lengthTextView.setText(calculateLength(startHour, endHour));
                painTextView.setText(String.valueOf(cursor.getInt(painIndex)));
                sleepTextView.setText(String.valueOf(cursor.getInt(sleepIndex)));
                stressTextView.setText(String.valueOf(cursor.getInt(stressIndex)));
                eyesTextView.setText(String.valueOf(cursor.getInt(eyeIndex)));
                painTypeTextView.setText(cursor.getString(typeIndex));
                painSourceTextView.setText(cursor.getString(sourceIndex));
                String medicationfull = cursor.getString(medicationIndex);
                int i = medicationfull.indexOf(' ');
                String medication = medicationfull.substring(0, i);
                medicationTextView.setText(medication);
                if(cycleDay != 0) cycleDayTextView.setText(String.valueOf(cycleDay));
                tempTextView.setText(String.valueOf(cursor.getDouble(tempIndex)));
                humTextView.setText(String.valueOf(cursor.getDouble(humIndex)));
                apTextView.setText(String.valueOf(cursor.getDouble(apIndex)));
                temp12ChangeTextView.setText(String.valueOf(cursor.getDouble(temp12Index)));
                temp24ChangeTextView.setText(String.valueOf(cursor.getDouble(temp24Index)));
                hum12ChangeTextView.setText(String.valueOf(cursor.getDouble(hum12Index)));
                hum24ChangeTextView.setText(String.valueOf(cursor.getDouble(hum24Index)));
                ap12ChangeTextView.setText(String.valueOf(cursor.getDouble(ap12Index)));
                ap24ChangeTextView.setText(String.valueOf(cursor.getDouble(ap24Index)));

                Drawable checkmark = ContextCompat.getDrawable(getActivity(),android.R.drawable.checkbox_on_background);
                Drawable x = ContextCompat.getDrawable(getActivity(),android.R.drawable.checkbox_off_background);
                if (cursor.getInt(auraIndex) > 0) auraImageView.setImageDrawable(checkmark);
                else auraImageView.setImageDrawable(x);
                if (cursor.getInt(eatenIndex) > 0) eatenImageView.setImageDrawable(checkmark);
                else eatenImageView.setImageDrawable(x);
                if (cursor.getInt(waterIndex) > 0) waterImageView.setImageDrawable(checkmark);
                else waterImageView.setImageDrawable(x);

                StringBuilder sb = new StringBuilder();
                if (cursor.getInt(nauseaIndex) > 0) sb.append("Nausea\n");
                if (cursor.getInt(lightIndex) > 0) sb.append("Sensitivity to light\n");
                if (cursor.getInt(noiseIndex) > 0) sb.append("Sensitivity to noise\n");
                if (cursor.getInt(smellIndex) > 0) sb.append("Sensitivity to smell\n");
                if (cursor.getInt(congestionIndex) > 0) sb.append("Nasal congestion\n");
                if (cursor.getInt(earsIndex) > 0) sb.append("Ringin/popped ears\n");
                if (cursor.getInt(confusionIndex) > 0) sb.append("Confusion/mental fog");
                symptomsTextView.setText(sb.toString());
            }
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    private String calculateLength(long start, long end){
        long milliseconds = end - start;
        int days = (int) (milliseconds / (1000*60*60*24));
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        return days + "d" + hours + "h" + minutes + "m";
    }
}
