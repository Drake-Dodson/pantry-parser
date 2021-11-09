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
    private String name;
    @Getter
    @Setter
    private int num;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    @Getter
    @Setter
    private Recipe recipe;

    /**
     * basic constructor for a step
     * @param step text content of the step
     * @param order position it should be in
     * @param recipe recipe this step belongs to
     */
    public Step (String step, int order, Recipe recipe){
        this.name = step;
        this.num = order;
        this.recipe = recipe;
    }

    public Step() {

    }
}
