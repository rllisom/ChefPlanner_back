package com.salesianostriana.chefplanner.ingredient.repository;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, JpaSpecificationExecutor<Ingredient> {


    boolean existsByName(String name);


    @Query("SELECT i FROM Ingredient i WHERE i.id NOT IN " +
            "(SELECT pi.id FROM UserProfile up JOIN up.pantryIngredients pi WHERE up.userUuid = :uuid)")
    Page<Ingredient> findIngredientsNotInPantry(@Param("uuid") String uuid, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);

}
