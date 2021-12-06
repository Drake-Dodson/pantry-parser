package com.example.pantry_parser.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantry_parser.Network.FavoriteSocket;
import com.example.pantry_parser.Pages.Recipe_Page;
import com.example.pantry_parser.Pages.Settings.Settings_Page;
import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;
import com.example.pantry_parser.Utilities.URLs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListView extends AppCompatActivity implements RecyclerViewAdapter.OnRecipeListener {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Recipe> dataset = new ArrayList<>();
    boolean isLoading = false;
    private static final String URL_RECIPES = "http://coms-309-032.cs.iastate.edu:8080/recipes?pageNo=0";
    private static final String URL_USER = "http://coms-309-032.cs.iastate.edu:8080/user/1/recipes/";
    private static final String URL_FAV = "http://coms-309-032.cs.iastate.edu:8080/user/1/favorites/";
    private static final String URL_PARSER = "http://coms-309-032.cs.iastate.edu:8080/pantry-parser";
    String origURL;
    String URL_TO_USE;
    private RequestQueue queue;
    FloatingActionButton newRecipe;
    SearchView searchView;
    Switch toggle;

    /**
     *Create listview activity and instantiate elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String viewType = (String) getIntent().getSerializableExtra("SwitchView");

        setupRecycler();
        setupAdapter();

        try {
            initializeElements(viewType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FavoriteSocket.changeContext(this);
    }

    /**
     * Initial setup of infinite recycler for recycler view
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
     *Inializes volley request queue and determines the dataset to fill list view with
     * @param viewType View to be initialized
     */
    private void initializeElements(String viewType) throws JSONException{
        queue = Volley.newRequestQueue(this);
        searchView = findViewById(R.id.searchRecipe);
        searchView.setQueryHint("Search by Recipe");
        toggle = findViewById(R.id.toggle);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false){
                    searchView.setQueryHint("Search By Recipe");
                    URL_TO_USE = URLs.switchBaseUrl(URL_TO_USE, origURL);
                    URL_TO_USE = URLs.updatePaginatedQueryUrl(URL_TO_USE, "query", searchView.getQuery().toString());
                }
                else {
                    URL_TO_USE = URL_PARSER;
                    searchView.setQueryHint("Search By Ingredient");
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    dataset.clear();
                    URL_TO_USE = URLs.updatePaginatedQueryUrl(URL_TO_USE, "pageNo", "0");
                    if(URL_TO_USE.contains(URL_PARSER)) {
                        searchByIngredient(query);
                    } else {
                        URL_TO_USE = URLs.updatePaginatedQueryUrl(URL_TO_USE, "query", query);
                        System.out.println(URL_TO_USE);
                        popData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        newRecipe = findViewById(R.id.createRecipe);
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
                origURL = URL_TO_USE;
                popData();
                break;

            case ("MY_RECIPES"):
                URL_TO_USE = URL_USER;
                origURL = URL_TO_USE;
                newRecipe.show();
                popData();
                break;

            case ("FAV_RECIPES"):
                URL_TO_USE = URL_FAV;
                origURL = URL_TO_USE;
                popData();
                break;

            default:
                toggle.setChecked(true);
                searchView.setQuery(viewType, true);
                URL_TO_USE = URL_PARSER;
                break;

        }
    }

    /**
     * Method to get more recipes once the user scrolls to the end of the current view
     */

    private void getMoreData() {
        URL_TO_USE = URLs.getNextPaginatedQueryPageUrl(URL_TO_USE);

        if(URL_TO_USE.contains(URL_PARSER)) {
            try {
                searchByIngredient(searchView.getQuery().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            popData();
        }

        isLoading = false;
    }

    /**
     * Populates list view with recipes from selected database endpoint
     */
    private void popData() {
        JsonObjectRequest recipeRequest = new JsonObjectRequest(Request.Method.GET, URL_TO_USE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int pageSize = 0;
                if (response != null) {
                    int i = 0;
                    JSONArray recipeArray = null;
                    try {
                        recipeArray = response.getJSONArray("content");
                        pageSize = response.getInt("size");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(recipeArray.length() <= 0) {
                        Toast.makeText(ListView.this, "No recipes found!", Toast.LENGTH_SHORT).show();
                    }
                    while (!recipeArray.isNull(i) && i <=pageSize) {
                        try {
                            Recipe recipe = getRecipe(i, recipeArray);
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
             *Does not fill recipes if no data is returned from database
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(recipeRequest);
    }

    @NonNull
    private Recipe getRecipe(int i, JSONArray recipeArray) throws JSONException {
        JSONObject JSONRecipe = recipeArray.getJSONObject(i);
        Recipe recipe = new Recipe(JSONRecipe.getString("name"));
        recipe.setRecipeID(JSONRecipe.getString("id"));
        recipe.setTimeToMake(JSONRecipe.getInt("time"));
        recipe.setSummary(JSONRecipe.getString("summary"));
        recipe.setAuthor(JSONRecipe.getString("creatorName"));
        recipe.setUserId(JSONRecipe.getInt("creatorId"));
        recipe.setChefVerified(JSONRecipe.getBoolean("chef_verified"));
        recipe.setRating((float) JSONRecipe.getDouble("rating"));
        ArrayList<String> ingredients = new ArrayList<>();
        JSONArray jsonIngredients = JSONRecipe.getJSONArray("ingredients");
        for (int j = 0; j< jsonIngredients.length();j++){
            ingredients.add(jsonIngredients.getJSONObject(j).getString("name"));
        }
        recipe.setIngredients(ingredients);

        ArrayList<String> steps = new ArrayList<>();
        JSONArray jsonSteps = JSONRecipe.getJSONArray("steps");
        for (int j = 0; j< jsonSteps.length();j++){
            steps.add(jsonSteps.getJSONObject(j).getString("name"));
        }
        recipe.setSteps(steps);
        return recipe;
    }

    /**
     * Initialize recyclerView adapter
     */
    private void setupAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(dataset, this, "r");
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    /**
     *Opens new recipe view on click and passes recpe object to new activity
     * @param position Position of recipe in view
     */
    @Override
    public void onRecipeClick(int position) {
        dataset.get(position);
        Intent intent = new Intent(this, Recipe_Page.class);
        intent.putExtra("Recipe", dataset.get(position));
        startActivity(intent);
    }

    public void searchByIngredient(String query) throws JSONException {
        dataset.clear();
        recyclerViewAdapter.notifyDataSetChanged();
        String[] arr = query.split(",");
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < arr.length; i++) {
            jsonArray.put(arr[i]);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ingredients", jsonArray);

        JsonObjectRequest recipeByIng = new JsonObjectRequest(Request.Method.PUT, URL_TO_USE, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    int i = 0;
                    JSONArray recipeArray = null;
                    try {
                        recipeArray = response.getJSONArray("content");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    while (!recipeArray.isNull(i) && i < recipeArray.length()) {
                        try {
                            Recipe recipe = getRecipe(i, recipeArray);
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
             *Does not fill recipes if no data is returned from database
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Fail");
            }
        });
        queue.add(recipeByIng);
    }
}