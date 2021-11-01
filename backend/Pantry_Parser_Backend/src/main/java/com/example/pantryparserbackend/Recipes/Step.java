package com.example.pantryparserbackend.Recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String step;
    private int order;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    public Step (String step, int order, Recipe recipe){
        this.step = step;
        this.order = order;
        this.recipe = recipe;
    }

    public Step() {

    }

    public int getId() { return this.id; }
    public String getStep(){
        return this.step;
    }
    public int getOrder() {
        return this.order;
    }
    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setStep(String step) {
        this.step = step;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
