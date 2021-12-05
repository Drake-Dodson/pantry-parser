package com.example.pantryparserbackend.Requests;

public class UserRequest {
    public String password;
    public String displayName;
    public String email;

    public UserRequest(String password, String displayName, String email) {
        this.password = password;
        this.displayName = displayName;
        this.email = email;
    }
}
