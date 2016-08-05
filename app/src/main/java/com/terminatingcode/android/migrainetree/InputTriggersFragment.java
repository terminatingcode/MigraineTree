package com.terminatingcode.android.migrainetree;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MenstrualRecord;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputTriggersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputTriggersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputTriggersFragment extends Fragment {
    private static final String NAME  = "InputTriggersFragment";

    private OnFragmentInteractionListener mListener;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private ImageButton mSetButton;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private TextView mCityTextView;
    private Button mSetNewLocationButton;
    private TextView painTextView;
    private CheckBox auraCheckbox;
    private CheckBox eatenCheckbox;
    private CheckBox waterCheckbox;
    private TextView sleepTextView;
    private TextView stressTextView;
    private TextView eyeStrainTextView;
    private Spinner typeOfPainSpinner;
    private Spinner areaOfPainSpinner;
    private Spinner typeOfMedsSpinner;
    private CheckBox nauseaCheckBox;
    private CheckBox sensitivityLightCheckBox;
    private CheckBox sensitivityNoiseCheckBox;
    private CheckBox sensitivitySmellCheckBox;
    private CheckBox nasalCongestionCheckBox;
    private CheckBox earsCheckBox;
    private CheckBox confusionCheckBox;
    private TextView cycleDayTextView;
    private String date;
    private String time;
    private Button updateCalendarButton;
    private View menstrualDataLayout;
    private Button mSaveRecordButton;
    private SharedPrefsUtils mPrefUtils;
    private String city;
    private int numSetButtonPressed = 0;
    private static final int VIEW_DATE_PICKER = 1;
    private static final int VIEW_TIME_PICKER = 2;
    private static final int MILLISECONDS_IN_DAY = 24*60*60*1000;


    public InputTriggersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputTriggersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputTriggersFragment newInstance(String param1, String param2) {
        InputTriggersFragment fragment = new InputTriggersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_input_triggers, container, false);
        mDateTextView = (TextView) rootView.findViewById(R.id.migraineStartDateTextView);
        mTimeTextView = (TextView) rootView.findViewById(R.id.migraineStartTimeTextView);
        mDatePicker = (DatePicker) rootView.findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        mSetButton = (ImageButton) rootView.findViewById(R.id.set_button);
        mCityTextView = (TextView) rootView.findViewById(R.id.locationTextView);
        mSetNewLocationButton = (Button) rootView.findViewById(R.id.newLocationButton);
        auraCheckbox = (CheckBox) rootView.findViewById(R.id.auraCheckBox);
        eatenCheckbox = (CheckBox) rootView.findViewById(R.id.eatenCheckBox);
        waterCheckbox = (CheckBox) rootView.findViewById(R.id.waterCheckBox);
        nauseaCheckBox = (CheckBox) rootView.findViewById(R.id.nauseaCheckBox);
        sensitivityLightCheckBox = (CheckBox) rootView.findViewById(R.id.sensitivityLightCheckBox);
        sensitivityNoiseCheckBox = (CheckBox) rootView.findViewById(R.id.sensitivityNoiseCheckBox);
        sensitivitySmellCheckBox = (CheckBox) rootView.findViewById(R.id.sensitivitySmellCheckBox);
        nasalCongestionCheckBox = (CheckBox) rootView.findViewById(R.id.nasalCongestionCheckBox);
        earsCheckBox = (CheckBox) rootView.findViewById(R.id.earsCheckBox);
        confusionCheckBox = (CheckBox) rootView.findViewById(R.id.confusionCheckBox);
        menstrualDataLayout = rootView.findViewById(R.id.menstrualDataLayout);
        cycleDayTextView = (TextView) rootView.findViewById(R.id.cycleDayTextView);
        typeOfPainSpinner = (Spinner) rootView.findViewById(R.id.typeOfPainSpinner);
        areaOfPainSpinner = (Spinner) rootView.findViewById(R.id.areasOfPainSpinner);
        typeOfMedsSpinner = (Spinner) rootView.findViewById(R.id.typeOfMedsSpinner);
        painTextView = (TextView) rootView.findViewById(R.id.painLevelTextView);
        sleepTextView = (TextView) rootView.findViewById(R.id.sleepLevelTextView);
        stressTextView = (TextView) rootView.findViewById(R.id.stressLevelTextView);
        eyeStrainTextView = (TextView) rootView.findViewById(R.id.eyesLevelTextView);
        updateCalendarButton = (Button) rootView.findViewById(R.id.switchToMenstrualCalendarButton);
        mSaveRecordButton = (Button) rootView.findViewById(R.id.saveRecordButton);

        Calendar calendar = Calendar.getInstance();
        long milliseconds = calendar.getTimeInMillis();
        mDatePicker.setMaxDate(milliseconds);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSetButtonPressed++;
                int stage = numSetButtonPressed % 3;
                if(stage == VIEW_DATE_PICKER) mDatePicker.setVisibility(View.VISIBLE);
                else if(stage == VIEW_TIME_PICKER){
                    setDate();
                    mDatePicker.setVisibility(View.GONE);
                    mTimePicker.setVisibility(View.VISIBLE);
                }
                else {
                    setTime();
                    mTimePicker.setVisibility(View.GONE);
                }

            }
        });
        //display saved city
        SharedPreferences mSharedPreferences = getActivity()
                .getSharedPreferences(Constants.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        mPrefUtils = new SharedPrefsUtils(mSharedPreferences);
        city = mPrefUtils.getSavedCity();
        mCityTextView.setText(city);

        //signal to mainactivity to switch fragments when new location button clicked
        mSetNewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewLocationButtonClicked();
            }
        });

        //initialise Spinners
        ArrayAdapter<CharSequence> typeOfPainAdapter =
                ArrayAdapter
                        .createFromResource(getActivity(),
                                R.array.pain_types_array,
                                android.R.layout.simple_spinner_dropdown_item);
        typeOfPainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfPainSpinner.setAdapter(typeOfPainAdapter);
        ArrayAdapter<CharSequence> areasOfPainAdapter =
                ArrayAdapter
                        .createFromResource(getActivity(),
                                R.array.pain_areas_array,
                                android.R.layout.simple_spinner_dropdown_item);
        areasOfPainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaOfPainSpinner.setAdapter(areasOfPainAdapter);
        ArrayAdapter<CharSequence> typeOfMedsAdapter =
                ArrayAdapter
                        .createFromResource(getActivity(),
                                R.array.medication_types,
                                android.R.layout.simple_spinner_dropdown_item);
        typeOfMedsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfMedsSpinner.setAdapter(typeOfMedsAdapter);
        //have TextViews change with SeekBar
        updateProgressTextView(painTextView, R.id.painSeekBar, rootView);
        updateProgressTextView(sleepTextView, R.id.sleepSeekBar, rootView);
        updateProgressTextView(stressTextView, R.id.stressSeekBar, rootView);
        updateProgressTextView(eyeStrainTextView, R.id.eyesSeekBar, rootView);
        updateCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateCalendarButtonClicked();
            }
        });

        mSaveRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWeatherHistoryService(date, time);
            }
        });
        return rootView;
    }

    /**
     * fetch user preference to track menstrual data,
     * if true, query MenstrualRecords and return most recent cycle day
     * if false, disappear the menstrual data view
     */
    private void initializeMenstrualData(Long migraineDate) {
        boolean trackingMenstrualData = mPrefUtils.getsavedMenstrualPref();
        if(!trackingMenstrualData){
            menstrualDataLayout.setVisibility(View.GONE);
        }else{
            ContentResolver mResolver = getActivity().getContentResolver();
            String sortOrder = MenstrualRecord.DATE + " DESC";
            Cursor cursor = mResolver.query(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, null, null, null, sortOrder);
            int difference = 0;
            try{
                if(cursor != null  && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int dateIndex = cursor.getColumnIndex(MenstrualRecord.DATE);
                    int menstrualDate = (int) (cursor.getLong(dateIndex) / MILLISECONDS_IN_DAY);
                    int currentDay = (int) (migraineDate / MILLISECONDS_IN_DAY);
                    difference = currentDay - menstrualDate;
                    while (cursor.moveToNext()) {
                        int previousMenstrualDate = (int) (cursor.getLong(dateIndex) / MILLISECONDS_IN_DAY);
                        if (previousMenstrualDate - menstrualDate > 1) break;
                        else difference++;
                    }
                }
            }finally {
                if(cursor != null) cursor.close();
            }
            cycleDayTextView.setText(String.valueOf(difference));
        }
    }

    /**
     * Update startDateButton text to be the date chosen by the user
     */
    private void setDate() {
        int day = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth() + 1;
        int year = mDatePicker.getYear();
        date = day + "/" + month + "/" + year;
        Log.d(NAME, date);
        mDateTextView.setText(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        initializeMenstrualData(calendar.getTimeInMillis());
    }

    /**
     * Updates the startDateButton text to be the
     * time specified by the user
     */
    private void setTime() {
        final DecimalFormat decimalFormat = new DecimalFormat("00");
        String hour = decimalFormat.format(mTimePicker.getHour());
        String minutes = decimalFormat.format(mTimePicker.getMinute());
        time =  hour +
                ":" +
                minutes;
        mTimeTextView.setText(time);
    }

    /**
     * Tracks changes to the SeekBar and updates TextView with progress
     * @param textView the TextView to be updated
     * @param seekBarId the SeekBar to be tracked
     * @param view the root view of the Fragment
     */
    public void updateProgressTextView(final TextView textView, int seekBarId, View view){
        SeekBar seekbar = (SeekBar) view.findViewById(seekBarId);
        if(seekbar != null){
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    String s = String.valueOf(progress);
                    textView.setText(s);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
    }


    /**
     * signals to MainActivity to replace this fragment with UserSettingsFragment
     */
    public void onNewLocationButtonClicked() {
        if (mListener != null) {
            mListener.onSetLocationPressed();
        }
    }

    /**
     * signals to MainActivity to replace this fragment with CalendarFragment
     */
    public void onUpdateCalendarButtonClicked() {
        if (mListener != null) {
            mListener.onUpdateCalendarButtonPressed();
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
        void onSetLocationPressed();
        void onUpdateCalendarButtonPressed();
        void onSaveRecordButtonPressed(String date, String locationUID, MigraineRecordObject migraineRecordObject);
    }

    /**
     * starts WeatherHistoryService to parse past 24 hours
     * will not start if date or time not set
     * displays a toast to notify user if null
     * @param date the start date
     * @param time the start time
     */
    public void startWeatherHistoryService(String date, String time){
        String locationUID = mPrefUtils.getSavedLocationUID();
        if(Objects.equals(locationUID, Constants.CITY_NOT_SET)){
            Toast.makeText(getActivity(), R.string.error_city_not_set, Toast.LENGTH_LONG).show();
        }else if(date == null || time == null){
            Toast.makeText(getActivity(), R.string.dateTimeError, Toast.LENGTH_LONG).show();
        }else if(cycleDayTextView.getText().equals(getActivity().getString(R.string.zero))) {
            Toast.makeText(getActivity(), R.string.noMenstrualData, Toast.LENGTH_LONG).show();
        }else{
            onSaveRecordPressed(date + time, locationUID);
        }
    }

    /**
     * signals to MainActivity to add ProcessRecordFragment to stack
     */
    public void onSaveRecordPressed(String dateTime, String locationUID) {
        if (mListener != null) {
            MigraineRecordObject migraineRecordObject = saveMigraineData();
            if(migraineRecordObject != null) mListener.onSaveRecordButtonPressed(dateTime, locationUID, migraineRecordObject);
            else{
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
            }
        }
    }

    public MigraineRecordObject saveMigraineData(){
        long startHour = Constants.DEFAULT_NO_DATA;
        try {
            startHour = convertStringToInt();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.dateTimeError, Toast.LENGTH_LONG).show();
            return null;
        }
        int painAtOnset = Integer.valueOf(painTextView.getText().toString());
        boolean aura = auraCheckbox.isChecked();
        boolean eaten = eatenCheckbox.isChecked();
        boolean water = waterCheckbox.isChecked();
        int sleep = Integer.valueOf(sleepTextView.getText().toString());
        int stress = Integer.valueOf(stressTextView.getText().toString());
        int eyeStrain = Integer.valueOf(eyeStrainTextView.getText().toString());
        String painType = typeOfPainSpinner.getSelectedItem().toString();
        String painSource = areaOfPainSpinner.getSelectedItem().toString();
        String medication = typeOfMedsSpinner.getSelectedItem().toString();
        boolean nausea = nauseaCheckBox.isChecked();
        boolean light = sensitivityLightCheckBox.isChecked();
        boolean noise = sensitivityNoiseCheckBox.isChecked();
        boolean smell = sensitivitySmellCheckBox.isChecked();
        boolean congestion = nasalCongestionCheckBox.isChecked();
        boolean ears = earsCheckBox.isChecked();
        boolean confusion = confusionCheckBox.isChecked();
        int cycleDay = Integer.valueOf(cycleDayTextView.getText().toString());

        MigraineRecordObject migraineRecordObject = new MigraineRecordObject();
        migraineRecordObject.setStartHour(startHour);
        migraineRecordObject.setCity(city);
        migraineRecordObject.setPainAtOnset(painAtOnset);
        migraineRecordObject.setAura(aura);
        migraineRecordObject.setEaten(eaten);
        migraineRecordObject.setWater(water);
        migraineRecordObject.setSleep(sleep);
        migraineRecordObject.setStress(stress);
        migraineRecordObject.setEyeStrain(eyeStrain);
        migraineRecordObject.setPainType(painType);
        migraineRecordObject.setPainSource(painSource);
        migraineRecordObject.setMedication(medication);
        migraineRecordObject.setNausea(nausea);
        migraineRecordObject.setSensitivityToLight(light);
        migraineRecordObject.setSensitivityToNoise(noise);
        migraineRecordObject.setSensitivityToSmell(smell);
        migraineRecordObject.setCongestion(congestion);
        migraineRecordObject.setEars(ears);
        migraineRecordObject.setConfusion(confusion);
        migraineRecordObject.setMenstrualDay(cycleDay);

        return migraineRecordObject;
    }

    public long convertStringToInt() throws ParseException {
        String dateTime = date + time;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyyhh:mm", Locale.getDefault());
        Date date = df.parse(dateTime);
        return date.getTime();
    }
}
