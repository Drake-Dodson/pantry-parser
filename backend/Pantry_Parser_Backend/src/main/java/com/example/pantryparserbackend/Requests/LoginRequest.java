package com.example.pantryparserbackend.Requests;

/**
 * basic login model for logins
 */
public class LoginRequest {
    public String email;
    public String password;

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
}
