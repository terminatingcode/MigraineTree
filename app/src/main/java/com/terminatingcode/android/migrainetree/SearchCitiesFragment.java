package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 *
 */
public class SearchCitiesFragment extends Fragment {
    private static final String NAME = "SearchCitiesFragment";
    private static GeoLookupArrayAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private AutoCompleteTextView mAutoCompleteTextView;
    private TextView cityTextView;
    private Button searchButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_cities, container, false);
        mAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.location);
        cityTextView = (TextView) rootView.findViewById(R.id.displayCity);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);

        mAdapter = new GeoLookupArrayAdapter(getActivity(),R.layout.row_cities );
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setAdapter(mAdapter);
        mAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoCompleteTextView.setText("");
                mAdapter.clear();
            }
        });
        mAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
                    String inputtedCity = mAutoCompleteTextView.getText().toString();
                    startGeoLookupService(inputtedCity);
                    return true;
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputtedCity = mAutoCompleteTextView.getText().toString();
                startGeoLookupService(inputtedCity);
            }
        });
        //if a user selects a city, save to SharedPreferences
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String citySelected = (String) parent.getItemAtPosition(position);
                mSharedPreferences = getActivity()
                        .getSharedPreferences(getString(R.string.PREFERENCES_FILE_KEY), Context.MODE_PRIVATE);
                SharedPrefsUtils mSharedPrefsUtils = new SharedPrefsUtils(mSharedPreferences);
                String cityUID = CitiesMapSingleton.newInstance().getUID(citySelected);
                mSharedPrefsUtils.saveSelectedCity(citySelected, cityUID);
                cityTextView.setText(citySelected);
                cityTextView.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
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
     * Receives String[] from EventBus GeoLookupService
     * updates autocompleteTextView with potential locations
     * @param event The cities received by the JsonRequest
     */
    @Subscribe
    public void onMessageEventSetCities(MessageEvent event){
        Log.d(NAME, event.cities[0]);
        if(mAdapter != null){
            for (String CITY : event.cities) {
                mAdapter.add(CITY);
            }
        }
    }

    public void startGeoLookupService(String inputtedCity){
        Intent intent = new Intent(getActivity(), GeoLookupService.class);
        intent.setAction(inputtedCity);
        getActivity().startService(intent);
    }
}
