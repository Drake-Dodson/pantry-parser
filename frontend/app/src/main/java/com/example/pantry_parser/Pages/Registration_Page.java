package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pantry_parser.Utilities.IView;
import com.example.pantry_parser.AppController;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Logic.RegistrationLogic;
import com.example.pantry_parser.Network.ServerRequest;

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
                startActivity(new Intent(getApplicationContext(), Login_Page.class));
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
