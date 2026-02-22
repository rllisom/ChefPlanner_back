package com.salesianostriana.chefplanner.recipes.repository;


import com.salesianostriana.chefplanner.recipes.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    Page<Recipe> findByAuthorId(Long authorId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT r" +
            " FROM Recipe r" +
            " WHERE r.featured = true")
    Page<Recipe> findFeaturedRecipes(Pageable pageable);

    Page<Recipe> findByAuthorUserUuid(String userUuid, Pageable pageable);

    @Query("SELECT AVG(r.minutes) " +
            "FROM Recipe r")
    Double getAverageDuration();







}
