package com.example.pantry_parser.Network;


public interface RequestListener {
    /**
     *
     * @param response Response received from server
     */
    public void onSuccess(Object response);

    /**
     *
     * @param errorMessage Error received from server
     */
    public void onFailure(String errorMessage);
}
