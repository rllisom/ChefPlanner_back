package com.salesianostriana.chefplanner.recipeingredient.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para un ingrediente de receta")
public record RecipeIngredientResponse(
        @Schema(description = "ID del ingrediente de receta", example = "1")
        Long id,
        @Schema(description = "Nombre del ingrediente", example = "Tomate")
        String nameIngredient,
        @Schema(description = "Cantidad del ingrediente", example = "2")
        int quantity,
        @Schema(description = "Unidad de medida del ingrediente", example = "kg")
        String unit
) {
}
