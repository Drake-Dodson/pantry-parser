package com.example.pantryparserbackend.Reviews;

import com.example.pantryparserbackend.Recipes.Ingredient;
import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {

    Review findById(int id);

    @Query("Select r FROM Review r " +
            "WHERE r.reviewer = :user")
    Page<Review> findByUser(User user, Pageable pageable);
    @Query("Select r FROM Review r " +
            "WHERE r.recipe_reviewed = :recipe")
    Page<Review> fingByRecipe(Recipe recipe, Pageable pageable);

}
