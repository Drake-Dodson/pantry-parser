package com.example.pantryparserbackend.Recipes;

import java.util.List;

import com.example.pantryparserbackend.Util.MessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.bridge.Message;
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

    //generic recipe stuff
    /**
     * gets a list of all the recipes
     * @return a list of all the recipes in the database
     */
    @ApiOperation(value = "Get all of the recipes in the database")
    @GetMapping(path = "/recipes")
    Page<Recipe> getAllRecipes(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage){
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("rating"));
        return recipeRepository.findAll(page);
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
     * @param recipe the input values for the recipe
     * @return either success or a failure message
     */
    @ApiOperation(value = "Create a new recipe with the given user ")
    @PostMapping(path = "/user/{user_id}/recipes")
    String createRecipe(@PathVariable int user_id, @RequestBody Recipe recipe){
        if(recipe == null)
            return MessageUtil.newResponseMessage(false, "invalid input");
        recipe.setCreatedDate();
        User u = userRepository.findById(user_id);
        if(u == null){
            return MessageUtil.newResponseMessage(false, "invalid user");
        }
        recipe.setCreator(u);
        u.addRecipe(recipe);
        recipeRepository.save(recipe);
        userRepository.save(u);
        return MessageUtil.newResponseMessage(true, "successfully created recipe");
    }

    /**
     * updates the values of a recipe
     * @param recipe_id the id of the recipe we are looking to update
     * @param request the new set of values
     * @return the new recipe
     */
    @ApiOperation(value = "Updates the recipe of the given id")
    @PatchMapping(path = "/recipe/{recipe_id}")
    Recipe updateRecipe(@PathVariable int recipe_id, @RequestBody Recipe request){
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null)
            return null;
        recipe.update(request);
        recipeRepository.save(recipe);
        return recipeRepository.findById(recipe_id);
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
        recipeRepository.delete(recipe);
        return MessageUtil.newResponseMessage(true, "successfully deleted");
    }

    //recipe steps stuff
    /**
     * gets the step of the given id
     * @param step_id the id of the step we want to look at
     * @return the step at the provided id, or null
     */
    @ApiOperation(value = "Gets the steps of the given id")
    @GetMapping(path = "/step/{step_id}")
    Step getStep(@PathVariable int step_id) { return stepRepository.findById(step_id); }

    /**
     * updates a step
     * @param step_id the id of the step we want to update
     * @param newStep the new values of the step
     * @return either success or a failure message
     */
    @ApiOperation(value = "Updates the given step")
    @PatchMapping(path = "/step/{step_id}")
    String updateStep(@PathVariable int step_id, @RequestBody Step newStep) {
        Step step = stepRepository.findById(step_id);
        if(step == null){
            return MessageUtil.newResponseMessage(false, "step does not exist");
        }
        if(newStep == null){
            return MessageUtil.newResponseMessage(false, "step cannot be null");
        }
        step.setName(newStep.getName());
        stepRepository.save(step);

        if(step.getNum() != newStep.getNum() && newStep.getNum() > 0){
            Recipe recipe = recipeRepository.findById(step.getRecipe().getId());
            if(recipe == null){
                return MessageUtil.newResponseMessage(false, "recipe does not exist");
            }
            if(newStep.getNum() >= recipe.getSteps().size())
            {
                //caps out possible order positions to the bottom of the array
                newStep.setNum(recipe.getSteps().size());
            }
            recipe.shiftStep(step, newStep.getNum() - 1);
            recipeRepository.save(recipe);

            for(Step s : recipe.getSteps()){
                s.setNum(recipe.getSteps().indexOf(s) + 1);
            }
            stepRepository.saveAll(recipe.getSteps());
        }
        return MessageUtil.newResponseMessage(true, "successfully modified");
    }

    /**
     * deletes a step
     * @param step_id the id of the step we want to delete
     * @return either success or a failure message
     */
    @ApiOperation(value = "Deletes the step of the given id")
    @DeleteMapping(path = "/step/{step_id}")
    String deleteStep(@PathVariable int step_id){
        Step step = stepRepository.findById(step_id);
        if(step == null){
            return MessageUtil.newResponseMessage(false, "step does not exist");
        }

        Recipe recipe = recipeRepository.findById(step.getRecipe().getId());
        if(recipe == null){
            return MessageUtil.newResponseMessage(false, "recipe does not exist");
        }

        recipe.removeStep(step);
        stepRepository.delete(step);
        recipeRepository.save(recipe);

        for(Step s : recipe.getSteps()){
            s.setNum(recipe.getSteps().indexOf(s) + 1);
        }
        stepRepository.saveAll(recipe.getSteps());
        return MessageUtil.newResponseMessage(true, "successfully deleted");
    }

    //steps by recipe
    /**
     * gets the steps a recipe has
     * @param recipe_id the id of the recipe whose steps we're looking for
     * @return a list of steps from the provided recipe
     */
    @ApiOperation(value = "Gets all of the steps of a given recipe id")
    @GetMapping(path = "/recipe/{recipe_id}/steps")
    List<Step> showStepsByRecipe(@PathVariable int recipe_id){
        Recipe r = recipeRepository.findById(recipe_id);
        if(r == null) {
            return null;
        }
        return r.getSteps();
    }

    /**
     * gets a step from a recipe
     * @param recipe_id the id of the recipe whose steps we want to look at
     * @param pos the step number of the recipe
     * @return the step we want to look at
     */
    @ApiOperation(value = "Gets a specific step of a recipe")
    @GetMapping(path = "/recipe/{recipe_id}/step/{pos}")
    Step getOrderedStep(@PathVariable int recipe_id, @PathVariable int pos) {
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null) {
            return null;
        }
        int step_id = recipe.getStepByOrder(pos - 1).getId();
        return this.getStep(step_id);
    }

    /**
     * creates a step on a recipe
     * @param recipe_id the id of the recipe that we want to add a step to
     * @param step the new step we are creating
     * @return either success or a failure message
     */
    @ApiOperation(value = "Creates a step for the given recipe")
    @PostMapping(path = "/recipe/{recipe_id}/steps")
    String createStep(@PathVariable int recipe_id, @RequestBody Step step) {
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null){
            return MessageUtil.newResponseMessage(false, "recipe does not exist");
        }
        if(step == null){
            return MessageUtil.newResponseMessage(false, "step cannot be null");
        }
        step.setNum(recipe.getSteps().size() + 1);
        recipe.addStep(step);
        step.setRecipe(recipe);
        recipeRepository.save(recipe);
        stepRepository.save(step);
        return MessageUtil.newResponseMessage(true, "successfully created");
    }

    /**
     * updates a step from a recipe
     * @param recipe_id the id of the recipe whose step we want to edit
     * @param pos the step number on the recipe
     * @param newStep the new step values we want to update
     * @return either success or a failure message
     */
    @ApiOperation(value = "Updates the step at the given position in a given recipe")
    @PatchMapping(path = "/recipe/{recipe_id}/step/{pos}")
    String updateOrderedStep(@PathVariable int recipe_id, @PathVariable int pos, @RequestBody Step newStep) {
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null){
            return MessageUtil.newResponseMessage(false, "recipe does not exist");
        }
        if(recipe.getSteps().size() <= pos || pos <= 0){
            return MessageUtil.newResponseMessage(false, "that is not a step on this recipe");
        }
        int step_id = recipe.getStepByOrder(pos - 1).getId();
        return this.updateStep(step_id, newStep);
    }
    /**
     * deletes a step from a recipe
     * @param recipe_id the id of the recipe whose step we want to remove
     * @param pos the step number on the recipe
     * @return either success or a failure message
     */
    @ApiOperation(value = "Deletes the step at the given position in a given recipe")
    @DeleteMapping(path = "/recipe/{recipe_id}/step/{pos}")
    String deleteOrderedStep(@PathVariable int recipe_id, @PathVariable int pos){
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null){
            return MessageUtil.newResponseMessage(false, "recipe does not exist");
        }
        if(recipe.getSteps().size() <= pos || pos <= 0){
            return MessageUtil.newResponseMessage(false, pos + " is not a valid step, this recipe only has " + recipe.getSteps().size());
        }
        int step_id = recipe.getStepByOrder(pos - 1).getId();
        return this.deleteStep(step_id);
    }

    //recipe ingredient stuff
    /**
     * gets a list of all ingredients
     * @return a list of all ingredients in the database
     */
    @ApiOperation(value = "Gets the list of all ingredients")
    @GetMapping(path = "/ingredients")
    Page<Ingredient> showIngredients(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @RequestParam(defaultValue = "") String query){
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("name"));
        return query.equals("") ? ingredientRepository.findAll(page) : ingredientRepository.findAllSearch(query, page);
    }

    /**
     * creates a new ingredient
     * @param request the input values for the new ingredient
     * @return either succsess or an error message
     */
    @ApiOperation(value = "Creates a new ingredient")
    @PostMapping(path = "/ingredients")
    String createIngredient(@RequestBody Ingredient request){
        if(request == null){
            return MessageUtil.newResponseMessage(false, "request body was null");
        }
        request.nameToLower();
        if(ingredientRepository.findByName(request.getName()) != null){
            return MessageUtil.newResponseMessage(false, "ingredient already exists");
        }
        ingredientRepository.save(request);
        return MessageUtil.newResponseMessage(true, "successfully created");
    }

    /**
     * gets a list of recipes by the name of an ingredient
     * @param name the input name of the ingredient
     * @return list of recipes
     */
    @ApiOperation(value = "Gets all of the recipes that have the given ingredient")
    @GetMapping(path="/ingredient/{name}/recipes")
    Page<Recipe> ingredientRecipes(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @PathVariable String name){
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("rating"));
        Ingredient i = ingredientRepository.findByName(name);
        if(i == null){
            return null;
        }
        return recipeRepository.getByIngredient(i.getId(), page);
    }

    /**
     * the main functionality of our app
     * takes in an input array of ingredients, and returns
     * a list of recipes at least one of those ingredients,
     * sorted by the percentage of the recipe's ingredients that
     * match the input
     * @param input array of strings with ingredient names
     * @return list of recipes
     */
    @ApiOperation(value = "Gets a list of ingredients and searches recipes based on that list")
    @PutMapping(path = "/pantry-parser")
    Page<Recipe> recipesByIngrents(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @RequestBody List<String> input){
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("rating"));
        return recipeRepository.getByIngredients(input, page);
    }

    //adding and removing ingredients
    /**
     * returns a list of the ingredients on a specific recipe
     * @param recipe_id the id of the recipe
     * @return a list of ingredients
     */
    @ApiOperation(value = "Gets the list of ingredients for a recipe")
    @GetMapping(path = "/recipe/{recipe_id}/ingredients")
    List<Ingredient> ingredientsByRecipe(@PathVariable int recipe_id){
        Recipe r = recipeRepository.findById(recipe_id);
        if (r == null){
            return null;
        }
        return r.getIngredients();
    }

    /**
     * attaches an ingredient to a recipe and vice versa
     * @param recipe_id the id of the recipe
     * @param name name of the ingredient we want to add
     * @return either success or a failure message
     */
    @ApiOperation(value = "Attaches a recipe and ingredient to each other")
    @PatchMapping(path = "/recipe/{recipe_id}/ingredient/{name}")
    String addIngredient(@PathVariable int recipe_id, @PathVariable String name){
        Recipe r = recipeRepository.findById(recipe_id);
        Ingredient i = ingredientRepository.findByName(name.toLowerCase());
        if(r == null || i == null){
            return MessageUtil.newResponseMessage(false, (r == null ? "recipe " : "ingredient ") + "does not exist");
        }

        if(r.getIngredients().contains(i)) {
            return MessageUtil.newResponseMessage(false, "already exists");
        }

        r.addIngredient(i);
        recipeRepository.save(r);
        return MessageUtil.newResponseMessage(true, "added successfully");
    }
    /**
     * removes an ingredient to from a recipe and vice versa
     * @param recipe_id the id of the recipe
     * @param name name of the ingredient we want to remove
     * @return either success or a failure message
     */
    @ApiOperation(value = "Deletes an ingredient from a recipe")
    @DeleteMapping(path = "/recipe/{recipe_id}/ingredient/{name}")
    String removeIngredient(@PathVariable int recipe_id, @PathVariable String name){
        Recipe r = recipeRepository.findById(recipe_id);
        Ingredient i = ingredientRepository.findByName(name.toLowerCase());
        if(r == null || i == null){
            return MessageUtil.newResponseMessage(false, (r == null ? "recipe " : "ingredient ") + "does not exist");
        }

        if(!r.getIngredients().contains(i)) {
            return MessageUtil.newResponseMessage(false, "relationship does not exist");
        }

        r.removeIngredient(i);
        recipeRepository.save(r);
        return MessageUtil.newResponseMessage(true, "successfully removed");
    }
}
