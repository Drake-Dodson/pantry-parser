package com.example.pantryparserbackend.Recipes;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Ingredients.IngredientRepository;
import com.example.pantryparserbackend.Ingredients.RecipeIngredientRepository;
import com.example.pantryparserbackend.Permissions.IPRepository;
import com.example.pantryparserbackend.Requests.RecipeIngredientRequest;
import com.example.pantryparserbackend.Requests.RecipeRequest;
import com.example.pantryparserbackend.Services.IPService;
import com.example.pantryparserbackend.Services.PermissionService;
import com.example.pantryparserbackend.Utils.MessageUtil;
import com.example.pantryparserbackend.Users.User;
import com.example.pantryparserbackend.Users.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
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
    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;
	@Mock
	private IPRepository ipRepository;
	@Mock
	private IPService ipService;
	@Mock
	private PermissionService permissionService;
	@Mock
	private HttpServletRequest mockRequest;

    @Test
    void onRecipeCreate_successful() {
        MockitoAnnotations.openMocks(this);

        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "cups"));
        RecipeRequest input = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);
        int user_id = 1;
        User mockUser = new User("pass", "emalil@lail.com");
        mockUser.setCreated_recipes(new ArrayList<>());

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(new Ingredient(ingredientName));
		when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(true, "successfully created recipe");
        String actual = recipeController.createRecipe(user_id, input, mockRequest);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
        Mockito.verify(recipeIngredientRepository).saveAll(anyList());
		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
    }

    @Test
    void onRecipeCreate_partiallySuccessful() {
        MockitoAnnotations.openMocks(this);

        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "cups"));
        RecipeRequest input = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);
        int user_id = 1;
        User mockUser = new User("pass", "emalil@lail.com");
        mockUser.setCreated_recipes(new ArrayList<>());

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);
		when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(true, "created some ingredients to make this work");
        String actual = recipeController.createRecipe(user_id, input, mockRequest);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
        Mockito.verify(recipeIngredientRepository).saveAll(anyList());
		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
    }

    @Test
    void onRecipeCreate_duplicateIngredient_returnFailure() {
        MockitoAnnotations.openMocks(this);

        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        Ingredient mockIngredient = new Ingredient(ingredientName);
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "cups"));
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "tablespoons"));
        RecipeRequest input = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);
        int user_id = 1;
        User mockUser = new User("pass", "emalil@lail.com");
        mockUser.setCreated_recipes(new ArrayList<>());

        when(userRepository.findById(user_id)).thenReturn(mockUser);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(mockIngredient);
		when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "you have a duplicate ingredient, this is not allowed");
        String actual = recipeController.createRecipe(user_id, input, mockRequest);

        assertEquals(expected, actual);
		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
    }

//    @Test
//    void onRecipeCreate_badUnits_returnFailure() {
//        MockitoAnnotations.openMocks(this);
//
//        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
//        List<String> stepList = new ArrayList<>();
//        stepList.add("step 1");
//        stepList.add("step 2");
//        String ingredientName = "fried chicken";
//        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "coops"));
//        RecipeRequest input = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);
//        int user_id = 1;
//        User mockUser = new User("pass", "emalil@lail.com");
//        mockUser.setCreated_recipes(new ArrayList<>());
//
//        when(userRepository.findById(user_id)).thenReturn(mockUser);
//        when(ingredientRepository.findByName(ingredientName)).thenReturn(new Ingredient(ingredientName));
//		when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
//		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);
//
//        String expected = MessageUtil.newResponseMessage(false, "one of your ingredients had an invalid unit");
//        String actual = recipeController.createRecipe(user_id, input, mockRequest);
//
//        assertEquals(expected, actual);
//		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
//    }

    @Test
    void OnRecipeCreate_invalidUser_returnError() {
        MockitoAnnotations.openMocks(this);

        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "cups"));
        int user_id = 1;
        RecipeRequest input = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);
        User mockUser = new User("pass", "emalil@lail.com");
        mockUser.setCreated_recipes(new ArrayList<>());

        when(userRepository.findById(user_id)).thenReturn(null);
        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);
		when(ipService.getCurrentUser(mockRequest)).thenReturn(null);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "no user, please log in again");
        String actual = recipeController.createRecipe(user_id, input, mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    void onRecipeUpdate_goodInputs_returnSuccess() {
        MockitoAnnotations.openMocks(this);

        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "cups"));
        int recipe_id = 1;
        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
        RecipeRequest mockInput = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);

        when(ingredientRepository.findByName(ingredientName)).thenReturn(new Ingredient(ingredientName));
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(true, "successfully updated recipe");
        String actual = recipeController.updateRecipe(recipe_id, mockInput, mockRequest);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
        Mockito.verify(recipeIngredientRepository).saveAll(anyList());
		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
    }
    @Test
    void onRecipeUpdate_badIngredient_returnPartialSuccess() {
        MockitoAnnotations.openMocks(this);

        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "cups"));
        int recipe_id = 1;
        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
        RecipeRequest mockInput = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);

        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(true, "created some ingredients to make this work");
        String actual = recipeController.updateRecipe(recipe_id, mockInput, mockRequest);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).saveAll(anyList());
        Mockito.verify(recipeRepository).save(anyObject());
        Mockito.verify(recipeIngredientRepository).saveAll(anyList());
		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
    }

    @Test
    void onRecipeUpdate_badRecipe_returnFailure() {
        MockitoAnnotations.openMocks(this);

        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
        List<String> stepList = new ArrayList<>();
        stepList.add("step 1");
        stepList.add("step 2");
        String ingredientName = "fried chicken";
        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "cups"));
        int recipe_id = 1;
        RecipeRequest mockInput = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);

        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(null);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "recipe does not exist");
        String actual = recipeController.updateRecipe(recipe_id, mockInput, mockRequest);

        assertEquals(expected, actual);
    }
//    @Test
//    void onRecipeUpdate_badUnit_returnFailure() {
//        MockitoAnnotations.openMocks(this);
//
//        List<RecipeIngredientRequest> ingredientList = new ArrayList<>();
//        List<String> stepList = new ArrayList<>();
//        stepList.add("step 1");
//        stepList.add("step 2");
//        String ingredientName = "fried chicken";
//        ingredientList.add(new RecipeIngredientRequest(ingredientName, 5, "coops"));
//        int recipe_id = 1;
//        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
//        RecipeRequest mockInput = new RecipeRequest("name", 4, 8, 12, "good for you", "summary", "description", ingredientList, stepList);
//
//        when(ingredientRepository.findByName(ingredientName)).thenReturn(null);
//        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
//		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);
//
//        String expected = MessageUtil.newResponseMessage(false, "one of your ingredients had an invalid unit");
//        String actual = recipeController.updateRecipe(recipe_id, mockInput, mockRequest);
//
//        assertEquals(expected, actual);
//		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
//    }

    @Test
    void onDeleteRecipe_goodRecipe_returnSuccess() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("name", 4, "summary", "description");
        int recipe_id = 1;

        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(true, "successfully deleted");
        String actual = recipeController.deleteRecipe(recipe_id, mockRequest);

        assertEquals(expected, actual);
        Mockito.verify(stepsRepository).deleteAll(mockRecipe.getSteps());
        Mockito.verify(recipeRepository).delete(mockRecipe);
		Mockito.verify(permissionService).canRecipe(anyString(), anyObject(), anyObject());
    }

    @Test
    void onDeleteRecipe_notARecipe_returnFailure() {
        MockitoAnnotations.openMocks(this);

        int recipe_id = 1;

        when(recipeRepository.findById(recipe_id)).thenReturn(null);
		when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "recipe does not exist");;
        String actual = recipeController.deleteRecipe(recipe_id, mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    void testVerifyRecipe_whenRecipeVerified_returnSuccess() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("Yummy Lasagna", 12, "16 cheese lasagna", "Mamma Mia that's a spicy lasagna");
        int recipe_id = 1;

        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);
        when(recipeRepository.save(mockRecipe)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(true, "successfully verified the recipe");;
        String actual = recipeController.verifyRecipe(recipe_id, mockRequest);

        assertTrue(mockRecipe.isChef_verified());
        assertEquals(expected, actual);
        verify(recipeRepository).save(mockRecipe);
    }

    @Test
    void testUnverifyRecipe_whenRecipeVerified_returnSuccess() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("Yummy Lasagna", 12, "16 cheese lasagna", "Mamma Mia that's a spicy lasagna");
        mockRecipe.setChef_verified(true);
        int recipe_id = 1;

        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(permissionService.canRecipe(anyString(), anyObject(), anyObject())).thenReturn(true);
        when(recipeRepository.save(mockRecipe)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(true, "successfully unverified the recipe");;
        String actual = recipeController.unverifyRecipe(recipe_id, mockRequest);

        assertFalse(mockRecipe.isChef_verified());
        assertEquals(expected, actual);
        verify(recipeRepository).save(mockRecipe);
    }
}
