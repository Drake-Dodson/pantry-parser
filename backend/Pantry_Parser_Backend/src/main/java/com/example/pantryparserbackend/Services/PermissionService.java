package com.example.pantryparserbackend.Services;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Permissions.IP;
import com.example.pantryparserbackend.Permissions.IPRepository;
import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class PermissionService {
	@Autowired
	IPRepository ipRepository;
	@Autowired
	IPService ipService;

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
				case "Verify":
					return currentUser.isAdmin();
				default:
					return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

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
