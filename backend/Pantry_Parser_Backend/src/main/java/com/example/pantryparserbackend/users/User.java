package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Reviews.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter
    @Setter
    private String displayName;

    @Column(unique = true)
    @Getter
    @Setter
    private String email;

    private String password;

    @Getter
    @Setter
    private String role;

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    private List<Recipe> created_recipes;

    @Getter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
    )
    @JsonIgnore
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

    public void setPassword(String password) {
        this.password = this.newHash(password);
    }
    public void favorite(Recipe favorite) {
        this.favorites.add(favorite);
    }
    public void unfavorite(Recipe favorite) {
        this.favorites.remove(favorite);
    }
    public void addRecipe(Recipe r){
        this.created_recipes.add(r);
    }

    public boolean authenticate(String password) {
        String stored = this.password;
        String[] parts = stored.split(":");
        byte[] salt = fromHex(parts[1]);

        String passwordToTest = hash(password, salt);
        return stored.equals(passwordToTest);
    }
    private String newHash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return hash(password, salt);
    }
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
    private static byte[] fromHex(String hex){
        byte[] bytes = new byte[hex.length()/2];
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }
}
