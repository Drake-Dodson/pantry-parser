package com.example.pantry_parser.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class RecipeCreator_Page extends AppCompatActivity implements View.OnClickListener {

    private static final String URL_USER = "http://coms-309-032.cs.iastate.edu:8080/user/";

    EditText rName, rSummary, rDescription, rPreptime, rCooktime, rServings, rNutrition;
    TextView addIngredients, addSteps;
    Button createRecipe, cancelRecipe;
    ImageView rImage;

    String recipeName, recipeSummary, recipeDescription, recipeNutrition;
    String recipeIngredientsDisplay, recipeStepsDisplay = "";
    String sData, user_id;
    int recipePreptime, recipeCooktime, recipeTotaltime, recipeServings;

    ArrayList<String> recipeIngredients = new ArrayList<>();
    ArrayList<String> recipeSteps = new ArrayList<>();

    private RequestQueue Queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_creator_page);

        rName = findViewById(R.id.editText_recipeName);
        rSummary = findViewById(R.id.editText_recipeSummary);
        rDescription = findViewById(R.id.editText_recipeDescription);
        rPreptime = findViewById(R.id.editText_recipePrepTime);
        rCooktime = findViewById(R.id.editText_recipeCookTime);
        rServings = findViewById(R.id.editText_recipeServings);
        rNutrition = findViewById(R.id.editText_recipeNutrition);

        rImage = findViewById(R.id.imageView_RecipeImage);
        addIngredients = findViewById(R.id.textView_recipeIngredients);
        addSteps = findViewById(R.id.textView_recipeSteps);
        createRecipe = findViewById(R.id.button_createRecipe);
        cancelRecipe = findViewById(R.id.button_cancelRecipe);

        Queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");

        ActivityResultLauncher<String> imageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                rImage.setImageURI(result);
            }
        });

        ActivityResultLauncher<Intent> ingredientsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result != null && result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                Intent data = result.getData();
                                sData = data.getStringExtra("result");

                                if(sData.isEmpty()){
                                    recipeIngredientsDisplay = "";
                                    recipeIngredients.clear();
                                }
                                else {
                                    recipeIngredientsDisplay = "";
                                    recipeIngredients.clear();
                                    Scanner scanner = new Scanner(sData);
                                    while(scanner.hasNextLine()){
                                        String nextIngredient = scanner.nextLine();
                                        recipeIngredients.add(nextIngredient);
                                    }
                                    recipeIngredientsDisplay = recipeIngredients.get(0) + "\n";
                                    for(int i = 1; i < recipeIngredients.size(); i++) {
                                        recipeStepsDisplay += recipeIngredients.get(i) + "\n";
                                    }
                                }
                                addIngredients.setText(recipeIngredientsDisplay);
                            }
                        }
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
                                sData = data.getStringExtra("result");

                                if(sData.isEmpty()){
                                    recipeStepsDisplay = "";
                                    recipeSteps.clear();
                                }
                                else {
                                    recipeStepsDisplay = "";
                                    recipeSteps.clear();

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
                            }
                            addSteps.setText(recipeStepsDisplay);
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
                Intent addIngredients = new Intent(getApplicationContext(), AddIngredient_Page.class);
                addIngredients.putExtra("ingredients", sData);
                ingredientsActivityResultLauncher.launch(addIngredients);
            }
        });

        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSteps = new Intent(getApplicationContext(), AddSteps_Page.class);
                addSteps.putExtra("steps", sData);
                stepsActivityResultLauncher.launch(addSteps);
            }
        });

        createRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(recipeName)){
                    rName.setError("All great recipes deserve a name!");
                }
                if(TextUtils.isEmpty(recipeSummary)){
                    rSummary.setError("Please add in a recipe summary!");
                }
                if(TextUtils.isEmpty(recipeDescription)){
                    rDescription.setError("Please add in a recipe description!");
                }
                if(TextUtils.isEmpty(rPreptime.getText())){
                    rPreptime.setError("Please add in recipe prep time!");
                }
                if(TextUtils.isEmpty(rCooktime.getText())){
                    rCooktime.setError("Please add in recipe cook time!");
                }
                if(TextUtils.isEmpty(rServings.getText())){
                    rServings.setError("Please add in how many servings the recipe makes!");
                }
                if(TextUtils.isEmpty(addIngredients.getText())){
                    addIngredients.setError("You haven't added any ingredients yet!");
                }
                if(TextUtils.isEmpty(addSteps.getText())){
                    addSteps.setError("You haven't added any steps yet!");
                }
                if(!TextUtils.isEmpty(recipeName)
                        && !TextUtils.isEmpty(recipeSummary)
                        && !TextUtils.isEmpty(recipeDescription)
                        && !TextUtils.isEmpty(rPreptime.getText())
                        && !TextUtils.isEmpty(rCooktime.getText())
                        && !TextUtils.isEmpty(rServings.getText())
                        && !TextUtils.isEmpty(addIngredients.getText())
                        && !TextUtils.isEmpty(addSteps.getText())){
                    createRecipe();
                }
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

    public void createRecipe(){
        recipeName = rName.getText().toString();
        recipeSummary = rSummary.getText().toString();
        recipePreptime = Integer.parseInt(rPreptime.getText().toString());
        recipeCooktime = Integer.parseInt(rCooktime.getText().toString());
        recipeTotaltime = recipePreptime + recipeCooktime;
        recipeDescription = rDescription.getText().toString();
        recipeNutrition = rNutrition.getText().toString();
        recipeServings = Integer.parseInt(rServings.getText().toString());
        JSONArray ingredients = new JSONArray((recipeIngredients));
        JSONArray steps = new JSONArray(recipeSteps);

        JSONObject recipe = new JSONObject();
        try{
            recipe.put("name", recipeName);
            recipe.put("prep_time", recipePreptime);
            recipe.put("cook_time", recipeCooktime);
            recipe.put("summary", recipeSummary);
            recipe.put("description", recipeDescription);
            recipe.put("nutrition_facts", recipeNutrition);
            recipe.put("num_servings", recipeServings);
            recipe.put("ingredients", ingredients);
            recipe.put("steps", steps);

        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.POST, URL_USER + user_id + "/recipes/", recipe,
           new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject jsonObject = new JSONObject();
                    String message = jsonObject.getString("success");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipeCreator_Page.this,
                        "Error with processing your request. Please try again!" + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
       Queue.add(recipeRequest);
    }
}