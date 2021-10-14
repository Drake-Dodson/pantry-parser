package com.example.pantryparserbackend.Recipes;

import javax.persistence.*;

import com.example.pantryparserbackend.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Entity
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
    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User creator;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> favoritedBy;

    public Recipe(String name, int time, String summary, String description)
    {
        this.name = name;
        this.time = time;
        this.summary = summary;
        this.description = description;
        this.created_date = new Date();
        this.rating = 0;
    }
    public Recipe(){}

    public int getId(){ return id; }
    public String getName() { return name; }
    public int getTime() { return time; }
    public String getSummary() { return summary; }
    public String getDescription() { return description; }
    public Date getCreated_date() { return created_date; }
    public double getRating() { return rating; }
    public String getCreatorName() { return creator.getDisplayName(); }

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

    public void update(Recipe request) {
        this.setName(request.getName());
        this.setTime(request.getTime());
        this.setSummary(request.getSummary());
        this.setDescription(request.getDescription());
        this.updateRating();
    }
}
