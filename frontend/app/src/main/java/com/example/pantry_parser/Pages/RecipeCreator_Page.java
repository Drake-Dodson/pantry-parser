package com.example.pantry_parser.Pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pantry_parser.R;

import java.util.ArrayList;
import java.util.Scanner;

public class RecipeCreator_Page extends AppCompatActivity implements View.OnClickListener {

    EditText rName, rDescription, rCooktime, rServings;
    TextView addIngredients, addSteps;
    Button createRecipe, cancelRecipe;
    ImageView rImage;

    String recipeName, recipeDescription, recipeStepsDisplay;
    int recipeCooktime, recipeServings;
    ArrayList<String> recipeIngredients = new ArrayList<>();
    ArrayList<String> recipeSteps = new ArrayList<>();

    ActivityResultLauncher<String> imageResultLauncher;
    ActivityResultLauncher<Intent> stepsResultsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_creator_page);

        rName = findViewById(R.id.editText_recipeName);
        rDescription = findViewById(R.id.editText_recipeDescription);
        rCooktime = findViewById(R.id.editText_recipeCookTime);
        rServings = findViewById(R.id.editText_recipeCookTime);

        rImage = findViewById(R.id.imageView_RecipeImage);
        addIngredients = findViewById(R.id.textView_recipeIngredients);
        addSteps = findViewById(R.id.textView_recipeSteps);
        createRecipe = findViewById(R.id.button_createRecipe);
        cancelRecipe = findViewById(R.id.button_cancelRecipe);

        imageResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                rImage.setImageURI(result);
            }
        });

        ActivityResultLauncher<Intent> stepsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result != null && result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                Intent data = result.getData();
                                String sData = data.getStringExtra("result");

                                if(sData.isEmpty()){
                                    recipeStepsDisplay ="";
                                }
                                else {
                                    Scanner scanner = new Scanner(sData);
                                    while(scanner.hasNextLine()){
                                        String nextStep = scanner.nextLine();
                                        recipeSteps.add(nextStep);
                                    }
                                    int count = 1;
                                    recipeStepsDisplay = count + ". " + recipeSteps.get(0) + "\n";
                                    for(int i = 1; i < recipeSteps.size(); i++) {
                                        count++;
                                        recipeStepsDisplay += count + ". " + recipeSteps.get(i) + "\n";
                                    }
                                }
                                addSteps.setText(recipeStepsDisplay);
                            }
                        }
                    }
                });

        rImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageResultLauncher.launch("image/*");
            }
        });

        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddIngredient_Page.class));
            }
        });

        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSteps = new Intent(getApplicationContext(), AddSteps_Page.class);
                stepsActivityResultLauncher.launch(addSteps);
            }
        });

        createRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListView.class));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


}