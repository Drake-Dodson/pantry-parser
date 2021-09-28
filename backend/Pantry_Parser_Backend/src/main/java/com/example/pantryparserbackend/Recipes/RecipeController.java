package com.example.pantryparserbackend.Recipes;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    @Autowired
    RecipeRepository recipeRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/recipes")
    List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    @GetMapping(path = "/recipes/{id}")
    Recipe getRecipeById(@PathVariable int id){
        return recipeRepository.findByID(id);
    }

    @PostMapping(path = "/recipes")
    String createRecipe(@RequestBody Recipe recipe){
        if(recipe == null)
            return failure;
        recipeRepository.save(recipe);
        return success;
    }

    @PutMapping(path = "/laptops/{id}")
    Recipe updateRecipe(@PathVariable int id, @RequestBody Recipe request){
        Recipe recipe = recipeRepository.findByID(id);
        if(recipe == null)
            return null;
        recipeRepository.save(request);
        return recipeRepository.findByID(id);
    }

    @DeleteMapping(path = "/laptops/{id}")
    String deleteRecipe(@PathVariable int id){
        recipeRepository.deleteById(id);
        return success;
    }
}
