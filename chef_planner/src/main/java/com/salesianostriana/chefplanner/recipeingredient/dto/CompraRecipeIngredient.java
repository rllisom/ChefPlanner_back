package com.salesianostriana.chefplanner.recipeingredient.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un ingrediente necesario para hacer recetas, con su nombre y la cantidad total.")
public record CompraRecipeIngredient(
        @Schema(description = "Nombre del ingrediente", example = "Harina")
        String ingredientName,
        @Schema(description = "Cantidad total del ingrediente necesaria para hacer las recetas", example = "500")
        int quantityTotal,
        @Schema(description = "Unidad de medida del ingrediente", example = "gramos")
        String unit
) {

}
