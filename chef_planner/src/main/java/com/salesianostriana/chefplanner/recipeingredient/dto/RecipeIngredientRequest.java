package com.salesianostriana.chefplanner.recipeingredient.dto;

import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredientId;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para crear o actualizar una relación entre receta e ingrediente")
public record RecipeIngredientRequest(
        @Schema(description = "ID de la receta", example = "1")
        Long recipeId,
        @Schema(description = "ID del ingrediente", example = "1")
        Long ingredientId,
        @Schema(description = "Cantidad del ingrediente", example = "2")
        int quantity,
        @Schema(description = "Unidad de medida del ingrediente", example = "kg")
        String unit
) {
    public static RecipeIngredient toEntity(RecipeIngredientRequest request) {
        RecipeIngredientId id = new RecipeIngredientId();

        id.setRecipeId(request.recipeId());
        id.setIngredientId(request.ingredientId());
        RecipeIngredient entity = new RecipeIngredient();

        entity.setId(id);
        entity.setQuantity(request.quantity());
        entity.setUnit(request.unit());
        return entity;
    }
}
