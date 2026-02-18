package com.salesianostriana.chefplanner.menuitem.dto;

import com.salesianostriana.chefplanner.menuitem.model.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MenuItemRequest(


        @Schema(description = "Fecha del elemento de menú", example = "2024-01-15")
        @NotNull(message = "La fecha es obligatoria")
        LocalDate date,

        @Schema(description = "Tipo de comida", example = "LUNCH")
        @NotNull(message = "El tipo de comida es obligatorio")
        MealType mealType,

        @Schema(description = "ID de la receta asociada", example = "42")
        @NotNull(message = "La receta es obligatoria")
        Long recipeId

) {}