package com.example.pantry_parser.Pages;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
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


    /**
     *Initiazes the recipe elements
     * @param savedInstanceState
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_recipe);
            Recipe recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

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
                    switch (tab.getPosition()){
                        case (0):
                            scrollView.post(() -> scrollView.setScrollX(0));
                            break;
                        case (1):
                            scrollView.post(() -> scrollView.setScrollX(1440));
                            break;
                        case (2):
                            scrollView.post(() -> scrollView.setScrollX(1440*2));
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
        }



    }
