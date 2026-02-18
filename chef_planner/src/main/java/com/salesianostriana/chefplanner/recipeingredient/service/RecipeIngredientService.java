package com.salesianostriana.chefplanner.recipeingredient.service;

import com.salesianostriana.chefplanner.ingredient.error.IngredientNotFoundException;
import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.recipeingredient.dto.RecipeIngredientRequest;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipeingredient.repository.RecipeIngredientRepository;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.service.RecipeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeService recipeService;



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
}
