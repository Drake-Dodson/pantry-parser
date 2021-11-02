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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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

    @Test
    public void testWriteReview_whenGivenReview_thenReturnSuccess(){
        MockitoAnnotations.openMocks(this);
        int user_id = 1;
        int recipe_id = 2;
        String expected = "{\"success\":\"true\", \"message\":\"Review created\"}";

        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        Review mockReview = new Review(5, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);

        mockReview.setReviewer(mockReviewer);

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(null);

        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
        Mockito.verify(reviewRepository).save(mockReview);
    }

    @Test
    public void testWriteReview_whenUserTriesToReviewTheirOwnRecipe_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);
        int user_id = 0;
        int recipe_id = 2;
        String expected = "{\"success\":\"false\", \"message\":\"Users can't review their own recipes\"}";

        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        Review mockReview = new Review(5, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);
        List<Review> mockReviewList = new ArrayList<Review>();
        mockReviewList.add(mockReview);
        mockReview.setReviewer(mockReviewer);

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);

        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteReview_whenUserHasAlreadyReviewedARecipe_thenReturnFailure(){
        MockitoAnnotations.openMocks(this);
        int user_id = 0;
        int recipe_id = 2;
        String expected = "{\"success\":\"false\", \"message\":\"Users can't review their own recipes\"}";

        User mockUser = new User("Password", "john@somemail.com");
        User mockReviewer = new User("Password1", "jim@somemail.com");
        Review mockReview = new Review(5, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);
        List<Review> mockReviewList = new ArrayList<Review>();
        mockReviewList.add(mockReview);
        mockReview.setReviewer(mockReviewer);

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockRecipe.getRecipeReviews()).thenReturn(mockReviewList);

        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);

        assertEquals(expected, actual);
    }
}