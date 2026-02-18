package com.salesianostriana.chefplanner.ingredient.repository;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
