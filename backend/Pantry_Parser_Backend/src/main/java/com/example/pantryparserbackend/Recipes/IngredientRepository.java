package com.example.pantryparserbackend.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for ingredients
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findById(int id);
    Ingredient findByName(String name);
}
