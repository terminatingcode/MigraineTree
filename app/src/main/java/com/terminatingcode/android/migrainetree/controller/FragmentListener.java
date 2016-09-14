package com.terminatingcode.android.migrainetree.controller;

import com.terminatingcode.android.migrainetree.model.MigraineRecordItems;
import com.terminatingcode.android.migrainetree.model.MigraineRecordObject;

/**
 * An interface to define all Fragment Listener behaviour
 * Methods are implemented by the MainActivity class
 * Created by Sarah on 9/4/2016.
 */
public interface FragmentListener {
    /**
     * When the delete button is pressed on the RecordsFragment,
     * the MainActivity deletes the records from SQLite and DynamoDB
     * @param item from the list of migraine records
     */
    void onListDeleteItem(MigraineRecordItems.RecordItem item);

    /**
     * When the NewRecordButton is pressed on NewRecordFragment,
     * the MainActivity displays the InputTriggersFragment
     */
    void onNewRecordButtonClicked();

    /**
     * ProcessRecordFragment signals to MainActivity when a partial record has been
     * completed and sends the user data in MigraineRecordObject format
     * This then starts the MLPredictionService and redirects the user to the FeelBetterFragment
     * @param recordObject containing the user data
     */
    void onPartialRecordConfirmed(MigraineRecordObject recordObject);

    /**
     * recreates the NavigationDrawer if user preferences changed
     * the menstrual calendar to visible/invisible
     */
    void onPreferenceChanged();

    /**
     * if user clicks ok on sign in prompt, redirect to SignInFragment
     */
    void onPromptForSignin();

    /**
     * starts WeatherHistoryService and adds processRecordFragment to frame
     * @param date the start date of migraine record
     * @param locationUID the current user's location Weather Underground id
     * @param migraineRecordObject the inputted data for the migraine record
     */
    void onSaveRecordButtonPressed(String date,
                                   String locationUID,
                                   MigraineRecordObject migraineRecordObject);
    /**
     * replaces InputTriggersFragment with UserSettingsFragment
     */
    void onSetLocationPressed();

    /**
     * replaces InputTriggersFragment with CalendarFragment
     */
    void onUpdateCalendarButtonPressed();

    /**
     * switches from SignInFragment once user signed in successfully
     * replaces with FinishRecordFragment if app opened from status bar
     * else, replaces with UserSettingsFragment if no location set
     * or NewRecordFragment if all conditions met
     */
    void switchToHome();

    /**
     * When ViewButton pressed from RecordsFragment,
     * MainActivity adds RecordSummaryFragment with selected migraine record
     * @param item from the list of migraine records
     */
    void onListViewItem(MigraineRecordItems.RecordItem item);
}
