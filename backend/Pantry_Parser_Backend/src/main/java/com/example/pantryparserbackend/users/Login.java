package com.example.pantryparserbackend.users;

/**
 * basic login model for logins
 */
public class Login {
    protected String email;
    protected String password;

    protected Login (String email, String password){
        this.email = email;
        this.password = password;
    }
}
