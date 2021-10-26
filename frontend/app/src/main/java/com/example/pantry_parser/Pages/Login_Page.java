package com.example.pantry_parser.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Network.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Login_Page extends AppCompatActivity implements View.OnClickListener{

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private Button eGuest;
    private TextView eAttemptsInfo;
    private TextView eSignUp;

    private String adminUserName = "Admin";
    private String adminPassword = "2_do_8_comS_309";

    boolean isAdmin = false;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.text_Username);
        ePassword = findViewById((R.id.text_Password));
        eLogin = findViewById(R.id.button_Login);
        eLogin.setOnClickListener(this);
        eAttemptsInfo = findViewById(R.id.text_Attempts);

        eSignUp = findViewById(R.id.text_SignUp);
        eSignUp.setOnClickListener(this);

        eGuest = findViewById(R.id.bt_Guest);
        eGuest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intentSignUp = new Intent(getApplicationContext(), Registration_Page.class);
        Intent intentLogin = new Intent(Login_Page.this, Home_Page.class);

        switch (view.getId()){
            case R.id.button_Login:
                String inputName = eName.getText().toString();
                String inputPassword = ePassword.getText().toString();

                isAdmin = validateAdmin(inputName, inputPassword);
                if(isAdmin){
                    startActivity(intentLogin);
                }
                else if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(Login_Page.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    try{
                        sendLoginInfo(eName.getText().toString(), ePassword.getText().toString());
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    if (counter == 0) {
                        eLogin.setEnabled(false);
                    }
                }
                break;
                case R.id.bt_Guest:
                    Toast.makeText(Login_Page.this, "logged in as Guest", Toast.LENGTH_SHORT).show();
                    startActivity(intentLogin);
                    break;
                case R.id.text_SignUp:
                    startActivity(intentSignUp);
        }
    }

    private void sendLoginInfo(String eName, String ePassword) throws JSONException {
        RequestListener loginListener = new RequestListener(){

            @Override
            public void onSuccess(Object jsonObject) {
                JSONObject object = (JSONObject)jsonObject;
                Intent intentLogin = new Intent(Login_Page.this, Home_Page.class);
                try{
                    intentLogin.putExtra("message", object.get("message").toString());
                    startActivity(intentLogin);
                    counter = 5;
                    eAttemptsInfo.setText("No. of attempts remaining: " + counter);
                }
                catch(JSONException jsonException){
                    System.out.println("connection issues");
                }
            }
            @Override
            public void onFailure(String error) {
                counter--;
                Toast.makeText(Login_Page.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                eAttemptsInfo.setText("No. of attempts remaining: " + counter);
            }
        };

        JSONObject data = new JSONObject();
        data.put("email", eName);
        data.put("password", ePassword);

       // VolleyListener.makeRequest(getApplicationContext(), "/login", loginListener, data, Request.Method.POST);
    }

    private boolean validateAdmin(String name, String password) {
        if (name.equalsIgnoreCase(adminUserName) && password.equals(adminPassword)) {
            return true;
        }
        return false;
    }
}
