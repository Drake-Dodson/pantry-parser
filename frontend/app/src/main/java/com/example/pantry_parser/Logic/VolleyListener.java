package com.example.pantry_parser.Services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pantry_parser.AppController;
import com.example.pantry_parser.Network.RequestListener;

import org.json.JSONObject;

public class VolleyListener {
    private static String baseUrl = "http://coms-309-032.cs.iastate.edu:8080";

    public static void makeRequest(Context context, String path, RequestListener requestListener, JSONObject data, int method){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, baseUrl + path, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestListener.onFailure(error.getMessage());
                    }
                });
        AppController.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void makeRequest(Context context, String path, RequestListener requestListener, int method){

        StringRequest stringRequest = new StringRequest(method, baseUrl + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        requestListener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestListener.onFailure(error.getMessage());
                    }
                });
        AppController.getInstance(context).addToRequestQueue(stringRequest);
    }

}
