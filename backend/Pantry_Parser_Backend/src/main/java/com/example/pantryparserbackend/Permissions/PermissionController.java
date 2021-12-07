package com.example.pantryparserbackend.Permissions;

import com.example.pantryparserbackend.Requests.AdminRequest;
import com.example.pantryparserbackend.Services.IPService;
import com.example.pantryparserbackend.Services.PermissionService;
import com.example.pantryparserbackend.Users.User;
import com.example.pantryparserbackend.Users.UserRepository;
import com.example.pantryparserbackend.Utils.MessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * User controller, responsible for all user stuff
 */
@Api(value = "Permission Controller", description = "Contains all of the routes for permission stuff")
@RestController
public class PermissionController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	IPService ipService;
	@Autowired
	PermissionService permissionService;

	/**
	 * Checks if provided user is a admin
	 * @param user_id user we are checking
	 * @return true or false
	 */
	@ApiOperation(value = "A route to check if the inputted user is an admin")
	@GetMapping("/user/{user_id}/isAdmin")
	String isUserAdmin(@PathVariable int user_id) {
		User user = userRepository.findById(user_id);
		if(user == null) {
			return MessageUtil.newResponseMessage(false, "That is not a user");
		}else if(user.isAdmin()){
			return MessageUtil.newResponseMessage(true, "User is an admin");
		}
		return MessageUtil.newResponseMessage(false, "User is not an admin");
	}

	/**
	 * Checks if provided user is a chef
	 * @param user_id user we are checking
	 * @return true or false
	 */
	@ApiOperation(value = "A route to check if the inputted user is a chef")
	@GetMapping("/user/{user_id}/isChef")
	String isUserChef(@PathVariable int user_id) {
		User user = userRepository.findById(user_id);
		if(user == null) {
			return MessageUtil.newResponseMessage(false, "That is not a user");
		}else if(user.isChef()){
			return MessageUtil.newResponseMessage(true, "User is a chef");
		}
		return MessageUtil.newResponseMessage(false, "User is not a chef");
	}

	/**
	 * Checks if current user is a admin
	 * @param request request
	 * @return true or false
	 */
	@ApiOperation(value = "A route to check if the current user associated with this IP address is an admin")
	@GetMapping("/user/isAdmin")
	String isUserAdmin(HttpServletRequest request) {
		User user = ipService.getCurrentUser(request);
		if(user == null) {
			return MessageUtil.newResponseMessage(false, "No current user");
		}else if(user.isAdmin()){
			return MessageUtil.newResponseMessage(true, "Current user is an admin");
		}
		return MessageUtil.newResponseMessage(false, "Current user is not an admin");
	}

	/**
	 * Checks if current user is a chef
	 * @param request request
	 * @return true or false
	 */
	@ApiOperation(value = "A route to check if the current user associated with this IP address is a chef")
	@GetMapping("/user/isChef")
	String isUserChef(HttpServletRequest request) {
		User user = ipService.getCurrentUser(request);
		if(user == null) {
			return MessageUtil.newResponseMessage(false, "No current user");
		}else if(user.isChef()){
			return MessageUtil.newResponseMessage(true, "Current user is a chef");
		}
		return MessageUtil.newResponseMessage(false, "Current user is not a chef");
	}

	/**
	 * the route for a giving a user a role. Must be an admin to do so
	 * @param user_id the id of the user to have their role changed
	 * @return either success or a failure message
	 */
	@ApiOperation(value = "The route for a user to update a user role")
	@PatchMapping(path = "/user/{user_id}/assignrole")
	public String giveRole(@PathVariable int user_id, @RequestBody AdminRequest adminCreds, HttpServletRequest request){
		if(adminCreds == null){
			return MessageUtil.newResponseMessage(false, "adminCreds was null");
		}

		User admin = userRepository.findByEmail(adminCreds.adminEmail);
		User user = userRepository.findById(user_id);

		if(admin == null || user == null){
			return MessageUtil.newResponseMessage(false, (admin == null ? "admin " : "user ") + "does not exist");
		}

		if(!permissionService.canUser("ChangeRole", user, request)) {
			return MessageUtil.newResponseMessage(false, "You don't have permission to do that");
		}

		if(!admin.isAdmin() /*|| !admin.authenticate(adminCreds.adminPassword)*/){
			return MessageUtil.newResponseMessage(false, "Invalid admin credentials");
		} else {
			try{
				user.setRole(adminCreds.role);
				userRepository.save(user);
				return MessageUtil.newResponseMessage(true, "User role updated");
			}
			catch(Exception ex){
				return MessageUtil.newResponseMessage(false, "Error saving to the database");
			}
		}
	}
}
