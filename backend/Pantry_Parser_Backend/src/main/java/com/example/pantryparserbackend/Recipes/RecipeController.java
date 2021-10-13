package com.example.pantryparserbackend.Recipes;

import java.util.List;
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

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

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
            return failure;
        recipe.setCreatedDate();
        User u = userRepository.findById(user_id);
        recipe.setCreator(u);
        u.addRecipe(recipe);
        recipeRepository.save(recipe);
        userRepository.save(u);
        return success;
    }

    @PatchMapping(path = "/recipes/{id}")
    Recipe updateRecipe(@PathVariable int id, @RequestBody Recipe request){
        Recipe recipe = recipeRepository.findById(id);
        if(recipe == null)
            return null;
        recipe.update(request);
        recipeRepository.save(recipe);
        return recipeRepository.findById(id);
    }

    @DeleteMapping(path = "/recipes/{id}")
    String deleteRecipe(@PathVariable int id){
        recipeRepository.deleteById(id);
        return success;
    }
}
