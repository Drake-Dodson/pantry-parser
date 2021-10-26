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

    private String tag_json_obj = "json_obj_req";
    private IVolleyListener l;

    public void sendToServer(String url, JSONObject newUserObj, String methodType) {

        int method = Request.Method.GET;

        if (methodType.equals("POST")) {
            method = Request.Method.POST;
        }

        JsonObjectRequest registerUserRequest = new JsonObjectRequest(method, url, newUserObj,
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
                        //Toast.makeText(r, "Registration Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        AppController.getInstance(this).addToRequestQueue(registerUserRequest, tag_json_obj);
    }

    public void addVolleyListener(IVolleyListener logic){
        l = logic;
    }
}




