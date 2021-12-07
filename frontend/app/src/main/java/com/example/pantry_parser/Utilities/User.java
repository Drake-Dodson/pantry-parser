package com.example.pantry_parser.Utilities;

public class User {
    private int id;
    private String username, email, password;
    public static final String DESIGNATION_ADMIN = "admin";
    public static final String DESIGNATION_CHEF = "chef";
    public static final String DESIGNATION_MAIN = "main";
    public static final String[] ROLES = {
            DESIGNATION_ADMIN,
            DESIGNATION_CHEF,
            DESIGNATION_MAIN
    };

    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(){
        this.password = password;
    }

}
