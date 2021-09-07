package com.example.hello_android_studio2;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.buttonMessage);
        b2 = findViewById(R.id.buttonNext);

        b1.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Hello Android!", Toast.LENGTH_LONG).show());

        b2.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
        });
    }
}