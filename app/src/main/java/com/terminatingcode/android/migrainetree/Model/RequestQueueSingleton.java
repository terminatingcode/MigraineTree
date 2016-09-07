package com.terminatingcode.android.migrainetree.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

/**
 * Singleton to hold Volley RequestQueue for GeoLookupService
 * Created by Sarah on 6/20/2016.
 */
public class RequestQueueSingleton {
    private static RequestQueueSingleton instance = null;
    private com.android.volley.RequestQueue mRequestQueue;
    private static Context sContext;

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if(instance == null)
            instance = new RequestQueueSingleton(context);
        return instance;
    }

    private RequestQueueSingleton(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    public com.android.volley.RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
