package com.example.pantry_parser;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.Pages.Login_Page;
import com.example.pantry_parser.Utilities.User;

public class AppController extends Application {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";

    private static AppController mInstance;
    private static RequestQueue mRequestQueue;
//    private static Context ctx;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance(){
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <Obj> void addToRequestQueue(Request<Obj> req){
        getRequestQueue().add(req);
    }


//    //this method will store the user data in shared preferences
//    public void userLogin(User user) {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(KEY_ID, user.getId());
//        editor.putString(KEY_USERNAME, user.getName());
//        editor.putString(KEY_EMAIL, user.getEmail());
//        editor.apply();
//    }
//
//    //this method will checker whether user is already logged in or not
//    public boolean isLoggedIn() {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_USERNAME, null) != null;
//    }
//
//    //this method will give the logged in user
//    public User getUser() {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return new User(
//                sharedPreferences.getInt(KEY_ID, -1),
//                sharedPreferences.getString(KEY_USERNAME, null),
//                sharedPreferences.getString(KEY_EMAIL, null)
//        );
//    }
//
//    //this method will logout the user
//    public void logout() {
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        ctx.startActivity(new Intent(ctx, Login_Page.class));
//    }
}
