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
import android.widget.CheckBox;
import android.widget.TextView;

import com.terminatingcode.android.migrainetree.Weather.GeoLookupArrayAdapter;
import com.terminatingcode.android.migrainetree.Weather.GeoLookupService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 *
 */
public class UserSettingsFragment extends Fragment {
    private static final String NAME = "UserSettingsFragment";
    private OnFragmentInteractionListener mListener;
    private static GeoLookupArrayAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private AutoCompleteTextView mAutoCompleteTextView;
    private TextView cityTextView;
    private Button searchButton;
    private CheckBox menstrualDataEnabledCheckbox;
    private boolean preference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        mAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.location);
        cityTextView = (TextView) rootView.findViewById(R.id.displayCity);
        searchButton = (Button) rootView.findViewById(R.id.searchCitiesButton);
        menstrualDataEnabledCheckbox = (CheckBox) rootView.findViewById(R.id.useMenstrualDataCheckBox);

        //set cityTextView
        mSharedPreferences = getActivity()
                .getSharedPreferences(Constants.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        final SharedPrefsUtils mSharedPrefsUtils = new SharedPrefsUtils(mSharedPreferences);
        String city = mSharedPrefsUtils.getSavedCity();
        cityTextView.setText(city);
        //create auto-complete
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
                String cityUID = CitiesMapSingleton.newInstance().getUID(citySelected);
                mSharedPrefsUtils.saveSelectedCity(citySelected, cityUID);
                cityTextView.setText(citySelected);
            }
        });

        preference = mSharedPrefsUtils.getsavedMenstrualPref();
        menstrualDataEnabledCheckbox.setChecked(preference);
        //if user disables menstrual data, save to sharedPreferences and alert mainActivity
        menstrualDataEnabledCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean preference = menstrualDataEnabledCheckbox.isChecked();
                mSharedPrefsUtils.saveMenstrualDataPrefs(preference);
                if (mListener != null) {
                    mListener.onPreferenceChanged();
                }
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


    public void startGeoLookupService(String inputtedCity){
        Intent intent = new Intent(getActivity(), GeoLookupService.class);
        intent.setAction(inputtedCity);
        getActivity().startService(intent);
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
        void onPreferenceChanged();
    }
}
