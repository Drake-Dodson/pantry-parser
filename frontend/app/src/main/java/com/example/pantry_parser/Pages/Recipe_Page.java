package com.example.pantry_parser.Pages;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.google.android.material.tabs.TabLayout;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Recipe_Page extends AppCompatActivity {
        private Recipe recipe;
        private TextView NameRecipe;
        private TextView AuthorRecipe;
        private TabLayout tabLayout;
        private ViewPager viewPager;
        private TextView details;
        private TextView steps;
        private TextView ingredients;
        private HorizontalScrollView scrollView;
        private Button favButton;
        private WebSocketClient mWebSocketClient;


    /**
     *Initiazes the recipe elements
     * @param savedInstanceState
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_recipe);
            recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

            NameRecipe = findViewById(R.id.RecipeName);
            NameRecipe.setText(recipe.getRecipeName());
            AuthorRecipe = findViewById(R.id.RecipeAuthor);
            AuthorRecipe.setText(recipe.getAuthor());
            connectWebSocket();
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

            favButton = findViewById(R.id.FavButton);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWebSocketClient.send("favorite:" + recipe.getRecipeID());
                }
            });

        }

    private void connectWebSocket() {
        URI uri;
        try {
            /*
             * To test the clientside without the backend, simply connect to an echo server such as:
             *  "ws://echo.websocket.org"
             */
            uri = new URI("ws://coms-309-032.cs.iastate.edu:8080/websocket/2"); // 10.0.2.2 = localhost
            // uri = new URI("ws://echo.websocket.org");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String msg) {
                Log.i("Websocket", "Message Received");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                Toast.makeText(Recipe_Page.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onClose(int errorCode, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
    @Override
    public void onBackPressed() {
        mWebSocketClient.close();
        super.onBackPressed();
    }

    }
