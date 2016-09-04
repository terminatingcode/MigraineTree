package com.terminatingcode.android.migrainetree.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton to hold Volley RequestQueue for GeoLookupService
 * Created by Sarah on 6/20/2016.
 */
public class MyRequestQueue {
    private static MyRequestQueue ourInstance = null;
    private RequestQueue mRequestQueue;
    private static Context sContext;

    public static synchronized MyRequestQueue getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new MyRequestQueue(context);
        return ourInstance;
    }

    private MyRequestQueue(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
