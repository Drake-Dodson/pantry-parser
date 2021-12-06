package com.example.pantry_parser.Pages.RecipeCreator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.R;

public class AddIngredient_Page extends AppCompatActivity {

    EditText et_ingredients, et_ingredientName, et_quantity, et_unit;
    Button bt_addIngredient, bt_back, bt_clear;

    private String ingredients, ingredientName, units;
    private String quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient_page);
//        FavoriteSocket.changeContext(this);

        et_ingredients = findViewById(R.id.editText_recipeIngredients);
        et_ingredientName = findViewById(R.id.editText_ingredientName);
        et_quantity = findViewById(R.id.editText_ingredientAmount);
        et_unit = findViewById(R.id.editText_ingredientUnit);
        bt_addIngredient = findViewById(R.id.button_addIngredient);
        bt_back = findViewById(R.id.button_backIngredients);
        bt_clear = findViewById(R.id.button_clearIngredients);

        bt_addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients = et_ingredients.getText().toString();
                ingredientName = et_ingredientName.getText().toString();
                units = et_unit.getText().toString();
                quantity = et_quantity.getText().toString();

                if(ingredients.isEmpty()){
                    ingredients += quantity + " " + units + " " + ingredientName;
                }
                else{
                    ingredients += "\n";
                    ingredients += quantity + " " + units + " " + ingredientName;
                }
                et_ingredients.setText(ingredients);
                System.out.println(ingredients);
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
                et_ingredients.getText().clear();
            }
        });
    }
}