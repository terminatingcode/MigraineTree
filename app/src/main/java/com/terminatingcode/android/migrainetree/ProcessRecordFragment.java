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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


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
    private TextView temp12ChangeTextView;
    private TextView temp24ChangeTextView;
    private TextView hum12ChangeTextView;
    private TextView hum24ChangeTextView;
    private TextView ap12ChangeTextView;
    private TextView ap24ChangeTextView;

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
        temp12ChangeTextView = (TextView) rootView.findViewById(R.id.temp12ChangeTextView);
        temp24ChangeTextView = (TextView) rootView.findViewById(R.id.temp24ChangeTextView);
        hum12ChangeTextView = (TextView) rootView.findViewById(R.id.hum12ChangeTextView);
        hum24ChangeTextView = (TextView) rootView.findViewById(R.id.hum24ChangeTextView);
        ap12ChangeTextView = (TextView) rootView.findViewById(R.id.ap12ChangeTextView);
        ap24ChangeTextView = (TextView) rootView.findViewById(R.id.ap24ChangeTextView);
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
            String temp12Change = String.valueOf(mWeather24Hour.getTempChange12Hrs());
            String temp24Change = String.valueOf(mWeather24Hour.getTempChange24Hrs());
            String hum12Change = String.valueOf(mWeather24Hour.getHumChange12Hrs());
            String hum24Change = String.valueOf(mWeather24Hour.getHumChange24Hrs());
            String ap12Change = String.valueOf(mWeather24Hour.getApChange12Hrs());
            String ap24Change = String.valueOf(mWeather24Hour.getApChange24Hrs());
            temp12ChangeTextView.setText(temp12Change);
            temp24ChangeTextView.setText(temp24Change);
            hum12ChangeTextView.setText(hum12Change);
            hum24ChangeTextView.setText(hum24Change);
            ap12ChangeTextView.setText(ap12Change);
            ap24ChangeTextView.setText(ap24Change);
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
