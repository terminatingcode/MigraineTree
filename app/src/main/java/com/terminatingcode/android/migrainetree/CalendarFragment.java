package com.terminatingcode.android.migrainetree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MenstrualRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    private static final String NAME = "CalendarFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HashSet<Calendar> events;

    private OnFragmentInteractionListener mListener;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        //get events from content provider
        events = getDates();
        final CalendarView cv = ((CalendarView) rootView.findViewById(R.id.calendar_view));
        cv.updateCalendar(events);
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date, float x, float y)
            {
                DateFormat df = SimpleDateFormat.getDateInstance();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                //sanitize time values
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                //add or remove date from events
                if(events.contains(calendar)) {
                    deleteDateInProvider(calendar);
                    events = getDates();
                    cv.updateCalendar(events);
                }else{
                    addDateToProvider(calendar, events);
                    events = getDates();
                    cv.updateCalendar(events);
                }
                Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void addDateToProvider(Calendar calendar, HashSet<Calendar> events) {
        ContentValues storedEvents = new ContentValues();

        Long milliseconds = calendar.getTimeInMillis();
        int dayInCycle = calculateDayInCycle(calendar, events);
        storedEvents.put(
                MenstrualRecord.DATE,
                milliseconds);
        storedEvents.put(MenstrualRecord.DAY_IN_CYCLE,
                dayInCycle);
        Context context = getContext();
        Uri rowInserted = context.getContentResolver().insert(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, storedEvents);
        Log.d(NAME, "# row inserted: " + rowInserted);
    }

    private void deleteDateInProvider(Calendar calendar){
        Long milliseconds = calendar.getTimeInMillis();
        String selection = MenstrualRecord.DATE
                + " = " + milliseconds;
        Context context = getContext();
        int rowsDeleted = context.getContentResolver().delete(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, selection, null);
        Log.d(NAME, "# rows deleted: " + rowsDeleted);
    }

    public static int calculateDayInCycle(Calendar calendar, HashSet<Calendar> events){
        int dayInCycle = 1;
        //find beginning of cycle
        while(events.contains(calendar)){
            calendar.add(Calendar.DATE, -1);
            if(events.contains(calendar)) dayInCycle ++;
        }
        return dayInCycle;
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

    public HashSet<Calendar> getDates(){
        String[] projection = {MenstrualRecord.DATE};

        Cursor cursor = null;
        HashSet<Calendar> events = new HashSet<>();
        try {
            cursor = getContext().getApplicationContext()
                    .getContentResolver().query(LocalContentProvider.CONTENT_URI_MENSTRUAL_RECORDS, projection, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    Calendar calendar = Calendar.getInstance();
                    int datesIndex = cursor.getColumnIndex(MenstrualRecord.DATE);
                    Long milliseconds = cursor.getLong(datesIndex);
                    calendar.setTimeInMillis(milliseconds);
                    Log.d(NAME, calendar.toString());
                    events.add(calendar);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            Log.d(NAME, e.getMessage());
        }finally{
            if(cursor != null) cursor.close();
        }
        return events;
    }
}
