package com.example.pantryparserbackend.Requests;

import java.util.List;

public class RecipeRequest {
	public String name;
	public int prep_time;
	public int cook_time;
	public String nutrition_facts;
	public String summary;
	public String description;
	public int num_servings;
	public List<RecipeIngredientRequest> ingredients;
	public List<String> steps;

	public RecipeRequest(String name, int prep_time, int cook_time, int num_servings, String nutrition_facts, String summary, String description, List<RecipeIngredientRequest> ingredients, List<String> steps) {
		this.name = name;
		this.prep_time = prep_time;
		this.cook_time = cook_time;
		this.nutrition_facts = nutrition_facts;
		this.num_servings = num_servings;
		this.summary = summary;
		this.description = description;
		this.ingredients = ingredients;
		this.steps = steps;
	}

	public boolean containsIngredient(String s) {
		for (RecipeIngredientRequest ri: this.ingredients) {
			if (ri.ingredient_name.equals(s)) {
				return true;
			}
		}
		return false;
	}
}
