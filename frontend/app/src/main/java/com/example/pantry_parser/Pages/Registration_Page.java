package com.example.pantry_parser.Pages;

import static com.example.pantry_parser.Utilities.PasswordValidation.isValidPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pantry_parser.AppController;
import com.example.pantry_parser.Logic.RegistrationLogic;
import com.example.pantry_parser.Network.ServerRequest;
import com.example.pantry_parser.Pages.Login_Page;
import com.example.pantry_parser.R;
import com.example.pantry_parser.IView;

import org.json.JSONException;

public class Registration_Page extends AppCompatActivity implements IView, View.OnClickListener {

        private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
        private Button registerSubmitButton, alreadyRegisteredButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new AppController();
            setContentView(R.layout.activity_registration_page);

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

        ServerRequest serverRequest = new ServerRequest();
        final RegistrationLogic logic = new RegistrationLogic(this, serverRequest);

        @Override
        public void onClick(View view) {
            Intent intentAlreadyReg = new Intent(getApplicationContext(), Login_Page.class);
            switch (view.getId()) {
                case R.id.bt_Register:
                    String name = nameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                    if(email.isEmpty() == true){
                        Toast.makeText(Registration_Page.this,"Please enter your email!",Toast.LENGTH_SHORT).show();
                    }
                    if(name.isEmpty() == true){
                        Toast.makeText(Registration_Page.this,"Please enter your name!",Toast.LENGTH_SHORT).show();
                    }
                    if(password.isEmpty() == true){
                        Toast.makeText(Registration_Page.this,"Please enter your password!",Toast.LENGTH_SHORT).show();
                    }
                    if(!(password == confirmPassword)){
                        Toast.makeText(Registration_Page.this,"Passwords do not match!",Toast.LENGTH_SHORT).show();
                    }

                    try{
                        if (!email.isEmpty() && !name.isEmpty() && isValidPassword(password, confirmPassword) == true){
                            logic.registerUser(name, email, password);
                        }
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

