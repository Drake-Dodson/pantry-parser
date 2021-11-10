package com.example.pantryparserbackend.Recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Column(unique = true)
    @Getter
    @Setter
    private String name;

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    @Getter
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
