package com.example.pantryparserbackend.Recipes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for ingredients
 */
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Long> {

    Ingredient findById(int id);
    Ingredient findByName(String name);

	@Query (value = "SELECT i FROM Ingredient i WHERE " +
			"lower(i.name) LIKE lower(CONCAT('%', :query, '%'))")
	Page<Ingredient> findAllSearch(String query, Pageable pageable);
}
