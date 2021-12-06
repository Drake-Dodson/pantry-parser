package com.example.pantryparserbackend.Ingredients;

import com.example.pantryparserbackend.Ingredients.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * basic repository for steps
 */
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    RecipeIngredient findById(int id);
    @Transactional
    void deleteById(int id);
}
