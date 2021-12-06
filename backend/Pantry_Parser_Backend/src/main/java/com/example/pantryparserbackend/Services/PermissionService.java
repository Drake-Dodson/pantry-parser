package com.example.pantryparserbackend.Services;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Permissions.IP;
import com.example.pantryparserbackend.Permissions.IPRepository;
import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.Users.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Api(value = "Permissions Service", description = "Service that handles whether or not a current user can do something")
public class PermissionService {
	@Autowired
	IPRepository ipRepository;
	@Autowired
	IPService ipService;

	/**
	 * A function that controls permissions for user-based operations
	 * @param method the operation type we are validating
	 * @param user the user being modified (if relevant)
	 * @param request current request
	 * @return true = can do the thing, false = can't
	 */
	@ApiOperation(value = "Permissions for User-based operations")
	public boolean canUser(String method, User user, HttpServletRequest request) {
		IP address = ipRepository.findByIP(ipService.getClientIp(request));
		User currentUser = address == null ? null : address.getUser();

		if (currentUser != null && currentUser.isAdmin()) {
			return true;
		}

		try {
			switch(method) {
				case "ViewAny":
					return false;
				case "View":
					return true;
				case "Create":
					return true;
				case "Update":
					return currentUser.equals(user);
				case "Delete":
					return currentUser.equals(user);
				case "ChangeRole":
					return currentUser.isAdmin();
				default:
					return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * A function that controls permissions for recipe-based operations
	 * @param method the operation type we are validating
	 * @param recipe the recipe being modified (if relevant)
	 * @param request current request
	 * @return true = can do the thing, false = can't
	 */
	@ApiOperation(value = "Permissions for Recipe-based operations")
	public boolean canRecipe(String method, Recipe recipe, HttpServletRequest request) {
		IP address = ipRepository.findByIP(ipService.getClientIp(request));
		User currentUser = address == null ? null : address.getUser();

		if (currentUser != null && currentUser.isAdmin()) {
			return true;
		}

		try {
			switch(method) {
				case "ViewAny":
					return true;
				case "View":
					return true;
				case "Create":
					return currentUser.isEmail_verified();
				case "Update":
					return currentUser.equals(recipe.getCreator());
				case "Verify":
					return currentUser.isChef();
				case "Delete":
					return currentUser.equals(recipe.getCreator());
				default:
					return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * A function that controls permissions for ingredient-based operations
	 * @param method the operation type we are validating
	 * @param ingredient the ingredient being modified (if relevant)
	 * @param request current request
	 * @return true = can do the thing, false = can't
	 */
	@ApiOperation(value = "Permissions for Ingredient-based operations")
	public boolean canIngredient(String method, Ingredient ingredient, HttpServletRequest request) {
		IP address = ipRepository.findByIP(ipService.getClientIp(request));
		User currentUser = address == null ? null : address.getUser();

		if (currentUser != null && currentUser.isAdmin()) {
			return true;
		}

		try {
			switch(method) {
				case "ViewAny":
					return true;
				case "View":
					return true;
				case "Create":
					return currentUser.isChef();
				case "Update":
					return currentUser.isChef();
				case "Delete":
					return currentUser.isChef();
				default:
					return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * A function that controls permissions for review-based operations
	 * @param method the operation type we are validating
	 * @param review the user being modified (if relevant)
	 * @param request current request
	 * @return true = can do the thing, false = can't
	 */
	@ApiOperation(value = "Permissions for Review-based operations")
	public boolean canReview(String method, Review review, HttpServletRequest request) {
		IP address = ipRepository.findByIP(ipService.getClientIp(request));
		User currentUser = address == null ? null : address.getUser();

		if (currentUser != null && currentUser.isAdmin()) {
			return true;
		}

		try {
			switch (method) {
				case "ViewAny":
					return true;
				case "View":
					return true;
				case "Create":
					return currentUser.isEmail_verified();
				case "Update":
					return currentUser.equals(review.getReviewer());
				case "Delete":
					return currentUser.equals(review.getReviewer());
				default:
					return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}
}
