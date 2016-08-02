package com.terminatingcode.android.migrainetree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.terminatingcode.android.migrainetree.Weather.WeatherHistoryService;
import com.terminatingcode.android.migrainetree.jwetherell_heart_rate_monitor.HeartRateMonitor;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NewRecordFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener,
        ChartsFragment.OnFragmentInteractionListener,
        InputTriggersFragment.OnFragmentInteractionListener,
        ProcessRecordFragment.OnFragmentInteractionListener,
        UserSettingsFragment.OnFragmentInteractionListener{

    private static final String NAME = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private FragmentManager fragmentManager;
    private boolean enableCalendar;
    private boolean locationNeedsToBeSet;
    private SharedPrefsUtils sharedPrefsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = this.getSharedPreferences(Constants.PREFERENCES_FILE_KEY, MODE_PRIVATE);
        sharedPrefsUtils = new SharedPrefsUtils(mSharedPreferences);
        chooseStartScreenBasedOnLocationStored();
        initializeNavigationDrawer();
    }

    public void initializeNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        enableCalendar = sharedPrefsUtils.getsavedMenstrualPref();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.getMenu().findItem(R.id.nav_calendar).setVisible(enableCalendar);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void chooseStartScreenBasedOnLocationStored() {
        fragmentManager = getSupportFragmentManager();
        locationNeedsToBeSet = sharedPrefsUtils.needLocationSpecified();
        if(locationNeedsToBeSet){
            fragmentManager.beginTransaction().add(R.id.content_frame, new UserSettingsFragment()).commit();
        }else{
            fragmentManager.beginTransaction().add(R.id.content_frame, new NewRecordFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_calendar) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CalendarFragment()).commit();
        } else if (id == R.id.nav_charts) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ChartsFragment()).commit();
        } else if (id == R.id.nav_weather) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new NewRecordFragment()).commit();
        } else if (id == R.id.nav_set_location) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new UserSettingsFragment()).commit();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, HeartRateMonitor.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Log.d(NAME, "starting AWS activity");
            Intent intent = new Intent(this, com.terminatingcode.android.migrainetree.amazonaws.UI.MainActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * recreates the NavigationDrawer if user preferences changed
     * sets the menstrual calendar to visible/invisible
     */
    @Override
    public void onPreferenceChanged(){
        initializeNavigationDrawer();
    }

    /**
     * replaces NewRecordFragment with InputTriggersFragment
     */
    @Override
    public void onNewRecordButtonClicked() {
        fragmentManager.beginTransaction().replace(R.id.content_frame, new InputTriggersFragment()).commit();
    }

    /**
     * replaces InputTriggersFragment with UserSettingsFragment
     */
    @Override
    public void onSetLocationPressed() {
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new UserSettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * replaces InputTriggersFragment with CalendarFragment
     */
    @Override
    public void onUpdateCalendarButtonPressed() {
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new CalendarFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * starts WeatherHistoryService and adds processRecordFragment to frame
     * @param date the start date of migraine record
     * @param locationUID the current user's location Weather Underground id
     */
    @Override
    public void onSaveRecordButtonPressed(String date, String locationUID, Uri uri) {
        Log.d(NAME, "starting intent with date = " + date);
        Intent intent = new Intent(this, WeatherHistoryService.class);
        intent.putExtra(Constants.DATE_KEY, date);
        intent.putExtra(Constants.LOCATIONUID, locationUID);
        startService(intent);
        ProcessRecordFragment processRecordFragment = ProcessRecordFragment.newInstance(uri);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, processRecordFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRecordConfirmed() {

    }

    public void createAWSClient(){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                "us-east-1:1e6250e2-8df1-40a7-9766-da9b93628ad0",    /* Identity Pool ID */
                Regions.US_EAST_1          /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );
    }
}
