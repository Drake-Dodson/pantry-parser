package com.example.pantryparserbackend.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RecipeRepository extends JpaRepository <Recipe, Long> {
    Recipe findByID(int id);

    @Transactional
    void deleteById(int id);
}
