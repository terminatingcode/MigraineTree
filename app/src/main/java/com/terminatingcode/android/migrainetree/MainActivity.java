package com.terminatingcode.android.migrainetree;

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

import com.amazonaws.AmazonClientException;
import com.terminatingcode.android.migrainetree.SQL.LocalContentProvider;
import com.terminatingcode.android.migrainetree.SQL.MigraineRecord;
import com.terminatingcode.android.migrainetree.Weather.WeatherHistoryService;
import com.terminatingcode.android.migrainetree.amazonaws.AWSMobileClient;
import com.terminatingcode.android.migrainetree.amazonaws.UI.PushListenerService;
import com.terminatingcode.android.migrainetree.amazonaws.UI.SignInFragment;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DemoNoSQLTableBase;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DemoNoSQLTableFactory;
import com.terminatingcode.android.migrainetree.amazonaws.nosql.DynamoDBUtils;
import com.terminatingcode.android.migrainetree.amazonaws.user.IdentityManager;
import com.terminatingcode.android.migrainetree.amazonaws.user.IdentityProvider;
import com.terminatingcode.android.migrainetree.amazonaws.util.ThreadUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NewRecordFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener,
        ChartsFragment.OnFragmentInteractionListener,
        InputTriggersFragment.OnFragmentInteractionListener,
        ProcessRecordFragment.OnFragmentInteractionListener,
        UserSettingsFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        RecordsFragment.OnListFragmentInteractionListener,
        View.OnClickListener{

    private static final String NAME = "MainActivity";
    private static final String DYNAMODB_TABLE_NAME = "MigraineRecord";
    private SharedPreferences mSharedPreferences;
    private FragmentManager fragmentManager;
    private boolean enableCalendar;
    private boolean locationNeedsToBeSet;
    private SharedPrefsUtils sharedPrefsUtils;
    private NavigationView mNavigationView;
    private View headerView;
    private Button signOutButton;
    private Button   signInButton;
    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(NAME, "onCreate");
        setContentView(R.layout.activity_main);
        mSharedPreferences = this.getSharedPreferences(Constants.PREFERENCES_FILE_KEY, MODE_PRIVATE);
        sharedPrefsUtils = new SharedPrefsUtils(mSharedPreferences);
        setUpAWS();
        initializeNavigationDrawer();
        chooseStartScreenBasedOnLocationStored();
    }

    /**
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     */
    private void setUpAWS(){
        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();
    }

    public void initializeNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    private void chooseStartScreenBasedOnLocationStored() {
        final IdentityManager identityManager =
                AWSMobileClient.defaultMobileClient().getIdentityManager();
        final IdentityProvider identityProvider =
                identityManager.getCurrentIdentityProvider();
        boolean needToSignIn = (identityProvider == null);
        fragmentManager = getSupportFragmentManager();
        locationNeedsToBeSet = sharedPrefsUtils.needLocationSpecified();
        if(locationNeedsToBeSet){
            fragmentManager.beginTransaction().add(R.id.content_frame, new UserSettingsFragment()).commit();
        }else if(needToSignIn) {
            fragmentManager.beginTransaction().add(R.id.content_frame, new SignInFragment()).commit();
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
        if (id == R.id.action_about) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, new AboutFragment()).commit();
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
//            Intent intent = new Intent(this, HeartRateMonitor.class);
//            startActivity(intent);
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RecordsFragment()).commit();
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
    public void onSaveRecordButtonPressed(String date, String locationUID, MigraineRecordObject migraineRecordObject) {
        Log.d(NAME, "starting intent with date = " + date);
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

    @Override
    public void onPartialRecordConfirmed() {
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, FeelBetterFragment.newInstance())
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(NAME, "onResume");


        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // pause/resume Mobile Analytics collection
        awsMobileClient.handleOnResume();

        // register notification receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                new IntentFilter(PushListenerService.ACTION_SNS_NOTIFICATION));

    }

    /**
     * copied on 8/3/2016 from AWS Amazon Demo Sample App
     * generated by the MobileHub
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(NAME, "onPause");

        // Obtain a reference to the mobile client.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // pause/resume Mobile Analytics collection
        awsMobileClient.handleOnPause();

        // unregister notification receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(NAME, "onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(NAME, "onDestroy");
    }

    /**
     * switches from SignInFragment once user signed in succesfully
     * replaces with UserSettingsFragment if no location set
     * or NewRecordFragment if all conditions met
     */
    @Override
    public void switchToHome() {
        fragmentManager = getSupportFragmentManager();
        locationNeedsToBeSet = sharedPrefsUtils.needLocationSpecified();
        if(locationNeedsToBeSet){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new UserSettingsFragment()).commit();
        }else{
            fragmentManager.beginTransaction().replace(R.id.content_frame, new NewRecordFragment()).commit();
        }
    }

    @Override
    public void onListDeleteItem(MigraineRecordItems.RecordItem item) {
        deleteFromSQLite(item);
        deleteFromAWS(item);
    }

    private void deleteFromAWS(MigraineRecordItems.RecordItem item) {
                final DemoNoSQLTableBase awsTable = DemoNoSQLTableFactory.instance(getApplicationContext())
                .getNoSQLTableByTableName(DYNAMODB_TABLE_NAME);
        final long finalStartHour = item.startHour;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    awsTable.deleteRecord(finalStartHour);
                } catch (final AmazonClientException ex) {
                    DynamoDBUtils.showErrorDialogForServiceException(MainActivity.this,
                            getString(R.string.nosql_dialog_title_failed_operation_text), ex);
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            dialogBuilder.setTitle(R.string.nosql_dialog_title_removed_data_text);
                            dialogBuilder.setMessage(R.string.nosql_dialog_message_removed_data_text);
                            dialogBuilder.setNegativeButton(R.string.nosql_dialog_ok_text, null);
                            dialogBuilder.show();
                    }
                });
            }
        }).start();
    }

    private void deleteFromSQLite(MigraineRecordItems.RecordItem item) {
        ContentResolver mResolver = getContentResolver();
        String whereClause = MigraineRecord._ID + " = " + item.id;
        int deleted = mResolver.delete(
                LocalContentProvider.CONTENT_URI_MIGRAINE_RECORDS, whereClause, null);
        Log.d(NAME, "deleted: " + deleted);
    }


}
