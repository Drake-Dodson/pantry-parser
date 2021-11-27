package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Recipes.Recipe;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Basic repository for users
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findById(int id);
    User findByEmail(String email);

    /**
     * gets all the users that favorited the provided recipe
     * @param recipe
     * @return
     */
    @Query(value = "SELECT u FROM User u JOIN u.favorites r WHERE r = :recipe")
    List<User> findByFavorited(Recipe recipe);

    /**
     * causes a user to unfavorite a recipe
     * @param user_id id of the user
     * @param recipe_id id of the recipe
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favorites WHERE user_id = :user_id AND recipe_id = :recipe_id", nativeQuery = true)
    void deleteFavorite(int user_id, int recipe_id);

    /**
     * causes a user to favorite a recipe
     * @param user_id id of the user
     * @param recipe_id id of the recipe
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO favorites (user_id, recipe_id) VALUES (:user_id, :recipe_id)", nativeQuery = true)
    void addFavorite(int user_id, int recipe_id);
}
