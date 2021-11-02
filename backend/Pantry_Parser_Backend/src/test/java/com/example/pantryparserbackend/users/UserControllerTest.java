package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Util.MessageUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @InjectMocks
    UserController userController;
    @Mock
    private UserRepository userRepository;

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
    public void testGetUserByEmail_WhenEmailFound_ThenReturnSuccess() {

        //assert()
    }

    @Test
    public void testGetUserByEmail_WhenEmailNotFound_ThenThrowException() {

    }
}