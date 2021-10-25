package com.example.pantry_parser;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance=this;
    }

    public static synchronized AppController getInstance(Context context) { return mInstance; }

    public RequestQueue getRequestQueue() { return mRequestQueue; }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
