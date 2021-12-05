package com.example.pantryparserbackend.Reviews;

import com.example.pantryparserbackend.Recipes.Recipe;
import com.example.pantryparserbackend.Users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * repository for the reviews
 */
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {
    Review findById(int id);

    /**
     * Finds the reviews a specific user rote
     * @param user the user
     * @param pageable paginator settings
     * @return list of reviews
     */
    @Query("Select r FROM Review r " +
            "WHERE r.reviewer = :user")
    Page<Review> findByUser(User user, Pageable pageable);
    /**
     * Finds the reviews written about a specific recipe
     * @param recipe the recipe
     * @param pageable paginator settings
     * @return list of reviews
     */
    @Query("Select r FROM Review r " +
            "WHERE r.recipe_reviewed = :recipe")
    Page<Review> fingByRecipe(Recipe recipe, Pageable pageable);

}
