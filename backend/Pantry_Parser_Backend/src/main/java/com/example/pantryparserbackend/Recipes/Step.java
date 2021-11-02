package com.example.pantryparserbackend.Recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter
    @Setter
    private String step;
    @Getter
    @Setter
    private int order;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    @Getter
    @Setter
    private Recipe recipe;

    public Step (String step, int order, Recipe recipe){
        this.step = step;
        this.order = order;
        this.recipe = recipe;
    }

    public Step() {

    }
}
