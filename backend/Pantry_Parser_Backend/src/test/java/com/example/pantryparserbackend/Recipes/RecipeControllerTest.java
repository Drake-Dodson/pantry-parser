package com.example.pantryparserbackend.Recipes;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Ingredients.IngredientRepository;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class RecipeControllerTest {
    @InjectMocks
    RecipeController recipeController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private StepsRepository stepsRepository;

    @Test
    void onRecipeCreate_successful() {
        String expected = MessageUtil.newResponseMessage(true, "successfully created recipe");
        List<String> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(ingredientName);
        RecipeRequest input = new RecipeRequest("name", 4, "summary", "description", ingredientList, stepList);
        int user_id = 1;
        User mockUser = new User("pass", "emalil@lail.com");
        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(new Ingredient(ingredientName));
        String actual = recipeController.createRecipe(user_id, input);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
        assertEquals(expected, actual);
    }

    @Test
    void onRecipeCreate_partiallySuccessful() {
        String expected = MessageUtil.newResponseMessage(true, "recipe was created, however some ingredients didn't exist and were not added");
        List<String> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(ingredientName);
        RecipeRequest input = new RecipeRequest("name", 4, "summary", "description", ingredientList, stepList);
    }

}
