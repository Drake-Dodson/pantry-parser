package com.example.pantry_parser.Pages;

import static com.example.pantry_parser.Utilities.URLs.URL_LOGIN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.Network.RequestListener;
import com.example.pantry_parser.Network.VolleyListener;
import com.example.pantry_parser.Pages.Settings.PasswordReset_Page;
import com.example.pantry_parser.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class Login_Page extends AppCompatActivity implements View.OnClickListener{

        private EditText eName;
        private EditText ePassword;
        private Button eLogin;
        private Button eGuest;
        private TextView eAttemptsInfo;
        private TextView forgotPassword;
        private TextView eSignUp;

        private String adminUserName = "Admin";
        private String adminPassword = "2_do_8_comS_309";
        private String user_id, email;
        private static final String URL = URL_LOGIN;

        boolean user_logged_in = false;
        boolean isAdmin = false;
        private int counter = 5;

    /**
     *
     * @param savedInstanceState
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            eName = findViewById(R.id.text_Username);
            ePassword = findViewById((R.id.text_Password));
            eLogin = findViewById(R.id.button_Login);
            eLogin.setOnClickListener(this);
            eAttemptsInfo = findViewById(R.id.text_Attempts);

            forgotPassword = findViewById(R.id.text_ForgotPassword);
            forgotPassword.setOnClickListener(this);

            eSignUp = findViewById(R.id.text_SignUp);
            eSignUp.setOnClickListener(this);

            eGuest = findViewById(R.id.bt_Guest);
            eGuest.setOnClickListener(this);

            SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            boolean login = prefs.getBoolean((String.valueOf(user_logged_in)), false);
            String email = prefs.getString("email", "");
            String user_id = prefs.getString("user_id", "");

            if(login) {
                Intent intent = new Intent(getApplicationContext(), Home_Page.class);
                Toast.makeText(getApplicationContext(), "User id: " + user_id + "User email: " + email, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }

    /**
     *
     * @param view
     */
    @Override
        public void onClick(View view) {
            Intent intentSignUp = new Intent(getApplicationContext(), Registration_Page.class);
            Intent intentLogin = new Intent(Login_Page.this, Home_Page.class);

            switch (view.getId()){
                case R.id.button_Login:
                    email = eName.getText().toString();
                    String inputPassword = ePassword.getText().toString();

                    isAdmin = validateAdmin(email, inputPassword);
                    if(isAdmin){
                        startActivity(intentLogin);
                    }
                    else if (email.isEmpty() || inputPassword.isEmpty()) {
                        Toast.makeText(Login_Page.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    } else {
                        try{
                            this.sendLoginInfo(eName.getText().toString(), ePassword.getText().toString());
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        if (counter == 0) {
                            eLogin.setEnabled(false);
                        }
                    }
                    break;
                case R.id.bt_Guest:
                    SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_id", "");
                    editor.putString("role", "");
                    editor.putString("email", "");
                    editor.putBoolean("is_logged_in", false);
                    editor.commit();
                    editor.apply();
                    Toast.makeText(Login_Page.this, "logged in as Guest", Toast.LENGTH_SHORT).show();
                    startActivity(intentLogin);
                    break;
                case R.id.text_SignUp:
                    startActivity(intentSignUp);
                    break;
                case R.id.text_ForgotPassword:
                    startActivity(new Intent(getApplicationContext(), PasswordReset_Page.class));
                    break;
            }
        }

    /**
     *
     * @param eName name inputted by user
     * @param ePassword password inputted by user
     * @throws JSONException throws error
     */
    private void sendLoginInfo(String eName, String ePassword) throws JSONException {
            RequestListener loginListener = new RequestListener(){

                /**
                 *
                 * @param jsonObject
                 */
                @Override
                public void onSuccess(Object jsonObject) {
                    JSONObject object = (JSONObject)jsonObject;
                    Intent intentLogin = new Intent(Login_Page.this, Home_Page.class);
                    try{
//                        intentLogin.putExtra("message", object.get("message").toString());
                        String message = object.get("success").toString();
                        String user_id = object.get("message").toString();
                        if(message.equals("true")) {
                            SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            String[] chunks = user_id.split(":");
                            editor.putString("user_id", chunks[0]);
                            editor.putString("role", chunks[1]);
                            editor.putString("email", email);
                            editor.putBoolean("is_logged_in", true);
                            editor.commit();
                            editor.apply();

                            connectWebSocket();

                            startActivity(intentLogin);
                            counter = 5;
                            eAttemptsInfo.setText("No. of attempts remaining: " + counter);
                        } else{
                            counter--;
                            eAttemptsInfo.setText("No. of attempts remaining: " + counter);
                            if(counter == 0){
                                eLogin.setEnabled(false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        eLogin.setEnabled(true);
                                    }
                                }, 120000);
                                Toast.makeText(Login_Page.this, "Too many attempts please try again in 2 minutes", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(Login_Page.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    catch(JSONException jsonException){
                        System.out.println("connection issues");
                    }
                }

                /**
                 *
                 * @param error
                 */
                @Override
                public void onFailure(String error) {
                    System.out.println(error);
                    counter--;
                    Toast.makeText(Login_Page.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                    eAttemptsInfo.setText("No. of attempts remaining: " + counter);
                }
            };

            JSONObject data = new JSONObject();
            data.put("email", eName);
            data.put("password", ePassword);

            VolleyListener.makeRequest(getApplicationContext(), "/login/", loginListener, data, Request.Method.POST);
        }

    /**
     *
     * @param name Admin username
     * @param password Admin password
     * @return Returns true if correct admin, otherwise false
     */
    protected boolean validateAdmin(String name, String password) {
            if (name.equalsIgnoreCase(adminUserName) && password.equals(adminPassword)) {
                return true;
            }
            return false;
        }

    private void connectWebSocket() {
        URI uri;
        try {

            String user_id = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("user_id", "");
            if(user_id == "") {
                throw new NullPointerException("No user logged in");
            }
            uri = new URI("ws://coms-309-032.cs.iastate.edu:8080/websocket/" + user_id);

            FavoriteSocket.connectFavoriteSocket(uri);
            FavoriteSocket.changeContext(this);
        } catch (URISyntaxException | NullPointerException e) {
            e.printStackTrace();
            return;
        }

    }
}

