package com.example.pantry_parser.Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.R;

public class AddIngredient_Page extends AppCompatActivity {

    EditText et_ingredients, et_ingredientName, et_quantity, et_unit;
    Button bt_addIngredient, bt_back, bt_clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient_page);

        bt_addIngredient = findViewById(R.id.button_addIngredient);
        bt_back = findViewById(R.id.button_backIngredients);
        bt_clear = findViewById(R.id.button_clearIngredients);

        bt_addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}