package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
    private Button startDateButton;
    private ImageButton addDateTimeButton;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

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
        mDatePicker = (DatePicker) rootView.findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        addDateTimeButton = (ImageButton) rootView.findViewById(R.id.set_button);
        startDateButton = (Button) rootView.findViewById(R.id.start_date_button);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        return rootView;
    }

    private void setDate() {
        mDatePicker.setVisibility(View.VISIBLE);
        addDateTimeButton.setVisibility(View.VISIBLE);
        addDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                String date = day + "/" + month + "/" + year;
                startDateButton.setText(date);
                mDatePicker.setVisibility(View.GONE);
                setTime(day, month, year);
            }
        });
    }

    //TODO: store date
    private String setTime(final int day, final int month, final int year) {
        mTimePicker.setVisibility(View.VISIBLE);
        addDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.setVisibility(View.GONE);
            }
        });
        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        return year + month + day +" " + hour + ":" + minute;
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
