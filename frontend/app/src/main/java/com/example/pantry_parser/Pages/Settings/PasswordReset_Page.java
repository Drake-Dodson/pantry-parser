package com.example.pantry_parser.Pages.Settings;

import static com.example.pantry_parser.Utilities.URLs.URL_USER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.Pages.Login_Page;
import com.example.pantry_parser.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordReset_Page extends AppCompatActivity {

    TextView tv_otpSent;
    TextInputEditText idt_email, idt_otp, idt_newPassword, idt_confirmPassword;
    Button bt_back, bt_sendOTP, bt_confirm;

    private String user_id, user_email, username, otp, nPassword, cPassword;
    private static final String URL = URL_USER;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        tv_otpSent = findViewById(R.id.textView_otpSentReset);
        idt_email = findViewById(R.id.textInput_emailReset);
        idt_otp = findViewById(R.id.textInput_otpReset);
        idt_newPassword = findViewById(R.id.textInput_newPasswordReset);
        idt_confirmPassword = findViewById(R.id.textInput_confirmPass);
        bt_back = findViewById(R.id.button_backResetPassword);
        bt_sendOTP = findViewById(R.id.button_sendOTPReset);
        bt_confirm = findViewById(R.id.button_confirmReset);

        queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");

        tv_otpSent.setVisibility(View.INVISIBLE);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings_Page.class));
            }
        });

        bt_sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email = idt_email.getText().toString().trim();
                if(!user_email.isEmpty()) {
                    sendOTP();
                }
                bt_sendOTP.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bt_sendOTP.setEnabled(true);
                    }
                }, 3000);
            }
        });

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = idt_otp.getText().toString().trim();
                nPassword = idt_newPassword.getText().toString().trim();
                cPassword = idt_confirmPassword.getText().toString().trim();
                if(!user_email.isEmpty() && !nPassword.isEmpty() && !cPassword.isEmpty()) {
                    if(nPassword.equals(cPassword)){
                        resetPassword();
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please make sure all fields are filled", Toast.LENGTH_SHORT).show();
                }
                bt_confirm.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bt_confirm.setEnabled(true);
                    }
                }, 3000);
            }
        });
    }

    /**
     * Method to send request to server to obtain a one-time-password
     */
    public void sendOTP(){
        JsonObjectRequest otpRequest = new JsonObjectRequest(Request.Method.GET, URL + "email/" + user_email + "/password-reset/", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String success = response.getString("success");
                            String message = response.getString("message");

                            if(success.equals("true")){
                                tv_otpSent.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(PasswordReset_Page.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(otpRequest);
    }

    /**
     * Method that sends a request to server to reset the user's password
     */
    public void resetPassword(){
        JSONObject changeObj = new JSONObject();
        try{
            changeObj.put("newPassword", nPassword);
            changeObj.put("OTP", otp);
        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest changePasswordRequest = new JsonObjectRequest(Request.Method.POST, URL + "email/" + user_email + "/password-reset/", changeObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String success = response.getString("success");
                            String message = response.getString("message");

                            if(success.equals("true")){
                                startActivity(new Intent(getApplicationContext(), Login_Page.class));
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(PasswordReset_Page.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PasswordReset_Page.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(changePasswordRequest);
    }
}