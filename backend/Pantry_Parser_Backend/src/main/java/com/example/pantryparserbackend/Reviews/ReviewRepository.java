package com.example.pantryparserbackend.Reviews;

import com.example.pantryparserbackend.Recipes.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findById(int id);

}
