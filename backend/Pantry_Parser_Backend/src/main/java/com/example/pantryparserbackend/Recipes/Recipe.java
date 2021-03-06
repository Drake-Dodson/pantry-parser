package com.example.pantryparserbackend.Recipes;

import javax.persistence.*;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Ingredients.RecipeIngredient;
import com.example.pantryparserbackend.Requests.RecipeRequest;
import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Basic recipe model
 */
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @Getter
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;
    @Getter
    @Setter
    @Column(nullable = false)
    private String summary;
    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "text")
    private String description;
    @Getter
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;
    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "int default 0")
    private int num_ingredients;
    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "bool default false")
    private boolean chef_verified;
    @Getter
    @Setter
    private String nutrition_facts;
    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "int default 0")
    private int num_servings;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "int default 0")
    private int prep_time;
    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "int default 0")
    private int cook_time;
    @Getter
    @Formula("prep_time + cook_time")
    private int time;

    @Getter
    @Column(nullable = false, columnDefinition = "int default 0")
    private int num_reviews;
    @Getter
    @Column(nullable = false, columnDefinition = "double default 0.0")
    private double rating;

    @Getter
    @Setter
    private String imagePath;

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "creator_id")
    private User creator;
    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "recipe_reviewed")
    private List<Review> recipes_reviews;
    @JsonIgnore
    @ManyToMany(mappedBy = "favorites")
    @Getter
    private List<User> favoritedBy;
    @Getter
    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> ingredients;
    @Getter
    @OrderBy("num")
    @OneToMany(mappedBy = "recipe")
    private List<Step> steps;

    /**
     * basic constructor for a recipe
     * @param name input name
     * @param prep_time input time estimate
     * @param summary input summary
     * @param description input description
     */
    public Recipe(String name, int prep_time, String summary, String description) {
        this.name = name;
        this.prep_time = prep_time;
        this.summary = summary;
        this.description = description;
        this.created_date = new Date();
        this.steps = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.num_reviews = 0;
        this.num_ingredients = 0;
        this.chef_verified = false;
    }

    public Recipe(String name,
                  int prep_time,
                  int cook_time,
                  String summary,
                  String description,
                  Date created_date,
                  int num_reviews,
                  double rating,
                  int num_servings,
                  String nutrition_facts,
                  boolean chef_verified)
    {
        this.name            = name;
        this.prep_time       = prep_time;
        this.summary         = summary;
        this.description     = description;
        this.created_date    = created_date;
        this.steps           = new ArrayList<>();
        this.ingredients     = new ArrayList<>();
        this.num_reviews     = num_reviews;
        this.num_ingredients = 0;
        this.rating          = rating;
        this.num_servings    = num_servings;
        this.cook_time       = cook_time;
        this.nutrition_facts = nutrition_facts;
        this.chef_verified   = chef_verified;
    }

    public Recipe(RecipeRequest request) {
        this.created_date = new Date();
        this.steps = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.num_reviews = 0;
        this.num_ingredients = 0;
        this.chef_verified = false;
        this.update(request);
    }

    public Recipe(){}

    @JsonIgnore
    public List<Review> getRecipeReviews () {
        return this.recipes_reviews;
    }

    /**
     * Gets the id of the user that created this recipe
     * @return int Creator id
     */
    public int getCreatorId(){
        if (creator != null)
            return creator.getId();
        return -1;
    }

    /**
     * Gets the name of the user that created this recipe
     * @return String Creator name
     */
    public String getCreatorName() {
        if (creator != null)
            return creator.getDisplayName();
        return "[DELETED_USER]";
    }

    /**
     * Returns the step at a certain position
     * @param pos step number we are looking for
     * @return the step at the provided position
     */
    public Step getStepByOrder(int pos) { return this.steps.get(pos); }

    /**
     * Sets the created date to the current date
     * This is the only setter for this property, since it shouldn't
     * be changed ever
     */
    public void setCreatedDate() {
        this.created_date = new Date();
    }

    /**
     * updates the rating property of this recipe based on its reviews
     * works perfectly, but has an O(n) runtime
     */
    public void updateRating0N(){
        double total = 0;
        this.num_reviews = recipes_reviews.size();
        for (Review recipes_review : recipes_reviews) {
            total += recipes_review.getStarNumber();
        }
        this.rating = total / recipes_reviews.size();
    }

    /**
     * updates the rating based on the current rating, the
     * original number of stars a review had, and the updated number
     * has a constant runtime
     * @param oldRating the previous value for this review
     * @param newRating the new value for this review
     */
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

    /**
     * updates the rating based on the current overall rating and the
     * number of stars in the new review
     * has a constant runtime
     * @param newRating the new star value for the rating
     */
    public void addRating(int newRating){
        if(this.num_reviews == 0){
            this.rating = newRating;
            this.num_reviews++;
        }
        else{
            double total = this.rating * this.num_reviews;
            total += newRating;
            this.num_reviews++;
            this.rating = total / this.num_reviews;
        }
    }

    /**
     * updates the rating based on the current overall rating and the
     * number of stars in the removed review
     * has a constant runtime
     * @param removedRating the value of the removed rating
     */
    public void removeRating(int removedRating){
        if(this.num_reviews <= 1){
            this.num_reviews--;
            this.rating = 0;
        }
        else {
            double total = this.rating * this.num_reviews;
            total -= removedRating;
            this.num_reviews--;
            this.rating = total / this.num_reviews;
        }
    }

    /**
     * creates a link between the provided ingredient and
     * this recipe
     * @param i input ingredient
     */
    public void addIngredient(RecipeIngredient i){
        this.num_ingredients++;
        this.ingredients.add(i);
    }
    /**
     * removes the link between the provided ingredient and
     * this recipe
     * @param i input ingredient
     */
    public void removeIngredient(RecipeIngredient i){
        this.num_ingredients--;
        this.ingredients.remove(i);
    }
    /**
     * creates a link between the provided step and
     * this recipe
     * @param s input step
     */
    public void addStep(Step s){
        this.steps.add(s);
    }
    /**
     * removes a link between the provided step and
     * this recipe
     * @param s input step
     */
    public void removeStep(Step s){
        this.steps.remove(s);
    }
    /**
     * changes the position of the provided step on this recipe
     * @param s input step
     * @param pos new position
     */
    public void shiftStep(Step s, int pos) {
        this.steps.set(pos, s);
    }

    /**
     * Updates a recipe based on a batch of values
     * @param request the new recipe values
     */
    public void update(RecipeRequest request) {
        this.prep_time       = request.prep_time;
        this.cook_time       = request.cook_time;
        this.name            = request.name;
        this.nutrition_facts = request.nutrition_facts;
        this.summary         = request.summary;
        this.description     = request.description;
    }

    /**
     * returns index of ingredient i if it has it, otherwise null
     * @param i
     * @return
     */
    public RecipeIngredient getIngredient(Ingredient i) {
        for (RecipeIngredient ri : this.ingredients) {
            if(ri.getIngredient().equals(i)) {
                return ri;
            }
        }
        return null;
    }
}
