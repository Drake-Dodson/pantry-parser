package com.example.pantryparserbackend.Users;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User mockUser;
    void initialize() {
        mockUser = new User("Password", "test@gmail.com");
    }

    @Test
    void testAuthenticate() {
        initialize();
        assertEquals(true, mockUser.authenticate("Password"));
        assertEquals(false, mockUser.authenticate("password"));
    }

}
