package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeController;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.Websockets.FavoriteSocket;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private FavoriteSocket favoriteSocket;
    @Mock
    private User mockUser;

    // Work In Progress unit tests
    @Test
    public void testRegister_NoDuplicate_ThenReturnSuccess() {
        MockitoAnnotations.openMocks(this);
        User mockUser = new User("password", "mockitoUserTest@email.com");
        String expected = MessageUtil.newResponseMessage(true, "User created");
        String actual = userController.createUser(mockUser);
        assertEquals(expected, actual);
        Mockito.verify(userRepository).save(mockUser);
    }
//    @Test
//    public void testRegister_Duplicate_ThenReturnFail() {
//        MockitoAnnotations.openMocks(this);
//        User mockUser = new User("password", "mockitoUserTest@email.com");
//        when(userRepository.save(mockUser)).thenThrow(new Exception());
//        String expected = MessageUtil.newResponseMessage(false, "Email already used");
//        String actual = userController.createUser(mockUser);
//        assertEquals(expected, actual);
//    }
    @Test
    public void testLogin_WhenCorrect_ThenReturnSuccess() {
        MockitoAnnotations.openMocks(this);
        User mockUser = new User("password", "mockitoUserTest@email.com");
        Login mockLogin = new Login("mockitoUserTest@email.com", "password");
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        String expected = MessageUtil.newResponseMessage(true, "" + mockUser.getId());
        String actual = userController.login(mockLogin);
        assertEquals(expected, actual);
    }
    @Test
    public void testLogin_IncorrectEmail_ReturnFailedEmail() {
        MockitoAnnotations.openMocks(this);
        User mockUser = new User("password", "mockito1UserTest@email.com");
        Login mockLogin = new Login("mockitoUserTest@email.com", "password");
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        String expected = MessageUtil.newResponseMessage(false, "email incorrect");
        String actual = userController.login(mockLogin);
        assertEquals(expected, actual);
    }
    @Test
    public void testLogin_WhenBadPass_ThenReturnFail() {
        MockitoAnnotations.openMocks(this);
        User mockUser = new User("password1", "mockitoUserTest@email.com");
        Login mockLogin = new Login("mockitoUserTest@email.com", "password");
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        String expected = MessageUtil.newResponseMessage(false, "password incorrect");
        String actual = userController.login(mockLogin);
        assertEquals(expected, actual);
    }
    @Test
    public void testFavorite_whenNewFavorite_ThenReturnSuccess() {
        MockitoAnnotations.openMocks(this);
        //this.mockUser = new User("password1", "mockitoUserTest@email.com");
        Recipe mockRecipe = new Recipe("Drake's soup", 4, "Very stinky", "This is a description");
        int user_id = 1;
        int recipe_id = 1;
        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        List<Recipe> favorites = new ArrayList<Recipe>();
        when(mockUser.getFavorites()).thenReturn(favorites);

        String expected = MessageUtil.newResponseMessage(true, "favorited");
        String actual = userController.favorite(user_id, recipe_id);
        assertEquals(actual, expected);
        Mockito.verify(favoriteSocket).onFavorite(mockRecipe, mockUser);
    }
    @Test
    public void testFavorite_whenAlreadyFavorite_ThenReturnFailure() {
        MockitoAnnotations.openMocks(this);
        //this.mockUser = new User("password1", "mockitoUserTest@email.com");
        Recipe mockRecipe = new Recipe("Drake's soup", 4, "Very stinky", "This is a description");
        int user_id = 1;
        int recipe_id = 1;
        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        List<Recipe> favorites = new ArrayList<Recipe>();
        favorites.add(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);

        String expected = MessageUtil.newResponseMessage(false, "releationship already exists");
        String actual = userController.favorite(user_id, recipe_id);
        assertEquals(actual, expected);
    }
    @Test
    public void testUnFavorite_whenAlreadyFavorited_ThenReturnSuccess() {
        MockitoAnnotations.openMocks(this);
        Recipe mockRecipe = new Recipe("Drake's soup", 4, "Very stinky", "This is a description");
        int user_id = 1;
        int recipe_id = 1;
        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        List<Recipe> favorites = new ArrayList<Recipe>();
        favorites.add(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);

        String expected = MessageUtil.newResponseMessage(true, "successfully unfavorited");
        String actual = userController.unfavorite(user_id, recipe_id);
        assertEquals(actual, expected);
        Mockito.verify(favoriteSocket).onUnfavorite(mockRecipe, mockUser);
    }
    @Test
    public void testUnFavorite_whenNotAlreadyFavorite_ThenReturnFailure() {
        MockitoAnnotations.openMocks(this);
        //this.mockUser = new User("password1", "mockitoUserTest@email.com");
        Recipe mockRecipe = new Recipe("Drake's soup", 4, "Very stinky", "This is a description");
        int user_id = 1;
        int recipe_id = 1;
        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        List<Recipe> favorites = new ArrayList<Recipe>();
        when(mockUser.getFavorites()).thenReturn(favorites);

        String expected = MessageUtil.newResponseMessage(false, "relationship does not exist");
        String actual = userController.unfavorite(user_id, recipe_id);
        assertEquals(actual, expected);
    }
    @Test
    public void testGetUserByEmail_WhenEmailFound_ThenReturnSuccess() {

        //assert()
    }

    @Test
    public void testGetUserByEmail_WhenEmailNotFound_ThenThrowException() {

    }
}