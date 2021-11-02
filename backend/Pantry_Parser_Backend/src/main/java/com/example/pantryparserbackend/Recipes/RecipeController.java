package com.example.pantryparserbackend.Recipes;

import java.util.List;

import com.example.pantryparserbackend.Util.MessageUtil;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.pantryparserbackend.users.UserRepository;
import com.example.pantryparserbackend.users.User;

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
    @GetMapping(path = "/recipes")
    List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }
    @GetMapping(path = "/recipes/{id}")
    Recipe getRecipeById(@PathVariable int id){
        return recipeRepository.findById(id);
    }
    @PostMapping(path = "/recipes/{user_id}")
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
    @PatchMapping(path = "/recipes/{recipe_id}")
    Recipe updateRecipe(@PathVariable int recipe_id, @RequestBody Recipe request){
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null)
            return null;
        recipe.update(request);
        recipeRepository.save(recipe);
        return recipeRepository.findById(recipe_id);
    }
    @DeleteMapping(path = "/recipes/{recipe_id}")
    String deleteRecipe(@PathVariable int recipe_id){
        Recipe recipe = recipeRepository.findById(recipe_id);
        if (recipe == null){
            return MessageUtil.newResponseMessage(false, "recipe does not exist");
        }
        recipeRepository.delete(recipe);
        return MessageUtil.newResponseMessage(true, "successfully deleted");
    }

    //recipe steps stuff
    @GetMapping(path = "/step/{step_id}")
    Step getStep(@PathVariable int step_id) { return stepRepository.findById(step_id); }
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
    @GetMapping(path = "/recipe/{recipe_id}/steps")
    List<Step> showStepsByRecipe(@PathVariable int recipe_id){
        Recipe r = recipeRepository.findById(recipe_id);
        if(r == null) {
            return null;
        }
        return r.getSteps();
    }
    @GetMapping(path = "/recipe/{recipe_id}/step/{pos}")
    Step getOrderedStep(@PathVariable int recipe_id, @PathVariable int pos) {
        Recipe recipe = recipeRepository.findById(recipe_id);
        if(recipe == null) {
            return null;
        }
        int step_id = recipe.getStepByOrder(pos - 1).getId();
        return this.getStep(step_id);
    }
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
    @GetMapping(path = "/ingredients")
    List<Ingredient> showIngredients(){
        return ingredientRepository.findAll();
    }
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
    @GetMapping(path="/ingredient/{name}/recipes")
    List<Recipe> ingredientRecipes(@PathVariable String name){
        Ingredient i = ingredientRepository.findByName(name);
        if(i == null){
            return null;
        }
        return i.getRecipes();
    }
    @PutMapping(path = "/ingredients/recipes")
    List<Recipe> recipesByIngrents(@RequestBody List<String> input){
        return recipeRepository.getByIngredients(input);
    }

    //adding and removing ingredients
    @GetMapping(path = "/recipes/{id}/ingredients")
    List<Ingredient> ingredientsByRecipe(@PathVariable int id){
        Recipe r = recipeRepository.findById(id);
        if (r == null){
            return null;
        }
        return r.getIngredients();
    }
    @PatchMapping(path = "/recipes/{id}/ingredients/{name}")
    String addIngredient(@PathVariable int id, @PathVariable String name){
        Recipe r = recipeRepository.findById(id);
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
    @DeleteMapping(path = "/recipes/{id}/ingredients/{name}")
    String removeIngredient(@PathVariable int id, @PathVariable String name){
        Recipe r = recipeRepository.findById(id);
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
