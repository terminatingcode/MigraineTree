package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputTriggersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputTriggersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputTriggersFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView dateTextView;
    private TextView timeTextView;
    private ImageButton mSetButton;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private TextView mCityTextView;


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
        dateTextView = (TextView) rootView.findViewById(R.id.migraineStartDateTextView);
        timeTextView = (TextView) rootView.findViewById(R.id.migraineStartTimeTextView);
        mDatePicker = (DatePicker) rootView.findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        mSetButton = (ImageButton) rootView.findViewById(R.id.set_button);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        mCityTextView = (TextView) rootView.findViewById(R.id.locationTextView);
        SharedPreferences mSharedPreferences = getActivity()
                .getSharedPreferences(Constants.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        SharedPrefsUtils mSharedPrefsUtils = new SharedPrefsUtils(mSharedPreferences);
        String city = mSharedPrefsUtils.getSavedCity();
        mCityTextView.setText(city);
        //initialise Spinners
        Spinner typeOfPainSpinner = (Spinner) rootView.findViewById(R.id.typeOfPainSpinner);
        ArrayAdapter<CharSequence> typeOfPainAdapter =
                ArrayAdapter
                        .createFromResource(getActivity(),
                                R.array.pain_types_array,
                                android.R.layout.simple_spinner_dropdown_item);
        typeOfPainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfPainSpinner.setAdapter(typeOfPainAdapter);
        Spinner areaOfPainSpinner = (Spinner) rootView.findViewById(R.id.areasOfPainSpinner);
        ArrayAdapter<CharSequence> areasOfPainAdapter =
                ArrayAdapter
                        .createFromResource(getActivity(),
                                R.array.pain_areas_array,
                                android.R.layout.simple_spinner_dropdown_item);
        areasOfPainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaOfPainSpinner.setAdapter(areasOfPainAdapter);
        Spinner typeOfMedsSpinner = (Spinner) rootView.findViewById(R.id.typeOfMedsSpinner);
        ArrayAdapter<CharSequence> typeOfMedsAdapter =
                ArrayAdapter
                        .createFromResource(getActivity(),
                                R.array.medication_types,
                                android.R.layout.simple_spinner_dropdown_item);
        typeOfMedsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfMedsSpinner.setAdapter(typeOfMedsAdapter);
        //have TextViews change with SeekBar
        updateProgressTextView(R.id.painLevelTextView, R.id.painSeekBar, rootView);
        updateProgressTextView(R.id.sleepLevelTextView, R.id.sleepSeekBar, rootView);
        updateProgressTextView(R.id.stressLevelTextView, R.id.stressSeekBar, rootView);
        updateProgressTextView(R.id.eyesLevelTextView, R.id.eyesSeekBar, rootView);
        return rootView;
    }

    /**
     * Update startDateButton text to be the date chosen by the user
     */
    private void setDate() {
        mDatePicker.setVisibility(View.VISIBLE);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                String date = day + "/" + month + "/" + year;
                dateTextView.setText(date);
                mDatePicker.setVisibility(View.GONE);
                setTime();

            }
        });
    }

    /**
     * Updates the startDateButton text to be the
     * time specified by the user
     */
    private void setTime() {
        mTimePicker.setVisibility(View.VISIBLE);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.setVisibility(View.GONE);
                int hour = mTimePicker.getHour();
                int minutes = mTimePicker.getMinute();
                String time =
                        hour +
                        ":" +
                        minutes;
                timeTextView.setText(time);
            }
        });
    }

    /**
     * Tracks changes to the SeekBar and updates TextView with progress
     * @param textViewId the TextView to be updated
     * @param seekBarId the SeekBar to be tracked
     * @param view the root view of the Fragment
     */
    public void updateProgressTextView(int textViewId, int seekBarId, View view){
        SeekBar seekbar = (SeekBar) view.findViewById(seekBarId);
        final TextView textView = (TextView) view.findViewById(textViewId);
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
