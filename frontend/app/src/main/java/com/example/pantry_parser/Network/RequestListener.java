package com.example.pantry_parser.Network;

public interface RequestListener {
    public void onSuccess(Object response);
    public void onFailure(String errorMessage);
}
