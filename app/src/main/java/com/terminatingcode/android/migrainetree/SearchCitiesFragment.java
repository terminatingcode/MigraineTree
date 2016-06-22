package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 *
 */
public class SearchCitiesFragment extends Fragment {
    private static final String NAME = "SearchCitiesFragment";
    private static GeoLookupArrayAdapter mAdapter;
    private SharedPreferences mSharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_cities, container, false);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.location);
        mAdapter = new GeoLookupArrayAdapter(getActivity(),R.layout.row_cities );
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(mAdapter);
        //add location results from Weather Underground as user modifies text
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Intent intent = new Intent(getActivity(), GeoLookupService.class);
                intent.setAction(s.toString());
                getActivity().startService(intent);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
        //if a user selects a city, save to SharedPreferences
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String citySelected = (String) parent.getItemAtPosition(position);
                mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.PREFERENCES_FILE_KEY), Context.MODE_PRIVATE);
                SharedPrefsUtils mSharedPrefsUtils = new SharedPrefsUtils(mSharedPreferences);
                String cityUID = CitiesMapSingleton.newInstance().getUID(citySelected);
                mSharedPrefsUtils.saveSelectedCity(citySelected, cityUID);
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
        mAdapter.clear();
        if(mAdapter != null){
            for (String CITY : event.cities) {
                mAdapter.add(CITY);
            }
        }
    }
}
