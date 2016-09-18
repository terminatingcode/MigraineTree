package com.terminatingcode.android.migrainetree.controller;

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
import android.support.v4.content.ContextCompat;
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

import com.terminatingcode.android.migrainetree.utils.Constants;
import com.terminatingcode.android.migrainetree.utils.DateUtils;
import com.terminatingcode.android.migrainetree.model.eventBus.Weather24HourMessageEvent;
import com.terminatingcode.android.migrainetree.model.MigraineRecordObject;
import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.model.SQLite.LocalContentProvider;
import com.terminatingcode.android.migrainetree.model.SQLite.MigraineRecord;
import com.terminatingcode.android.migrainetree.model.weather.Weather24Hour;
import com.terminatingcode.android.migrainetree.model.weather.WeatherHour;
import com.terminatingcode.android.migrainetree.model.amazonaws.nosql.DynamoDBUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;


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
    private long endHour;
    private int painAtPeak;
    private boolean endDataComplete;
    private boolean weatherDataReceived;
    private int numSetButtonPressed = 0;
    private static final int VIEW_DATE_PICKER = 1;
    private static final int VIEW_TIME_PICKER = 2;
    private String date;
    private String time;
    private MigraineRecordObject mMigraineRecordObject;
    private FragmentListener mListener;

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
        migraineDoneSwitch = (Switch) rootView.findViewById(R.id.switchMigraineDone);
        migraineDoneView = (LinearLayout) rootView.findViewById(R.id.migraineDoneView);
        peakPainLevelTextView = (TextView) rootView.findViewById(R.id.painPeakLevelTextView);
        peakPainSeekbar = (SeekBar) rootView.findViewById(R.id.painPeakSeekBar);
        endTimeTextView = (TextView) rootView.findViewById(R.id.migraineEndTimeTextView);
        endDateTextView = (TextView) rootView.findViewById(R.id.migraineEndDateTextView);
        setTimeDateButton = (ImageButton) rootView.findViewById(R.id.end_set_button);
        endDatePicker = (DatePicker) rootView.findViewById(R.id.endDatePicker);
        endTimePicker = (TimePicker) rootView.findViewById(R.id.endTimePicker);
        confirmButton = (Button) rootView.findViewById(R.id.confirmButton);
        endDataComplete = false;
        weatherDataReceived = false;
        makeMigraineDoneVisible();
        setOnClickListenersLogic();
        return rootView;
    }

    private void setOnClickListenersLogic() {
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
                ContentResolver mResolver = getActivity().getContentResolver();
                Uri uri = saveToSQLite(mResolver, mMigraineRecordObject);
                if(uri != null) {
                    if (endDataComplete) {
                        //insert local sql then send data to cloud
                        updateMigraineRecordObject();
                        DynamoDBUtils.persistToAWS(mMigraineRecordObject, getActivity());
                    } else {
                        //make a prediction with current data and ensure user comes back to input end data
                        displayNotification(uri);
                        onPartialRecordConfirmed();
                    }
                }
            }
        });
    }


    private void updateMigraineRecordObject() {
        mMigraineRecordObject.setEndHour(endHour);
        mMigraineRecordObject.setPainAtPeak(painAtPeak);
    }

    public Uri saveToSQLite(ContentResolver mResolver, MigraineRecordObject recordObject) {
        ContentValues values = new ContentValues();

        values.put(MigraineRecord.START_HOUR, recordObject.getStartHour());
        values.put(MigraineRecord.CITY, recordObject.getCity());
        values.put(MigraineRecord.PAIN_AT_ONSET, recordObject.getPainAtOnset());
        values.put(MigraineRecord.AURA, recordObject.isAura());
        values.put(MigraineRecord.EATEN, recordObject.isEaten());
        values.put(MigraineRecord.WATER, recordObject.isWater());
        values.put(MigraineRecord.SLEEP, recordObject.getSleep());
        values.put(MigraineRecord.STRESS, recordObject.getStress());
        values.put(MigraineRecord.EYE_STRAIN, recordObject.getEyeStrain());
        values.put(MigraineRecord.PAIN_TYPE, recordObject.getPainType());
        values.put(MigraineRecord.PAIN_SOURCE, recordObject.getPainSource());
        values.put(MigraineRecord.MEDICATION, recordObject.getMedication());
        values.put(MigraineRecord.NAUSEA, recordObject.isNausea());
        values.put(MigraineRecord.SENSITIVITY_TO_LIGHT, recordObject.isSensitivityToLight());
        values.put(MigraineRecord.SENSITIVITY_TO_NOISE, recordObject.isSensitivityToNoise());
        values.put(MigraineRecord.SENSITIVITY_TO_SMELL, recordObject.isSensitivityToSmell());
        values.put(MigraineRecord.CONGESTION, recordObject.isCongestion());
        values.put(MigraineRecord.EARS, recordObject.isEars());
        values.put(MigraineRecord.CONFUSION, recordObject.isConfusion());
        values.put(MigraineRecord.MENSTRUAL_DAY, recordObject.getMenstrualDay());

        if(weatherDataReceived) {
            Log.d(NAME, "saving weather data ap = " + mWeather24Hour.getCurrentHour().getPressure());
            WeatherHour hour = mWeather24Hour.getCurrentHour();
            values.put(MigraineRecord.CURRENT_TEMP, hour.getTemp());
            values.put(MigraineRecord.CURRENT_HUM, hour.getHum());
            values.put(MigraineRecord.CURRENT_AP, hour.getPressure());
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

        if(endDataComplete) {
            endHour = Constants.DEFAULT_NO_DATA;
            try {
                endHour = DateUtils.convertStringToLong(date + time);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.dateTimeError, Toast.LENGTH_LONG).show();
            }
            if(endHour <= recordObject.getStartHour()){
                Toast.makeText(getActivity(), R.string.timeError, Toast.LENGTH_LONG).show();
                return null;
            }
            painAtPeak = Integer.valueOf(peakPainLevelTextView.getText().toString());
            values.put(MigraineRecord.PAIN_AT_PEAK, painAtPeak);
            values.put(MigraineRecord.END_HOUR, endHour);
        }

        return mResolver.insert(LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, values);
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
                if(stage == VIEW_DATE_PICKER){
                    endDatePicker.setVisibility(View.VISIBLE);
                    endDataComplete = false;
                }else if(stage == VIEW_TIME_PICKER){
                    setDate();
                    endDatePicker.setVisibility(View.GONE);
                    endTimePicker.setVisibility(View.VISIBLE);
                }
                else {
                    setTime();
                    endTimePicker.setVisibility(View.GONE);
                    endDataComplete = true;
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

    private void disappearMigraineDoneView() {
        migraineDoneView.setVisibility(View.GONE);
        endDataComplete = false;
    }


    /**
     * once user confirms submission, alert MainActivity
     */
    public void onPartialRecordConfirmed() {
        if (mListener != null) {
            Log.d(NAME, "weather before sending " + mMigraineRecordObject.getCurrentAP());
            mListener.onPartialRecordConfirmed(mMigraineRecordObject);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(NAME, "ATTACH");
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
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
     * display results from Weather History Service Parser
     * @param event the Weather24HourMessageEvent sent upon completion of parsing
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherCalculated(Weather24HourMessageEvent event){
        if(event.mWeather24Hour != null) {
            if (event.mWeather24Hour.getSize() == 24) {
                mWeather24Hour = event.mWeather24Hour;
                event.mWeather24Hour.calculateChanges();
                double temp3 = event.mWeather24Hour.getTempChange3Hrs();
                double temp12 = event.mWeather24Hour.getTempChange12Hrs();
                double temp24 = event.mWeather24Hour.getTempChange24Hrs();
                double hum3 = event.mWeather24Hour.getHumChange3Hrs();
                double hum12 = event.mWeather24Hour.getHumChange12Hrs();
                double hum24 = event.mWeather24Hour.getHumChange24Hrs();
                double ap3 = event.mWeather24Hour.getApChange3Hrs();
                double ap12 = event.mWeather24Hour.getApChange12Hrs();
                double ap24 = event.mWeather24Hour.getApChange24Hrs();
                String temp12Change = String.valueOf(temp12);
                String temp24Change = String.valueOf(temp24);
                String hum12Change = String.valueOf(hum12);
                String hum24Change = String.valueOf(hum24);
                String ap12Change = String.valueOf(ap12);
                String ap24Change = String.valueOf(ap24);

                WeatherHour currentHour = mWeather24Hour.getCurrentHour();
                mMigraineRecordObject.setCurrentTemp(currentHour.getTemp());
                mMigraineRecordObject.setCurrentHum(currentHour.getHum());
                mMigraineRecordObject.setCurrentAP(currentHour.getPressure());
                mMigraineRecordObject.setTemp3Hours(temp3);
                mMigraineRecordObject.setTemp12Hours(temp12);
                mMigraineRecordObject.setTemp24Hours(temp24);
                mMigraineRecordObject.setHum3Hours(hum3);
                mMigraineRecordObject.setHum12Hours(hum12);
                mMigraineRecordObject.setHum24Hours(hum24);
                mMigraineRecordObject.setAP3Hours(ap3);
                mMigraineRecordObject.setAP12Hours(ap12);
                mMigraineRecordObject.setAP24Hours(ap24);
                temp12ChangeTextView.setText(temp12Change);
                temp24ChangeTextView.setText(temp24Change);
                hum12ChangeTextView.setText(hum12Change);
                hum24ChangeTextView.setText(hum24Change);
                ap12ChangeTextView.setText(ap12Change);
                ap24ChangeTextView.setText(ap24Change);
                weatherDataReceived = true;
                Log.d(NAME, "weather ap = " + mMigraineRecordObject.getCurrentAP());
            }
        }
    }

    /**
     * uses MigraineRecordObject to retrieve user data inputted in InputTriggesFragment
     * and displays it to the user
     */
    public void getTriggersInputted(){
        String startHour = DateUtils.convertLongToString(mMigraineRecordObject.getStartHour());
        String city = mMigraineRecordObject.getCity();
        String painAtOnset = String.valueOf(mMigraineRecordObject.getPainAtOnset());
        String sleep = String.valueOf(mMigraineRecordObject.getSleep());
        String stress = String.valueOf(mMigraineRecordObject.getStress());
        String eyeStrain = String.valueOf(mMigraineRecordObject.getEyeStrain());
        String painType = mMigraineRecordObject.getPainType();
        String painSource = mMigraineRecordObject.getPainSource();
        String medicationfull = mMigraineRecordObject.getMedication();
        int i = medicationfull.indexOf(' ');
        String medication = medicationfull.substring(0, i);
        String cycleDay = String.valueOf(mMigraineRecordObject.getMenstrualDay());

        locationTextView.setText(city);
        if (startHour != null) dateTextView.setText(startHour);
        painTextView.setText(painAtOnset);
        Drawable checkmark = ContextCompat.getDrawable(getActivity(), android.R.drawable.checkbox_on_background);
        Drawable x = ContextCompat.getDrawable(getActivity(), android.R.drawable.checkbox_off_background);
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

    private void displayNotification(Uri uri) {
        String message = getActivity().getString(R.string.complete_your_migraine_record);
        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        notificationIntent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Constants.INSERTED_URI, uri)
                .putExtra(Constants.RECORD_OBJECT, mMigraineRecordObject);
        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), requestID, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Display a notification with an icon and message as content. It also
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
    public interface OnFragmentInteractionListener extends FragmentListener{
        void onPartialRecordConfirmed(MigraineRecordObject migraineRecordObject);
    }
}
