package com.example.pantry_parser.Pages.Settings;

import static com.example.pantry_parser.Utilities.URLs.URL_USER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.Pages.Home_Page;
import com.example.pantry_parser.Pages.Login_Page;
import com.example.pantry_parser.R;
import com.example.pantry_parser.RecyclerView.ListView;
import com.example.pantry_parser.Utilities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings_Page extends AppCompatActivity {

    TextView usernameDisplay, emailDisplay, line6;
    TextView changeUsername, changeEmail, changePassword, goMyRecipes, goAdminView;
    Button back, logout;
    ImageView profileImage, adminIcon, adminArrow;

    private String username, email, user_id;
    private static final String URL = URL_USER;

    private RequestQueue queue;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changeUsername = findViewById(R.id.TextView_changeUsername);
        changeEmail = findViewById(R.id.TextView_changeEmail);
        changePassword = findViewById(R.id.TextView_changePassword);
        goMyRecipes = findViewById(R.id.TextView_goMyRecipes);
        usernameDisplay = findViewById(R.id.TextView_username);
        emailDisplay = findViewById(R.id.TextView_email);
        back = findViewById(R.id.button_backSettings);
        logout = findViewById(R.id.button_Logout);
        profileImage = findViewById(R.id.imageView_profileImage);
        line6 = findViewById(R.id.line6);
        goAdminView = findViewById(R.id.TextView_goAdminView);
        adminIcon = findViewById(R.id.adminViewIcon);
        adminArrow = findViewById(R.id.adminViewArrow);
        FavoriteSocket.changeContext(this);
        queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");

        if(!prefs.getString("role", "").trim().toLowerCase().contains(User.DESIGNATION_ADMIN)){
            line6.setVisibility(View.INVISIBLE);
            goAdminView.setVisibility(View.INVISIBLE);
            adminIcon.setVisibility(View.INVISIBLE);
            adminArrow.setVisibility(View.INVISIBLE);
        }else {
            line6.setVisibility(View.VISIBLE);
            goAdminView.setVisibility(View.VISIBLE);
            adminIcon.setVisibility(View.VISIBLE);
            adminArrow.setVisibility(View.VISIBLE);
        }

        if(!prefs.getBoolean("is_logged_in", false)) {
            usernameDisplay.setText("Guest");
            emailDisplay.setText("");
        } else {
            setUserInfo();
        }

        ActivityResultLauncher<String> imageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        profileImage.setImageURI(result);
                    }
                });

        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangeUsername_Page.class));
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangeEmail_Page.class));
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePassword_Page.class));
            }
        });

        goMyRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListView.class);
                intent.putExtra("SwitchView", "MY_RECIPES");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home_Page.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                editor.apply();
                startActivity(new Intent(getApplicationContext(), Login_Page.class));
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageResultLauncher.launch("image/*");
            }
        });

        goAdminView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangeEmail_Page.class);
                intent.putExtra("SwitchView", "ADMIN");
                startActivity(intent);
            }
        });

    }

    private void setUserInfo(){
        JsonObjectRequest infoRequest = new JsonObjectRequest(Request.Method.GET, URL + user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try{
                        email = response.getString("email");
                        username =response.getString("displayName");
                        usernameDisplay.setText(username);
                        emailDisplay.setText(email);
                        SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("username", username);
                        editor.commit();
                        editor.apply();
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
}