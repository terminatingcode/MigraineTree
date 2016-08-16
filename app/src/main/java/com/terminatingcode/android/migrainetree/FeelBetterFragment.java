package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.terminatingcode.android.migrainetree.EventMessages.MLPredictionMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeelBetterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeelBetterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeelBetterFragment extends Fragment {
    private static final String NAME = "FeelBetterFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView predictionTextView;
    private ProgressBar mProgressBar;

    private OnFragmentInteractionListener mListener;

    public FeelBetterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FeelBetterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeelBetterFragment newInstance() {
        FeelBetterFragment fragment = new FeelBetterFragment();
        Bundle args = new Bundle();
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
        View rootview =  inflater.inflate(R.layout.fragment_feel_better, container, false);
        predictionTextView = (TextView) rootview.findViewById(R.id.predictionTextView);
        mProgressBar = (ProgressBar) rootview.findViewById(R.id.preditionProgressBar);
        return rootview;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
    public void onPredictionMade(MLPredictionMessage message){
        if(message != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            predictionTextView.setText(message.result);
        }

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
