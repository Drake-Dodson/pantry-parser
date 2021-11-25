package com.example.pantry_parser.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.R;

public class RecipeCreator_Page extends AppCompatActivity implements View.OnClickListener {

    EditText rName, rDescription, rCooktime, rServings;
    TextView addIngredients, addSteps;
    Button createRecipe, cancelRecipe;
    ImageView rImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_creator_page);

        rName = findViewById(R.id.editText_recipeName);
        rDescription = findViewById(R.id.editText_recipeDescription);
        rCooktime = findViewById(R.id.editText_recipeCookTime);
        rServings = findViewById(R.id.editText_recipeCookTime);

        rImage = findViewById(R.id.imageView_RecipeImage);
        rImage.setOnClickListener(this);

        addIngredients = findViewById(R.id.textView_recipeIngredients);
        addIngredients.setOnClickListener(this);

        addSteps = findViewById(R.id.textView_recipeSteps);
        addSteps.setOnClickListener(this);

        createRecipe = findViewById(R.id.button_createRecipe);
        createRecipe.setOnClickListener(this);

        cancelRecipe = findViewById(R.id.button_cancelRecipe);
        cancelRecipe.setOnClickListener(this);

        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_recipeIngredients:
                startActivity(new Intent(getApplicationContext(), AddIngredient_Page.class));
                break;
            case R.id.textView_recipeSteps:
                startActivity(new Intent(getApplicationContext(), AddSteps_Page.class));
                break;
            case R.id.button_createRecipe:

                break;
            case R.id.button_cancelRecipe:
                startActivity(new Intent(getApplicationContext(), ListView.class));
                break;
            case R.id.imageView_RecipeImage:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
                break;

        }
    }
}