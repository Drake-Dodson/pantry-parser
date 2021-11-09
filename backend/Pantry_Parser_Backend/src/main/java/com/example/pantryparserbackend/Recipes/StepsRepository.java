package com.example.pantryparserbackend.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * basic repository for steps
 */
public interface StepsRepository extends JpaRepository<Step, Long> {
    Step findById(int id);
    @Transactional
    void deleteById(int id);
}
