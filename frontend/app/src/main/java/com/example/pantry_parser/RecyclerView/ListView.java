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
import com.example.pantry_parser.View_Recipe;

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
        popData();
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
                        getMoreData();
                    }
                }
            }
        });
    }

    private void getMoreData() {
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

    private void popData() {
        JsonArrayRequest recipeRequest = new JsonArrayRequest(Request.Method.GET, URL_RECIPES, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int i = 1;
                    while (!response.isNull(i) && i <=30) {
                        try {
                            Recipe recipe = new Recipe(response.getJSONObject(i).getString("name"));
                            recipe.setRecipeID(response.getJSONObject(i).getInt("id"));
                            recipe.setRating((float) response.getJSONObject(i).getDouble("rating"));
                            recipe.setTimeToMake(response.getJSONObject(i).getInt("time"));
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
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(recipeRequest);
    }


    private void setupAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(dataset, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onRecipeClick(int position) {
        dataset.get(position);
        Intent intent = new Intent(this, View_Recipe.class);
        intent.putExtra("Recipe", dataset.get(position).getRecipeID());
        intent.putExtra("RecipeName", dataset.get(position).getRecipeName());
        startActivity(intent);
    }
}