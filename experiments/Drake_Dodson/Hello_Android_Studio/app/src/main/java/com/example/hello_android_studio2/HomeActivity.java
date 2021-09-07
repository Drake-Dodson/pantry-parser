package com.example.hello_android_studio2;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        b1 = findViewById(R.id.buttonBack);

        b1.setOnClickListener(view -> {
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "General Kenobi!", Toast.LENGTH_LONG).show();
        });
    }
}