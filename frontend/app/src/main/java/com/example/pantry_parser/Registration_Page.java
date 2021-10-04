package com.example.pantry_parser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Registration_Page extends AppCompatActivity implements View.OnClickListener {

    private EditText userName, userPassword, userConfirmPassword, userEmail;
    private Button  registerButton, alreadyRegbutton;
    private static final String URL_REGIST = "http://coms-309-032.cs.iastate.edu:8080/user";
    private RequestQueue Queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        Queue = Volley.newRequestQueue(this);

        userName = findViewById(R.id.text_registrationUsername);
        userPassword = findViewById((R.id.text_registrationPassword));
        userConfirmPassword = findViewById(R.id.text_registrationConfirmPassword);
        userEmail = findViewById(R.id.text_registrationEmail);
        registerButton = findViewById(R.id.bt_Register);
        alreadyRegbutton = findViewById(R.id.bt_AlreadyRegistered);

        Button registerButton = findViewById(R.id.bt_Register);
        registerButton.setOnClickListener(this);

        Button alreadyRegButton = findViewById(R.id.bt_AlreadyRegistered);
        alreadyRegButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intentAlreadyReg = new Intent(getApplicationContext(), Login_Page.class);
        switch (view.getId()){
            case R.id.bt_Register:
                Register();
                break;
            case R.id.bt_AlreadyRegistered:
                startActivity(intentAlreadyReg);
                break;
        }
    }

    private void Register() {
        final String user_Name = userName.getText().toString().trim();
        final String user_Email = userEmail.getText().toString().trim();
        final String user_Password = userPassword.getText().toString().trim();
        Intent intentLogin = new Intent(getApplicationContext(), Login_Page.class);

        JSONObject params = new JSONObject();
        try {
            params.put("displayname", user_Name);
            params.put("email", user_Email);
            params.put("password", user_Password);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        final String requestBody = params.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if(message.equals("success")){
                                Toast.makeText(Registration_Page.this,
                                        "Registration Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(intentLogin);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Registration_Page.this,
                                    "Registration Error!" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Registration_Page.this,
                                "Registration Error!" + error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {

                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };




        /*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("displayname", user_Name);
                params.put("email", user_Email);
                params.put("password", user_Password);

                return params;
            }
        };
        */

        Queue.add(stringRequest);
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
    }
}