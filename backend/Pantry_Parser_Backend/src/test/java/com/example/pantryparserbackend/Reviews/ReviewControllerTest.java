package com.example.pantryparserbackend.Reviews;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class ReviewControllerTest {

    @InjectMocks
    ReviewController reviewController;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @Test
    public void testWriteReview(){
//        MockitoAnnotations.openMocks(this);
//        int user_id = 1;
//        int recipe_id = 2;
//        String expected = "Test";
//
//        User mockUser = new User("John", "john@somemail.com");
//        Recipe mockRecipe = new Recipe("Johns Burgers", 60, "Delicious and nutritious", "Burger");
//        Review mockReview = new Review(5, "Amazing!", "The best thing I've ever made!", mockUser, mockRecipe);
//
//        when(userRepository.findById(user_id)).thenReturn(mockUser);
//        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
//
//        String actual = reviewController.writeReview(user_id, recipe_id, mockReview);
//
//        assertEquals(expected, actual);
//        Mockito.verify(reviewRepository).save(mockReview);

    }
}