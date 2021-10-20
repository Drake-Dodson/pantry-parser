package com.example.pantry_parser;

import android.view.View;

import com.example.pantry_parser.Services.IServerRequest;
import com.example.pantry_parser.Services.IView;
import com.example.pantry_parser.Services.IVolleyListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    public RegistrationLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void registerUser(String name, String email, String password) throws JSONException {
        final String url = "http://coms-309-032.cs.iastate.edu:8080/user";
        JSONObject newUserObj = new JSONObject();

        try{
            newUserObj.put("name", name);
            newUserObj.put("email", email);
            newUserObj.put("password", password);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        serverRequest.sendToServer(url, newUserObj, "POST");
    }

    @Override
    public void onSuccess(String email) {
        if (email.length() > 0) {
            //startActivity(new Intent(getApplicationContext(), Login_Page.class));
            r.showText("You are logged in!");
        } else {
           r.showText("Error with request, please try again");
        }
    }

    @Override
    public void onError (String errorMessage) {
        r.toastText(errorMessage);
        r.showText("Error with request, please try again");
    }
}
