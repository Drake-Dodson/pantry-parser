package com.example.pantryparserbackend.Requests;

public class AdminRequest {
    public String adminPassword;
    public String adminEmail;
    public String role;

    public AdminRequest(String adminPassword, String adminEmail, String role){
        this.adminEmail = adminPassword;
        this.adminPassword = adminEmail;
        this.role = role;
    }
}
