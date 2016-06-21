package com.terminatingcode.android.migrainetree;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GeoLookupService extends IntentService {
    private static final String NAME = "GeoLookupService";
    private static final int TIMEOUT = 10000;



    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.terminatingcode.android.migrainetree.action.FOO";
    private static final String ACTION_BAZ = "com.terminatingcode.android.migrainetree.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.terminatingcode.android.migrainetree.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.terminatingcode.android.migrainetree.extra.PARAM2";

    public GeoLookupService() {
        super("GeoLookupService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GeoLookupService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GeoLookupService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                //call geoLookup
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
        setIntentRedelivery(false);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void geoLookup(String location) {
        MyRequestQueue queue = MyRequestQueue.getInstance(this);
        final GeoLookupParser glp = new GeoLookupParser();
        final String[] splitLocation = location.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(R.string.WUurl)
                .append(R.string.WUAPIKey)
                .append(R.string.geolookup);
        for (String aSplitLocation : splitLocation) {
            sb.append("/").append(aSplitLocation);
        }
        sb.append(R.string.json);
        final String url = sb.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String[] cities = glp.parse(response);

                        } catch (JSONException e) {
                            Log.d(NAME, "received response");
                        }
                    }
                }, new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error){
                               Log.d(NAME, "Error in JsonObjectRequest");
                            }
                });
        queue.addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
