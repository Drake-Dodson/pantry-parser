package com.example.helloworld_androidstudios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Counter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);


        Button backButton = findViewById(R.id.counter_back_button);
        Button decreaseButton = findViewById(R.id.counter_decrease_button);
        Button decrease10Button = findViewById(R.id.counter_decrease10_button);
        Button incrementButton = findViewById(R.id.counter_increment_button);
        Button increment10Button = findViewById(R.id.counter_increment10_button);
        TextView counterText = findViewById(R.id.counter_counterText);

        final Integer[] count = {0};

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0]++;
                counterText.setText(count[0].toString());
            }
        });

        increment10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0] = count[0] + 10;
                counterText.setText(count[0].toString());
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0]--;
                counterText.setText(count[0].toString());
            }
        });

        decrease10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0] = count[0] - 10;
                counterText.setText(count[0].toString());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });
    }
}