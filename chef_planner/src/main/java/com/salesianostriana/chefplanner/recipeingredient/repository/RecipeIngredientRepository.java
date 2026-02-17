package com.salesianostriana.chefplanner.recipeingredient.repository;

import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient,Long> {


    List<RecipeIngredient> findByIdRecipeId(Long recipeId);
}
