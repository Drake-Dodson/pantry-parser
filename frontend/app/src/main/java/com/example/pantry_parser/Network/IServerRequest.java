package com.example.pantry_parser.Network;

import android.content.Intent;

import com.example.pantry_parser.Logic.IVolleyListener;

import org.json.JSONObject;

public interface IServerRequest {
    public void sendToServer(String url, JSONObject newUserObj, int methodType, Intent applicationContext);
    public void addVolleyListener(IVolleyListener logic);
}
