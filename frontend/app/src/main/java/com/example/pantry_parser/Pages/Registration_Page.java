package com.example.pantry_parser.Pages;

import static com.example.pantry_parser.Utilities.PasswordValidation.isValidPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Registration_Page extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText email, username, password, confirmPassword;
    private Button  registerButton, alreadyRegbutton, back;
    private static final String URL_REGIST = "http://coms-309-032.cs.iastate.edu:8080/users";
    private RequestQueue Queue;

    String sUsername, sEmail, sPassword, sConfirmPassword;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        Queue = Volley.newRequestQueue(this);

        email = findViewById(R.id.textInput_email);
        username = findViewById(R.id.textInput_username);
        password = findViewById((R.id.textInput_password));
        confirmPassword = findViewById(R.id.textInput_cPassword);

        registerButton = findViewById(R.id.button_register);
        alreadyRegbutton = findViewById(R.id.bt_AlreadyRegistered);
        back = findViewById(R.id.button_backRegistration);

        registerButton.setOnClickListener(this);
        alreadyRegbutton.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intentAlreadyReg = new Intent(getApplicationContext(), Login_Page.class);
        switch (view.getId()){
            case R.id.button_register:
                sEmail = email.getText().toString().trim();
                sUsername = username.getText().toString().trim();
                sPassword = password.getText().toString().trim();
                sConfirmPassword = confirmPassword.getText().toString().trim();
                if(isValidPassword(sPassword)){
                    if(sPassword.equals(sConfirmPassword)) {
                        if (!sEmail.isEmpty() && !sUsername.isEmpty()) {
                            Register();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: please make sure all fields are filled out and try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(getApplicationContext(),"Error: please check your passwords and try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error: not a valid password.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_AlreadyRegistered:
                startActivity(intentAlreadyReg);
                break;
            case R.id.button_backRegistration:
                startActivity(new Intent(getApplicationContext(), Login_Page.class));
        }
    }

    /**
     * Creates a new user from inputted username, email, password, and confirms passwords match.
     */
    public void Register() {
        final String user_Name = username.getText().toString().trim();
        final String user_Email = email.getText().toString().trim();
        final String user_Password = password.getText().toString().trim();
        Intent intentLogin = new Intent(getApplicationContext(), Home_Page.class);

        JSONObject params = new JSONObject();
        try {
            params.put("displayName", user_Name);
            params.put("email", user_Email);
            params.put("password", user_Password);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        final String requestBody = params.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            /**
             *
             * @param response
             */
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("success");

                    if(message.equals("true")){
                        Toast.makeText(Registration_Page.this,
                                "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(intentLogin);
                    } else{
                        Toast.makeText(Registration_Page.this, "Error, " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                    /**
                     *
                     * @param error
                     */
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

        Queue.add(stringRequest);
    }
}


