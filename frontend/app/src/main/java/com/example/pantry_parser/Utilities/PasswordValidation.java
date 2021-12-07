package com.example.pantry_parser.Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidation {

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if(password == null){
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);
        System.out.println(m.matches());
        return m.matches();
    }
}
