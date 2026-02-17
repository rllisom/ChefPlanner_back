package com.salesianostriana.chefplanner.recipes.service;

import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: Uncomment when RecipeRequest DTO is implemented
// import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {
    private final RecipeRepository repository;
    // TODO: Add UserRepository when User entity is implemented
    // private final UserRepository userRepository;

    // TODO: Implement when RecipeRequest DTO and User entity are available
    /*
    @Transactional
    public Recipe save(RecipeRequest dto, Long authorId) {
        User author = userRepository.getReferenceById(authorId);

        Recipe recipe = Recipe.builder()
                .title(dto.title())
                .description(dto.description())
                .minutes(Duration.ofMinutes(dto.minutes()))
                .difficulty(dto.difficulty())
                .author(author)
                .featured(false)
                .build();

        return repository.save(recipe);
    }
    */

    public Page<Recipe> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // TODO: Uncomment when User entity is implemented
    /*
    public Page<Recipe> findMyRecipes(Long authorId, Pageable pageable) {
        return repository.findByAuthorId(authorId, pageable);
    }
    */

    public Recipe findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta no encontrada"));
    }

    // TODO: Implement when RecipeRequest DTO is available
    /*
    @Transactional
    public Recipe edit(Long id, RecipeRequest dto) {
        Recipe recipe = findById(id);

        recipe.setTitle(dto.title());
        recipe.setDescription(dto.description());
        recipe.setMinutes(Duration.ofMinutes(dto.minutes()));
        recipe.setDifficulty(dto.difficulty());

        return recipe;
    }
    */

    @Transactional
    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    @Transactional
    public void toggleFeatured(Long id) {
        Recipe recipe = findById(id);
        recipe.setFeatured(!recipe.isFeatured());
    }


}
