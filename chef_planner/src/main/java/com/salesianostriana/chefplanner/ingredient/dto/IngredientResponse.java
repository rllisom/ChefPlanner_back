package com.salesianostriana.chefplanner.ingredient.dto;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta para mostrar un ingrediente")
public record IngredientResponse(
        @Schema(description = "Identificador del ingrediente",example = "1")
        Long id,
        @Schema(description = "Nombre del ingrediente",example = "Tomate")
        String nombre
) {
    public static IngredientResponse of(Ingredient ingredient){
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName()
        );
    }
}
