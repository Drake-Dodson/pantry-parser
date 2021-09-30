package com.example.pantry_parser.Services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApplicationScope {

    /* These variables will only be instantiated ONCE
     * from then on the same instance variables will be used
     * This is the Singleton design pattern
     **/
    private static ApplicationScope applicationScope;
    private static RequestQueue requestQueue;
    private static Context context;

    private ApplicationScope(Context context){
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if (this.requestQueue == null)
            return Volley.newRequestQueue(this.context);
        return requestQueue;
    }

    public<Obj> void addToRequestQueue(Request<Obj> request){
        this.requestQueue.add(request);
    }

    /* This has to be synchronised as multiple threads may request this at the same time*/
    public static synchronized ApplicationScope getInstance(Context context) {
        if(applicationScope == null)
            applicationScope = new ApplicationScope(context);
        return applicationScope;
    }


}
