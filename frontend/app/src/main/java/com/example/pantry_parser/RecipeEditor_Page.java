package com.example.pantry_parser;

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
import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.Pages.Home_Page;
import com.example.pantry_parser.Pages.RecipeCreator.AddIngredient_Page;
import com.example.pantry_parser.Pages.RecipeCreator.AddSteps_Page;
import com.example.pantry_parser.RecyclerView.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class RecipeEditor_Page extends AppCompatActivity {

    private static final String URL_USER = "http://coms-309-032.cs.iastate.edu:8080/user/";
    private static final String URL_RECIPE = "http://coms-309-032.cs.iastate.edu:8080/recipe/";

    EditText rName, rSummary, rDescription, rPrepTime, rCookTime, rServings, rNutrition;
    TextView addIngredients, addSteps;
    Button updateRecipe, cancelRecipe;
    ImageView rImage;

    String recipeName, recipeSummary, recipeDescription, recipeNutrition;
    String recipeIngredientsDisplay, recipeStepsDisplay = "";
    String sData, user_id, recipe_id;
    int recipePrepTime, recipeCookTime, recipeServings;

    ArrayList<String> lData = new ArrayList<>();
    ArrayList<String> recipeIngredients = new ArrayList<>();
    ArrayList<String> recipeSteps = new ArrayList<>();

    JSONArray ingredients;

    private RequestQueue Queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_creator_page);
        FavoriteSocket.changeContext(this);

        rName = findViewById(R.id.editText_recipeName);
        rSummary = findViewById(R.id.editText_recipeSummary);
        rDescription = findViewById(R.id.editText_recipeDescription);
        rPrepTime = findViewById(R.id.editText_recipePrepTime);
        rCookTime = findViewById(R.id.editText_recipeCookTime);
        rServings = findViewById(R.id.editText_recipeServings);
        rNutrition = findViewById(R.id.editText_recipeNutrition);

        rImage = findViewById(R.id.imageView_RecipeImage);
        addIngredients = findViewById(R.id.textView_recipeIngredients);
        addSteps = findViewById(R.id.textView_recipeSteps);
        updateRecipe = findViewById(R.id.button_createRecipe);
        cancelRecipe = findViewById(R.id.button_cancelRecipe);

        Queue = Volley.newRequestQueue(this);

        getRecipe();

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
                                lData = data.getStringArrayListExtra("result");
                                if(lData.isEmpty()){
                                    recipeIngredientsDisplay = "";
                                    recipeIngredients.clear();
                                }
                                else {
                                    recipeIngredientsDisplay = "";
                                    recipeIngredients.clear();

                                    for(int i = 0; i < lData.size(); i++) {
                                        recipeIngredientsDisplay += lData.get(i) + "\n";
                                    }
                                }
                                recipeIngredientsDisplay.trim();
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
                addIngredients.putExtra("ingredients", lData);
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

        updateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(rName.getText())){
                    rName.setError("All great recipes deserve a name!");
                }
                if(TextUtils.isEmpty(rSummary.getText())){
                    rSummary.setError("Please add in a recipe summary!");
                }
                if(TextUtils.isEmpty(rDescription.getText())){
                    rDescription.setError("Please add in a recipe description!");
                }
                if(TextUtils.isEmpty(rPrepTime.getText())){
                    rPrepTime.setError("Please add in recipe prep time!");
                }
                if(TextUtils.isEmpty(rCookTime.getText())){
                    rCookTime.setError("Please add in recipe cook time!");
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
                if(!TextUtils.isEmpty(rName.getText())
                        && !TextUtils.isEmpty(rSummary.getText())
                        && !TextUtils.isEmpty(rDescription.getText())
                        && !TextUtils.isEmpty(rPrepTime.getText())
                        && !TextUtils.isEmpty(rCookTime.getText())
                        && !TextUtils.isEmpty(rServings.getText())
                        && !TextUtils.isEmpty(addIngredients.getText())
                        && !TextUtils.isEmpty(addSteps.getText())){

                    updateRecipe();
                }
                else{
                    Toast.makeText(getApplicationContext(), "A field is empty", Toast.LENGTH_SHORT).show();
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

    private void getRecipe(){
        JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.GET, URL_RECIPE + recipe_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            rName.setText();
                            rSummary =
                            rDescription =
                            rPrepTime =
                            rCookTime =
                            rServings =
                            rNutrition =

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void updateRecipe(){
        recipeName = rName.getText().toString();
        recipeSummary = rSummary.getText().toString();
        recipePrepTime = Integer.parseInt(rPrepTime.getText().toString());
        recipeCookTime = Integer.parseInt(rCookTime.getText().toString());
        recipeDescription = rDescription.getText().toString();
        recipeNutrition = rNutrition.getText().toString();
        recipeServings = Integer.parseInt(rServings.getText().toString());
        convertArrayTOJSONArray();
        JSONArray steps = new JSONArray(recipeSteps);

        JSONObject recipe = new JSONObject();
        try{
            recipe.put("name", recipeName);
            recipe.put("prep_time", recipePrepTime);
            recipe.put("cook_time", recipeCookTime);
            recipe.put("summary", recipeSummary);
            recipe.put("description", recipeDescription);
            recipe.put("nutrition_facts", recipeNutrition);
            recipe.put("num_servings", recipeServings);
            recipe.put("ingredients", ingredients);
            recipe.put("steps", steps);

        } catch (JSONException e){
            e.printStackTrace();
        }
        System.out.println(recipeName);
        System.out.println(recipePrepTime);
        System.out.println(recipeCookTime);
        System.out.println(recipeSummary);
        System.out.println(recipeDescription);
        System.out.println(recipeNutrition);
        System.out.println(recipeServings);
        for(int i = 0; i < ingredients.length(); i++){
            try {
                System.out.println(ingredients.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(ingredients);
        System.out.println(steps);

        JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.PATCH, URL_RECIPE + recipe_id, recipe,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String success = response.getString("success");
                            String message = response.getString("message");
                            if(success.equals("true")){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Home_Page.class));
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipeEditor_Page.this,
                        "Error with processing your request. Please try again!" + error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        Queue.add(recipeRequest);
    }

    public void convertArrayTOJSONArray(){
        ingredients = new JSONArray();
        try{
            for(int i = 0; i < lData.size(); i++){
                String listString = lData.get(i);
                String[] splitString = listString.split(" ");

                JSONObject list = new JSONObject();
                if(splitString.length > 3){
                    String ingredientNameLong = "";
                    for(int j = 2; j < splitString.length; j++){
                        ingredientNameLong += splitString[j] + " ";
                    }
                    ingredientNameLong.trim();
                    list.put("ingredient_name", ingredientNameLong);
                } else {
                    list.put("ingredient_name", splitString[2]);
                }
                list.put("quantity", Double.parseDouble(splitString[0]));
                list.put("units", splitString[1]);
                ingredients.put(list);
            }
        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}