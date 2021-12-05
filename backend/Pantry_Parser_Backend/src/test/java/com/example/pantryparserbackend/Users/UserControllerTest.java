package com.example.pantryparserbackend.Users;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Requests.LoginRequest;
import com.example.pantryparserbackend.Requests.UserRequest;
import com.example.pantryparserbackend.Util.MessageUtil;
import com.example.pantryparserbackend.Websockets.FavoriteSocket;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Test
    public void testRegister_Duplicate_ThenReturnFail() {
//        MockitoAnnotations.openMocks(this);
//
//        UserRequest userRequest = new UserRequest("password", "pantryparser@gmail.com", "Name");
//
//        when(userRepository.save(mockUser)).thenThrow(new DataIntegrityViolationException("already exists"));
//        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
//
//        String expected = MessageUtil.newResponseMessage(false, "Email already used");
//        String actual = userController.createUser(userRequest);
//
//        assertEquals(expected, actual);
    }

    @Test
    public void testRegister_onNullInput_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        String expected = MessageUtil.newResponseMessage(false, "UserRequest was null");
        String actual = userController.createUser(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testLogin_WhenCorrect_ThenReturnSuccess() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password", "mockitoUserTest@email.com");
        LoginRequest mockLogin = new LoginRequest("mockitoUserTest@email.com", "password");

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);

        String expected = MessageUtil.newResponseMessage(true, "" + mockUser.getId());
        String actual = userController.login(mockLogin);

        assertEquals(expected, actual);
    }
    @Test
    public void testLogin_IncorrectEmail_ReturnFailedEmail() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password", "mockito1UserTest@email.com");
        LoginRequest mockLogin = new LoginRequest("mockitoUserTest@email.com", "password");

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);

        String expected = MessageUtil.newResponseMessage(false, "email incorrect");
        String actual = userController.login(mockLogin);

        assertEquals(expected, actual);
    }
    @Test
    public void testLogin_WhenNotAUser_ThenReturnFail() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password1", "mockitoUserTest@email.com");
        LoginRequest mockLogin = new LoginRequest("mockitoUserTest@email.com", "password");

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "email incorrect");
        String actual = userController.login(mockLogin);

        assertEquals(expected, actual);
    }
    @Test
    public void testLogin_WhenBadPass_ThenReturnFail() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password1", "mockitoUserTest@email.com");
        LoginRequest mockLogin = new LoginRequest("mockitoUserTest@email.com", "password");

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);

        String expected = MessageUtil.newResponseMessage(false, "password incorrect");
        String actual = userController.login(mockLogin);

        assertEquals(expected, actual);
    }

    @Test
    public void testFavorite_whenNewFavorite_ThenReturnSuccess() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("Drake's soup", 4, "Very stinky", "This is a description");
        int user_id = 1;
        int recipe_id = 1;
        List<Recipe> favorites = new ArrayList<>();

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);

        String expected = MessageUtil.newResponseMessage(true, "favorited");
        String actual = userController.favorite(user_id, recipe_id);

        assertEquals(actual, expected);
        Mockito.verify(favoriteSocket).onFavorite(mockRecipe, mockUser);
    }
    @Test
    public void testFavorite_whenNotRecipe_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "recipe does not exist");
        String actual = userController.favorite(user_id, recipe_id);

        assertEquals(expected, actual);
    }
    @Test
    public void testFavorite_whenNotUser_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(new Recipe());

        String expected = MessageUtil.newResponseMessage(false, "user does not exist");
        String actual = userController.favorite(user_id, recipe_id);

        assertEquals(expected, actual);
    }
    @Test
    public void testFavorite_whenAlreadyFavorite_ThenReturnFailure() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("Drake's soup", 4, "Very stinky", "This is a description");
        int user_id = 1;
        int recipe_id = 1;
        List<Recipe> favorites = new ArrayList<>();
        favorites.add(mockRecipe);

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
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
        List<Recipe> favorites = new ArrayList<>();
        favorites.add(mockRecipe);

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);

        String expected = MessageUtil.newResponseMessage(true, "successfully unfavorited");
        String actual = userController.unfavorite(user_id, recipe_id);

        assertEquals(actual, expected);
        Mockito.verify(favoriteSocket).onUnfavorite(mockRecipe, mockUser);
    }
    @Test
    public void testUnFavorite_whenNotRecipe_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "recipe does not exist");
        String actual = userController.unfavorite(user_id, recipe_id);

        assertEquals(expected, actual);
    }
    @Test
    public void testUnFavorite_whenNotUser_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(new Recipe());

        String expected = MessageUtil.newResponseMessage(false, "user does not exist");
        String actual = userController.unfavorite(user_id, recipe_id);

        assertEquals(expected, actual);
    }
    @Test
    public void testUnFavorite_whenNotAlreadyFavorite_ThenReturnFailure() {
        MockitoAnnotations.openMocks(this);

        Recipe mockRecipe = new Recipe("Drake's soup", 4, "Very stinky", "This is a description");
        int user_id = 1;
        int recipe_id = 1;
        List<Recipe> favorites = new ArrayList<>();

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);

        String expected = MessageUtil.newResponseMessage(false, "relationship does not exist");
        String actual = userController.unfavorite(user_id, recipe_id);

        assertEquals(actual, expected);
    }
}