package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.Util.PasswordUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * basic user model
 */
@Entity
@Table(name = "users")
public class User {

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

    @Column(nullable = false)
    private String password;

    @Getter
    @Setter
    private String role;

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
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Recipe> favorites;

    @Getter
    @OneToMany(mappedBy = "reviewer")
    private List<Review> userReviews;

    public User(String password, String email) {
        this.password = PasswordUtil.newHash(password);
        this.email = email;
        this.role = "Main";
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
        this.password = PasswordUtil.newHash(password);
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
        return PasswordUtil.comparePasswords(password, this.password);
    }
}
