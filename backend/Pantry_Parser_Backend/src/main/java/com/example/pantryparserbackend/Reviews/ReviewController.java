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

    @GetMapping(path = "/reviews")
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
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
        if(recipeReviewed.getCreatorId() == user_id) {
            return MessageUtil.newResponseMessage(false, "Users can't review their own recipes");
        }

        review.setReviewer(reviewer);
        review.setRecipeReviewed(recipeReviewed);

        // Verify that there is a correct start count
        if(review.getStarNumber() > 5 || review.getStarNumber() < 0) {
            return MessageUtil.newResponseMessage(false, "Invalid Star number. Try 0 - 5");
        }

        if(recipeReviewed.getRecipeReviews() != null) {
            for(Review reviewCheck : recipeReviewed.getRecipeReviews()) {
                if(reviewCheck.getUserId() == user_id){
                    return MessageUtil.newResponseMessage(false, "User has already reviewed this recipe");
                }
            }
        }

        // Attempt to add the review
        try {
            reviewRepository.save(review);
        }
        catch(Exception ex) {
            return MessageUtil.newResponseMessage(false, "Error saving to the repository");
        }

        // Update rating total
        recipeReviewed.updateRating();
        recipeRepository.save(recipeReviewed);

        return MessageUtil.newResponseMessage(true, "Review created");
    }

    @PatchMapping(path = "review/{review_id}")
    public String updateReview(@PathVariable int review_id, @RequestBody Review reviewChanges)
    {
        Review review = reviewRepository.findById(review_id);

        if(review == null) {
            return MessageUtil.newResponseMessage(false, "Review not found");
        }

        // This might be redundant. I'm not sure
        review.setStarNumber(reviewChanges.getStarNumber());
        review.setTitle(reviewChanges.getTitle());
        review.setReviewBody(reviewChanges.getReviewBody());

        // Attempt to add the review
        try {
            reviewRepository.save(review);
        }
        catch(Exception ex) {
            return MessageUtil.newResponseMessage(false, "Error saving to the repository");
        }

        return MessageUtil.newResponseMessage(true, "Review updated");
    }
}
