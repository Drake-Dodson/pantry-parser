package com.example.pantry_parser.Network;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pantry_parser.AppController;
import com.example.pantry_parser.Logic.IVolleyListener;

import org.json.JSONObject;

public class ServerRequest extends AppCompatActivity implements IServerRequest{
    private IVolleyListener l;

    public void sendToServer(String url, JSONObject obj, String methodType) {

        int method = Request.Method.GET;

        if (methodType.equals("POST")) {
            method = Request.Method.POST;
        }

        JsonObjectRequest request = new JsonObjectRequest(method, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            if (response != null){
                                l.onSuccess(response.toString());
                                System.out.println(response.toString());
                            } else {
                                l.onError("Null Response object received");
                            }
                    }},

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        l.onError(error.getMessage());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

    public void addVolleyListener(IVolleyListener logic){
        l = logic;
    }
}




