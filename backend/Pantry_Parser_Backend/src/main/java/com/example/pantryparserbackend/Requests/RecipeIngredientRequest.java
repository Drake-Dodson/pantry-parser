package com.example.pantryparserbackend.Requests;

public class RecipeIngredientRequest {
    public String ingredient_name;
    public double quantity;
    public String units;

    public RecipeIngredientRequest(String ingredient_name, double quantity, String units) {
        this.ingredient_name = ingredient_name;
        this.quantity = quantity;
        this.units = units;
    }
}
