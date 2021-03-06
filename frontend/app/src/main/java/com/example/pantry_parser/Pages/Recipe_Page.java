package com.example.pantry_parser.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.example.pantry_parser.Network.RequestListener;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Models.Recipe;
import com.example.pantry_parser.Pages.RecipeCreator.RecipeEditor_Page;
import com.example.pantry_parser.RecyclerView.ListView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  Recipe_Page extends AppCompatActivity {
        private static final String URL_USER = "http://coms-309-032.cs.iastate.edu:8080/user/";
        String userId;
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
        private ImageView recipePic;
        private boolean hasUserFavorited;
        private RequestQueue queue;
        private RatingBar recipeRating;
        private String role;
        private SharedPreferences chef;
        private String imageUrl;
        private TextView chefVerified;

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
            imageUrl = (String) getIntent().getSerializableExtra("ImageURL");
            recipePic = findViewById(R.id.RecipeImage);
            Picasso.get().load(recipe.getImageUrl()).centerCrop().resize(670, 365).into(recipePic);

            hasUserFavorited = false;

            role = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("role", "");
            String user_id = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("user_id", "");
            if(user_id != "") {
                String url = "http://coms-309-032.cs.iastate.edu:8080/user/" + user_id + "/favorited/" + recipe.getRecipeID();
                getIsFavorited(url);
            }

            NameRecipe = findViewById(R.id.RecipeName);
            chefVerified = findViewById(R.id.ChefVerified);
            updateChefIcon();


            NameRecipe.setText(recipe.getRecipeName());
            recipeRating = findViewById(R.id.Reciperating);
            recipeRating.setRating((float)recipe.getRating());
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
                    if(FavoriteSocket.mWebSocketClient != null && FavoriteSocket.mWebSocketClient.isOpen()) {
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


        chefVerified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(role.equals("admin") || role.equals("chef")){
                        verify(recipe.getRecipeID(), recipe.getChefVerified());
                    } else {
                        if(recipe.getChefVerified()) {
                            Toast.makeText(Recipe_Page.this, "This recipe has been verified by a chef to be of quality!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Recipe_Page.this, "This recipe is from a user of the app", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
        setupRating(userId);
        }

    private void setupRating(String user_id) {
        recipeRating = findViewById(R.id.Reciperating);
        recipeRating.setRating((float) recipe.getRating());
        recipeRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(user_id == "") {
                    Toast.makeText(Recipe_Page.this, "You can't rate as a guest", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject JSONrating = new JSONObject();
                    try {
                        JSONrating.put("title", "");
                        JSONrating.put("reviewBody", "");
                        JSONrating.put("starNumber", rating);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest ratingRequest = new JsonObjectRequest(Request.Method.POST, URL_USER + user_id + "/recipe/" + recipe.getRecipeID() + "/review", JSONrating, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String success = response.getString("success");
                                String message = response.getString("message");
                                if (success.equals("true")) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    queue.add(ratingRequest);

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

    public void updateChefIcon() {
        if(recipe.getChefVerified()) {
            chefVerified.setBackgroundResource(R.drawable.profile);
        } else {
            chefVerified.setBackgroundResource(R.drawable.ic_person);
        }
    }

    public void verify(String recipeId, Boolean verified){
        String urlVerified;
        if (verified == true){
            urlVerified = "unverify";
        }
       else{
           urlVerified = "verify";
        }
        JsonObjectRequest verifyRequest = new JsonObjectRequest(Request.Method.GET, "http://coms-309-032.cs.iastate.edu:8080/recipe/" + recipeId + "/" + urlVerified, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                   String success = response.getString("success");
                   String message = response.getString("message");

                   if(success.equals("true")){
                       Toast.makeText(Recipe_Page.this, message, Toast.LENGTH_LONG).show();
                       recipe.setChefVerified(!verified);
                       updateChefIcon();
                   }
                   else{
                       Toast.makeText(Recipe_Page.this, message, Toast.LENGTH_LONG).show();
                   }
                }
              catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(verifyRequest);

    }
}
