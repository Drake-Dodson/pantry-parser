package com.example.pantry_parser.Pages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.example.pantry_parser.Pages.RecipeCreator.RecipeEditor_Page;
import com.google.android.material.tabs.TabLayout;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  Recipe_Page extends AppCompatActivity {
        private Recipe recipe;
        private TextView NameRecipe;
        private TextView AuthorRecipe;
        private TextView editRecipe;
        private TabLayout tabLayout;
        private ViewPager viewPager;
        private TextView details;
        private TextView steps;
        private TextView ingredients;
        private HorizontalScrollView scrollView;
        private Button favButton;
        private WebSocketClient mWebSocketClient;
        private boolean hasUserFavorited;
        private RequestQueue queue;


    /**
     *Initiazes the recipe elements
     * @param savedInstanceState
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            FavoriteSocket.changeContext(this);
            queue = Volley.newRequestQueue(this);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_recipe);
            recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
            hasUserFavorited = false;

            String user_id = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("user_id", "");
            String url = "http://coms-309-032.cs.iastate.edu:8080/user/" + user_id + "/favorited/" + recipe.getRecipeID();
            getIsFavorited(url);

            NameRecipe = findViewById(R.id.RecipeName);
            NameRecipe.setText(recipe.getRecipeName());
            AuthorRecipe = findViewById(R.id.RecipeAuthor);
            AuthorRecipe.setText(recipe.getAuthor());
            scrollView = findViewById(R.id.RecipeScrollView);
            tabLayout = findViewById(R.id.tablayout);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                /**
                 *Changes displayed information
                 * @param tab the tab to be selected, details, ingredients, or steps
                 */
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = displayMetrics.widthPixels;
                    switch (tab.getPosition()){
                        case (0):
                            scrollView.post(() -> scrollView.setScrollX(0));
                            break;
                        case (1):
                            scrollView.post(() -> scrollView.setScrollX(width));

                            break;
                        case (2):
                            scrollView.post(() -> scrollView.setScrollX(width * 2));
                            break;
                    }
                }

                /**
                 *
                 * @param tab
                 */
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                /**
                 *
                 * @param tab
                 */
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            details = findViewById(R.id.DetailsText);
            steps = findViewById(R.id.StepsText);
            ingredients = findViewById(R.id.IngredientsText);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;

            details.setWidth(screenWidth);
            ingredients.setWidth(screenWidth);
            steps.setWidth(screenWidth);

            details.setText(recipe.getSummary());


            ArrayList<String> stepsList = recipe.getSteps();
            if(stepsList != null) {
                String stepsConc = "";
                for (int s = 0; s < stepsList.size(); s++) {
                    stepsConc = stepsConc + (s+1) + ") " + stepsList.get(s) + "\r\n";
                }
                steps.setText(stepsConc);
            }

            ArrayList<String> ingredientsList = recipe.getIngredients();
            if(ingredientsList != null) {
                String ingConc = "";
                for (int j = 0; j < ingredientsList.size(); j++) {
                    String ing = ingredientsList.get(j);
                    ingConc = ingConc + "- " + ing.substring(0,1).toUpperCase() + ing.substring(1) + "\r\n";
                }
                ingredients.setText(ingConc);
            }

            editRecipe = findViewById(R.id.EditRecipe);
            editRecipe.setVisibility(View.INVISIBLE);
            String username = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("username", "");
            if(recipe.getAuthor().equals(username)){
                editRecipe.setVisibility(View.VISIBLE);
            }
            editRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RecipeEditor_Page.class);
                    intent.putExtra("recipe_id", recipe.getRecipeID());
                    startActivity(intent);
                }
            });

            favButton = findViewById(R.id.FavButton);
            updateFaveButton();
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FavoriteSocket.mWebSocketClient.isOpen()) {
                        if(!hasUserFavorited) {
                            FavoriteSocket.mWebSocketClient.send("favorite:" + recipe.getRecipeID());
                            hasUserFavorited = true;
                        } else {
                            FavoriteSocket.mWebSocketClient.send("unfavorite:" + recipe.getRecipeID());
                            hasUserFavorited = false;
                        }
                        updateFaveButton();
                    } else {
                        Toast.makeText(Recipe_Page.this, "cannot favorite as a guest", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    private void updateFaveButton() {
        if(hasUserFavorited) {
            favButton.setText("unfavorite");
            favButton.setTextColor(Color.RED);
        } else {
            favButton.setText("favorite");
            favButton.setTextColor(Color.BLACK);
        }
    }
    private void getIsFavorited(String url) {

        JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    hasUserFavorited = response.getString("success").contains("true");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            /**
             *Does not fill recipes if no data is returned from database
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(recipeRequest);
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
