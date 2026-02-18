package com.salesianostriana.chefplanner.ingredient.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cuerpo del request necesario para crear o editar a un ingrediente")
public record IngredientRequest(
        @Schema(description = "Nombre del ingrediente", example = "Tomate")
        String name
) {}
