package com.example.pantryparserbackend.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface RecipeRepository extends JpaRepository <Recipe, Long> {

    Recipe findById(int id);

    @Transactional
    void deleteById(int id);
}
