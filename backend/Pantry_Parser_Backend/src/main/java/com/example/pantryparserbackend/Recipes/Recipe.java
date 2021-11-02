package com.example.pantryparserbackend.Recipes;

import javax.persistence.*;

import com.example.pantryparserbackend.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.annotation.Order;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
    )
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

    @OneToMany(mappedBy = "recipe")
    @OrderBy("num")
    private List<Step> steps;

    public Recipe(String name, int time, String summary, String description, List<Step> steps)
    {
        this.name = name;
        this.time = time;
        this.summary = summary;
        this.description = description;
        this.created_date = new Date();
        this.rating = 0;
        this.steps = steps;
    }
    public Recipe(){}

    public int getId(){ return this.id; }
    public String getName() { return this.name; }
    public int getTime() { return this.time; }
    public String getSummary() { return this.summary; }
    public String getDescription() { return this.description; }
    public Date getCreated_date() { return this.created_date; }
    public double getRating() { return this.rating; }
    public String getCreatorName() { return this.creator.getDisplayName(); }
    public List<Ingredient> getIngredients() { return this.ingredients; }
    public int getNum_ingredients() { return this.num_ingredients; }
    public List<Step> getSteps() { return this.steps; }
    public Step getStepByOrder(int pos) { return this.steps.get(pos); }

    //not including a set for created_date since this shouldn't be changed
    public void setName(String name){ this.name = name; }
    public void setTime(int time){ this.time = time; }
    public void setSummary(String summary){ this.summary = summary; }
    public void setDescription(String description){ this.description = description; }
    public void updateRating(){ /* get average of reviews */ }
    public void setCreator(User creator) { this.creator = creator; }
    public void setCreatedDate() {
        this.created_date = new Date();
    }
    public void addIngredient(Ingredient i){
        this.num_ingredients++;
        this.ingredients.add(i);
    }
    public void removeIngredient(Ingredient i){
        this.num_ingredients--;
        this.ingredients.remove(i);
    }
    public void addStep(Step s){
        this.steps.add(s);
    }
    public void removeStep(Step s){
        this.steps.remove(s);
    }
    public void shiftStep(Step s, int pos) {
        this.steps.set(pos, s);
    }

    public void update(Recipe request) {
        this.setName(request.getName());
        this.setTime(request.getTime());
        this.setSummary(request.getSummary());
        this.setDescription(request.getDescription());
        this.updateRating();
    }
}
