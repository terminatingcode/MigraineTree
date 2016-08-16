package com.terminatingcode.android.migrainetree;

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
 * A Fragment to display the prediction results to the user
 */
public class FeelBetterFragment extends Fragment {
    private static final String NAME = "FeelBetterFragment";
    private TextView predictionTextView;
    private ProgressBar mProgressBar;


    public FeelBetterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_feel_better, container, false);
        predictionTextView = (TextView) rootview.findViewById(R.id.predictionTextView);
        mProgressBar = (ProgressBar) rootview.findViewById(R.id.preditionProgressBar);
        return rootview;
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

    /**
     * Upon receiving the prediction, stop the progress bar
     * and display the result to the user
     * @param message received from the MLPredictionService
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPredictionMade(MLPredictionMessage message){
        if(message != null && message.result != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            predictionTextView.setText(message.result);
        }

    }
}
