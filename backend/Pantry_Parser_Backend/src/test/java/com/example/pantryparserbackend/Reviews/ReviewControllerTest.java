package com.example.pantryparserbackend.Reviews;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @InjectMocks
    ReviewController reviewController;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private Recipe mockRecipe;

    @Mock
    private Review mockReview;

    @Mock
    private List<Review> mockReviewList;

    @Test
    public void testWriteReview_whenGivenReview_thenReturnSuccess(){
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 2;
        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        Review mockReview = new Review(5, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);
        mockReview.setReviewer(mockReviewer);
        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(true, "Review created");
        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
        Mockito.verify(reviewRepository).save(mockReview);
        Mockito.verify(mockRecipe).addRating(5);
        Mockito.verify(recipeRepository).save(mockRecipe);
    }

    @Test
    public void testWriteReview_whenUserTriesToReviewTheirOwnRecipe_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);

        int user_id = 0;
        int recipe_id = 2;
        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        Review mockReview = new Review(5, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);
        List<Review> mockReviewList = new ArrayList<Review>();
        mockReviewList.add(mockReview);
        mockReview.setReviewer(mockReviewer);

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);

        String expected =  MessageUtil.newResponseMessage(false, "Users can't review their own recipes");
        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteReview_whenUserHasAlreadyReviewedARecipe_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 2;
        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        mockReview.setReviewer(mockReviewer);
        mockReviewList.add(new Review(5, "great", "pie", mockReviewer, mockRecipe));
        List<Review> mockReviewList = new ArrayList<>();
        mockReviewList.add(mockReview);

        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(mockReview.getUserId()).thenReturn(1);
        when(mockRecipe.getCreatorId()).thenReturn(2);

        String expected = MessageUtil.newResponseMessage(false, "User has already reviewed this recipe");
        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteReview_whenInvalidUser_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 2;
        User mockReviewer = new User("Password1", "jim@somemail.com");
        mockReview.setReviewer(mockReviewer);
        mockReviewList.add(new Review(5, "great", "pie", mockReviewer, mockRecipe));
        List<Review> mockReviewList = new ArrayList<>();
        mockReviewList.add(mockReview);

        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(userRepository.findById(user_id)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(mockReview.getUserId()).thenReturn(1);
        when(mockRecipe.getCreatorId()).thenReturn(2);

        String expected = MessageUtil.newResponseMessage(false, "User Not Found");
        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteReview_whenInvalidRecipe_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 2;
        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        mockReview.setReviewer(mockReviewer);
        mockReviewList.add(new Review(5, "great", "pie", mockReviewer, mockRecipe));
        List<Review> mockReviewList = new ArrayList<>();
        mockReviewList.add(mockReview);

        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(null);
        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(mockReview.getUserId()).thenReturn(1);
        when(mockRecipe.getCreatorId()).thenReturn(2);

        String expected = MessageUtil.newResponseMessage(false, "Recipe Not Found");
        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteReview_whenInvalidStars_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 2;
        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        mockReview.setReviewer(mockReviewer);
        mockReviewList.add(new Review(8, "great", "pie", mockReviewer, mockRecipe));
        List<Review> mockReviewList = new ArrayList<>();

        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);
        when(mockReview.getUserId()).thenReturn(1);
        when(mockReview.getStarNumber()).thenReturn(8);
        when(mockRecipe.getCreatorId()).thenReturn(2);

        String expected = MessageUtil.newResponseMessage(false, "Invalid Star number. Try 0 - 5");
        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateReview_whenUserUpdatesReview_thenReturnSuccess(){
        MockitoAnnotations.openMocks(this);

        int review_id = 3;
        int initial_stars = 5;
        int new_stars = 4;
        User mockUser = new User("Password", "john@somemail.com");
        Review mockReview1 = new Review(initial_stars, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);
        Review mockUpdateReview = new Review(new_stars, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);

        when(reviewRepository.findById(review_id)).thenReturn(mockReview1);

        String expected = MessageUtil.newResponseMessage(true, "Review updated");
        String actual = reviewController.updateReview(review_id, mockUpdateReview);

        assertEquals(expected, actual);
        assertEquals(4, mockReview1.getStarNumber());
        Mockito.verify(reviewRepository).save(mockReview1);
        Mockito.verify(mockRecipe).updateRating(initial_stars, new_stars);
        Mockito.verify(recipeRepository).save(mockRecipe);
    }

    @Test
    public void testUpdateReview_whenNotReview_thenReturnFail(){
        MockitoAnnotations.openMocks(this);

        int review_id = 3;
        int new_stars = 4;
        User mockUser = new User("Password", "john@somemail.com");
        Review mockUpdateReview = new Review(new_stars, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);

        when(reviewRepository.findById(review_id)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "Review not found");
        String actual = reviewController.updateReview(review_id, mockUpdateReview);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteReview_whenUserDeletesRecipe_thenReturnSuccess(){
        MockitoAnnotations.openMocks(this);

        int review_id = 3;
        User mockUser = new User("Password", "john@somemail.com");
        Review mockReview1 = new Review(5, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);

        when(reviewRepository.findById(review_id)).thenReturn(mockReview1);

        String expected = MessageUtil.newResponseMessage(true, "Review deleted");
        String actual = reviewController.deleteReview(review_id);

        assertEquals(expected, actual);
        Mockito.verify(reviewRepository).delete(mockReview1);
        Mockito.verify(mockRecipe).removeRating(mockReview1.getStarNumber());
        Mockito.verify(recipeRepository).save(mockRecipe);
    }

    @Test
    public void testDeleteReview_whenNotARecipe_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);

        int review_id = 3;

        when(reviewRepository.findById(review_id)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "Review not found");
        String actual = reviewController.deleteReview(review_id);

        assertEquals(expected, actual);
    }
}