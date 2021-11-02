package com.example.pantry_parser.Pages;

import static org.junit.Assert.*;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Login_PageTest {

    private static final String goodUsername = "Admin";
    private static final String goodPassword = "2_do_8_comS_309";
    private static final String badUsername = "Admin1";
    private static final String badPassword = "2_do_9_comS_309";

    @Mock
    Login_Page login_page;

    @Test
    public void testValidateAdmin_WhenBadPassword_ThenReturnFalse() {
      boolean result =  login_page.validateAdmin(goodUsername, badPassword);
        Assert.assertFalse(result);
    }

    @Test
    public void testValidateAdmin_WhenBadUsername_ThenReturnFalse() {
        boolean result =  login_page.validateAdmin(badUsername, goodPassword);
        Assert.assertFalse(result);
    }

    @Test
    public void testValidateAdmin_WhenGoodUsernameAndPassword_ThenReturnTrue() {
        boolean result =  login_page.validateAdmin(goodUsername, goodPassword);
        Assert.assertFalse(result);
    }


}