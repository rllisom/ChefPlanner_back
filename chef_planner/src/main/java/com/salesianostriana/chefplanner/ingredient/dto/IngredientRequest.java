package com.salesianostriana.chefplanner.ingredient.dto;

import com.salesianostriana.chefplanner.ingredient.validation.UniqueIngredient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Cuerpo del request necesario para crear o editar a un ingrediente")
public record IngredientRequest(
        @NotNull(message = "No se puede agregar un ingrediente sin conocer el nombre")
        @UniqueIngredient(message = "{ingredientRequest.name.uniqueingredient}")
        @Size(max = 40)
        @Schema(description = "Nombre del ingrediente", example = "Tomate")
        String name
) {}
