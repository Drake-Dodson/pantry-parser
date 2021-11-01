package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pantry_parser.FragmentDetails;
import com.example.pantry_parser.FragmentIngredients;
import com.example.pantry_parser.FragmentSteps;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.example.pantry_parser.VPAdapter;
import com.google.android.material.tabs.TabLayout;

    public class Recipe_Page extends AppCompatActivity {
        private Recipe recipe;
        private TextView NameRecipe;
        private TextView AuthorRecipe;
        private TabLayout tabLayout;
        private ViewPager viewPager;
        private TextView details;


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

            tabLayout = findViewById(R.id.tablayout);
            viewPager = findViewById(R.id.viewpager);

            tabLayout.setupWithViewPager(viewPager);

            VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            FragmentDetails detailsFrag = new FragmentDetails();
            FragmentIngredients ingFrag = new FragmentIngredients();
            FragmentSteps stepsFrag = new FragmentSteps();

            vpAdapter.addFragment(detailsFrag, "Details");
            vpAdapter.addFragment(ingFrag, "Ingredients");
            vpAdapter.addFragment(stepsFrag, "Steps");
            viewPager.setAdapter(vpAdapter);

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.detailsContainer, detailsFrag)
//                .replace(R.id.ingContainer, ingFrag)
//                .replace(R.id.stepsContainer, stepsFrag)
//                .commit();
        }
    }
