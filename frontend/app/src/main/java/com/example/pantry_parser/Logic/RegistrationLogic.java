package com.example.pantry_parser.Logic;

import static com.example.pantry_parser.Utilities.URLs.URL_REGISTER;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.Network.IServerRequest;
import com.example.pantry_parser.Pages.Home_Page;
import com.example.pantry_parser.IView;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationLogic extends AppCompatActivity implements IVolleyListener{

    private IView r;
    private IServerRequest serverRequest;

    public RegistrationLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void registerUser(String name, String email, String password) throws JSONException {
        final String url = URL_REGISTER;

        JSONObject newUserObj = new JSONObject();
        try {
            newUserObj.put("name", name);
            newUserObj.put("email", email);
            newUserObj.put("password", password);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        serverRequest.sendToServer(url, newUserObj, "POST");
    }

    @Override
    public void onSuccess(String message) {
        if(message.equals("success")) {
            startActivity(new Intent(getApplicationContext(), Home_Page.class));
        }
            r.toastText("Login Successful!");
    }

    @Override
    public void onError(String errorMessage) {
        r.toastText("Error with Request, please try again");
    }
}
