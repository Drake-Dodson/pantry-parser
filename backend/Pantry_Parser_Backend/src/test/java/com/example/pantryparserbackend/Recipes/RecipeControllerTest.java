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
import org.mockito.MockitoAnnotations;

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
        MockitoAnnotations.openMocks(this);

        List<String> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(ingredientName);
        RecipeRequest input = new RecipeRequest("name", 4, "summary", "description", ingredientList, stepList);
        int user_id = 1;
        User mockUser = new User("pass", "emalil@lail.com");
        mockUser.setCreated_recipes(new ArrayList<>());

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(new Ingredient(ingredientName));

        String expected = MessageUtil.newResponseMessage(true, "successfully created recipe");
        String actual = recipeController.createRecipe(user_id, input);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
    }

    @Test
    void onRecipeCreate_partiallySuccessful() {
        MockitoAnnotations.openMocks(this);

        List<String> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(ingredientName);
        RecipeRequest input = new RecipeRequest("name", 4, "summary", "description", ingredientList, stepList);
        int user_id = 1;
        User mockUser = new User("pass", "emalil@lail.com");
        mockUser.setCreated_recipes(new ArrayList<>());

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(true, "recipe was created, however some ingredients didn't exist and were not added");
        String actual = recipeController.createRecipe(user_id, input);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
    }

    @Test
    void OnRecipeCreate_invalidUser_returnError() {
        MockitoAnnotations.openMocks(this);

        List<String> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(ingredientName);
        int user_id = 1;
        RecipeRequest input = new RecipeRequest("name", 4, "summary", "description", ingredientList, stepList);
        User mockUser = new User("pass", "emalil@lail.com");
        mockUser.setCreated_recipes(new ArrayList<>());

        when(userRepository.findById(user_id)).thenReturn(null);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "invalid user");
        String actual = recipeController.createRecipe(user_id, input);

        assertEquals(expected, actual);
    }

    @Test
    void onRecipeUpdate_goodInputs_returnSuccess() {
        MockitoAnnotations.openMocks(this);

        List<String> mockIngredientList = new ArrayList<>();
        List<String> mockStepList = new ArrayList<>();
        mockStepList.add("step 1");
        mockStepList.add("step 2");
        String ingredientName = "fried chicken";
        mockIngredientList.add(ingredientName);
        int recipe_id = 1;

        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
        RecipeRequest mockInput = new RecipeRequest("name", 4, "summary", "description", mockIngredientList, mockStepList);

        when(ingredientRepository.findByName(ingredientName)).thenReturn(new Ingredient(ingredientName));
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);

        String expected = MessageUtil.newResponseMessage(true, "successfully updated recipe");
        String actual = recipeController.updateRecipe(recipe_id, mockInput);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
    }
    @Test
    void onRecipeUpdate_badIngredient_returnPartialSuccess() {
        MockitoAnnotations.openMocks(this);

        List<String> mockIngredientList = new ArrayList<>();
        List<String> mockStepList = new ArrayList<>();
        mockStepList.add("step 1");
        mockStepList.add("step 2");
        String ingredientName = "fried chicken";
        mockIngredientList.add(ingredientName);
        int recipe_id = 1;

        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
        RecipeRequest mockInput = new RecipeRequest("name", 4, "summary", "description", mockIngredientList, mockStepList);

        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);

        String expected = MessageUtil.newResponseMessage(true, "recipe was updated, however some ingredients didn't exist and were not added");
        String actual = recipeController.updateRecipe(recipe_id, mockInput);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
    }

    @Test
    void onDeleteRecipe_goodRecipe_returnSuccess() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
        int recipe_id = 1;

        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);

        String expected = MessageUtil.newResponseMessage(true, "successfully deleted");
        String actual = recipeController.deleteRecipe(recipe_id);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).deleteAll(mockRecipe.getSteps());
        Mockito.verify(recipeRepository).delete(mockRecipe);
    }

    @Test
    void onDeleteRecipe_notARecipe_returnSuccess() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
        int recipe_id = 1;

        when(recipeRepository.findById(recipe_id)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "recipe does not exist");;
        String actual = recipeController.deleteRecipe(recipe_id);

        assertEquals(expected, actual);
    }
}
