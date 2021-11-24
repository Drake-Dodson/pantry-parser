package com.example.pantryparserbackend.Recipes;

import javax.persistence.*;

import com.example.pantryparserbackend.Ingredients.Ingredient;
import com.example.pantryparserbackend.Reviews.Review;
import com.example.pantryparserbackend.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

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
    private int time;
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
    private double rating;
    @Getter
    @Setter
    private int num_ingredients;

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
    private List<User> favoritedBy;

    @Getter
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"})
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Ingredient> ingredients;

    @Getter
    @OrderBy("num")
    @OneToMany(mappedBy = "recipe")
    private List<Step> steps;

    /**
     * basic constructor for a recipe
     * @param name input name
     * @param time input time estimate
     * @param summary input summary
     * @param description input description
     */
    public Recipe(String name, int time, String summary, String description) {
        this.name = name;
        this.time = time;
        this.summary = summary;
        this.description = description;
        this.created_date = new Date();
        this.rating = 0;
        this.steps = new ArrayList<>();
        this.ingredients = new ArrayList<>();

        this.num_ingredients = 0;
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
        for (Review recipes_review : recipes_reviews) {
            total += recipes_review.getStarNumber();
        }
        this.rating = total / recipes_reviews.size();
    }

    // Not currently implemented

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
        if(recipes_reviews.size() == 0){
            this.rating = newRating;
        }
        else{
            double total = this.rating * (recipes_reviews.size() - 1);
            total += newRating;
            this.rating = total / (recipes_reviews.size());
        }
    }

    /**
     * updates the rating based on the current overall rating and the
     * number of stars in the removed review
     * has a constant runtime
     * @param rating the value of the removed rating
     */
    public void removeRating(int rating){
        if(recipes_reviews.size() == 0){
            this.rating = 0;
        }
        else {
            double total = this.rating * (recipes_reviews.size() + 1);
            total -= rating;
            this.rating = total / (recipes_reviews.size());
        }
    }

    /**
     * creates a link between the provided ingredient and
     * this recipe
     * @param i input ingredient
     */
    public void addIngredient(Ingredient i){
        this.num_ingredients++;
        this.ingredients.add(i);
    }
    /**
     * removes the link between the provided ingredient and
     * this recipe
     * @param i input ingredient
     */
    public void removeIngredient(Ingredient i){
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
        this.setName(request.name);
        this.setTime(request.time);
        this.setSummary(request.summary);
        this.setDescription(request.description);
    }
}
