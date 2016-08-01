package com.terminatingcode.android.migrainetree.EventMessages;

import com.terminatingcode.android.migrainetree.MigraineRecordObject;

/**
 * EventBus Message Event class for sending MigraineRecordObjects
 * Created by Sarah on 7/29/2016.
 */
public class MigraineRecordMessageEvent {
    public final MigraineRecordObject mMigraineRecordObject;

    public MigraineRecordMessageEvent(MigraineRecordObject migraineRecordObject){
        this.mMigraineRecordObject = migraineRecordObject;
    }
}
