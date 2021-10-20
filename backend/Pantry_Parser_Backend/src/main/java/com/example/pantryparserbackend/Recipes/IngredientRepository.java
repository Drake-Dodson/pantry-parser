package com.example.pantryparserbackend.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findById(int id);
    Ingredient findByName(String name);
}
