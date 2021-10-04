package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pantry_parser.R;

public class Home_Page extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button btFindRecipes = findViewById(R.id.bt_FindRecipes);
        btFindRecipes.setOnClickListener(this);

        Button btMyRecipes = findViewById(R.id.bt_MyRecipes);
        btMyRecipes.setOnClickListener(this);

        Button btFavorites = findViewById(R.id.bt_Favorites);
        btFavorites.setOnClickListener(this);

        Button btSettings = findViewById(R.id.bt_Settings);
        btSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_FindRecipes:
                Intent pantryParserIntent = new Intent(getApplicationContext(), PantryParser_Page.class);
                startActivity(pantryParserIntent);
                break;
            case R.id.bt_MyRecipes:
                Intent myRecipesIntent = new Intent(getApplicationContext(), MyRecipes_Page.class);
                startActivity(myRecipesIntent);
                break;
            case R.id.bt_Favorites:
                Intent FavoritesIntent = new Intent(getApplicationContext(), Favorites_Page.class);
                startActivity(FavoritesIntent);
                break;
            case R.id.bt_Settings:
                Intent settingsIntent = new Intent(getApplicationContext(), Settings_Page.class);
                startActivity(settingsIntent);
                break;
        }
    }
}