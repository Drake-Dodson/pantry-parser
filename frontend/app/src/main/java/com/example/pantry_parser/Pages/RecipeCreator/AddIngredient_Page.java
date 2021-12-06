package com.example.pantry_parser.Pages.RecipeCreator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.R;

import java.util.ArrayList;
import java.util.Scanner;

public class AddIngredient_Page extends AppCompatActivity {

    EditText et_ingredients, et_ingredientName, et_quantity, et_unit;
    Button bt_addIngredient, bt_back, bt_clear;

    private String ingredients, ingredientName, units, quantity;

    private ArrayList<String> ingredientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient_page);
        FavoriteSocket.changeContext(this);

        et_ingredients = findViewById(R.id.editText_recipeIngredients);
        et_ingredientName = findViewById(R.id.editText_ingredientName);
        et_quantity = findViewById(R.id.editText_ingredientAmount);
        et_unit = findViewById(R.id.editText_ingredientUnit);
        bt_addIngredient = findViewById(R.id.button_addIngredient);
        bt_back = findViewById(R.id.button_backIngredients);
        bt_clear = findViewById(R.id.button_clearIngredients);

        Bundle extras = getIntent().getExtras();
        ingredientsList = extras.getStringArrayList("ingredients");
        ingredients = "";
        for(int i = 0; i < ingredientsList.size(); i++){
            ingredients += ingredientsList.get(i) + "\n";
            ingredients.trim();
        }
        et_ingredients.setText(ingredients);
        System.out.println(ingredients);

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
                ingredientsList.clear();
                ingredients = et_ingredients.getText().toString().trim();
                Scanner scanner = new Scanner(ingredients);
                while(scanner.hasNextLine()){
                    ingredientsList.add(scanner.nextLine());
                }
                Intent intent = new Intent();
                intent.putExtra("result", ingredientsList);
                setResult(RESULT_OK, intent);
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