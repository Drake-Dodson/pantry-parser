package com.example.experiment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;

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
        eAttemptsInfo = findViewById(R.id.text_Attempts);

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputName = eName.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    isValid = validateAdmin(inputName, inputPassword);
                    if (!isValid) {
                        counter--;
                        Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                        eAttemptsInfo.setText("No. of attempts remaining: " + counter);

                        if (counter == 0) {
                            eLogin.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                        counter = 5;
                        eAttemptsInfo.setText("No. of attempts remaining: " + counter);
                    }

                }
            }

        });

    }

    private boolean validateAdmin(String name, String password) {
        if (name.equalsIgnoreCase(adminUserName) && password.equals(adminPassword)) {
            return true;
        }
        return false;
    }
}