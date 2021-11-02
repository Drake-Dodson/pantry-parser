package com.example.pantryparserbackend.Recipes;

import javax.persistence.*;

import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int time;
    private String summary;
    private String description;

    // Used for recipe score
    private int numberOfReviews;
    private int totalStars;
    private int currentPos;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;
    @Nullable
    private double rating;
    @Nullable
    private int num_ingredients;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User creator;

    @Setter
    @OneToMany(mappedBy = "recipe_reviewed")
    private List<Review> recipes_reviews;

    @ManyToMany(mappedBy = "favorites")
    @JsonIgnore
    private List<User> favoritedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"})
    )
    private List<Ingredient> ingredients;


    public Recipe(String name, int time, String summary, String description)
    {
        this.name = name;
        this.time = time;
        this.summary = summary;
        this.description = description;
        this.created_date = new Date();
        this.rating = 0;
        // Used for recipe score
        this.numberOfReviews = 0;
        this.totalStars = 0;
        this.currentPos = 0;
    }

    public Recipe(){}

    public int getId(){ return this.id; }
    public int getCreatorId(){ return creator.getId(); }
    public String getName() { return this.name; }
    public int getTime() { return this.time; }
    public String getSummary() { return this.summary; }
    public String getDescription() { return this.description; }
    public Date getCreated_date() { return this.created_date; }
    public double getRating() { return this.rating; }
    public String getCreatorName() { return this.creator.getDisplayName(); }
    public List<Ingredient> getIngredients() { return this.ingredients; }
    public int getNum_ingredients() { return this.num_ingredients; }
    public List<Review>getRecipeReviews() {return this.recipes_reviews; }

    //not including a set for created_date since this shouldn't be changed
    public void setName(String name){ this.name = name; }
    public void setTime(int time){ this.time = time; }
    public void setSummary(String summary){ this.summary = summary; }
    public void setDescription(String description){ this.description = description; }
    public void setCreator(User creator) { this.creator = creator; }
    public void setCreatedDate() {
        this.created_date = new Date();
    }

    public void updateRating0N(){
        double total = 0;
        for (Review recipes_review : recipes_reviews) {
            total += recipes_review.getStarNumber();
        }
        this.rating = total / recipes_reviews.size();
    }

    // Not currently implemented
    public void updateRating(int oldRating, int newRating){
        if(recipes_reviews.size() <= 1){
            this.rating = newRating;
        }
        else{
            double total = this.rating * recipes_reviews.size();
            total -= oldRating;
            total += newRating;
            this.rating = total / recipes_reviews.size();
        }
    }

    public void addRating(int newRating){
        if(recipes_reviews.size() == 0){
            this.rating = newRating;
        }
        else{
            double total = this.rating * (recipes_reviews.size() - 1);
            total += newRating;
            this.rating = total / (recipes_reviews.size());
        }
    }

    public void removeRating(int newRating){
        if(recipes_reviews.size() == 0){
            this.rating = 0;
        }
        else {
            double total = this.rating * recipes_reviews.size();
            total -= newRating;
            this.rating = total / (recipes_reviews.size() - 1);
        }
    }

    public void addIngredient(Ingredient i){
        this.num_ingredients++;
        this.ingredients.add(i);
    }
    public void removeIngredient(Ingredient i){
        this.num_ingredients--;
        this.ingredients.remove(i);
    }

    public void update(Recipe request) {
        this.setName(request.getName());
        this.setTime(request.getTime());
        this.setSummary(request.getSummary());
        this.setDescription(request.getDescription());
    }
}
