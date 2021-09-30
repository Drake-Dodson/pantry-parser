package com.example.pantry_parser.services;

public interface RequestListener {
    public void onSuccess(Object response);
    public void onFailure(String errorMessage);
}
