package com.salesianostriana.chefplanner.recipeingredient.dto;

import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para un ingrediente de receta")
public record RecipeIngredientResponse(
        @Schema(description = "ID del ingrediente", example = "1")
        Long ingredientId,
        @Schema(description = "Nombre del ingrediente", example = "Tomate")
        String nameIngredient,
        @Schema(description = "Cantidad del ingrediente", example = "2")
        int quantity,
        @Schema(description = "Unidad de medida del ingrediente", example = "kg")
        String unit
) {
        public static RecipeIngredientResponse of(RecipeIngredient ri) {
                return new RecipeIngredientResponse(
                        ri.getIngredient().getId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity(),
                        ri.getUnit()
                        );
        }
}
