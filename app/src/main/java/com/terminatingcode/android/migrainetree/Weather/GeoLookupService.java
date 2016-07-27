package com.terminatingcode.android.migrainetree.Weather;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.terminatingcode.android.migrainetree.MessageEvent;
import com.terminatingcode.android.migrainetree.MyRequestQueue;
import com.terminatingcode.android.migrainetree.R;

import org.greenrobot.eventbus.EventBus;
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

    public GeoLookupService() {
        super(NAME);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            geoLookup(action);
        }
        setIntentRedelivery(false);
    }

    /**
     * Makes a geolookup JsonRequest to Weather Underground
     * Once received, calls on GeoLookupParser and sends it to
     * UserSettingsFragment to update UI
     * @param location user inputted location
     */
    private void geoLookup(String location) {
        Log.d(NAME, "geolookup service started");
        MyRequestQueue queue = MyRequestQueue.getInstance(this);
        final GeoLookupParser glp = new GeoLookupParser();
        final String[] splitLocation = location.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.WUurl))
                .append(getString(R.string.WUAPIKey))
                .append(getString(R.string.geolookup));
        for (String aSplitLocation : splitLocation) {
            sb.append("/").append(aSplitLocation);
        }
        sb.append(getString(R.string.json));
        final String url = sb.toString();
        Log.d(NAME, "sending url  " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(NAME, "received response " + response);
                            String[] cities = glp.parse(response);
                            EventBus.getDefault().post(new MessageEvent(cities));
                        } catch (JSONException e) {
                            Log.d(NAME, "JSONException " + e);
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
}
