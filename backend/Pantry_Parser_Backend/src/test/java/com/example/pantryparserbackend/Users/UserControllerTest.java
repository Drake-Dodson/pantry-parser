package com.example.pantryparserbackend.Users;

import com.example.pantryparserbackend.Permissions.IPRepository;
import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Services.IPService;
import com.example.pantryparserbackend.Services.PermissionService;
import com.example.pantryparserbackend.Utils.MessageUtil;
import com.example.pantryparserbackend.Websockets.FavoriteSocket;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;
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
    @Mock
    private IPRepository ipRepository;
    @Mock
    private IPService ipService;
    @Mock
    private PermissionService permissionService;
    @Mock
    private HttpServletRequest mockRequest;

    @Test
    public void testRegister_Duplicate_ThenReturnFail() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password", "pantryparser@gmail.com");

        when(userRepository.save(mockUser)).thenThrow(new DataIntegrityViolationException("already exists"));
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "Email already used");
        String actual = userController.createUser(mockUser, mockRequest);

        assertEquals(expected, actual);
    }
    @Test
    public void testRegister_onNullInput_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "User was null");
        String actual = userController.createUser(null, mockRequest);

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
        when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(true, "favorited");
        String actual = userController.favorite(user_id, recipe_id, mockRequest);

        assertEquals(actual, expected);
        Mockito.verify(favoriteSocket).onFavorite(mockRecipe, mockUser);
    }
    @Test
    public void testFavorite_whenNotRecipe_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(null);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "recipe does not exist");
        String actual = userController.favorite(user_id, recipe_id, mockRequest);

        assertEquals(expected, actual);
    }
    @Test
    public void testFavorite_whenNotUser_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(null);
        when(ipService.getCurrentUser(mockRequest)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(new Recipe());
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "user does not exist");
        String actual = userController.favorite(user_id, recipe_id, mockRequest);

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
        when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "releationship already exists");
        String actual = userController.favorite(user_id, recipe_id, mockRequest);

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
        when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(true, "successfully unfavorited");
        String actual = userController.unfavorite(user_id, recipe_id, mockRequest);

        assertEquals(actual, expected);
        Mockito.verify(favoriteSocket).onUnfavorite(mockRecipe, mockUser);
    }
    @Test
    public void testUnFavorite_whenNotRecipe_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(this.mockUser);
        when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(null);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "recipe does not exist");
        String actual = userController.unfavorite(user_id, recipe_id, mockRequest);

        assertEquals(expected, actual);
    }
    @Test
    public void testUnFavorite_whenNotUser_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        int user_id = 1;
        int recipe_id = 1;

        when(userRepository.findById(user_id)).thenReturn(null);
        when(ipService.getCurrentUser(mockRequest)).thenReturn(null);
        when(recipeRepository.findById(recipe_id)).thenReturn(new Recipe());
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "user does not exist");
        String actual = userController.unfavorite(user_id, recipe_id, mockRequest);

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
        when(ipService.getCurrentUser(mockRequest)).thenReturn(mockUser);
        when(recipeRepository.findById(recipe_id)).thenReturn(mockRecipe);
        when(mockUser.getFavorites()).thenReturn(favorites);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "relationship does not exist");
        String actual = userController.unfavorite(user_id, recipe_id, mockRequest);

        assertEquals(actual, expected);
    }
}