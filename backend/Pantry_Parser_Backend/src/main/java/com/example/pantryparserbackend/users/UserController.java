package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Util.MessageUtil;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/")
    public String welcome() {
        return "Pantry Parser Super Cool Homepage";
    }

    @GetMapping(path = "/user/{id}")
    public User getUserById(@PathVariable int id)
    {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/users")
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @GetMapping(path = "/user/email/{email}")
    public User getUserByEmail(@PathVariable String email) throws Exception {
        return userRepository.findByEmail(email);
    }

    @PostMapping(path = "/user")
    String createUser(@RequestBody User users){
        if (users == null)
            return MessageUtil.newResponseMessage(false, "User was null");

      //  try {
            userRepository.save(users);
    //    }
    //    catch(Exception ex) {
    //        return MessageUtil.newResponseMessage(false, "Email already used");
     //   }

        return MessageUtil.newResponseMessage(true, "User created");
    }

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

    @GetMapping(path = "/user/{user_id}/recipes")
    public List<Recipe> allRecipes(@PathVariable int user_id){
        User u = userRepository.findById(user_id);
        if(u == null){
            return null;
        }
        return u.getRecipes();
    }

    @GetMapping(path = "/user/{user_id}/favorites")
    public List<Recipe> allFavorites(@PathVariable int user_id){
        User u = userRepository.findById(user_id);
        if(u == null){
            return null;
        }
        return u.getFavorites();
    }
    @PatchMapping(path = "/user/{user_id}/favorites/{recipe_id}")
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
    @DeleteMapping(path = "/user/{user_id}/favorites/{recipe_id}")
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
