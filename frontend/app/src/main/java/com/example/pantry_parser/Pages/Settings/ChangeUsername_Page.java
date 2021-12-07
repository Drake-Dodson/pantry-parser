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
import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeUsername_Page extends AppCompatActivity {

    TextView tv_username;
    TextInputEditText idt_username, idt_password;
    Button bt_back, bt_submit;

    private String username, nUsername, password, email, user_id;
    private static final String URL = URL_USER;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        FavoriteSocket.changeContext(this);

        tv_username = findViewById(R.id.textView_currentUsername);
        idt_username = findViewById(R.id.textInput_username);
        idt_password = findViewById(R.id.textInput_password);
        bt_back = findViewById(R.id.button_backChangeUsername);
        bt_submit = findViewById(R.id.button_submitUsernameChange);

        queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");
        if(!prefs.getBoolean("is_logged_in", false)) {
            Toast.makeText(this, "You can't do that as a guest", Toast.LENGTH_LONG).show();
            finish();
        }
        getUserInfo();

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings_Page.class));
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nUsername = idt_username.getText().toString().trim();
                password = idt_password.getText().toString().trim();
                if(!nUsername.isEmpty() || !password.isEmpty()){
                    updateUsername();
                } else {
                    Toast.makeText(ChangeUsername_Page.this, "Please make sure you have entered a new username and your password.", Toast.LENGTH_SHORT).show();
                }
                bt_submit.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bt_submit.setEnabled(true);
                    }
                }, 3000);
            }
        });
    }

    /**
     * Method that sends a request to the server to retrieve user information
     */
    private void getUserInfo(){
        JsonObjectRequest infoRequest = new JsonObjectRequest(Request.Method.GET, URL + user_id, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try{
                        email = response.getString("email");
                        username =response.getString("displayName");
                        tv_username.setText(username);
                    }catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(infoRequest);
    }

    /**
     * Method that sends a request to server to change the user's username
     */
    private void updateUsername() {
        String user_Email = email;
        String user_Name = idt_username.getText().toString().trim();
        String user_Password = idt_password.getText().toString().trim();

        JSONObject updateObj = new JSONObject();
        try {
            updateObj.put("password", user_Password);
            updateObj.put("email", user_Email);
            updateObj.put("displayName", user_Name);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ChangeUsername_Page.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PATCH, URL + user_id + "/update/", updateObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String success = response.getString("success");
                            String message = response.getString("message");

                            if (success.equals("true")) {
                                startActivity(new Intent(getApplicationContext(), Settings_Page.class));
                                Toast.makeText(ChangeUsername_Page.this, "Username successfully updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangeUsername_Page.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeUsername_Page.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeUsername_Page.this, "Updating Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(updateRequest);
    }
}