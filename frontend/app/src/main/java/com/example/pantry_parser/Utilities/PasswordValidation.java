package com.example.pantry_parser.Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidation {

    public static boolean isValidPassword(String password, String confirmPassword) {
        String regex =
                // Digit between 0-9 must occur at least once
                "^(?=.*[0-9])"
                        // Lower case and Upper case alphabet must occur at least once
                        + "(?=.*[a-z])(?=.*[A-Z])"
                        // Special character must occur at least once
                        + "(?=.*[@#$%^&+=])"
                        // No white spaces allowed and password must be between 8-20 characters
                        + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if(password == null){
            return false;
        }
        if(!(password == confirmPassword)){
            return false;
        }
        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        return m.matches();
    }
}
