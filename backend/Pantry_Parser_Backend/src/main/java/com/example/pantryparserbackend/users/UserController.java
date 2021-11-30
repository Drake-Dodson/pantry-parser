package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Passwords.OTPRepository;
import com.example.pantryparserbackend.Requests.PasswordResetRequest;
import com.example.pantryparserbackend.Util.EmailUtil;
import com.example.pantryparserbackend.Requests.LoginRequest;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Util.PasswordUtil;
import com.example.pantryparserbackend.Websockets.FavoriteSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private FavoriteSocket favoriteSocket;

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
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage) {
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("email"));
        return userRepository.findAll(page);
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
    public User getUserByEmail(@PathVariable String email) {
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
            if(userRepository.findByEmail(users.getEmail()) != null)
                return MessageUtil.newResponseMessage(false, "Email already used");
            return MessageUtil.newResponseMessage(false, "some fields were left empty");
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
    public String login(@RequestBody LoginRequest login){
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

    @GetMapping(path = "/user/{user_id}/verify/sendOTP")
    public String sendVerifyOTP(@PathVariable int user_id) {
        User user = userRepository.findById(user_id);
        if (user == null) {
            return MessageUtil.newResponseMessage(false, "user not found");
        }

        String pass = PasswordUtil.generateOTP(6, user, otpRepository);
        if (pass.contains("ERROR:")) {
            return MessageUtil.newResponseMessage(false, pass);
        }

        try {
            if (EmailUtil.sendRegistrationConfirmationEmail(user, pass)){
                return MessageUtil.newResponseMessage(true, "check your email for your OTP");
            } else {
                return MessageUtil.newResponseMessage(false, "there was an error sending the email");
            }
        } catch (Exception e) {
            return MessageUtil.newResponseMessage(false, "There was an error sending you your OTP");
        }
    }

    @PostMapping(path = "/user/{user_id}/verify-email")
    public String verifyEmail(@PathVariable int user_id, @RequestBody String OTP) {
        User user = userRepository.findById(user_id);
        if(user == null) {
            return MessageUtil.newResponseMessage(false, "user was not found");
        }
        if(PasswordUtil.useOTP(OTP, user, otpRepository)) {
            user.setEmail_verified(true);
            userRepository.save(user);
            return MessageUtil.newResponseMessage(true, "successfully changed password");
        }
        return MessageUtil.newResponseMessage(false, "invalid OTP, please try again");
    }

    /**
     * A password reset route that takes in an email to find the user
     * @param email email of the user
     * @return string message success or failure
     */
    @GetMapping(path = "/user/email/{email}/password-reset/sendOTP")
    public String sendResetOTP(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            return MessageUtil.newResponseMessage(false, "account not found");
        }
        return this.sendChangeOTP(user.getId());
    }

    /**
     * Route that performs a password reset by a user's email
     * @param email the email of the user
     * @param request the inputted values in the request
     * @return string success or fail
     */
    @PostMapping(path = "/user/email/{email}/password-reset/")
    public String resetPassword(@PathVariable String email, @RequestBody PasswordResetRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            return MessageUtil.newResponseMessage(false, "account not found");
        }
        return this.changePassword(user.getId(), request);
    }

    /**
     * a route for simply changing a user's password
     * @param user_id id of the user
     * @return string success or fail
     */
    @GetMapping(path = "/user/{user_id}/password-change/sendOTP")
    public String sendChangeOTP(@PathVariable int user_id) {
        User user = userRepository.findById(user_id);
        if (user == null){
            return MessageUtil.newResponseMessage(false, "user not found");
        }

        String pass = PasswordUtil.generateOTP(6, user, otpRepository);
        if (pass.contains("ERROR:")) {
            return MessageUtil.newResponseMessage(false, pass);
        }

        try {
            if (EmailUtil.sendPasswordResetEmail(user, pass)){
                return MessageUtil.newResponseMessage(true, "check your email for your OTP");
            } else {
                return MessageUtil.newResponseMessage(false, "there was an error sending the email");
            }
        } catch (Exception e) {
            return MessageUtil.newResponseMessage(false, "There was an error sending you your OTP");
        }
    }

    /**
     * A route that verifies the OTP then changes the password
     * @param user_id id of the user
     * @param request otp and new password
     * @return string success or fail
     */
    @PostMapping(path = "/user/{user_id}/password-change")
    public String changePassword(@PathVariable int user_id, @RequestBody PasswordResetRequest request) {
        User user = userRepository.findById(user_id);
        if(user == null) {
            return MessageUtil.newResponseMessage(false, "user was not found");
        }
        if(PasswordUtil.useOTP(request.OTP, user, otpRepository)) {
            user.setPassword(request.newPassword);
            userRepository.save(user);
            return MessageUtil.newResponseMessage(true, "successfully changed password");
        }
        return MessageUtil.newResponseMessage(false, "invalid OTP, please try again");
    }

    /**
     * gets a list of recipes the provided user has created
     * @param user_id the id of the user
     * @return list of recipes that user created
     */
    @ApiOperation(value = "gets all recipes of a user")
    @GetMapping(path = "/user/{user_id}/recipes")
    public Page<Recipe> allRecipes(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @RequestParam(defaultValue = "") String query, @PathVariable int user_id){
        User u = userRepository.findById(user_id);
        if(u == null){
            return null;
        }
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("rating"));
        return query.equals("") ? recipeRepository.getUserCreated(u, page) : recipeRepository.getUserCreatedSearch(u, query, page);
    }

    /**
     * gets a list of a certain user's favorites
     * @param user_id the id of the user
     * @return a list of the user's favorite recipes
     */
    @ApiOperation(value = "Gets all of the favorites of a user")
    @GetMapping(path = "/user/{user_id}/favorites")
    public Page<Recipe> allFavorites(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer perPage, @RequestParam(defaultValue = "") String query, @PathVariable int user_id){
        User u = userRepository.findById(user_id);
        if(u == null){
            return null;
        }
        Pageable page = PageRequest.of(pageNo, perPage, Sort.by("rating"));
        return query.equals("") ? recipeRepository.getUserFavorites(user_id, page) : recipeRepository.getUserFavoritesSearch(user_id, query, page);
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
        } else if(u.getFavorites().contains(r)) {
            return MessageUtil.newResponseMessage(false, "releationship already exists");
        } else {
            favoriteSocket.onFavorite(r, u);
            return MessageUtil.newResponseMessage(true, "favorited");
        }
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
        } else if(!u.getFavorites().contains(r)) {
            return MessageUtil.newResponseMessage(false, "relationship does not exist");
        } else {
            favoriteSocket.onUnfavorite(r, u);
            return MessageUtil.newResponseMessage(true, "successfully unfavorited");
        }
    }
}
