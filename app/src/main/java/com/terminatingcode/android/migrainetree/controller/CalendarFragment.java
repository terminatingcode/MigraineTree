package com.terminatingcode.android.migrainetree.controller;

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

import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.model.SQLite.LocalContentProvider;
import com.terminatingcode.android.migrainetree.model.SQLite.MenstrualRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    private static final String NAME = "CalendarFragment";
    private HashSet<Calendar> events;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
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
        storedEvents.put(MenstrualRecord.DATE, milliseconds);
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
