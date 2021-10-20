package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pantry_parser.Services.AppController;
import com.example.pantry_parser.Services.IView;
import com.example.pantry_parser.R;
import com.example.pantry_parser.RegistrationLogic;
import com.example.pantry_parser.ServerRequest;

import org.json.JSONException;

public class Registration_Page extends AppCompatActivity implements IView {

    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    public TextView registerErrorTextView;
    Button registerSubmitButton, alreadyRegisteredButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_registration_page);

        nameEditText = findViewById(R.id.text_registrationUsername);
        emailEditText = findViewById(R.id.text_registrationEmail);
        passwordEditText = findViewById((R.id.text_registrationPassword));
        confirmPasswordEditText = findViewById(R.id.text_registrationConfirmPassword);
        registerErrorTextView = findViewById(R.id.text_registerError);
        registerSubmitButton = findViewById(R.id.bt_Register);
        alreadyRegisteredButton = findViewById(R.id.bt_AlreadyRegistered);

        ServerRequest serverRequest = new ServerRequest();
        final RegistrationLogic logic = new RegistrationLogic(this,serverRequest);

        registerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = nameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    logic.registerUser(name, email, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        alreadyRegisteredButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //route to start login_page activity
            }
        }));
    }


    @Override
    public void showText(String s) {
        registerErrorTextView.setText(s);
        registerErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void toastText(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}


//@Override
//public void onClick(View view) {
//        Intent intentAlreadyReg = new Intent(getApplicationContext(), Login_Page.class);
//        switch (view.getId()){
//        case R.id.bt_Register:
//        registerUser();
//        break;
//        case R.id.bt_AlreadyRegistered:
//        startActivity(intentAlreadyReg);
//        break;
//        }
//        }
//
//private void registerUser() {
//
//        Intent intentLogin = new Intent(getApplicationContext(), Login_Page.class);
//
//        JSONObject newUserObj = new JSONObject();
//        try {
//        newUserObj.put("displayname", user_Name);
//        newUserObj.put("email", user_Email);
//        newUserObj.put("password", user_Password);
//        } catch (JSONException jsonException) {
//        jsonException.printStackTrace();
//        }
//
//final String requestBody = newUserObj.toString();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//@Override
//public void onResponse(String response) {
//        try{
//        JSONObject jsonObject = new JSONObject(response);
//        String message = jsonObject.getString("message");
//
//        if(message.equals("success")){
//        Toast.makeText(Registration_Page.this,
//        "Registration Successful!", Toast.LENGTH_SHORT).show();
//        startActivity(intentLogin);
//        }
//        } catch (JSONException e) {
//        e.printStackTrace();
//        Toast.makeText(Registration_Page.this,
//        "Registration Error!" + e.toString(),
//        Toast.LENGTH_SHORT).show();
//        }
//        }
//        },
//        new Response.ErrorListener() {
//@Override
//public void onErrorResponse(VolleyError error) {
//        Toast.makeText(Registration_Page.this,
//        "Registration Error!" + error.toString(),
//        Toast.LENGTH_SHORT).show();
//        }
//        }){
//@Override
//public String getBodyContentType() {
//        return "application/json; charset=utf-8";
//        }
//
//@Override
//public byte[] getBody() {
//        try {
//
//        return requestBody == null ? null : requestBody.getBytes("utf-8");
//        } catch (UnsupportedEncodingException e) {
//        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//        return null;
//        }
//        }
//        };
//
//        Queue.add(stringRequest);
//        }