package com.salesianostriana.chefplanner.recipes.repository;


import com.salesianostriana.chefplanner.recipes.Dto.FeaturedCountDTO;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    Page<Recipe> findByAuthorId(Long authorId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT r" +
            " FROM Recipe r" +
            " WHERE r.featured = true")
    Page<Recipe> findFeaturedRecipes(Pageable pageable);

    Page<Recipe> findByAuthorUserUuid(String userUuid, Pageable pageable);

    @Query(value = "SELECT AVG(EXTRACT(EPOCH FROM minutes)) FROM recipes", nativeQuery = true)
    Double getAverageDuration();

// En RecipeRepository.java

// En RecipeRepository.java

    @Query("""
   SELECT new com.salesianostriana.chefplanner.recipes.Dto.FeaturedCountDTO(u.username, COUNT(r))
   FROM Recipe r
   JOIN User u ON r.author.userUuid = CAST(u.id AS string)
   WHERE r.featured = true
   GROUP BY u.username
   """)
    List<FeaturedCountDTO> countFeaturedRecipesPerUser();

    @Query("SELECT r FROM Recipe r JOIN FETCH r.author")
    Page<Recipe> findAllWithAuthor(Pageable pageable);

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredients WHERE r.id = :id")
    Optional<Recipe> findByIdWithIngredients(@Param("id") Long id);


}
