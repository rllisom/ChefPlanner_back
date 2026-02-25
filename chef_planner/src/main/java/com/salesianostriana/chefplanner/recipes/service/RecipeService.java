package com.salesianostriana.chefplanner.recipes.service;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import com.salesianostriana.chefplanner.menuitem.repository.MenuItemRepository;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipes.Dto.FeaturedCountDTO;
import com.salesianostriana.chefplanner.recipes.Dto.RecipeSearchRequest;
import com.salesianostriana.chefplanner.recipes.error.RecipeNotFoundException;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {
    private final RecipeRepository repository;

    private final UserProfileRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public Recipe save(Recipe recipe, Long authorId) {

        recipe.setAuthor(userRepository.getReferenceById(authorId));

        return repository.save(recipe);
    }

    @jakarta.transaction.Transactional
    public Recipe saveDirectly(Recipe recipe) {
        return repository.save(recipe);
    }
    @Transactional(readOnly = true)
    public Page<Recipe> findAll(Pageable pageable) {
        return repository.findAllWithAuthor(pageable);
    }


    public Page<Recipe> findMyRecipes(Long authorId, Pageable pageable) {
        return repository.findByAuthorId(authorId, pageable);
    }

    public Recipe findById(Long id) {
        Recipe recipe = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta no encontrada"));
        Hibernate.initialize(recipe.getIngredients()); // ← añadir
        return recipe;
    }


    //filtro
    public List<Recipe> buscarRecetasConDTO(RecipeSearchRequest search) {

        Duration duration = (search.maxMinutes() != null)
                ? Duration.ofMinutes(search.maxMinutes())
                : null;

        return repository.findAll(
                Recipe.Specs.dificultad(search.difficulty())
                        .and(Recipe.Specs.tiempoMaximo(duration))
        );
    }

    @Transactional
    public Recipe edit(Long id, Recipe recipe) {
        Recipe originalRecipe = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receta no encontrada"));

        Hibernate.initialize(originalRecipe.getIngredients());

        originalRecipe.setTitle(recipe.getTitle());
        originalRecipe.setDescription(recipe.getDescription());
        originalRecipe.setMinutes(recipe.getMinutes());
        originalRecipe.setDifficulty(recipe.getDifficulty());

        return originalRecipe;
    }

    //Buscar por texto
    public List<Recipe> searchRecipesText(String texto) {
        return repository.findAll()
                .stream()
                .filter(r -> r.getTitle().toLowerCase().contains(texto.toLowerCase()))
                .toList();
    }

    public Page<Recipe> findByFeatured(Pageable pageable){
        return repository.findFeaturedRecipes(pageable);
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


    public Page<Recipe> findByAuthor(String userUuid, Pageable pageable) {
        return repository.findByAuthorUserUuid(userUuid, pageable);
    }


    public double cantRecetas(){
        return repository.findAll().stream()
                .count();
    }
    @Transactional(readOnly = true)
    public double tiempoMedioRecetas() {
        Double resultado = repository.getAverageDuration();

        return resultado != null ? resultado : 0.0;
    }

    @Transactional(readOnly = true)
    public List<FeaturedCountDTO> obtenerRecetasDestacadasPorUsuario() {
        return repository.countFeaturedRecipesPerUser();
    }


    //Eliminar receta (ADMIN y User)
    @Transactional
    public void deleteRecipe(Long id) {
        if (!repository.existsById(id)) {
            throw new RecipeNotFoundException("Receta no encontrada");
        }

        Recipe recipe = findById(id);


        List<MenuItem> menuItems = menuItemRepository.findMenuItemByRecipe_Id(id);
        for (MenuItem menuItem : menuItems) {
            menuItem.setRecipe(null);
        }
        menuItemRepository.saveAll(menuItems);

        recipe.getAuthor().getRecipes().remove(recipe);
        repository.deleteById(id);
    }

}
