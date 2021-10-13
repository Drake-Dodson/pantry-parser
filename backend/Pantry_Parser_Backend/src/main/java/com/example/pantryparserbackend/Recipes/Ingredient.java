package com.example.pantryparserbackend.Recipes;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;

    @ManyToMany
    private Set<Recipe> recipes;

    public Ingredient(String name){
        this.name = name;
    }
    public Ingredient(){}

    public String getName(){
        return this.name;
    }
    public void nameToLower() { this.name = this.name.toLowerCase(); }
}
