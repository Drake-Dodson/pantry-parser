package com.example.pantry_parser;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private Integer recipeID;
    public String recipeName;
    public int timeToMake;
    public String summary;
    public ArrayList<String> ingredients;
    public ArrayList<String> steps;
    public String author;
    public float rating;

    public Recipe(String recipeName, ArrayList<String> ingredients) {
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

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }

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

    public void setRecipeID(Integer recipeID){this.recipeID = recipeID;}

    public Integer getRecipeID(){return recipeID;}
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }
}
