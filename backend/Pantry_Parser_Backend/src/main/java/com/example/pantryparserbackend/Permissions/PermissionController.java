package com.example.pantryparserbackend.Permissions;

import com.example.pantryparserbackend.Services.IPService;
import com.example.pantryparserbackend.Services.PermissionService;
import com.example.pantryparserbackend.Users.User;
import com.example.pantryparserbackend.Users.UserRepository;
import com.example.pantryparserbackend.Utils.MessageUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
