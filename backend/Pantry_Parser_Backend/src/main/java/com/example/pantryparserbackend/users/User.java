package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Reviews.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.Set;

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
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Recipe> favorites;

    @Getter
    @OneToMany(mappedBy = "reviewer")
    private List<Review> userReviews;

    public User(String password, String email) {
        this.password = this.newHash(password);
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
        this.password = this.newHash(password);
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
        String stored = this.password;
        String[] parts = stored.split(":");
        byte[] salt = fromHex(parts[1]);

        String passwordToTest = hash(password, salt);
        return stored.equals(passwordToTest);
    }

    /**
     * specifically for creating / changing a password, this method generates a new salt and calls the hash function
     * @param password input string for the new password
     * @return hashed value for the new password
     */
    private String newHash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return hash(password, salt);
    }

    /**
     * hashes the inputted password with the provided salt
     * @param password input password string
     * @param salt input random salt
     * @return hexed value of hashed password
     */
    private String hash(String password, byte[] salt) {
        int iterations = 65536;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 512);
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return iterations + ":" + toHex(salt) + ":" + toHex(factory.generateSecret(spec).getEncoded());
        }catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            //probably will never happen since these are permanently set, so shouldn't have to handle this
            return "encryption error";
        }
    }

    /**
     * converts a byte array to a hexidecimal string
     * @param array array of bytes
     * @return hexidecimal string
     */
    private static String toHex(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        }else {
            return hex;
        }
    }

    /**
     * converts a hexidecimal string to a byte array
     * @param hex hexidecimal string
     * @return byte array
     */
    private static byte[] fromHex(String hex){
        byte[] bytes = new byte[hex.length()/2];
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }
}
