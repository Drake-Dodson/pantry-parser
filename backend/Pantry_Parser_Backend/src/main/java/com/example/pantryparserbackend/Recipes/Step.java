package com.example.pantryparserbackend.Recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * basic class for steps on a recipe
 */
@Entity
@Table(name = "steps")
public class Step {
    @Id
    @Getter
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false)
    private String name;
    @Getter
    @Setter
    @Column(nullable = false)
    private int num;

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recipe_id", nullable = false)
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
