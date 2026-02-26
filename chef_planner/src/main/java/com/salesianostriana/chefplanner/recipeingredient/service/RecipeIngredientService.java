package com.salesianostriana.chefplanner.recipeingredient.service;

import com.salesianostriana.chefplanner.ingredient.error.IngredientNotFoundException;
import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.recipeingredient.dto.RecipeIngredientRequest;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredientId;
import com.salesianostriana.chefplanner.recipeingredient.repository.RecipeIngredientRepository;
import com.salesianostriana.chefplanner.recipes.error.RecipeNotFoundException;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.recipes.service.RecipeService;
import com.salesianostriana.chefplanner.user.error.ProfileNotFoundException;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;
    private final UserProfileRepository userProfileRepository;


    @Transactional
    public RecipeIngredient addIngredientToRecipe(Long recipeId, RecipeIngredientRequest request) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Receta no encontrada"));
        Hibernate.initialize(recipe.getIngredients());
        Ingredient ingredient = ingredientRepository.findById(request.ingredientId()).orElseThrow(
                () -> new IngredientNotFoundException(request.ingredientId()));
        RecipeIngredientId id = new RecipeIngredientId();
        id.setRecipeId(recipeId);
        id.setIngredientId(request.ingredientId());

        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .id(id)
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity(request.quantity())
                .unit(request.unit())
                .build();
        return recipeIngredientRepository.save(recipeIngredient);
    }


    public void eliminarIngredienteDeReceta(Long recetaid, Long ingredienteid) {
        List<RecipeIngredient> ingredientes = recipeIngredientRepository.findByIdRecipeId(recetaid);
        Recipe recipe = recipeService.findById(recetaid);
        Ingredient ingredient = ingredientRepository.findById(ingredienteid).orElseThrow(
                () -> new IngredientNotFoundException(ingredienteid));
        for (RecipeIngredient ri : ingredientes) {
            if (ri.getIngredient().getId().equals(ingredienteid)) {
                recipeIngredientRepository.delete(ri);
                recipe.removeIngredient(ri);
                break;
            }
        }
    }

    public List<RecipeIngredient> obtenerRecipeIngredients(Long id ){
        return recipeIngredientRepository.findByRecipe_Id(id);
    }

    @Transactional
    public List<RecipeIngredient> mostrarIngredientesRecetas(UUID idUser){
        List<RecipeIngredient> ingredientesReceta = new ArrayList<>();
        UserProfile userProfile = userProfileRepository.findByUserUuid(idUser.toString()).orElseThrow(
                () -> new ProfileNotFoundException("Usuario no encontrado"));


        List<RecipeIngredient> ingredientesQueNecesitaParaRecetas = userProfile.getRecipes().stream()
                .map(Recipe::getIngredients)
                .flatMap(List::stream)
                .toList();

        List<Ingredient> ingredientesEnDespensa = userProfile.getPantryIngredients();

        for (RecipeIngredient ri : ingredientesQueNecesitaParaRecetas) {
            if (!ingredientesEnDespensa.contains(ri.getIngredient())) {
                ingredientesReceta.add(ri);
            }
        }

        if(ingredientesReceta.isEmpty()) {
            throw new IllegalArgumentException("La despensa del usuario tiene todos los ingredientes necesarios para las recetas.");
        }

        return ingredientesReceta;
    }






}
