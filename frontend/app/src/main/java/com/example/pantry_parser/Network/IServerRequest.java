package com.example.pantry_parser.Network;

import com.example.pantry_parser.Logic.IVolleyListener;

import org.json.JSONObject;

public interface IServerRequest {
    public void sendToServer(String url, JSONObject newUserObj, String methodType);
    public void addVolleyListener(IVolleyListener logic);
}
