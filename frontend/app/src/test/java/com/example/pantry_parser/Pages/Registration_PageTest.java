package com.example.pantry_parser.Pages;


import com.example.pantry_parser.Utilities.PasswordValidation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Registration_PageTest {
    private static final String badPassword = "pass";
    private static final String goodPassword = "Password1";

    @Mock
    Registration_Page registration_page;

    @Mock
    PasswordValidation passwordValidation;

    @Test
    public void testIsValidPassword_WhenBadPassword_ThenReturnFalse(){
        boolean result =  passwordValidation.isValidPassword(badPassword);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsValidPassword_WhenGoodPassword_ThenReturnFalse(){
        boolean result =  passwordValidation.isValidPassword(goodPassword);
        Assert.assertTrue(result);
    }
}
