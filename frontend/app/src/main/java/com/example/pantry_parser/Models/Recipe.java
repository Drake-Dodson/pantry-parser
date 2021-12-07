package com.example.pantry_parser.Models;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String recipeID;
    private String recipeName;
    private int timeToMake;
    private String summary;
    private ArrayList<String> ingredients;
    private ArrayList<String> steps;
    private String author;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private String imagePath;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    private int UserId;
    private float rating;
    private ImageView image;

    public Boolean getChefVerified() {
        return chefVerified;
    }

    public void setChefVerified(Boolean chefVerified) {
        this.chefVerified = chefVerified;
    }

    private Boolean chefVerified;

    /**
     *
     * @param recipeName Name of recipe
     * @param ingredients Recipe ingredients
     */
    public Recipe(String recipeName, ArrayList<String> ingredients) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
    }

    /**
     *
     * @return Returns recipe image
     */
    public ImageView getImage() {
        return image;
    }

    /**
     *
     * @param image Sets recipe image
     */
    public void setImage(ImageView image) {
        this.image = image;
    }

    /**
     *
     * @param recipeName name of recipe
     */
    public Recipe(String recipeName) { this.recipeName = recipeName; }

    /**
     *
     * @return Returns recipe name
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     *
     * @param recipeName Sets recipe name
     */
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    /**
     *
     * @return Returns recipe's list of ingredients
     */
    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    /**
     *
     * @param ingredients Sets recipe ingredients
     */
    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }

    /**
     *
     * @return Returns time to make recipe
     */
    public int getTimeToMake() {
        return timeToMake;
    }

    /**
     *
     * @return Returns recipe rating
     */
    public double getRating() { return rating; }

    /**
     *
     * @param rating Sets recipe rating
     */
    public void setRating(float rating) { this.rating = rating; }

    /**
     *
     * @param timeToMake Sets recipe cook time
     */
    public void setTimeToMake(int timeToMake) {
        this.timeToMake = timeToMake;
    }

    /**
     *
     * @param author Sets recipe author
     */
    public void setAuthor(String author) { this.author = author; }

    /**
     *
     * @return Returns recipe's authoer
     */
    public String getAuthor(){return author;}

    /**
     *
     * @param recipeID Sets recipe ID
     */
    public void setRecipeID(String recipeID){this.recipeID = recipeID;}

    /**
     *
     * @return Returns recipe's ID
     */
    public String getRecipeID(){return recipeID;}

    /**
     *
     * @return Returns recipe's summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary Sets recipe's summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     *
     * @return Returns recipe's steps
     */
    public ArrayList<String> getSteps() {
        return steps;
    }

    /**
     *
     * @param steps Sets recipe's steps
     */
    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }
}
