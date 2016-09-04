package com.terminatingcode.android.migrainetree.view;

import com.terminatingcode.android.migrainetree.model.MigraineRecordItems;
import com.terminatingcode.android.migrainetree.model.MigraineRecordObject;

/**
 * An interface to define all Fragment Listener behaviour
 * Created by Sarah on 9/4/2016.
 */
public interface FragmentListener {
    void onListDeleteItem(MigraineRecordItems.RecordItem item);
    void onNewRecordButtonClicked();
    void onPartialRecordConfirmed(MigraineRecordObject recordObject);
    void onPreferenceChanged();
    void onPromptForSignin();
    void onSaveRecordButtonPressed(String date, String locationUID, MigraineRecordObject migraineRecordObject);
    void onSetLocationPressed();
    void onUpdateCalendarButtonPressed();
    void switchToHome();
}
