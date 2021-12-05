package com.example.pantryparserbackend.Reviews;


import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * a basic model for reviews
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Getter
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String reviewBody;

    @Getter
    @Setter
    @Column(nullable = false)
    private int starNumber;

    @Getter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User reviewer;

    @Getter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe_reviewed;

    public Review(int starNumber, String title, String reviewBody, User reviewer, Recipe recipe_reviewed) {
        this.title = title;
        this.reviewBody = reviewBody;
        this.starNumber = starNumber;
        this.recipe_reviewed = recipe_reviewed;
        this.reviewer = reviewer;
    }

    public Review() {
        starNumber = 0;
    }

    public void setReviewer(User reviewer){
        this.reviewer = reviewer;
    }

    public void setRecipeReviewed(Recipe recipeReviewed){ this.recipe_reviewed = recipeReviewed; }

    /**
     * returns id of the user who wrote this review
     * @return id
     */
    public int getUserId(){
        return reviewer.getId();
    }

    /**
     * returns the id of the recipe being reviewed
     * @return recipe id
     */
    public int getRecipeId() { return recipe_reviewed.getId(); }
}
