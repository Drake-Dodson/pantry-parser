package com.example.pantryparserbackend.Ingredients;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Requests.RecipeIngredientRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id
    @Getter
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Getter
    @Setter
    private double quantity;

    @Getter
    @Setter
    private String units;

    public RecipeIngredient (Ingredient i, Recipe r, double quantity, String units) {
        this.ingredient = i;
        this.recipe = r;
        this.quantity = quantity;
        this.units = units;
    }

    public RecipeIngredient() {}

    public String getName() {
        return this.ingredient.getName();
    }

    public void update(RecipeIngredientRequest ri) {
        this.quantity = ri.quantity;
        this.units = ri.units;
    }
}
