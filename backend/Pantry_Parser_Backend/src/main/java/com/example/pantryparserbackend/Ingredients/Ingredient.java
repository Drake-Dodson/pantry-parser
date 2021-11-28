package com.example.pantryparserbackend.Ingredients;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * The basic entity for ingredients
 */
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @Getter
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @Getter
    @JsonIgnore
    @ManyToMany(mappedBy = "ingredients")
    private List<Recipe> recipes;

    /**
     * basic constructor for a new ingredient
     * @param name
     */
    public Ingredient(String name){
        this.name = name;
    }
    public Ingredient(){}

    /**
     * sets the name of the ingredient to lowercase letters
     * used for consistency in the database
     */
    public void nameToLower() { this.name = this.name.toLowerCase(); }
}
