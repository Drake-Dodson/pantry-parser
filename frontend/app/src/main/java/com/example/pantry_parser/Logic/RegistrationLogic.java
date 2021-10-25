package com.example.pantry_parser.Logic;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.pantry_parser.Utilities.URLs.URL_REGISTER;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.Pages.Login_Page;
import com.example.pantry_parser.Utilities.IView;
import com.example.pantry_parser.Network.IServerRequest;
import com.example.pantry_parser.Network.ServerRequest;
import com.example.pantry_parser.Pages.Registration_Page;



public class RegistrationLogic extends AppCompatActivity implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;
    final String url = URL_REGISTER;

    public RegistrationLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void registerUser(String name, String email, String password) throws JSONException {
        JSONObject newUserObj = new JSONObject();

        try{
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
        if (message.equals("success")) {
            startActivity(new Intent(getApplicationContext(), Login_Page.class));
            r.showText("You are logged in!");
        } else {
           r.showText("Error with request, please try again");
        }
    }

    @Override
    public void onError (String errorMessage) {
        r.toastText(errorMessage);
        r.showText("Error with request, please try again");
    }
}
