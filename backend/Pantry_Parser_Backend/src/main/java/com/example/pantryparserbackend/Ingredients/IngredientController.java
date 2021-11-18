package com.example.pantryparserbackend.Ingredients;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Util.MessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that manages ingredients
 * (recipes, ingredients, and steps)
 */
@Api(value = "Ingredient controller", description = "Contains all of the calls for Ingredient data")
@RestController
public class IngredientController {
	@Autowired
	IngredientRepository ingredientRepository;
	@Autowired
	RecipeRepository recipeRepository;
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
		try {
			ingredientRepository.save(request);
		} catch (Exception ex) {
			return MessageUtil.newResponseMessage(false, "you did not fill out all required fields");
		}
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
}
