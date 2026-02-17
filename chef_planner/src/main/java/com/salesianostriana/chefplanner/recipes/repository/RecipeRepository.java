package com.salesianostriana.chefplanner.recipes.repository;


import com.salesianostriana.chefplanner.recipes.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // TODO: Uncomment when User entity is implemented
    // Page<Recipe> findByAuthorId(Long authorId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT r" +
            " FROM Recipe r" +
            " WHERE r.featured = true")
    Page<Recipe> findFeaturedRecipes(Pageable pageable);

    Page<Recipe> findByFeaturedTrue(Pageable pageable);


}
