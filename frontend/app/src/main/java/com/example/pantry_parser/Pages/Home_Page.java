package com.example.pantry_parser.Pages;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.R;
import com.example.pantry_parser.RecyclerView.IngredientListView;
import com.example.pantry_parser.RecyclerView.ListView;

public class Home_Page extends AppCompatActivity implements View.OnClickListener{


    /**
     *Initializes home page elements
     * @param savedInstanceState
     */
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

        ImageView rotatingDonut = findViewById(R.id.DonutCircle);
        rotatingDonut.setOnClickListener(this);

        Animation a = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        a.setRepeatCount(-1);
        a.setDuration(30000);
        a.setInterpolator(new LinearInterpolator());
        rotatingDonut.startAnimation(a);

    }

    /**
     *Method to start new activities based on button click
     * @param view the button id
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_FindRecipes:
                Intent pantryParserIntent = new Intent(getApplicationContext(), ListView.class);
                pantryParserIntent.putExtra("SwitchView", "ALL_RECIPES");
                startActivity(pantryParserIntent);
                break;
            case R.id.bt_MyRecipes:
                Intent myRecipesIntent = new Intent(getApplicationContext(), IngredientListView.class);
               // myRecipesIntent.putExtra("SwitchView", "MY_RECIPES");
                startActivity(myRecipesIntent);
                break;
            case R.id.bt_Favorites:
                Intent FavoritesIntent = new Intent(getApplicationContext(), ListView.class);
                FavoritesIntent.putExtra("SwitchView", "FAV_RECIPES");
                startActivity(FavoritesIntent);
                break;
            case R.id.bt_Settings:
                Intent settingsIntent = new Intent(getApplicationContext(), Settings_Page.class);
                startActivity(settingsIntent);
                break;
            case R.id.DonutCircle:
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.sample);
                mp.start();
        }
    }
}