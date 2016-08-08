package com.terminatingcode.android.migrainetree;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;
import com.terminatingcode.android.migrainetree.dummy.MigraineRecordItems.RecordItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecordsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecordsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecordsFragment newInstance(int columnCount) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMM, yyyy", Locale.getDefault());

        List<RecordItem> items = new ArrayList<>();
        ContentResolver mResolver = getActivity().getContentResolver();
        Cursor cursor = mResolver.query(LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, null, null, null, null);
        try{
            if(cursor != null  && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int idIndex = cursor.getColumnIndex(MigraineRecord._ID);
                    int id = cursor.getInt(idIndex);
                    int startIndex = cursor.getColumnIndex(MigraineRecord.START_HOUR);
                    int painIndex = cursor.getColumnIndex(MigraineRecord.PAIN_AT_PEAK);
                    long startHour = cursor.getLong(startIndex);

                    Date date = new Date(startHour);
                    String content = String.valueOf(dateFormat.format(date));
                    String description = "Pain: " + String.valueOf(cursor.getInt(painIndex));
                    items.add(new RecordItem(id, content, description));
                }while (cursor.moveToNext());
            }
        }finally {
            if(cursor != null) cursor.close();
        }
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(items, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListDeleteItem(RecordItem item);
    }
}
