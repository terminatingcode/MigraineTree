package com.terminatingcode.android.migrainetree.controller;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.terminatingcode.android.migrainetree.utils.Constants;
import com.terminatingcode.android.migrainetree.utils.DateUtils;
import com.terminatingcode.android.migrainetree.model.MigraineRecordObject;
import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.model.SQLite.MigraineRecord;
import com.terminatingcode.android.migrainetree.model.amazonaws.nosql.DynamoDBUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

public class FinishRecordFragment extends Fragment {
    private static final String NAME = "FinishRecordFragment";
    private int numSetButtonPressed = 0;
    private static final int VIEW_DATE_PICKER = 1;
    private static final int VIEW_TIME_PICKER = 2;
    private static final String DYNAMODB_TABLE_NAME = "MigraineRecord";
    private String date;
    private String time;
    private TextView peakPainLevelTextView;
    private SeekBar peakPainSeekbar;
    private TextView endTimeTextView;
    private TextView endDateTextView;
    private ImageButton setTimeDateButton;
    private DatePicker endDatePicker;
    private TimePicker endTimePicker;
    private Button confirmButton;
    private Spinner typeOfMedsSpinner;
    private NotificationManager notificationManager;
    private Uri uri;
    private long startHour;
    private long endHour;
    private int painAtPeak;
    private String medication;
    private MigraineRecordObject mRecordObject;

    /**
     *  Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param uri The uri reference to the record in SQLite
     * @param migraineRecordObject the MigraineRecord Object holding partial record
     * @return A new instance of fragment FinishRecordFragment
     */
    public static FinishRecordFragment newInstance(Uri uri, MigraineRecordObject migraineRecordObject) {
        FinishRecordFragment fragment = new FinishRecordFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.INSERTED_URI, uri);
        args.putParcelable(Constants.RECORD_OBJECT, migraineRecordObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            uri = getArguments().getParcelable(Constants.INSERTED_URI);
            mRecordObject = getArguments().getParcelable(Constants.RECORD_OBJECT);
            startHour = mRecordObject.getStartHour();
            medication = mRecordObject.getMedication();
            Log.d(NAME, "received metadata uri " + uri + " starthour: " + mRecordObject.getStartHour());
            Log.d(NAME, "weather data " + mRecordObject.getCurrentAP());
        }
    }

    @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_finish_record, container, false);

        peakPainLevelTextView = (TextView) rootview.findViewById(R.id.FRpainPeakLevelTextView);
        peakPainSeekbar = (SeekBar) rootview.findViewById(R.id.FRpainPeakSeekBar);
        endTimeTextView = (TextView) rootview.findViewById(R.id.FREndTimeTextView);
        endDateTextView = (TextView) rootview.findViewById(R.id.FREndDateTextView);
        setTimeDateButton = (ImageButton) rootview.findViewById(R.id.fr_end_set_button);
        endDatePicker = (DatePicker) rootview.findViewById(R.id.FRendDatePicker);
        endDatePicker.setMinDate(startHour);
        endTimePicker = (TimePicker) rootview.findViewById(R.id.FRendTimePicker);
        confirmButton = (Button) rootview.findViewById(R.id.FRconfirmButton);
        typeOfMedsSpinner = (Spinner) rootview.findViewById(R.id.finishTypeOfMedsSpinner);
        initialiseSpinner();
        setUpDateTime();
        if(peakPainSeekbar != null){
            peakPainSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    String s = String.valueOf(progress);
                    peakPainLevelTextView.setText(s);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to sql
                if(updateSQLite()) {
                    updateMigraineObject();
                    DynamoDBUtils.persistToAWS(mRecordObject, getActivity());
                }
            }
        });
        return rootview;
    }

    private void initialiseSpinner() {
        ArrayAdapter<CharSequence> typeOfMedsAdapter =
                ArrayAdapter
                        .createFromResource(getActivity(),
                                R.array.medication_types,
                                android.R.layout.simple_spinner_dropdown_item);
        typeOfMedsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfMedsSpinner.setAdapter(typeOfMedsAdapter);
        typeOfMedsSpinner.setSelection(typeOfMedsAdapter.getPosition(medication));
    }

    /**
     * updates the SQLite record with the end time and peak pain
     * @return true if a successful update to SQLite
     * false if date not inputted, before the start time or an error with the URI
     */
    private boolean updateSQLite() {
        if(uri != null) {
            ContentResolver mResolver = getActivity().getContentResolver();
            ContentValues values = new ContentValues();
            endHour = Constants.DEFAULT_NO_DATA;
            try {
                endHour = DateUtils.convertStringToLong(date + time);
                Log.d(NAME, "end: " + endHour + "start: " + startHour);
                if(endHour <= startHour){
                    Toast.makeText(getActivity(), R.string.timeError, Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.dateTimeError, Toast.LENGTH_LONG).show();
                return false;
            }
            painAtPeak = Integer.valueOf(peakPainLevelTextView.getText().toString());
            medication = typeOfMedsSpinner.getSelectedItem().toString();
            values.put(MigraineRecord.MEDICATION, medication);
            values.put(MigraineRecord.PAIN_AT_PEAK, painAtPeak);
            values.put(MigraineRecord.END_HOUR, endHour);

            int updated = mResolver.update(uri, values, null, null);
            Log.d(NAME, "updated log "+ updated);

            //cancel Notification
            notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Constants.NOTIFICATION_ID);
            return true;
        }else{
            Log.d(NAME, "uri is null");
            return false;
        }
    }

    /**
     * updates the MigraineRecordObject with the end time and peak pain
     * so that the data can be passed to AWS to be inserted in DynamoDB
     */
    private void updateMigraineObject() {
        mRecordObject.setMedication(medication);
        mRecordObject.setPainAtPeak(painAtPeak);
        mRecordObject.setEndHour(endHour);
    }

    private void setUpDateTime() {
        Calendar calendar = Calendar.getInstance();
        long milliseconds = calendar.getTimeInMillis();
        endDatePicker.setMaxDate(milliseconds);
        setTimeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSetButtonPressed++;
                int stage = numSetButtonPressed % 3;
                if(stage == VIEW_DATE_PICKER) endDatePicker.setVisibility(View.VISIBLE);
                else if(stage == VIEW_TIME_PICKER){
                    setDate();
                    endDatePicker.setVisibility(View.GONE);
                    endTimePicker.setVisibility(View.VISIBLE);
                }
                else {
                    setTime();
                    endTimePicker.setVisibility(View.GONE);
                }

            }
        });
    }

    /**
     * Update startDateButton text to be the date chosen by the user
     */
    private void setDate() {
        int day = endDatePicker.getDayOfMonth();
        int month = endDatePicker.getMonth() + 1;
        int year = endDatePicker.getYear();
        date = day + "/" + month + "/" + year;
        Log.d(NAME, date);
        endDateTextView.setText(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
    }

    /**
     * Updates the startDateButton text to be the
     * time specified by the user
     * Android versions prior to Marshmallow do not recognize new methods
     * and must use depreciated methods
     */
    private void setTime() {
        final DecimalFormat decimalFormat = new DecimalFormat("00");
        int h, m;
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
            h = endTimePicker.getHour();
            m = endTimePicker.getMinute();
        } else {
            h = endTimePicker.getCurrentHour();
            m = endTimePicker.getCurrentMinute();
        }
        String hour = decimalFormat.format(h);
        String minutes = decimalFormat.format(m);
        time =  hour +
                ":" +
                minutes;
        endTimeTextView.setText(time);
    }
}
