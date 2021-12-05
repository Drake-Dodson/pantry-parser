package com.example.pantryparserbackend.Requests;

public class UserRequest {
    public String password;
    public String email;
    public String displayName;

    public UserRequest(String password, String email, String displayName) {
        this.password = password;
        this.email = email;
        this.displayName = displayName;
    }
}
