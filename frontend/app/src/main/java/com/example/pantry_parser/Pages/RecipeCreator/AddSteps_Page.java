package com.example.pantry_parser.Pages.RecipeCreator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.R;

public class AddSteps_Page extends AppCompatActivity {

    String stringSteps;
    EditText addSteps;
    Button back, clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_steps_page);
        FavoriteSocket.changeContext(this);

        addSteps = findViewById(R.id.editText_recipeSteps);
        back = findViewById(R.id.button_backSteps);
        clear = findViewById(R.id.button_clearSteps);

        Bundle extras = getIntent().getExtras();
        stringSteps = extras.getString("steps");
        addSteps.setText(stringSteps);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringSteps = addSteps.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("result", stringSteps);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSteps.getText().clear();
            }
        });
    }
}