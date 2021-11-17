package com.example.pantryparserbackend.Recipes;

import java.util.List;

public class RecipeRequest {
	protected String name;
	protected int time;
	protected String summary;
	protected String description;
	protected List<String> ingredients;
	protected List<String> steps;

	public RecipeRequest(String name, int time, String summary, String description, List<String> ingredients, List<String> steps) {
		this.name = name;
		this.time = time;
		this.summary = summary;
		this.description = description;
		this.ingredients = ingredients;
		this.steps = steps;
	}
}
