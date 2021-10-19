package com.example.pantry_parser;

import java.util.ArrayList;

public class Recipe {
    public String recipeName;
    public ArrayList<Ingredient> ingredients;
    public int timeToMake;
    private String author;
    private String recipeID;

    public float rating;

    public Recipe(String recipeName, ArrayList<Ingredient> ingredients) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
    }

    public Recipe(String recipeName) { this.recipeName = recipeName; }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) { this.ingredients = ingredients; }

    public int getTimeToMake() {
        return timeToMake;
    }

    public double getRating() { return rating; }

    public void setRating(float rating) { this.rating = rating; }

    public void setTimeToMake(int timeToMake) {
        this.timeToMake = timeToMake;
    }

    public void setAuthor(String author) { this.author = author; }

    public String getAuthor(){return author;}

    public void setRecipeID(String recipeID){this.recipeID = recipeID;}

    public String getRecipeID(){return recipeID;}
}
