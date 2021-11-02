package com.example.pantryparserbackend.Recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    public Ingredient(String name){
        this.name = name;
    }
    public Ingredient(){}

    public void nameToLower() { this.name = this.name.toLowerCase(); }
}
