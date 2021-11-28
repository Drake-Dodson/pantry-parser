package com.example.pantryparserbackend.Recipes;

import java.util.ArrayList;
import java.util.Objects;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Ingredients.IngredientRepository;
import com.example.pantryparserbackend.Ingredients.RecipeIngredient;
import com.example.pantryparserbackend.Ingredients.RecipeIngredientRepository;
import com.example.pantryparserbackend.Requests.PantryParserRequest;
import com.example.pantryparserbackend.Requests.RecipeIngredientRequest;
import com.example.pantryparserbackend.Requests.RecipeRequest;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.Util.UnitUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.example.pantryparserbackend.users.UserRepository;
import com.example.pantryparserbackend.users.User;

/**
 * Controller that manages all recipe stuff
 * (recipes, ingredients, and steps)
 */
@Api(value = "Recipe Controller", description = "Contains all of the calls for Recipe data")
@RestController
public class RecipeController {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    StepsRepository stepRepository;
    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    //generic recipe stuff
    /**
     * gets a list of all the recipes
     * @return a list of all the recipes in the database
     */
    @ApiOperation(value = "Get all of the recipes in the database")
    @GetMapping(path = "/recipes")
    Page<Recipe> getAllRecipes(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @RequestParam(defaultValue = "") String query){
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("rating"));
        return Objects.equals(query, "") ? recipeRepository.findAll(page) : recipeRepository.getAllBy(query, page);
    }

    /**
     * gets a recipe by its id
     * @param recipe_id id of the recipe you are looking for
     * @return the recipe that belongs to the id
     */
    @ApiOperation(value = "Get the recipe with the specified id")
    @GetMapping(path = "/recipe/{recipe_id}")
    Recipe getRecipeById(@PathVariable int recipe_id){
        return recipeRepository.findById(recipe_id);
    }

    /**
     * creates a new recipe as the provided user
     * @param user_id the id of the user who is creating the new recipe
     * @param recipeRequest the input values for the recipe
     * @return either success or a failure message
     */
    @ApiOperation(value = "Create a new recipe with the given user ")
    @PostMapping(path = "/user/{user_id}/recipes")
    String createRecipe(@PathVariable int user_id, @RequestBody RecipeRequest recipeRequest){
        if(recipeRequest == null)
            return MessageUtil.newResponseMessage(false, "invalid input");

        Recipe recipe = new Recipe(recipeRequest);
        recipe.setCreatedDate();

        User u = userRepository.findById(user_id);
        if(u == null){
            return MessageUtil.newResponseMessage(false, "invalid user");
        }
        recipe.setCreator(u);
        u.addRecipe(recipe);

        if (recipeRequest.steps == null) {
            recipeRequest.steps = new ArrayList<>();
        }
        if (recipeRequest.ingredients == null) {
            recipeRequest.ingredients = new ArrayList<>();
        }

        int i = 1;
        for (String s : recipeRequest.steps) {
            recipe.addStep(new Step(s, i, recipe));
            i++;
        }

        boolean all = true;
        for (RecipeIngredientRequest s : recipeRequest.ingredients) {
            Ingredient ingredient = ingredientRepository.findByName(s.ingredient_name);
            if(ingredient == null) {
                all = false;
                ingredient = new Ingredient(s.ingredient_name);
                ingredientRepository.save(ingredient);
            }

            if (!UnitUtil.isValidUnit(s.units)) {
                return MessageUtil.newResponseMessage(false, "one of your ingredients had an invalid unit");
            }else if(recipe.getIngredient(ingredient) != null) {
                return MessageUtil.newResponseMessage(false, "you have a duplicate ingredient, this is not allowed");
            }

            RecipeIngredient ri = new RecipeIngredient(ingredient, recipe, s.quantity, s.units);
            recipe.addIngredient(ri);
        }

        try {
            userRepository.save(u);
            recipeRepository.save(recipe);
            stepRepository.saveAll(recipe.getSteps());
            recipeIngredientRepository.saveAll(recipe.getIngredients());
        } catch (Exception ex) {
            return MessageUtil.newResponseMessage(false, "error saving to database, were all fields filled out correctly?");
        }
        return MessageUtil.newResponseMessage(true, all ? "successfully created recipe" : "created some ingredients to make this work");
    }

    /**
     * updates the values of a recipe
     * @param recipe_id the id of the recipe we are looking to update
     * @param recipeRequest the new set of values
     * @return success or failure message
     */
    @ApiOperation(value = "Updates the recipe of the given id")
    @PatchMapping(path = "/recipe/{recipe_id}")
    String updateRecipe(@PathVariable int recipe_id, @RequestBody RecipeRequest recipeRequest) {
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null)
            return MessageUtil.newResponseMessage(false, "recipe does not exist");

        recipe.update(recipeRequest);
        if (recipeRequest.steps == null) {
            recipeRequest.steps = new ArrayList<>();
        }
        if (recipeRequest.ingredients == null) {
            recipeRequest.ingredients = new ArrayList<>();
        }
        //steps
        int i = 0;
        for (String s : recipeRequest.steps) {
            //a new step was added
            if (recipe.getSteps().size() <= i) {
                Step newStep = new Step(s, i, recipe);
                recipe.addStep(newStep);
            } else if (!s.equals(recipe.getStepByOrder(i).getName())) {
                recipe.getStepByOrder(i).setName(s);
            }
            i++;
        }

        //step deletion
        if (recipe.getSteps().size() > recipeRequest.steps.size()) {
            //since it removes the step each time, the indexing shifts back down, so the position will be the same
            //for each step we want to remove, and we need to prepare the end index outside of the loop
            int pos = recipeRequest.steps.size();
            int num = recipe.getSteps().size();
            for (i = pos; i < num; i++) {
                Step step = recipe.getStepByOrder(pos);
                recipe.removeStep(step);
                try {
                    stepRepository.delete(step);
                } catch (Exception ex) {
                    return MessageUtil.newResponseMessage(false, "error deleting a step, this shouldn't happen");
                }
            }
        }

        boolean all = true;
        i = 0;
        for (RecipeIngredientRequest s : recipeRequest.ingredients) {
            if(!UnitUtil.isValidUnit(s.units)) {
                return MessageUtil.newResponseMessage(false, "one of your ingredients had an invalid unit");
            }
            Ingredient ingredient = ingredientRepository.findByName(s.ingredient_name);
            if (ingredient == null) {
                all = false;
                ingredient = new Ingredient(s.ingredient_name);
                ingredientRepository.save(ingredient);
            }
            RecipeIngredient ri = recipe.getIngredient(ingredient);
            if (ri == null) {
                ri = new RecipeIngredient(ingredient, recipe, s.quantity, s.units);
                recipe.addIngredient(ri);
            } else {
                ri.update(s);
            }
        }

        //removes extra ingredients
        for (i = 0; i < recipe.getIngredients().size(); i++) {
            RecipeIngredient s = recipe.getIngredients().get(i);
            if (!recipeRequest.containsIngredient(s.getName())) {
                recipe.removeIngredient(s);
                try {
                    recipeIngredientRepository.delete(s);
                }  catch (Exception ex) {
                    return MessageUtil.newResponseMessage(false, "error deleting a recipe ingredient, this shouldn't happen");
                }
                i--;
            }
        }

        try {
            recipeIngredientRepository.saveAll(recipe.getIngredients());
            stepRepository.saveAll(recipe.getSteps());
            recipeRepository.save(recipe);
        } catch (Exception ex) {
            return MessageUtil.newResponseMessage(false, "error saving to database, were all fields filled out?");
        }
        return MessageUtil.newResponseMessage(true, all ? "successfully updated recipe" : "created some ingredients to make this work");
    }

    /**
     * deletes a recipe
     * @param recipe_id the id of the recipe we want to delete
     * @return either success or a failure message
     */
    @ApiOperation(value = "Deletes the recipe of the given id")
    @DeleteMapping(path = "/recipe/{recipe_id}")
    String deleteRecipe(@PathVariable int recipe_id){
        Recipe recipe = recipeRepository.findById(recipe_id);
        if (recipe == null){
            return MessageUtil.newResponseMessage(false, "recipe does not exist");
        }
        stepRepository.deleteAll(recipe.getSteps());
        recipeIngredientRepository.deleteAll(recipe.getIngredients());
        recipeRepository.delete(recipe);
        return MessageUtil.newResponseMessage(true, "successfully deleted");
    }

    /**
     * the main functionality of our app
     * takes in an input array of ingredients, and returns
     * a list of recipes at least one of those ingredients,
     * sorted by the percentage of the recipe's ingredients that
     * match the input
     * @param request object with an array of strings being the names of the ingredients
     * @return list of recipes
     */
    @ApiOperation(value = "Gets a list of ingredients and searches recipes based on that list")
    @PutMapping(path = "/pantry-parser")
    Page<Recipe> recipesByIngredients(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @RequestBody PantryParserRequest request){
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("rating"));
        return recipeRepository.getByIngredients(request.ingredients, page);
    }
}
