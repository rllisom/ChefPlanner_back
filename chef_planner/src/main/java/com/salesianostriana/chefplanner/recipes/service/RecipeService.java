package com.salesianostriana.chefplanner.recipes.service;

import com.salesianostriana.chefplanner.recipes.Dto.RecipeRequest;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {
    private final RecipeRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public Recipe save(Recipe recipe, Long authorId) {

        recipe.setAuthor(userRepository.getReferenceById(authorId));

        return repository.save(recipe);
    }

    public Page<Recipe> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Recipe> findMyRecipes(Long authorId, Pageable pageable) {
        return repository.findByAuthorId(authorId, pageable);
    }

    public Recipe findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta no encontrada"));
    }

    @Transactional
    public Recipe edit(Long id, Recipe recipe) {

        Recipe originalRecipe = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta no encontrada"));

        originalRecipe.setTitle(recipe.getTitle());
        originalRecipe.setDescription(recipe.getDescription());
        originalRecipe.setMinutes(recipe.getMinutes());
        originalRecipe.setDifficulty(recipe.getDifficulty());

        return originalRecipe;

    }

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
