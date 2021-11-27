package com.example.pantryparserbackend.Reviews;

import com.example.pantryparserbackend.Util.MessageUtil;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Review Controller", description = "Contains all of the calls for writing reviews")
@RestController
public class ReviewController {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    /**
     * returns all the reviews the provided user has written
     * @param user_id id of the user
     * @return list of reviews
     */
    @ApiOperation(value = "Gets all of the recipes a user has created")
    @GetMapping(path = "/user/{user_id}/reviews")
    public Page<Review> getReviewsByUser(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @PathVariable int user_id) {
        User user = userRepository.findById(user_id);
        if(user == null) {
            return null;
        }
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("starNumber"));
        return reviewRepository.findByUser(user, page);
    }

    /**
     * returns all the reviews the provided recipe has received
     * @param recipe_id the id of the recipe
     * @return list of reviews
     */
    @ApiOperation(value = "Gets all of the reviews on a recipe")
    @GetMapping(path = "/recipe/{recipe_id}/reviews")
    public Page<Review> getRecipeReviews(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @PathVariable int recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null) {
            return null;
        }
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("starNumber"));
        return reviewRepository.fingByRecipe(recipe, page);
    }

    /**
     * gets all reviews in the database
     * @return list of reviews
     */
    @ApiOperation(value = "Gets all reviews in the database")
    @GetMapping(path = "/reviews")
    public Page<Review> getAllReviews(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage) {
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("starNumber"));
        return reviewRepository.findAll(page);
    }

    /**
     * writes a review as the provided user on the provided recipe
     * @param user_id id of user
     * @param recipe_id id of recipe
     * @param review review contents
     * @return either success or a failure message
     */
    @ApiOperation(value = "Creates a review object by a given user on a recipe")
    @PostMapping(path = "/user/{user_id}/recipe/{recipe_id}/review")
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
        recipeReviewed.addRating(review.getStarNumber());
        recipeRepository.save(recipeReviewed);

        return MessageUtil.newResponseMessage(true, "Review created");
    }

    /**
     * updates the values of a review
     * @param review_id id of review
     * @param reviewChanges new values for review
     * @return either success or a failure message
     */
    @ApiOperation(value = "Updates a given review")
    @PatchMapping(path = "/review/{review_id}")
    public String updateReview(@PathVariable int review_id, @RequestBody Review reviewChanges)
    {
        Review review = reviewRepository.findById(review_id);

        if(review == null) {
            return MessageUtil.newResponseMessage(false, "Review not found");
        }

        int oldRating = review.getStarNumber();
        int newRating = reviewChanges.getStarNumber();
        Recipe recipeStore = review.getRecipe_reviewed();

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

        review.getRecipe_reviewed().updateRating(oldRating, newRating);
        //review.getRecipe_reviewed().updateRating0N();
        recipeRepository.save(recipeStore);

        return MessageUtil.newResponseMessage(true, "Review updated");
    }

    /**
     * deletes a review
     * @param review_id id of review to delete
     * @return either success or a failure message
     */
    @ApiOperation(value = "Deletes a given review")
    @DeleteMapping(path = "/review/{review_id}")
    public String deleteReview(@PathVariable int review_id)
    {
        Review review = reviewRepository.findById(review_id);

        if(review == null) {
            return MessageUtil.newResponseMessage(false, "Review not found");
        }

        Recipe recipeStore = review.getRecipe_reviewed();
        recipeStore.getRecipeReviews().remove(review);
        int oldStars = review.getStarNumber();
        // Attempt to remove the review
        try {
            reviewRepository.delete(review);
        }
        catch(Exception ex) {
            return MessageUtil.newResponseMessage(false, "Error deleting entry from the repository");
        }

        recipeStore.removeRating(oldStars);
        //recipeStore.updateRating0N();
        recipeRepository.save(recipeStore);

        return MessageUtil.newResponseMessage(true, "Review deleted");
    }
}
