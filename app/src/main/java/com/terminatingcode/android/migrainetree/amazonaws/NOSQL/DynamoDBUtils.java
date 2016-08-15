package com.terminatingcode.android.migrainetree.amazonaws.nosql;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.amazonaws.AmazonClientException;
import com.terminatingcode.android.migrainetree.MainActivity;
import com.terminatingcode.android.migrainetree.MigraineRecordItems;
import com.terminatingcode.android.migrainetree.MigraineRecordObject;
import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.amazonaws.util.ThreadUtils;

import java.util.HashMap;
import java.util.Map;

public final class DynamoDBUtils {
    private static final String DDB_TYPE_STRING = "S";
    private static final String DDB_TYPE_NUMBER = "N";
    private static final String DDB_TYPE_BINARY = "B";
    private static final String TYPE_STRING = "String";
    private static final String TYPE_NUMBER = "Number";
    private static final String TYPE_BINARY = "Binary";
    private static final Map<String, String> typeLookup;
    private static final String DYNAMODB_TABLE_NAME = "MigraineRecord";
    static {
        typeLookup = new HashMap<>();
        typeLookup.put(DDB_TYPE_STRING, TYPE_STRING);
        typeLookup.put(DDB_TYPE_NUMBER, TYPE_NUMBER);
        typeLookup.put(DDB_TYPE_BINARY, TYPE_BINARY);
    }

    /**
     * Utility class has private constructor.
     */
    private DynamoDBUtils() {
    }

    /**
     * Convert a DynamoDB attribute type to a human readable type.
     * @param attributeType the DynamoDB attribute type
     * @return a human readable attribute type.
     */
    public static String humanReadableTypeFromDynamoDBAttributeType(final String attributeType) {
        final String humanReadableType;
        humanReadableType = typeLookup.get(attributeType);
        if (humanReadableType != null) {
            return humanReadableType;
        }
        return attributeType;
    }

    public static void showErrorDialogForServiceException(final FragmentActivity activity,
                                                                 final String title,
                                                                 final AmazonClientException ex) {
        if (activity == null) {
            return;
        }

        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(ex.getMessage())
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            }
        });
    }

    /**
     * created by Sarah c
     * using the MigraineRecordObject, send to DynamoDB table to persist
     * and notify user of success or failure with an Alertdialog
     * @param migraineRecordObject the user inputted data
     * @param activity the Activity the AlertDialog will be viewed on
     */
    public static void persistToAWS(final MigraineRecordObject migraineRecordObject, final FragmentActivity activity) {
        final DemoNoSQLTableBase demoTable = DemoNoSQLTableFactory.instance(activity)
                .getNoSQLTableByTableName(DYNAMODB_TABLE_NAME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    demoTable.insertRecord(migraineRecordObject);
                } catch (final AmazonClientException ex) {
                    DynamoDBUtils.showErrorDialogForServiceException(activity,
                            activity.getString(R.string.nosql_dialog_title_failed_operation_text), ex);
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                        dialogBuilder.setTitle(R.string.uploading_data);
                        dialogBuilder.setMessage(R.string.uploading_data_dialog_message);
                        dialogBuilder.setPositiveButton(R.string.nosql_dialog_ok_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(activity, MainActivity.class);
                                intent.setFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                        });
                        dialogBuilder.show();
                    }
                });
            }
        }).start();
    }

    /**
     * created by Sarah c
     * using the MigraineRecordObject, delete from DynamoDB table
     * and notify user of success or failure with an Alertdialog
     * @param item the list item from the RecordsFragment RecyclerView
     * @param activity the Activity the AlertDialog will be viewed on
     */
    public static void deleteFromAWS(MigraineRecordItems.RecordItem item, final FragmentActivity activity) {
        final DemoNoSQLTableBase awsTable = DemoNoSQLTableFactory.instance(activity.getApplicationContext())
                .getNoSQLTableByTableName(DYNAMODB_TABLE_NAME);
        final long finalStartHour = item.startHour;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    awsTable.deleteRecord(finalStartHour);
                } catch (final AmazonClientException ex) {
                    DynamoDBUtils.showErrorDialogForServiceException(activity,
                            activity.getString(R.string.nosql_dialog_title_failed_operation_text), ex);
                    return;
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                        dialogBuilder.setTitle(R.string.nosql_dialog_title_removed_data_text);
                        dialogBuilder.setMessage(R.string.nosql_dialog_message_removed_data_text);
                        dialogBuilder.setNegativeButton(R.string.nosql_dialog_ok_text, null);
                        dialogBuilder.show();
                    }
                });
            }
        }).start();
    }
}
