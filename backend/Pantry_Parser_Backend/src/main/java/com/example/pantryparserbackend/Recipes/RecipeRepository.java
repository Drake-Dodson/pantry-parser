package com.example.pantryparserbackend.Recipes;

import com.example.pantryparserbackend.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * basic repository for recipes
 */
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {
    Recipe findById(int id);

    /**
     * gets all recipes by a search term
     * @param query input search term
     * @param pageable paginator settings
     * @return page of recipes
     */
    @Query(value = "SELECT r from Recipe r WHERE " +
            "lower(r.name) LIKE lower(CONCAT('%', :query, '%')) OR " +
            "lower(r.summary) LIKE lower(CONCAT('%', :query, '%'))")
    Page<Recipe> getAllBy(String query, Pageable pageable);

    /**
     * Finds a specific user's favorite recipes by a search term
     * @param user_id id of the user
     * @param pageable paginator settings
     * @param query input search term
     * @return the user's favorites
     */
    @Query(value = "SELECT r.* FROM recipes r " +
            "JOIN favorites f ON f.recipe_id = r.id " +
            "WHERE f.user_id = :user_id AND (" +
            "lower(r.name) LIKE lower(CONCAT('%', :query, '%')) OR " +
            "lower(r.summary) LIKE lower(CONCAT('%', :query, '%')))",
            nativeQuery = true)
    Page<Recipe> getUserFavoritesSearch(Integer user_id, String query, Pageable pageable);

    /**
     * Gets the recipes a user has created by a search term
     * @param user the user whose recipes we are looking at
     * @param pageable paginator settings
     * @param query input search term
     * @return list of recipes
     */
    @Query(value = "SELECT r FROM Recipe r WHERE " +
            "r.creator = :user AND (" +
            "lower(r.name) LIKE lower(CONCAT('%', :query, '%')) OR " +
            "lower(r.summary) LIKE lower(CONCAT('%', :query, '%')))")
    Page<Recipe> getUserCreatedSearch(User user, String query, Pageable pageable);

    /**
     * Finds a specific user's favorite recipes
     * @param user_id id of the user
     * @param pageable paginator settings
     * @return the user's favorites
     */
    @Query(value = "SELECT r.* FROM recipes r " +
            "JOIN favorites f ON f.recipe_id = r.id " +
            "WHERE f.user_id = :user_id ",
            nativeQuery = true)
    Page<Recipe> getUserFavorites(Integer user_id, Pageable pageable);

    /**
     * Gets the recipes a user has created
     * @param user the user whose recipes we are looking at
     * @param pageable paginator settings
     * @return list of recipes
     */
    @Query(value = "SELECT r FROM Recipe r WHERE " +
            "r.creator = ?1")
    Page<Recipe> getUserCreated(User user, Pageable pageable);

    /**
     * Does the pantry-parser functionality
     * @param ingredients array of strings
     * @return list of recipes matching ingredients
     */
    @Query(value = "SELECT DISTINCT r.* FROM recipes r " +
            "JOIN recipe_ingredient ri ON r.id = ri.recipe_id " +
            "JOIN (SELECT * FROM ingredients " +
                "WHERE name IN :ingredients) " +
                "AS i ON i.id = ri.ingredient_id " +
            "GROUP BY r.id " +
            "ORDER BY count(*)/r.num_ingredients DESC",
            nativeQuery  = true)
    Page<Recipe> getByIngredients(List<String> ingredients, Pageable pageable);

    /**
     * Finds a list of recipes attached to a specific ingredient
     * @param ingredient_id the id of the ingredient
     * @param pageable paginator settings
     * @return list of recipes containing the provided ingredient
     */
    @Query(value = "SELECT r.* FROM recipes r " +
            "JOIN recipe_ingredient ri ON ri.recipe_id = r.id " +
            "WHERE ri.ingredient_id = :ingredient_id ",
            nativeQuery = true)
    Page<Recipe> getByIngredient(Integer ingredient_id, Pageable pageable);

    /**
     * gets the recipes a user created and returns a list instead of a page
     * @param user creator
     * @return list of recipes
     */
    @Query(value = "SELECT r FROM Recipe r WHERE " +
            "r.creator = ?1")
    List<Recipe> getUserCreated(User user);

    /**
     * gets the recipes a user has favorited and returns a list instead of a page
     * @param user_id id of the user
     * @return list
     */
    @Query(value = "SELECT r.id FROM recipes r " +
            "JOIN favorites f ON f.recipe_id = r.id " +
            "WHERE f.user_id = :user_id ",
            nativeQuery = true)
    List<Integer> getUserFavoriteIds(Integer user_id);

    @Transactional
    void deleteById(int id);
}
