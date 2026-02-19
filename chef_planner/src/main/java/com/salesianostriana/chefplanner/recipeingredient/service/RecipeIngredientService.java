package com.salesianostriana.chefplanner.recipeingredient.service;

import com.salesianostriana.chefplanner.ingredient.error.IngredientNotFoundException;
import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.recipeingredient.dto.RecipeIngredientRequest;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipeingredient.repository.RecipeIngredientRepository;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.recipes.service.RecipeService;
import com.salesianostriana.chefplanner.user.error.ProfileNotFoundException;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;
    private final UserProfileRepository userProfileRepository;



    public RecipeIngredient addIngredientToRecipe(Long recipeId,RecipeIngredientRequest request){
        Recipe recipe = recipeService.findById(recipeId);
        Ingredient ingredient = ingredientRepository.findById(request.ingredientId()).orElseThrow(
                () -> new IngredientNotFoundException(request.ingredientId()));
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity(request.quantity())
                .unit(request.unit())
                .build();
        recipe.addIngredient(recipeIngredient);
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

    public List<RecipeIngredient> mostrarIngredientesRecetas(Long idUser){
        List<RecipeIngredient> ingredientesReceta = new ArrayList<>();
        UserProfile userProfile = userProfileRepository.findById(idUser).orElseThrow(
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
