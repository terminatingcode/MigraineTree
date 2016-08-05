package com.terminatingcode.android.migrainetree;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.terminatingcode.android.migrainetree.EventMessages.Weather24HourMessageEvent;
import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;
import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DemoNoSQLTableBase;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DemoNoSQLTableFactory;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DynamoDBUtils;
import com.terminatingcode.android.migrainetree.amazonaws.util.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProcessRecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProcessRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProcessRecordFragment extends Fragment {
    private static final String NAME = "ProcessRecordFragment";
    private static final String ARG_RECORD = "record";
    private static final String DYNAMODB_TABLE_NAME = "MigraineRecord";

    private TextView temp12ChangeTextView;
    private TextView temp24ChangeTextView;
    private TextView hum12ChangeTextView;
    private TextView hum24ChangeTextView;
    private TextView ap12ChangeTextView;
    private TextView ap24ChangeTextView;
    private Weather24Hour mWeather24Hour;
    private TextView locationTextView;
    private TextView dateTextView;
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
    private Switch migraineDoneSwitch;
    private LinearLayout migraineDoneView;
    private TextView peakPainLevelTextView;
    private SeekBar peakPainSeekbar;
    private TextView endTimeTextView;
    private TextView endDateTextView;
    private ImageButton setTimeDateButton;
    private DatePicker endDatePicker;
    private TimePicker endTimePicker;
    private Button confirmButton;
    private boolean endDataInputted = false;
    private boolean weatherDataReceived = false;
    private int numSetButtonPressed = 0;
    private static final int VIEW_DATE_PICKER = 1;
    private static final int VIEW_TIME_PICKER = 2;
    private String date;
    private String time;
    private MigraineRecordObject mMigraineRecordObject;
    private OnFragmentInteractionListener mListener;

    public ProcessRecordFragment() {
        // Required empty public constructor
    }

    /**
     * factory method for ProcessRecordFragment
     * @param migraineRecordObject the temporary object holding User inputted data
     * @return a new instance of ProcessRecordFragment
     */
    public static ProcessRecordFragment newInstance(MigraineRecordObject migraineRecordObject) {
        ProcessRecordFragment fragment = new ProcessRecordFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECORD, migraineRecordObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mMigraineRecordObject = getArguments().getParcelable(ARG_RECORD);
            Log.d(NAME, "received migraine info");
        }
        Log.d(NAME, "onCREATE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(NAME, "onCREATEVIEW");
        View rootView = inflater.inflate(R.layout.fragment_process_record, container, false);
        temp12ChangeTextView = (TextView) rootView.findViewById(R.id.temp12ChangeTextView);
        temp24ChangeTextView = (TextView) rootView.findViewById(R.id.temp24ChangeTextView);
        hum12ChangeTextView = (TextView) rootView.findViewById(R.id.hum12ChangeTextView);
        hum24ChangeTextView = (TextView) rootView.findViewById(R.id.hum24ChangeTextView);
        ap12ChangeTextView = (TextView) rootView.findViewById(R.id.ap12ChangeTextView);
        ap24ChangeTextView = (TextView) rootView.findViewById(R.id.ap24ChangeTextView);
        locationTextView = (TextView) rootView.findViewById(R.id.recordLocationTextView);
        dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        painTextView = (TextView) rootView.findViewById(R.id.painEnteredTextView);
        auraImageView = (ImageView) rootView.findViewById(R.id.auraEntered);
        eatenImageView = (ImageView) rootView.findViewById(R.id.eatenEntered);
        waterImageView = (ImageView) rootView.findViewById(R.id.waterEntered);
        sleepTextView = (TextView) rootView.findViewById(R.id.sleepEntered);
        stressTextView = (TextView) rootView.findViewById(R.id.stressEntered);
        eyesTextView = (TextView) rootView.findViewById(R.id.eyeStrainEntered);
        painTypeTextView = (TextView) rootView.findViewById(R.id.typeOfPainEntered);
        painSourceTextView = (TextView) rootView.findViewById(R.id.sourceOfPainEntered);
        medicationTextView = (TextView) rootView.findViewById(R.id.medicationEntered);
        symptomsTextView = (TextView) rootView.findViewById(R.id.symptomsEntered);
        cycleDayTextView = (TextView) rootView.findViewById(R.id.cycleDayEntered);
        migraineDoneSwitch = (Switch) rootView.findViewById(R.id.switch1);
        migraineDoneView = (LinearLayout) rootView.findViewById(R.id.migraineDoneView);
        peakPainLevelTextView = (TextView) rootView.findViewById(R.id.painPeakLevelTextView);
        peakPainSeekbar = (SeekBar) rootView.findViewById(R.id.painPeakSeekBar);
        endTimeTextView = (TextView) rootView.findViewById(R.id.migraineEndTimeTextView);
        endDateTextView = (TextView) rootView.findViewById(R.id.migraineEndDateTextView);
        setTimeDateButton = (ImageButton) rootView.findViewById(R.id.end_set_button);
        endDatePicker = (DatePicker) rootView.findViewById(R.id.endDatePicker);
        endTimePicker = (TimePicker) rootView.findViewById(R.id.endTimePicker);
        confirmButton = (Button) rootView.findViewById(R.id.confirmButton);
        makeMigraineDoneVisible();
        migraineDoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) disappearMigraineDoneView();
                else makeMigraineDoneVisible();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = saveToSQLite();
                if(endDataInputted) {
                    //insert local sql then send data to cloud
                    persistToAWS(uri);
                }else{
                    //make a prediction with current data and ensure user comes back to input end data
                    displayNotification(uri);
                }
            }
        });
        return rootView;
    }

    private void persistToAWS(final Uri uri) {
        final DemoNoSQLTableBase demoTable = DemoNoSQLTableFactory.instance(getContext().getApplicationContext())
                .getNoSQLTableByTableName(DYNAMODB_TABLE_NAME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    demoTable.insertRecord(uri);
                } catch (final AmazonClientException ex) {
                    DynamoDBUtils.showErrorDialogForServiceException(getActivity(),
                            getString(R.string.nosql_dialog_title_failed_operation_text), ex);
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                        dialogBuilder.setTitle(R.string.uploading_data);
                        dialogBuilder.setMessage(R.string.uploading_data_dialog_message);
                        dialogBuilder.setNegativeButton(R.string.nosql_dialog_ok_text, null);
                        dialogBuilder.show();
                    }
                });
            }
        }).start();
    }

    private Uri saveToSQLite() {
        ContentResolver mResolver = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(MigraineRecord.START_HOUR, mMigraineRecordObject.getStartHour());
        values.put(MigraineRecord.CITY, mMigraineRecordObject.getCity());
        values.put(MigraineRecord.PAIN_AT_ONSET, mMigraineRecordObject.getPainAtOnset());
        values.put(MigraineRecord.AURA, mMigraineRecordObject.isAura());
        values.put(MigraineRecord.EATEN, mMigraineRecordObject.isEaten());
        values.put(MigraineRecord.WATER, mMigraineRecordObject.isWater());
        values.put(MigraineRecord.SLEEP, mMigraineRecordObject.getSleep());
        values.put(MigraineRecord.STRESS, mMigraineRecordObject.getStress());
        values.put(MigraineRecord.EYE_STRAIN, mMigraineRecordObject.getEyeStrain());
        values.put(MigraineRecord.PAIN_TYPE, mMigraineRecordObject.getPainType());
        values.put(MigraineRecord.PAIN_SOURCE, mMigraineRecordObject.getPainSource());
        values.put(MigraineRecord.MEDICATION, mMigraineRecordObject.getMedication());
        values.put(MigraineRecord.NAUSEA, mMigraineRecordObject.isNausea());
        values.put(MigraineRecord.SENSITIVITY_TO_LIGHT, mMigraineRecordObject.isSensitivityToLight());
        values.put(MigraineRecord.SENSITIVITY_TO_NOISE, mMigraineRecordObject.isSensitivityToNoise());
        values.put(MigraineRecord.SENSITIVITY_TO_SMELL, mMigraineRecordObject.isSensitivityToSmell());
        values.put(MigraineRecord.CONGESTION, mMigraineRecordObject.isCongestion());
        values.put(MigraineRecord.EARS, mMigraineRecordObject.isEars());
        values.put(MigraineRecord.CONFUSION, mMigraineRecordObject.isConfusion());
        values.put(MigraineRecord.MENSTRUAL_DAY, mMigraineRecordObject.getMenstrualDay());

        if(weatherDataReceived) {
            values.put(MigraineRecord.TEMP3HOURS, mWeather24Hour.getTempChange3Hrs());
            values.put(MigraineRecord.TEMP12HOURS, mWeather24Hour.getTempChange12Hrs());
            values.put(MigraineRecord.TEMP24HOURS, mWeather24Hour.getTempChange24Hrs());
            values.put(MigraineRecord.HUM3HOURS, mWeather24Hour.getHumChange3Hrs());
            values.put(MigraineRecord.HUM12HOURS, mWeather24Hour.getHumChange12Hrs());
            values.put(MigraineRecord.HUM24HOURS, mWeather24Hour.getHumChange24Hrs());
            values.put(MigraineRecord.AP3HOURS, mWeather24Hour.getApChange3Hrs());
            values.put(MigraineRecord.AP12HOURS, mWeather24Hour.getApChange12Hrs());
            values.put(MigraineRecord.AP24HOURS, mWeather24Hour.getApChange24Hrs());
        }

        if(endDataInputted) {
            long endHour = Constants.DEFAULT_NO_DATA;
            try {
                endHour = convertStringToInt();
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.dateTimeError, Toast.LENGTH_LONG).show();
            }
            int painAtPeak = Integer.valueOf(peakPainLevelTextView.getText().toString());
            values.put(MigraineRecord.PAIN_AT_PEAK, painAtPeak);
            values.put(MigraineRecord.END_HOUR, endHour);
        }

        return mResolver.insert(LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, values);
    }

    public long convertStringToInt() throws ParseException {
        String dateTime = date + time;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyyhh:mm", Locale.getDefault());
        Date date = df.parse(dateTime);
        return date.getTime();
    }

    private void makeMigraineDoneVisible() {
        migraineDoneView.setVisibility(View.VISIBLE);
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
                    endDataInputted = true;
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

    private void disappearMigraineDoneView() {
        migraineDoneView.setVisibility(View.GONE);
        endDataInputted = false;
    }

    /**
     * once user confirms submission, alert MainActivity
     */
    public void onConfirmButtonPressed() {
        if (mListener != null) {
            mListener.onRecordConfirmed();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(NAME, "ATTACH");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * if user destroys fragment by pressing back button or
     * closing app before finishing, cleanup SQLite data
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d(NAME, "subscribed to EventBus");
        if(mMigraineRecordObject != null) getTriggersInputted();
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d(NAME, "unsubscribed to EventBus");
    }

    /**
     * display results from Weather History
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherCalculated(Weather24HourMessageEvent event){
        if(event.mWeather24Hour != null) {
            if (event.mWeather24Hour.getSize() == 24) {
                mWeather24Hour = event.mWeather24Hour;
                event.mWeather24Hour.calculateChanges();
                String temp12Change = String.valueOf(event.mWeather24Hour.getTempChange12Hrs());
                String temp24Change = String.valueOf(event.mWeather24Hour.getTempChange24Hrs());
                String hum12Change = String.valueOf(event.mWeather24Hour.getHumChange12Hrs());
                String hum24Change = String.valueOf(event.mWeather24Hour.getHumChange24Hrs());
                String ap12Change = String.valueOf(event.mWeather24Hour.getApChange12Hrs());
                String ap24Change = String.valueOf(event.mWeather24Hour.getApChange24Hrs());
                temp12ChangeTextView.setText(temp12Change);
                temp24ChangeTextView.setText(temp24Change);
                hum12ChangeTextView.setText(hum12Change);
                hum24ChangeTextView.setText(hum24Change);
                ap12ChangeTextView.setText(ap12Change);
                ap24ChangeTextView.setText(ap24Change);
                weatherDataReceived = true;
            }
        }
    }

    public void getTriggersInputted(){

        String startHour = convertLongToString(mMigraineRecordObject.getStartHour());
        String city = mMigraineRecordObject.getCity();
        String painAtOnset = String.valueOf(mMigraineRecordObject.getPainAtOnset());
        String sleep = String.valueOf(mMigraineRecordObject.getSleep());
        String stress = String.valueOf(mMigraineRecordObject.getStress());
        String eyeStrain = String.valueOf(mMigraineRecordObject.getEyeStrain());
        String painType = mMigraineRecordObject.getPainType();
        String painSource = mMigraineRecordObject.getPainSource();
        String medication = mMigraineRecordObject.getMedication();
        String cycleDay = String.valueOf(mMigraineRecordObject.getMenstrualDay());

        locationTextView.setText(city);
        if (startHour != null) dateTextView.setText(startHour);
        painTextView.setText(painAtOnset);
        Drawable checkmark = getActivity().getDrawable(android.R.drawable.checkbox_on_background);
        Drawable x = getActivity().getDrawable(android.R.drawable.checkbox_off_background);
        if (mMigraineRecordObject.isAura()) auraImageView.setImageDrawable(checkmark);
        else auraImageView.setImageDrawable(x);
        if (mMigraineRecordObject.isEaten()) eatenImageView.setImageDrawable(checkmark);
        else eatenImageView.setImageDrawable(x);
        if (mMigraineRecordObject.isWater()) waterImageView.setImageDrawable(checkmark);
        else waterImageView.setImageDrawable(x);
        sleepTextView.setText(sleep);
        stressTextView.setText(stress);
        eyesTextView.setText(eyeStrain);
        painTypeTextView.setText(painType);
        painSourceTextView.setText(painSource);
        medicationTextView.setText(medication);
        cycleDayTextView.setText(cycleDay);

        StringBuilder sb = new StringBuilder();
        if (mMigraineRecordObject.isNausea()) sb.append("Nausea\n");
        if (mMigraineRecordObject.isSensitivityToLight()) sb.append("Sensitivity to light\n");
        if (mMigraineRecordObject.isSensitivityToNoise()) sb.append("Sensitivity to noise\n");
        if (mMigraineRecordObject.isSensitivityToSmell()) sb.append("Sensitivity to smell\n");
        if (mMigraineRecordObject.isCongestion()) sb.append("Nasal congestion\n");
        if (mMigraineRecordObject.isEars()) sb.append("Ringin/popped ears\n");
        if (mMigraineRecordObject.isConfusion()) sb.append("Confusion/mental fog");
        symptomsTextView.setText(sb.toString());
    }

    private String convertLongToString(long milliseconds){
        if(milliseconds != Constants.DEFAULT_NO_DATA) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy\nhh:mm", Locale.getDefault());
            Date date = new Date(milliseconds);
            return df.format(date);
        }
        return null;
    }

    private void displayNotification(Uri uri) {
        String message = getActivity().getString(R.string.complete_your_migraine_record);
        Intent notificationIntent = new Intent(getActivity(), FinishRecordActivity.class);
        notificationIntent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(Constants.INSERTED_URI, uri)
                .putExtra(Constants.START_HOUR, mMigraineRecordObject.getStartHour());
        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), requestID, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Display a notification with an icon, message as content, and default sound. It also
        // opens the app when the notification is clicked.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity()).setSmallIcon(
                R.drawable.ic_brain_notification)
                .setContentTitle(getString(R.string.notification_title))
                .setColor(Color.DKGRAY)
                .setContentText(message)
                .setOngoing(true)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onRecordConfirmed();
    }
}
