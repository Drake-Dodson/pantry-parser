package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;

public class Recipe_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        TextView NameRecipe = findViewById(R.id.RecipeName);
        NameRecipe.setText(recipe.getRecipeName());
        TextView AuthorRecipe = findViewById(R.id.RecipeAuthor);
        AuthorRecipe.setText(recipe.getAuthor());

    }
}