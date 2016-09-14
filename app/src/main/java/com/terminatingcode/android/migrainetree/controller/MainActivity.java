package com.terminatingcode.android.migrainetree.controller;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.model.MigraineRecordItems;
import com.terminatingcode.android.migrainetree.model.MigraineRecordObject;
import com.terminatingcode.android.migrainetree.model.SQLite.LocalContentProvider;
import com.terminatingcode.android.migrainetree.model.SQLite.MigraineRecord;
import com.terminatingcode.android.migrainetree.model.amazonaws.AWSMobileClient;
import com.terminatingcode.android.migrainetree.model.amazonaws.MLPrediction.MLPredictionService;
import com.terminatingcode.android.migrainetree.model.amazonaws.MLPrediction.UserModelConstants;
import com.terminatingcode.android.migrainetree.model.amazonaws.PushListenerService;
import com.terminatingcode.android.migrainetree.model.amazonaws.nosql.DynamoDBUtils;
import com.terminatingcode.android.migrainetree.model.amazonaws.user.IdentityManager;
import com.terminatingcode.android.migrainetree.model.amazonaws.user.IdentityProvider;
import com.terminatingcode.android.migrainetree.model.weather.WeatherHistoryService;
import com.terminatingcode.android.migrainetree.utils.Constants;
import com.terminatingcode.android.migrainetree.utils.SharedPrefsUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        FragmentListener{

    private static final String NAME = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private FragmentManager fragmentManager;
    private boolean enableCalendar;
    private boolean locationNeedsToBeSet;
    private SharedPrefsUtils sharedPrefsUtils;
    private NavigationView mNavigationView;
    private View headerView;
    private Button signOutButton;
    private Button   signInButton;
    private Uri partialRecordUri;
    private MigraineRecordObject partialMigraineRecordObject;
    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForExtras();
        setupSharedPrefs();
        setupAWS();
        initializeNavigationDrawer();
        chooseStartScreen();
    }

    /**
     * If app is started from a notification, the Intent will have extras attached
     * The user should then be navigated to the FinishRecordFragment
     * once AWS and NavigationDrawer have been setup
     */
    private void checkForExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            partialRecordUri = extras.getParcelable(Constants.INSERTED_URI);
            partialMigraineRecordObject = extras.getParcelable(Constants.RECORD_OBJECT);
        }
    }

    /**
     * Creates a reference to the user's preferences and instantiates the utils
     */
    private void setupSharedPrefs() {
        mSharedPreferences = this.getSharedPreferences(Constants.PREFERENCES_FILE_KEY, MODE_PRIVATE);
        sharedPrefsUtils = new SharedPrefsUtils(mSharedPreferences);
    }


    /**
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     */
    private void setupAWS(){
        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();
    }

    /**
     * AWS lifecycle methods
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     */
    @Override
    protected void onResume() {
        super.onResume();

        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // pause/resume Mobile Analytics collection
        awsMobileClient.handleOnResume();

        // register notification receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                new IntentFilter(PushListenerService.ACTION_SNS_NOTIFICATION));

    }

    /**
     * AWS lifecycle methods
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Obtain a reference to the mobile client.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // pause/resume Mobile Analytics collection
        awsMobileClient.handleOnPause();

        // unregister notification receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    /**
     * Initialises the Navigation Drawer that acts as the main menu for the app
     */
    public void initializeNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
        }catch(RuntimeException ex){
            Log.d(NAME, "Samsung specific exception " + ex.getMessage());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void syncState() {
                super.syncState();
                setupSignInButtons();
                updateUserName();
                updateUserImage();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setupSignInButtons();
                updateUserName();
                updateUserImage();
            }
        };;
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        enableCalendar = sharedPrefsUtils.getsavedMenstrualPref();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            if(headerView == null) {
                headerView = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
            }
            mNavigationView.getMenu().findItem(R.id.nav_calendar).setVisible(enableCalendar);
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    /**
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     * Initializes the sign-in and sign-out buttons.
     */
    private void setupSignInButtons() {
        if(headerView != null) {
            signOutButton = (Button) headerView.findViewById(R.id.my_button_signout);
            signOutButton.setOnClickListener(this);

            signInButton = (Button) headerView.findViewById(R.id.my_button_signin);
            signInButton.setOnClickListener(this);

            final boolean isUserSignedIn = identityManager.isUserSignedIn();
            signOutButton.setVisibility(isUserSignedIn ? View.VISIBLE : View.GONE);
            signInButton.setVisibility(!isUserSignedIn ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     */
    private void updateUserImage() {
        if(headerView != null) {
            final IdentityManager identityManager =
                    AWSMobileClient.defaultMobileClient().getIdentityManager();
            final IdentityProvider identityProvider =
                    identityManager.getCurrentIdentityProvider();

            final ImageView imageView =
                    (ImageView) headerView.findViewById(R.id.myUserImage);

            if (identityProvider == null && imageView != null) {
                // Not signed in
                if (Build.VERSION.SDK_INT < 22) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.user));
                } else {
                    imageView.setImageDrawable(getDrawable(R.mipmap.user));
                }

                return;
            }

            final Bitmap userImage = identityManager.getUserImage();
            if (userImage != null && imageView != null) {
                imageView.setImageBitmap(userImage);
            }
        }
    }

    /**
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     */
    private void updateUserName() {
        if(headerView != null) {
            final IdentityManager identityManager =
                    AWSMobileClient.defaultMobileClient().getIdentityManager();
            final IdentityProvider identityProvider =
                    identityManager.getCurrentIdentityProvider();

            final TextView userNameView = (TextView) headerView.findViewById(R.id.userName);

            if (identityProvider == null && userNameView != null) {
                // Not signed in
                userNameView.setText(getString(R.string.main_nav_menu_default_user_text));
                return;
            }

            final String userName =
                    identityProvider.getUserName();

            if (userName != null && userNameView != null) {
                userNameView.setText(userName);
            }
        }
    }

    /**
     * if user has not signed in, have the user begin at the SignInFragment
     * else if the app was opened from the status bar with a partial record begin at the FinishRecordFragment
     * else if they haven't specified a location, start at the SettingsFragment
     * else, start at the NewRecordFragment
     */
    private void chooseStartScreen() {
        final IdentityManager identityManager =
                AWSMobileClient.defaultMobileClient().getIdentityManager();
        final IdentityProvider identityProvider =
                identityManager.getCurrentIdentityProvider();
        boolean needToSignIn = (identityProvider == null);
        boolean recordNotfinished = (partialMigraineRecordObject != null && partialRecordUri != null);
        fragmentManager = getSupportFragmentManager();
        locationNeedsToBeSet = sharedPrefsUtils.needLocationSpecified();
        if(needToSignIn) {
            fragmentManager.beginTransaction().add(R.id.content_frame, new SignInFragment()).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if(drawer != null) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }else if(recordNotfinished){
            Fragment fragment = FinishRecordFragment.newInstance(partialRecordUri, partialMigraineRecordObject);
            fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
        }else if(locationNeedsToBeSet){
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

        if (id == R.id.action_about) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, AboutFragment.newInstance()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CalendarFragment()).commit();
        } else if (id == R.id.nav_charts) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ChartsFragment()).commit();
        } else if (id == R.id.nav_new_record) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new NewRecordFragment()).commit();
        } else if (id == R.id.nav_set_location) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new UserSettingsFragment()).commit();
        } else if (id == R.id.nav_records) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RecordsFragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(final View v) {
        if (v == signOutButton) {
            // The user is currently signed in with a provider. Sign out of that provider.
            identityManager.signOut();
            // Show the sign-in button and hide the sign-out button.
            signOutButton.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);

            // Close the navigation drawer.
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (v == signInButton) {
            // Start the sign-in activity. Do not finish this activity to allow the user to navigate back.
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SignInFragment()).commit();
            // Close the navigation drawer.
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
    }

    /*Fragment Listener methods*/
    /**
     * RecordsFragment signals to MainActivity when a user wishes to delete a record
     * This deletes the item from SQLite and AWS
     * @param item the list item
     */
    @Override
    public void onListDeleteItem(MigraineRecordItems.RecordItem item) {
        deleteFromSQLite(item);
        DynamoDBUtils.deleteFromAWS(item, MainActivity.this);
    }
    /**
     * replaces NewRecordFragment with InputTriggersFragment
     */
    @Override
    public void onNewRecordButtonClicked() {
        fragmentManager.beginTransaction().replace(R.id.content_frame, InputTriggersFragment.newInstance()).commit();
    }
    /**
     * ProcessRecordFragment signals to MainActivity when a partial record has been
     * completed and sends the user data in MigraineRecordObject format
     * This then starts the MLPredictionService and redirects the user to the FeelBetterFragment
     * @param recordObject containing the user data
     */
    @Override
    public void onPartialRecordConfirmed(MigraineRecordObject recordObject) {
        HashMap<String, String> record = UserModelConstants.makeRecord(recordObject);
        startPredictionService(record);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new FeelBetterFragment())
                .commit();
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
     * if user clicks ok on sign in prompt, redirect to SignInFragment
     */
    @Override
    public void onPromptForSignin() {
        fragmentManager.beginTransaction().add(R.id.content_frame, new SignInFragment()).commit();
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
     * starts WeatherHistoryService and adds processRecordFragment to frame
     * @param date the start date of migraine record
     * @param locationUID the current user's location Weather Underground id
     * @param migraineRecordObject the inputted data for the migraine record
     */
    @Override
    public void onSaveRecordButtonPressed(String date,
                                          String locationUID,
                                          MigraineRecordObject migraineRecordObject) {
        Intent intent = new Intent(this, WeatherHistoryService.class);
        intent.putExtra(Constants.DATE_KEY, date);
        intent.putExtra(Constants.LOCATIONUID, locationUID);
        startService(intent);
        ProcessRecordFragment processRecordFragment = ProcessRecordFragment.newInstance(migraineRecordObject);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, processRecordFragment)
                .addToBackStack(null)
                .commit();
    }
    /**
     * switches from SignInFragment once user signed in successfully
     * replaces with FinishRecordFragment if app opened from status bar
     * else, replaces with UserSettingsFragment if no location set
     * or NewRecordFragment if all conditions met
     */
    @Override
    public void switchToHome() {
        //unlock Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        fragmentManager = getSupportFragmentManager();
        locationNeedsToBeSet = sharedPrefsUtils.needLocationSpecified();
        if(partialMigraineRecordObject != null && partialRecordUri != null){
            Fragment fragment = FinishRecordFragment.newInstance(partialRecordUri,
                    partialMigraineRecordObject);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }else if(locationNeedsToBeSet){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new UserSettingsFragment())
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, NewRecordFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onListViewItem(MigraineRecordItems.RecordItem item) {
        Log.d(NAME, "clicked on item: " + item.id);
        int id = item.id;
        RecordSummaryFragment fragment = RecordSummaryFragment.newInstance(id);
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, fragment)
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
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     * intended to display GCM broadcasts published from Amazon SNS
     */
    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(NAME, "Received notification from local broadcast. Display it in a dialog.");

            Bundle data = intent.getBundleExtra(PushListenerService.INTENT_SNS_NOTIFICATION_DATA);
            String message = PushListenerService.getMessage(data);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.push_demo_title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    };

    /**
     * uses the list item to identify the SQLite record and delete it
     * @param item the list item from RecordsFragment
     */
    private void deleteFromSQLite(MigraineRecordItems.RecordItem item) {
        ContentResolver mResolver = getContentResolver();
        String whereClause = MigraineRecord._ID + " = " + item.id;
        int deleted = mResolver.delete(
                LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, whereClause, null);
        Log.d(NAME, "deleted: " + deleted);
    }

    /**
     * starts the intent for MLPredictionService
     * @param record Map of user data and model attributes
     */
    private void startPredictionService(HashMap<String, String> record){
        Intent intent = new Intent(this, MLPredictionService.class);
        intent.putExtra(Constants.RECORD, record);
        startService(intent);
    }

}