package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Util.MessageUtil;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * User controller, responsible for all user stuff
 */
@Api(value = "User Controller", description = "Contains all of the calls for the users")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * this is just a test method to show us our server is up
     */
    @ApiOperation(value = "The super cool homepage")
    @GetMapping("/")
    public String welcome() {
        return "Pantry Parser Super Cool Homepage";
    }

    /**
     * gets a list of all users in the database
     * @return list of users
     */
    @ApiOperation(value = "Gets all of the users in the database")
    @GetMapping(path = "/users")
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    /**
     * gets a user from the database by the provided id
     * @param user_id input user id
     * @return user
     */
    @ApiOperation(value = "Gets the given user's information")
    @GetMapping(path = "/user/{user_id}")
    public User getUserById(@PathVariable int user_id)
    {
        return userRepository.findById(user_id);
    }

    /**
     * finds a user by the input email
     * @param email input email
     * @return user
     * @throws Exception if the email doesn't exist in the database
     */
    @ApiOperation(value = "Finds a user by a given email")
    @GetMapping(path = "/user/email/{email}")
    public User getUserByEmail(@PathVariable String email) throws Exception {
        return userRepository.findByEmail(email);
    }

    /**
     * creates a new user
     * @param users new user input data
     * @return either success or a failure message
     */
    @ApiOperation(value = "Creates a new user")
    @PostMapping(path = "/users")
    String createUser(@RequestBody User users){
        if (users == null)
            return MessageUtil.newResponseMessage(false, "User was null");

        try {
            userRepository.save(users);
        }
        catch(Exception ex) {
            return MessageUtil.newResponseMessage(false, "Email already used");
        }

        return MessageUtil.newResponseMessage(true, "User created");
    }

    /**
     * attempts to log a user in by checking their credentials against what's in the database
     * @param login a special model with the input email and password
     * @return either success with the user id or a failure message
     */
    @ApiOperation(value = "Logs in a user")
    @PostMapping(path = "/login")
    public String login(@RequestBody Login login){
        if (login == null)
            return MessageUtil.newResponseMessage(false, "no login info detected");
        User actual = userRepository.findByEmail(login.email);
        if (actual == null)
            // Email not found
            return MessageUtil.newResponseMessage(false, "email incorrect");
        if(actual.authenticate(login.password)){
            return MessageUtil.newResponseMessage(true, "" + actual.getId());
        }else {
            // Password incorrect
            return MessageUtil.newResponseMessage(false, "password incorrect");
        }
    }

    /**
     * gets a list of recipes the provided user has created
     * @param user_id the id of the user
     * @return list of recipes that user created
     */
    @ApiOperation(value = "gets all recipes of a user")
    @GetMapping(path = "/user/{user_id}/recipes")
    public List<Recipe> allRecipes(@PathVariable int user_id){
        User u = userRepository.findById(user_id);
        if(u == null){
            return null;
        }
        return u.getRecipes();
    }

    /**
     * gets a list of a certain user's favorites
     * @param user_id the id of the user
     * @return a list of the user's favorite recipes
     */
    @ApiOperation(value = "Gets all of the favorites of a user")
    @GetMapping(path = "/user/{user_id}/favorites")
    public List<Recipe> allFavorites(@PathVariable int user_id){
        User u = userRepository.findById(user_id);
        if(u == null){
            return null;
        }
        return u.getFavorites();
    }

    /**
     * the route for a user to favorite a recipe
     * @param user_id the id of the user
     * @param recipe_id the id of the recipe
     * @return either success or a failure message
     */
    @ApiOperation(value = "Route to favorite a recipe")
    @PatchMapping(path = "/user/{user_id}/favorite/{recipe_id}")
    public String favorite(@PathVariable int user_id, @PathVariable int recipe_id){
        User u = userRepository.findById(user_id);
        Recipe r = recipeRepository.findById(recipe_id);

        if(u == null || r == null){
            return MessageUtil.newResponseMessage(false, (u == null ? "user " : "recipe ") + "does not exist");
        }
        if(u.getFavorites().contains(r)) {
            return MessageUtil.newResponseMessage(false, "releationship already exists");
        }

        u.favorite(r);
        userRepository.save(u);
        return MessageUtil.newResponseMessage(true, "favorited");
    }
    /**
     * the route for a user to unfavorite a recipe
     * @param user_id the id of the user
     * @param recipe_id the id of the recipe
     * @return either success or a failure message
     */
    @ApiOperation(value = "The route for a user to unfavorite a recipe")
    @DeleteMapping(path = "/user/{user_id}/favorite/{recipe_id}")
    public String unfavorite(@PathVariable int user_id, @PathVariable int recipe_id){
        User u = userRepository.findById(user_id);
        Recipe r = recipeRepository.findById(recipe_id);
        if(u == null || r == null){
            return MessageUtil.newResponseMessage(false, (u == null ? "user " : "recipe ") + "does not exist");
        }
        if(!u.getFavorites().contains(r)) {
            return MessageUtil.newResponseMessage(false, "relationship does not exist");
        }

        u.unfavorite(r);
        userRepository.save(u);
        return MessageUtil.newResponseMessage(true, "successfully unfavorited");
    }
}
