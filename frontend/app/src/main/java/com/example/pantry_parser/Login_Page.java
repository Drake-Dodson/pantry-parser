package com.example.pantry_parser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Page extends AppCompatActivity implements View.OnClickListener{

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private Button eGuest;
    private TextView eAttemptsInfo;
    private TextView eSignUp;

    private String adminUserName = "Admin";
    private String adminPassword = "2_do_8_comS_309";

    boolean isValid = false;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.text_Username);
        ePassword = findViewById((R.id.text_Password));
        eLogin = findViewById(R.id.button_Login);
        eLogin.setOnClickListener(this);
        eAttemptsInfo = findViewById(R.id.text_Attempts);

        eSignUp = findViewById(R.id.text_SignUp);
        eSignUp.setOnClickListener(this);

        eGuest = findViewById(R.id.bt_Guest);
        eGuest.setOnClickListener(this);
    }



            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(Login_Page.this, Home_Page.class);

                switch (view.getId()){
                    case R.id.button_Login:
                        String inputName = eName.getText().toString();
                        String inputPassword = ePassword.getText().toString();

                        if (inputName.isEmpty() || inputPassword.isEmpty()) {
                            Toast.makeText(Login_Page.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                        } else {
                            isValid = validateAdmin(inputName, inputPassword);
                            if (!isValid) {
                                counter--;
                                Toast.makeText(Login_Page.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                                eAttemptsInfo.setText("No. of attempts remaining: " + counter);

                                if (counter == 0) {
                                    eLogin.setEnabled(false);
                                }
                            } else {
                                Toast.makeText(Login_Page.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(intentLogin);
                                counter = 5;
                                eAttemptsInfo.setText("No. of attempts remaining: " + counter);
                            }

                        }
                        break;
                    case R.id.bt_Guest:
                        Toast.makeText(Login_Page.this, "logged in as Guest", Toast.LENGTH_SHORT).show();
                        startActivity(intentLogin);
                        break;
                    case R.id.text_SignUp:
                        startActivity(intentLogin);
                }








    }

    private boolean validateAdmin(String name, String password) {
        if (name.equalsIgnoreCase(adminUserName) && password.equals(adminPassword)) {
            return true;
        }
        return false;
    }
}
