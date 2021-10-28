package com.example.pantry_parser.Logic;

import android.content.Intent;

public interface IVolleyListener {
    public void onSuccess(String s, Intent intent);
    public void onError(String s);
}
