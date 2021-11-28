package com.example.pantryparserbackend.Ingredients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredient> recipes;

    /**
     * basic constructor for a new ingredient
     * @param name
     */
    public Ingredient(String name){
        this.name = name;
        this.nameToLower();
    }
    public Ingredient(){}

    /**
     * sets the name of the ingredient to lowercase letters
     * used for consistency in the database
     */
    public void nameToLower() { this.name = this.name.toLowerCase(); }
}
