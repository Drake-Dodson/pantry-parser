package com.example.pantryparserbackend.Requests;

import java.util.List;

public class RecipeRequest {
	public String name;
	public int time;
	public String summary;
	public String description;
	public List<String> ingredients;
	public List<String> steps;

	public RecipeRequest(String name, int time, String summary, String description, List<String> ingredients, List<String> steps) {
		this.name = name;
		this.time = time;
		this.summary = summary;
		this.description = description;
		this.ingredients = ingredients;
		this.steps = steps;
	}
}
