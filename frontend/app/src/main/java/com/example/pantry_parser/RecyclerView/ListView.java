package com.example.pantry_parser.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.Pages.Recipe_Page;
import com.example.pantry_parser.Pages.Settings_Page;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ListView extends AppCompatActivity implements RecyclerViewAdapter.OnRecipeListener {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Recipe> dataset = new ArrayList<>();
    boolean isLoading = false;
    private static final String URL_RECIPES = "http://coms-309-032.cs.iastate.edu:8080/recipes/";
    private static final String URL_USER = "http://coms-309-032.cs.iastate.edu:8080/user/1/recipes/";
    private static final String URL_FAV = "http://coms-309-032.cs.iastate.edu:8080/user/1/favorites/";
    String URL_TO_USE;
    private RequestQueue queue;
    FloatingActionButton newRecipe;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String viewType = (String) getIntent().getSerializableExtra("SwitchView");

        initializeElements(viewType);
        setupRecycler();
        popData();
        setupAdapter();
    }

    /**
     * Initial setup of recycler for recycler view
     */
    private void setupRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == dataset.size() - 1) {
                        isLoading = true;
                        getMoreData();
                    }
                }
            }
        });
    }

    /**
     *
     * @param viewType View to be initialized
     */
    private void initializeElements(String viewType) {
        queue = Volley.newRequestQueue(this);
        newRecipe = findViewById(R.id.addRecipeButton);
        newRecipe.setOnClickListener(new View.OnClickListener() {

            /**
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings_Page.class);
            }
        });
        newRecipe.hide();

        switch (viewType){
            case ("ALL_RECIPES"):
                URL_TO_USE = URL_RECIPES;
                break;

            case ("MY_RECIPES"):
            URL_TO_USE = URL_USER;
                newRecipe.show();
                break;

            case ("FAV_RECIPES"):
                URL_TO_USE = URL_FAV;
                break;
        }
    }

    /**
     * If end of data set is reached in view, then more items are created and inserted into view
     */
    private void getMoreData() {
        if (URL_TO_USE == URL_RECIPES) {
            dataset.add(null);
            recyclerViewAdapter.notifyItemInserted(dataset.size() - 1);
            dataset.remove(dataset.size() - 1);
            int currentSize = dataset.size();
            int nextSize = currentSize + 10;
            while (currentSize < nextSize) {
                Recipe recipe = new Recipe("Recipe " + currentSize);
                recipe.setTimeToMake(currentSize);
                recipe.setRating((float) currentSize / 5);
                dataset.add(recipe);
                currentSize++;
            }
            recyclerViewAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    }

    /**
     * Populates items in view with recipe data from server
     */
    private void popData() {
        JsonArrayRequest recipeRequest = new JsonArrayRequest(Request.Method.GET, URL_TO_USE, null, new Response.Listener<JSONArray>() {
            /**
             *
             * @param response
             */
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int i = 0;
                    while (!response.isNull(i) && i <=30) {
                        try {
                            Recipe recipe = new Recipe(response.getJSONObject(i).getString("name"));
                            recipe.setRecipeID(response.getJSONObject(i).getInt("id"));
                            recipe.setTimeToMake(response.getJSONObject(i).getInt("time"));
                            recipe.setSummary(response.getJSONObject(i).getString("summary"));
                            recipe.setAuthor(response.getJSONObject(i).getString("creatorName"));
                            recipe.setRating((float) response.getJSONObject(i).getDouble("rating"));

                            ArrayList<String> ingredients = new ArrayList<>();
                            JSONArray jsonIngredients = response.getJSONObject(i).getJSONArray("ingredients");
                            for (int j = 0; j< jsonIngredients.length();j++){
                                ingredients.add(jsonIngredients.getJSONObject(j).getString("name"));
                            }
                            recipe.setIngredients(ingredients);

                            ArrayList<String> steps = new ArrayList<>();
                            JSONArray jsonSteps = response.getJSONObject(i).getJSONArray("steps");
                            for (int j = 0; j< jsonSteps.length();j++){
                                steps.add(jsonSteps.getJSONObject(j).getString("name"));
                            }
                            recipe.setSteps(steps);

                            dataset.add(recipe);
                            recyclerViewAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(ListView.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        i++;
                    }

                }
            }
        }, new Response.ErrorListener() {
            /**
             *
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(recipeRequest);
    }

    /**
     * Constructor for adapter
     */
    private void setupAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(dataset, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    /**
     *
     * @param position Position of recipe in view
     */
    @Override
    public void onRecipeClick(int position) {
        dataset.get(position);
        Intent intent = new Intent(this, Recipe_Page.class);
        intent.putExtra("Recipe", dataset.get(position));
        startActivity(intent);
    }
}