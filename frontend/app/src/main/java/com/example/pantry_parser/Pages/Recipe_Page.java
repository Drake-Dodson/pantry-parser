package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.example.pantry_parser.RecyclerView.RecyclerViewAdapter;
import com.example.pantry_parser.VPAdapter;
import com.google.android.material.tabs.TabLayout;

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





        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_recipe);
            Recipe recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

            TextView NameRecipe = findViewById(R.id.RecipeName);
            NameRecipe.setText(recipe.getRecipeName());
            TextView AuthorRecipe = findViewById(R.id.RecipeAuthor);
            AuthorRecipe.setText(recipe.getAuthor());


            //Setting Recipe Params
            recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

            NameRecipe = findViewById(R.id.RecipeName);
            NameRecipe.setText(recipe.getRecipeName());

            AuthorRecipe = findViewById(R.id.RecipeAuthor);
            AuthorRecipe.setText(recipe.getAuthor());
            //Setting Recipe Params

            scrollView = findViewById(R.id.RecipeScrollView);
            tabLayout = findViewById(R.id.tablayout);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()){
                        case (0):
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.setScrollX(0);
                                }
                            });
                            break;
                        case (1):
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.setScrollX(1440);
                                }
                            });
                            break;
                        case (2):
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.setScrollX(1440*2);
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

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
                String stepsConc = null;
                for (int s = 0; s < stepsList.size(); s++) {
                    stepsConc = stepsConc + s + ") " + stepsList.get(s) + "\r\n";
                }
                steps.setText(stepsConc);
            }

            ArrayList<String> ingredientsList = recipe.getIngredients();
            if(ingredientsList != null) {
                String ingConc = "";
                for (int j = 0; j < ingredientsList.size(); j++) {
                    ingConc = ingConc + (j+1) + ") " + ingredientsList.get(j) + "\r\n";
                }
                ingredients.setText(ingConc);
            }
        }



    }
