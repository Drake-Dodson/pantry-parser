package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Util.MessageUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        User mockUser = new User("password", "mockitoUserTest@email.com");
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        String expected = MessageUtil.newResponseMessage(true, "User created");
        String actual = userController.createUser(mockUser);
        assertEquals(expected, actual);
    }
    @Test
    public void testRegister_Duplicate_ThenReturnFail() {
        User mockUser = new User("password", "mockitoUserTest@email.com");
        when(userRepository.save(mockUser)).thenThrow(new Exception("bad"));
        String expected = MessageUtil.newResponseMessage(false, "Email already used");
        String actual = userController.createUser(mockUser);
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