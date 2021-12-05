package com.example.pantryparserbackend.Recipes;

import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.Users.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {


    List<Review> reviewList;

    User mockUser;

    Recipe recipe;

    void initialization(){
        this.mockUser = new User("Password", "john@email.com");
        this.recipe = new Recipe("John's Burgers", 60, "", "Delicious and Nutrisious");
        recipe.setCreator(mockUser);
        this.reviewList = new ArrayList<Review>();
        User mockReviewer1 = new User("Password", "jim@email.com");
        User mockReviewer2 = new User("Password", "johnathan@email.com");
        User mockReviewer3 = new User("Password", "jay@email.com");
        reviewList.add(new Review(5, "Amazing!", "The best thing I've ever made!", mockReviewer1, recipe));
        reviewList.add(new Review(1, "Terrible!", "The worst thing I've ever made!", mockReviewer2, recipe));
        reviewList.add(new Review(3, "Mediocre!", "The most meh thing I've ever made!", mockReviewer3, recipe));
        recipe.setRecipes_reviews(reviewList);
    }

    @Test
    void testUpdateRating0N() {
        initialization();

        recipe.updateRating0N();

        double expected = 3;
        assertEquals(expected, recipe.getRating());
    }

    @Test
    void testAddRating() {
        initialization();

        User mockReviewer = new User("Password", "james@email.com");
        recipe.updateRating0N();
        reviewList.add(new Review(1, "Not for me", "Idk I just didn't like it", mockReviewer, recipe));

        recipe.setRecipes_reviews(reviewList);
        recipe.addRating(reviewList.get(3).getStarNumber());

        double expected = 2.5;
        assertEquals(expected, recipe.getRating());
    }

    @Test
    void updateRating() {
        initialization();

        recipe.updateRating0N();
        int oldRating = recipe.getRecipeReviews().get(1).getStarNumber();
        //changing the second review from a 1 star to a 4 star
        recipe.getRecipeReviews().get(1).setStarNumber(4);
        recipe.updateRating(oldRating, 4);

        double expected = 4.0;
        assertEquals(expected, recipe.getRating());
    }

    @Test
    void removeRating() {
        initialization();

        recipe.updateRating0N();
        int oldRating = recipe.getRecipeReviews().get(1).getStarNumber();
        //removes the 1 star review
        recipe.getRecipeReviews().remove(1);
        recipe.removeRating(oldRating);

        double expected = 4.0;
        assertEquals(expected, recipe.getRating());
    }
}