package com.terminatingcode.android.migrainetree;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DemoNoSQLTableBase;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DemoNoSQLTableFactory;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DynamoDBUtils;
import com.terminatingcode.android.migrainetree.amazonaws.util.ThreadUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

public class FinishRecordActivity extends AppCompatActivity {
    private static final String NAME = "FinishRecordActivity";
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
    private NotificationManager notificationManager;
    private Uri uri;
    private long startHour;
    private long endHour;
    private int painAtPeak;
    private MigraineRecordObject mRecordObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_record);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getParcelable(Constants.INSERTED_URI);
            mRecordObject = extras.getParcelable(Constants.RECORD_OBJECT);
            startHour = mRecordObject.getStartHour();
            Log.d(NAME, "received metadata uri " + uri + "starthour: " + mRecordObject.getStartHour());
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
                updateSQLite();
                updateMigraineObject();
                persistToAWS(mRecordObject);
            }
        });
    }



    private void updateSQLite() {
        if(uri != null) {
            ContentResolver mResolver = getContentResolver();
            ContentValues values = new ContentValues();
            endHour = Constants.DEFAULT_NO_DATA;
            try {
                endHour = DateUtils.convertStringToLong(date + time);
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
            painAtPeak = Integer.valueOf(peakPainLevelTextView.getText().toString());
            values.put(MigraineRecord.PAIN_AT_PEAK, painAtPeak);
            values.put(MigraineRecord.END_HOUR, endHour);

            int updated = mResolver.update(uri, values, null, null);
            Log.d(NAME, "updated log "+ updated);

            //cancel Notification
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(Constants.NOTIFICATION_ID);
        }else{
            Log.d(NAME, "uri is null");
        }
    }

    private void updateMigraineObject() {
        mRecordObject.setPainAtPeak(painAtPeak);
        mRecordObject.setEndHour(endHour);
    }

    private void persistToAWS(final MigraineRecordObject migraineRecordObject) {
        final DemoNoSQLTableBase demoTable = DemoNoSQLTableFactory.instance(this.getApplicationContext())
                .getNoSQLTableByTableName(DYNAMODB_TABLE_NAME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    demoTable.insertRecord(migraineRecordObject);
                } catch (final AmazonClientException ex) {
                    DynamoDBUtils.showErrorDialogForServiceException(FinishRecordActivity.this,
                            getString(R.string.nosql_dialog_title_failed_operation_text), ex);
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FinishRecordActivity.this);
                        dialogBuilder.setTitle(R.string.uploading_data);
                        dialogBuilder.setMessage(R.string.uploading_data_dialog_message);
                        dialogBuilder.setPositiveButton(R.string.nosql_dialog_ok_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(FinishRecordActivity.this, MainActivity.class);
                                intent.setFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        dialogBuilder.show();
                    }
                });
            }
        }).start();
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
