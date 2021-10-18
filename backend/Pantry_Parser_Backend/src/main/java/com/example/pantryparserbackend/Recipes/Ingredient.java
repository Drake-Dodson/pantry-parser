package com.example.pantryparserbackend.Recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    @JsonIgnore
    private List<Recipe> recipes;

    public Ingredient(String name){
        this.name = name;
    }
    public Ingredient(){}

    public String getName(){
        return this.name;
    }
    public void nameToLower() { this.name = this.name.toLowerCase(); }
    public List<Recipe> getRecipes(){
        return this.recipes;
    }
}
