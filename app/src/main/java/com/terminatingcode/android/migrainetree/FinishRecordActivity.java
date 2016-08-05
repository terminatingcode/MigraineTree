package com.terminatingcode.android.migrainetree;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FinishRecordActivity extends Activity {
    private static final String NAME = "FinishRecordActivity";
    private int numSetButtonPressed = 0;
    private static final int VIEW_DATE_PICKER = 1;
    private static final int VIEW_TIME_PICKER = 2;
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
    private NotificationManager notificationManager;
    private Uri uri;
    private long startHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_record);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getParcelable(Constants.INSERTED_URI);
            startHour = extras.getLong(Constants.START_HOUR);
            Log.d(NAME, "received metadata uri " + uri + "starthour: " + startHour);
        }

        peakPainLevelTextView = (TextView) findViewById(R.id.FRpainPeakLevelTextView);
        peakPainSeekbar = (SeekBar) findViewById(R.id.FRpainPeakSeekBar);
        endTimeTextView = (TextView) findViewById(R.id.FREndTimeTextView);
        endDateTextView = (TextView) findViewById(R.id.FREndDateTextView);
        setTimeDateButton = (ImageButton) findViewById(R.id.fr_end_set_button);
        endDatePicker = (DatePicker) findViewById(R.id.FRendDatePicker);
        endDatePicker.setMinDate(startHour);
        endTimePicker = (TimePicker) findViewById(R.id.FRendTimePicker);
        confirmButton = (Button) findViewById(R.id.FRconfirmButton);
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
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(Constants.NOTIFICATION_ID);
                updateSQLite();
            }
        });
    }


    private void updateSQLite() {
        if(uri != null) {
            ContentResolver mResolver = getContentResolver();
            ContentValues values = new ContentValues();
            long endHour = Constants.DEFAULT_NO_DATA;
            try {
                endHour = convertStringToInt();
                Log.d(NAME, "end: " + endHour + "start: " + startHour);
                if(endHour <= startHour){
                    Toast.makeText(this, R.string.timeError, Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.dateTimeError, Toast.LENGTH_LONG).show();
                return;
            }
            int painAtPeak = Integer.valueOf(peakPainLevelTextView.getText().toString());
            values.put(MigraineRecord.PAIN_AT_PEAK, painAtPeak);
            values.put(MigraineRecord.END_HOUR, endHour);

            int updated = mResolver.update(uri, values, null, null);
            Log.d(NAME, "updated log "+ updated);
        }else{
            Log.d(NAME, "uri is null");
        }
    }

    public long convertStringToInt() throws ParseException {
        String dateTime = date + time;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyyhh:mm", Locale.getDefault());
        Date date = df.parse(dateTime);
        return date.getTime();
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
     */
    private void setTime() {
        final DecimalFormat decimalFormat = new DecimalFormat("00");
        String hour = decimalFormat.format(endTimePicker.getHour());
        String minutes = decimalFormat.format(endTimePicker.getMinute());
        time =  hour +
                ":" +
                minutes;
        endTimeTextView.setText(time);
    }
}
