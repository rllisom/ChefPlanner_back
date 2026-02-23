package com.salesianostriana.chefplanner.recipes.repository;


import com.salesianostriana.chefplanner.recipes.Dto.FeaturedCountDTO;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    Page<Recipe> findByAuthorId(Long authorId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT r" +
            " FROM Recipe r" +
            " WHERE r.featured = true")
    Page<Recipe> findFeaturedRecipes(Pageable pageable);

    Page<Recipe> findByAuthorUserUuid(String userUuid, Pageable pageable);

    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM minutes)) FROM recipe", nativeQuery = true)
    Double getAverageDuration();

    @Query("""
       SELECT new com.salesianostriana.chefplanner.recipes.Dto.FeaturedCountDTO(r.author.userUuid, COUNT(r))
       FROM Recipe r
       WHERE r.featured = true
       GROUP BY r.author.userUuid
       """)
    List<FeaturedCountDTO> countFeaturedRecipesPerUser();






}
