package com.example.pantryparserbackend.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface RecipeRepository extends JpaRepository <Recipe, Long> {

    Recipe findById(int id);

    @Query(value = "SELECT DISTINCT r.* FROM recipes r " +
            "JOIN recipe_ingredient ri ON r.id = ri.recipe_id " +
            "JOIN (SELECT * FROM ingredients " +
                "WHERE name IN :ingredients) " +
                "AS i ON i.id = ri.ingredient_id " +
            "GROUP BY r.id " +
            "ORDER BY count(*)/r.num_ingredients DESC",
            nativeQuery  = true)
    List<Recipe> getByIngredients(List<String> ingredients);

    @Transactional
    void deleteById(int id);
}
