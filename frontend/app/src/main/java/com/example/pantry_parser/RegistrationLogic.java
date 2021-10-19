package com.example.pantry_parser;

import android.view.View;

import com.example.pantry_parser.Pages.Registration_Page;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    public RegistrationLogic(View r, IServerRequest serverRequest) {
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
            r.registerErrorTextView.setText("You are logged in!");
            r.registerErrorTextView.setVisibility(View.VISIBLE);
        } else {
            r.registerErrorTextView.setText("Error with request, please try again");
            r.registerErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError (String errorMessage) {
        Toast.makeText(r.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        r.registerErrorTextView.setText("Error with request, please try again");
        r.registerErrorTextView.setVisibility(View.VISIBLE);
    }
}
