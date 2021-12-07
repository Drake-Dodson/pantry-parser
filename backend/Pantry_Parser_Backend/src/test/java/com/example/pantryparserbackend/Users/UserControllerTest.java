package com.example.pantryparserbackend.Users;

import com.example.pantryparserbackend.Permissions.IPRepository;
import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Recipes.RecipeRepository;
import com.example.pantryparserbackend.Services.IPService;
import com.example.pantryparserbackend.Services.PermissionService;
import com.example.pantryparserbackend.Utils.MessageUtil;
import com.example.pantryparserbackend.Requests.LoginRequest;
import com.example.pantryparserbackend.Requests.UserRequest;
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

        UserRequest userRequest = new UserRequest("password", "pantryparser@gmail.com", "Name");

        when(userRepository.save(anyObject())).thenThrow(new DataIntegrityViolationException("already exists"));
        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);

        String expected = MessageUtil.newResponseMessage(false, "Email already used");
        String actual = userController.createUser(userRequest, mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void testRegister_onNullInput_thenReturnFail() {
        MockitoAnnotations.openMocks(this);

        when(permissionService.canUser(anyString(), anyObject(), anyObject())).thenReturn(true);
        String expected = MessageUtil.newResponseMessage(false, "UserRequest was null");
        String actual = userController.createUser(null, mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void testLogin_WhenCorrect_ThenReturnSuccess() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password", "mockitoUserTest@email.com");
        LoginRequest mockLogin = new LoginRequest("mockitoUserTest@email.com", "password");

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);

        String expected = MessageUtil.newResponseMessage(true, "" + mockUser.getId() + ":" + mockUser.getRole());
        String actual = userController.login(mockLogin, mockRequest);

        assertEquals(expected, actual);
    }
    @Test
    public void testLogin_IncorrectEmail_ReturnFailedEmail() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password", "mockito1UserTest@email.com");
        LoginRequest mockLogin = new LoginRequest("mockitoUserTest@email.com", "password");

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);

        String expected = MessageUtil.newResponseMessage(false, "email incorrect");
        String actual = userController.login(mockLogin, mockRequest);

        assertEquals(expected, actual);
    }
    @Test
    public void testLogin_WhenNotAUser_ThenReturnFail() {
        MockitoAnnotations.openMocks(this);

        User mockUser = new User("password1", "mockitoUserTest@email.com");
        LoginRequest mockLogin = new LoginRequest("mockitoUserTest@email.com", "password");

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(null);

        String expected = MessageUtil.newResponseMessage(false, "email incorrect");
        String actual = userController.login(mockLogin, mockRequest);

        assertEquals(expected, actual);
    }
}