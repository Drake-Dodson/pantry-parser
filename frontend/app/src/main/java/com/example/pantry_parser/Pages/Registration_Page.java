package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pantry_parser.AppController;
import com.example.pantry_parser.Logic.RegistrationLogic;
import com.example.pantry_parser.Network.ServerRequest;
import com.example.pantry_parser.R;
import com.example.pantry_parser.IView;

import org.json.JSONException;


public class Registration_Page extends AppCompatActivity implements IView, View.OnClickListener {

        private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
        private Button registerSubmitButton, alreadyRegisteredButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registration_page);
            new AppController(this);

            nameEditText = findViewById(R.id.text_registrationUsername);
            emailEditText = findViewById(R.id.text_registrationEmail);
            passwordEditText = findViewById((R.id.text_registrationPassword));
            confirmPasswordEditText = findViewById(R.id.text_registrationConfirmPassword);
            registerSubmitButton = findViewById(R.id.bt_Register);
            alreadyRegisteredButton = findViewById(R.id.bt_AlreadyRegistered);

            Button registerButton = findViewById(R.id.bt_Register);
            registerButton.setOnClickListener(this);

            Button alreadyRegButton = findViewById(R.id.bt_AlreadyRegistered);
            alreadyRegButton.setOnClickListener(this);
        }
        ServerRequest serverRequest = new ServerRequest(this);
        final RegistrationLogic logic = new RegistrationLogic(this, serverRequest, this);

        @Override
        public void onClick(View view) {
            Intent intentAlreadyReg = new Intent(getApplicationContext(), Login_Page.class);
            Intent intentRegister = new Intent(getApplicationContext(), Home_Page.class);
            switch (view.getId()) {
                case R.id.bt_Register:
                    try{
                        String name = nameEditText.getText().toString().trim();
                        String email = emailEditText.getText().toString().trim();
                        String password = passwordEditText.getText().toString().trim();
                        logic.registerUser(name, email, password, intentRegister);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case R.id.bt_AlreadyRegistered:
                    startActivity(intentAlreadyReg);
                    break;
            }
        }

    @Override
    public void showText(String s) {

    }

    @Override
    public void toastText(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}

