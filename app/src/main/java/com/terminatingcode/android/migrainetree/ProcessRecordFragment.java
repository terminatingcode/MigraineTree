package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.terminatingcode.android.migrainetree.Weather.Weather24Hour;
import com.terminatingcode.android.migrainetree.Weather.WeatherHour;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


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
    private static final String ARG_DATE = "param1";
    private static final String ARG_LOCATION = "param2";
    private TextView tempChangeTextView;
    private TextView humChangeTextView;
    private TextView apChangeTextView;

    private OnFragmentInteractionListener mListener;

    public ProcessRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProcessRecordFragment.
     */
    public static ProcessRecordFragment newInstance() {
        ProcessRecordFragment fragment = new ProcessRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_process_record, container, false);
        tempChangeTextView = (TextView) rootView.findViewById(R.id.tempChangeTextView);
        humChangeTextView = (TextView) rootView.findViewById(R.id.humChangeTextView);
        apChangeTextView = (TextView) rootView.findViewById(R.id.apChangeTextView);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onConfirmButtonPressed(Uri uri) {
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
    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d(NAME, "subscribed to EventBus");
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d(NAME, "unsubscribed to EventBus");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event){
        Weather24Hour mWeather24Hour = event.mWeather24Hour;
        if(mWeather24Hour.getSize() == 24){
            mWeather24Hour.calculateChanges();
            String tempChange = String.valueOf(mWeather24Hour.getTempChange12Hrs());
            String humChange = String.valueOf(mWeather24Hour.getHumChange12Hrs());
            String apChange = String.valueOf(mWeather24Hour.getApChange12Hrs());
            tempChangeTextView.append(tempChange);
            humChangeTextView.append(humChange);
            apChangeTextView.append(apChange);
            List<WeatherHour> hours = mWeather24Hour.getHours();
            for(WeatherHour hour : hours){
                Log.d(NAME, hour.toString());
            }
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
