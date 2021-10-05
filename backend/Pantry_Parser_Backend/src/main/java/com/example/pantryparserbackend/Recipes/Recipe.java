package com.example.pantryparserbackend.Recipes;

import javax.persistence.*;

import com.example.pantryparserbackend.users.User;
import org.springframework.lang.Nullable;

import java.util.Date;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int time;
    private String summary;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date created_date;
    @Nullable
    private double rating;
//    @Nullable
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "category_id")
//    private Category category;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    private User creator;

    public Recipe(String name, int time, String summary, String description, User creator)
    {
        this.name = name;
        this.time = time;
        this.summary = summary;
        this.description = description;
        this.created_date = new Date();
        this.rating = 0;
        this.creator = creator;
    }
    public Recipe(){}

    public int getId(){ return id; }
    public String getName() { return name; }
    public int getTime() { return time; }
    public String getSummary() { return summary; }
    public String getDescription() { return description; }
    public Date getCreated_date() { return created_date; }
    public double getRating() { return rating; }
//    public Category getCategory() { return category; }
    public User getCreator() { return creator; }

    //not including a set for created_date since this shouldn't be changed
    public void setId(int id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setTime(int time){ this.time = time; }
    public void setSummary(String summary){ this.summary = summary; }
    public void setDescription(String description){ this.description = description; }
    public void updateRating(){ /* get average of reviews */ }
//    public void setCategory(Category category) { this.category = category; }
    public void setCreator(User creator) { this.creator = creator; }
}
