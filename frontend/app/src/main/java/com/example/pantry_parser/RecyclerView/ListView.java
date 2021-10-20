package com.example.pantry_parser.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.Pages.RecipeView;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Queue;

public class ListView extends AppCompatActivity implements RecyclerViewAdapter.OnRecipeListener {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Recipe> dataset = new ArrayList<>();
    boolean isLoading = false;
    private static final String URL_RECIPES = "http://coms-309-032.cs.iastate.edu:8080/recipes/";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerView);
        populateData();
        setupAdapter();
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
                        //getMoreData();
                    }
                }
            }
        });
    }

//    private void getMoreData() {
//        dataset.add(null);
//        recyclerViewAdapter.notifyItemInserted(dataset.size() - 1);
//        dataset.remove(dataset.size() - 1);
//        int currentSize = dataset.size();
//        int nextSize = currentSize + 10;
//        while (currentSize < nextSize) {
//            Recipe recipe = new Recipe("Recipe " + currentSize);
//            recipe.setTimeToMake(currentSize);
//            recipe.setRating((float) currentSize / 5);
//            dataset.add(recipe);
//            currentSize++;
//        }
//        recyclerViewAdapter.notifyDataSetChanged();
//        isLoading = false;
//    }

    private void populateData() {
        int i = 1;
        while (i <= 6) {
            JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.GET, URL_RECIPES + i, null,
                    response -> {
                        if (response != null) {
                            try {
                                Recipe recipe = new Recipe(response.getString("name"));
                                recipe.setRecipeID(response.getInt("id"));
                                recipe.setRating((float) response.getDouble("rating"));
                                recipe.setTimeToMake(response.getInt("time"));
                                dataset.add(recipe);
                                recyclerViewAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Toast.makeText(ListView.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    error -> Toast.makeText(ListView.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show());
            i++;
            queue.add(recipeRequest);
        }
    }


    private void setupAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(dataset, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onRecipeClick(int position) {
        dataset.get(position);
        Intent intent = new Intent(this, RecipeView.class);
        intent.putExtra("RecipeID", dataset.get(position).getRecipeID());
        intent.putExtra("RecipeName", dataset.get(position).getRecipeName());
        startActivity(intent);
    }
}