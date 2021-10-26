package com.example.pantryparserbackend.Reviews;

import com.example.pantryparserbackend.Util.MessageUtil;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    // How the frontend may implement reviews Pseudo code
    // 1. Check to see that the reviewer hasn't already reviewed said recipe
    //   a. Check to see if the value is null
    // 2. Assuming the review hasn't already been written then pass

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping(path = "user/{user_id}/reviews")
    public List<Review> getReviewsByUser(@PathVariable int user_id) {
        User user = userRepository.findById(user_id);
        return user.getUserReviews();
    }

    @GetMapping(path = "recipe/{recipe_id}/reviews")
    public List<Review> getRecipeReviews(@PathVariable int recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id);
        return recipe.getRecipeReviews();
    }

    @PostMapping(path = "user/{user_id}/recipe/{recipe_id}/review")
    public String writeReview(@PathVariable int user_id, @PathVariable int recipe_id, @RequestBody Review review)
    {
        User reviewer = userRepository.findById(user_id);
        Recipe recipeReviewed = recipeRepository.findById(recipe_id);

        // Verify that the users and recipes exist
        if(reviewer == null) {
            return MessageUtil.newResponseMessage(false, "User Not Found");
        }
        if(recipeReviewed == null) {
            return MessageUtil.newResponseMessage(false, "Recipe Not Found");
        }

        review.setReviewer(reviewer);
        review.setRecipeReviewed(recipeReviewed);

        // Verify that there is a correct start count
        if(review.getStarNumber() > 5 || review.getStarNumber() < 0) {
            return MessageUtil.newResponseMessage(false, "Invalid Star number. Try 0 - 5");
        }

        // Attempt to add the review
        try {
            reviewRepository.save(review);
        }
        catch(Exception ex) {
            // Assuming I set everything up correctly there shouldn't be able to be 2 reviews from the same user
            return MessageUtil.newResponseMessage(false, "User has already reviewed this recipe");
        }

        // Update rating total
        recipeReviewed.updateRating();
        recipeRepository.save(recipeReviewed);

        return MessageUtil.newResponseMessage(true, "Review created");
    }
}
