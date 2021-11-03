package com.example.pantryparserbackend.Recipes;

import com.example.pantryparserbackend.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {

    Recipe findById(int id);

    @Query(value = "SELECT DISTINCT r.* FROM recipes r " +
            "JOIN recipe_ingredient ri ON r.id = ri.recipe_id " +
            "JOIN (SELECT * FROM ingredients " +
                "WHERE name IN :ingredients) " +
                "AS i ON i.id = ri.ingredient_id " +
            "GROUP BY r.id " +
            "ORDER BY count(*)/r.num_ingredients DESC",
            nativeQuery  = true)
    Page<Recipe> getByIngredients(List<String> ingredients, Pageable pageable);

    @Query(value = "SELECT r.* FROM recipes r " +
            "JOIN favorites f ON f.recipe_id = r.id " +
            "WHERE f.user_id = :user_id ",
        nativeQuery = true)
    Page<Recipe> getUserFavorites(Integer user_id, Pageable pageable);

    @Query(value = "SELECT r FROM Recipe r WHERE " +
            "r.creator = ?1")
    Page<Recipe> getUserCreated(User user, Pageable pageable);

    @Query(value = "SELECT r.* FROM recipes r " +
            "JOIN recipe_ingredient ri ON ri.recipe_id = r.id " +
            "WHERE ri.ingredient_id = :ingredient_id ",
            nativeQuery = true)
    Page<Recipe> getByIngredient(Integer ingredient_id, Pageable pageable);
    @Transactional
    void deleteById(int id);
}
