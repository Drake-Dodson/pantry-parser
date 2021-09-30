package com.example.pantry_parser.Services;

public interface RequestListener {
    public void onSuccess(Object response);
    public void onFailure(String errorMessage);
}
