package com.example.pantry_parser.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

public class RecipeView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        TextView slideTitle = findViewById(R.id.slideTitle);

        String recipeName;
        String recipeId;
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                recipeName = null;
                recipeId = null;
            } else {
                recipeName = extras.getString("RecipeName");
                recipeId = extras.getString("RecipeID");
            }
        } else {
            recipeName = (String) savedInstanceState.getSerializable("RecipeName");
            recipeId = (String) savedInstanceState.getSerializable("RecipeID");
        }

        slideTitle.setText(recipeName);

        SlidingUpPanelLayout layout = findViewById(R.id.slidingUp);

        layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                findViewById(R.id.slideTitle).setAlpha(1-slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                Toast.makeText(RecipeView.this, "Fuck", Toast.LENGTH_SHORT);
            }
            else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                Toast.makeText(RecipeView.this, "Off", Toast.LENGTH_SHORT);
            }
            }
        });
    }
}