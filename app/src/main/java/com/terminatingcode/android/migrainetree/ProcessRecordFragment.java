package com.terminatingcode.android.migrainetree;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.terminatingcode.android.migrainetree.EventMessages.Weather24HourMessageEvent;
import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;
import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
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
    private static final String ARG_URI = "uri";

    private Uri uri;
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
     * @param insertedUri the Uri for the ContentProvider
     * @return a new instance of ProcessRecordFragment
     */
    public static ProcessRecordFragment newInstance(Uri insertedUri) {
        Log.d(NAME, insertedUri.toString() + " NEWINSTANCE");
        ProcessRecordFragment fragment = new ProcessRecordFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, insertedUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            uri = getArguments().getParcelable(ARG_URI);
            Log.d(NAME, "uri = " + uri.toString());
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
        migraineDoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) disappearMigraineDoneView();
                else makeMigraineDoneVisible();
            }
        });
        return rootView;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        int deleted = deleteMigraineData();
        Log.d(NAME, "DETACH " + deleted);
    }
    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d(NAME, "subscribed to EventBus");
        if(uri != null) getTriggersInputted();
        Log.d(NAME, "START");
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d(NAME, "unsubscribed to EventBus");
        Log.d(NAME, "STOP");
    }

    public void onBackPressed(){
        deleteMigraineData();
    }

    /**
     * forget previous data as user has
     */
    private int deleteMigraineData() {
        ContentResolver mResolver = getActivity().getContentResolver();
        return mResolver.delete(uri, null, null);
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
            }
        }
    }

    public void getTriggersInputted(){
        ContentResolver mResolver = getActivity().getContentResolver();
        Cursor cursor = mResolver.query(uri, null, null, null, null);
        try{
            if(cursor != null && cursor.getCount() > 0){
                cursor.moveToNext();
                int dateColumnId = cursor.getColumnIndex(MigraineRecord.START_HOUR);
                int cityColumnId = cursor.getColumnIndex(MigraineRecord.CITY);
                int painAtOnsetColumnId = cursor.getColumnIndex(MigraineRecord.PAIN_AT_ONSET);
                int sleepColumnId = cursor.getColumnIndex(MigraineRecord.SLEEP);
                int stressColumnId = cursor.getColumnIndex(MigraineRecord.STRESS);
                int eyeStrainColumnId = cursor.getColumnIndex(MigraineRecord.EYE_STRAIN);
                int painTypeColumnId = cursor.getColumnIndex(MigraineRecord.PAIN_TYPE);
                int painSourceColumnId = cursor.getColumnIndex(MigraineRecord.PAIN_SOURCE);
                int medicationsColumnId = cursor.getColumnIndex(MigraineRecord.MEDICATION);
                int auraColumnId = cursor.getColumnIndex(MigraineRecord.AURA);
                int eatenColumnId = cursor.getColumnIndex(MigraineRecord.EATEN);
                int waterColumnId = cursor.getColumnIndex(MigraineRecord.WATER);
                int nauseaColumnId = cursor.getColumnIndex(MigraineRecord.NAUSEA);
                int lightColumnId = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_LIGHT);
                int noiseColumnId = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_NOISE);
                int smellColumnId = cursor.getColumnIndex(MigraineRecord.SENSITIVITY_TO_SMELL);
                int congestionColumnId = cursor.getColumnIndex(MigraineRecord.CONGESTION);
                int earsColumnId = cursor.getColumnIndex(MigraineRecord.EARS);
                int confusionColumnId = cursor.getColumnIndex(MigraineRecord.CONFUSION);
                int cycleDayColumnId = cursor.getColumnIndex(MigraineRecord.MENSTRUAL_DAY);

                String startHour = convertLongToString(cursor.getLong(dateColumnId));
                String city = cursor.getString(cityColumnId);
                String painAtOnset = String.valueOf(cursor.getInt(painAtOnsetColumnId));
                String sleep = String.valueOf(cursor.getInt(sleepColumnId));
                String stress = String.valueOf(cursor.getInt(stressColumnId));
                String eyeStrain = String.valueOf(cursor.getInt(eyeStrainColumnId));
                String painType = cursor.getString(painTypeColumnId);
                String painSource = cursor.getString(painSourceColumnId);
                String medication = cursor.getString(medicationsColumnId);
                String cycleDay = String.valueOf(cursor.getInt(cycleDayColumnId));

                locationTextView.setText(city);
                if (startHour != null) dateTextView.setText(startHour);
                painTextView.setText(painAtOnset);
                Drawable checkmark = getActivity().getDrawable(android.R.drawable.checkbox_on_background);
                Drawable x = getActivity().getDrawable(android.R.drawable.checkbox_off_background);
                if (cursor.getInt(auraColumnId) > 0) auraImageView.setImageDrawable(checkmark);
                else auraImageView.setImageDrawable(x);
                if (cursor.getInt(eatenColumnId) > 0) eatenImageView.setImageDrawable(checkmark);
                else eatenImageView.setImageDrawable(x);
                if (cursor.getInt(waterColumnId) > 0) waterImageView.setImageDrawable(checkmark);
                else waterImageView.setImageDrawable(x);
                sleepTextView.setText(sleep);
                stressTextView.setText(stress);
                eyesTextView.setText(eyeStrain);
                painTypeTextView.setText(painType);
                painSourceTextView.setText(painSource);
                medicationTextView.setText(medication);
                cycleDayTextView.setText(cycleDay);

                StringBuilder sb = new StringBuilder();
                if (cursor.getInt(nauseaColumnId) > 0) sb.append("Nausea\n");
                if (cursor.getInt(lightColumnId) > 0) sb.append("Sensitivity to light\n");
                if (cursor.getInt(noiseColumnId) > 0) sb.append("Sensitivity to noise\n");
                if (cursor.getInt(smellColumnId) > 0) sb.append("Sensitivity to smell\n");
                if (cursor.getInt(congestionColumnId) > 0) sb.append("Nasal congestion\n");
                if (cursor.getInt(earsColumnId) > 0) sb.append("Ringin/popped ears\n");
                if (cursor.getInt(confusionColumnId) > 0) sb.append("Confusion/mental fog");
                symptomsTextView.setText(sb.toString());
            }
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    private String convertLongToString(long milliseconds){
        if(milliseconds != Constants.DEFAULT_NO_DATA) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy\nhh:mm", Locale.getDefault());
            Date date = new Date(milliseconds);
            return df.format(date);
        }
        return null;
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
