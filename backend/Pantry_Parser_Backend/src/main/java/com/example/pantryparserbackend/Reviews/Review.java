package com.example.pantryparserbackend.Reviews;


import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String reviewBody;

    @Getter
    @Setter
    private int starNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User reviewer;

    @Getter
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
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

    public int getUserId(){
        return reviewer.getId();
    }

    public int getRecipeId() { return recipe_reviewed.getId(); }
}
