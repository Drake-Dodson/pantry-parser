package com.example.pantryparserbackend.Users;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.Services.PasswordService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * basic user model
 */
@Entity
@Table(name = "users")
public class User {
    public static String DESIGNATION_ADMIN = "admin";
    public static String DESIGNATION_CHEF = "chef";
    public static String DESIGNATION_MAIN = "main";
    public static String[] ROLES = {
            DESIGNATION_ADMIN,
            DESIGNATION_CHEF,
            DESIGNATION_MAIN
    };
    private static String[] DEFAULT_ADMINS = {
            "pbrink21@iastate.edu",
            "adang@iastate.edu",
            "dwdodson@iastate.edu",
            "jvandrie@iastate.edu"
    };

    @Id
    @Getter
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String displayName;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "bool default false")
    private boolean email_verified;

    @Column(nullable = false)
    private String password;

    @Getter
    @Setter
    private String role;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<Recipe> created_recipes;

    @Getter
    @JsonIgnore
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Recipe> favorites;

    @Getter
    @OneToMany(mappedBy = "reviewer")
    private List<Review> userReviews;

    public User(String password, String email, String displayName) {
        this.password = PasswordUtil.newHash(password);
        this.email = email;
        this.displayName = displayName;
        if(Arrays.asList(DEFAULT_ADMINS).contains(email)) {
            this.role = User.DESIGNATION_ADMIN;
        } else {
            this.role = User.DESIGNATION_MAIN;
        }
        this.created_recipes = new ArrayList<>();
    }

    public User(String password, String email) {
        this.password = PasswordService.newHash(password);
        this.email = email;
        if(Arrays.asList(DEFAULT_ADMINS).contains(email)) {
            this.role = User.DESIGNATION_ADMIN;
        } else {
            this.role = User.DESIGNATION_MAIN;
        }
        this.displayName = "New User";
        this.created_recipes = new ArrayList<>();
    }

    public User() {}

    public List<Recipe> getRecipes() {
        return this.created_recipes;
    }

    /**
     * encrypts the provided password and stores it in the user model
     * @param password input password
     */
    public void setPassword(String password) {
        this.password = PasswordService.newHash(password);
    }

    /**
     * adds a recipe to the user's favorites
     * @param favorite new recipe to favorite
     */
    public void favorite(Recipe favorite) {
        this.favorites.add(favorite);
    }

    /**
     * removes a recipe to the user's favorites
     * @param favorite new recipe to unfavorite
     */
    public void unfavorite(Recipe favorite) {
        this.favorites.remove(favorite);
    }

    /**
     * adds a recipe to the user's created list
     * @param r new recipe to add
     */
    public void addRecipe(Recipe r){
        this.created_recipes.add(r);
    }

    /**
     * hashes the inputted password and checks it against what is stored in the database
     * @param password input password guess
     * @return true if matching, false if not
     */
    public boolean authenticate(String password) {
        return PasswordService.comparePasswords(password, this.password);
    }

    public boolean hasRole(String role) {
        return this.role.equals(role);
    }

    public boolean isAdmin() {
        return this.hasRole(User.DESIGNATION_ADMIN);
    }

    public boolean isChef() {
        return this.hasRole(User.DESIGNATION_CHEF);
    }

    public boolean equals(User user) {
        return this.id == user.getId();
    }
}
